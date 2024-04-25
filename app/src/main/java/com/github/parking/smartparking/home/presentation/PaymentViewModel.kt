package com.github.parking.smartparking.home.presentation


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.data.remote.MpesaPay
import com.github.parking.smartparking.home.domain.model.Occupier
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.github.parking.smartparking.home.domain.model.STKPushQueryResponse
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.model.Slot
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import com.github.parking.smartparking.home.domain.utils.Constants
import com.github.parking.smartparking.loadData
import com.github.parking.smartparking.saveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val app: Application
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PaymentState())

    init {
        loadProviders()
    }

    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.CashAmountChanged -> {
                _state.update { it.copy(cashAmount = event.cashAmount) }
            }

            PaymentEvent.MakePayment -> {
                makePayment()
            }

            is PaymentEvent.PhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }
                val phoneNumber = validatePhoneNumber(state.value.phoneNumber)
                if (phoneNumber != null) {
                    _state.update { it.copy(phoneNumberError = null) }
                    if (state.value.cashAmount > 0 && state.value.phoneNumber.isNotEmpty()
                    ) {
                        _state.update { it.copy(payButtonEnabled = true) }
                    }
                } else {
                    _state.update {
                        it.copy(
                            phoneNumberError = "Invalid phone number",
                            payButtonEnabled = false
                        )
                    }

                }
            }


            is PaymentEvent.HoursToParkChanged -> {
                _state.update { it.copy(hoursToPark = event.hoursToPark) }
            }

            is PaymentEvent.SelectedSlot -> {
                _state.update {
                    it.copy(
                        selectedSlot = event.selectedSlot,
                        provider = event.provider
                    )
                }

            }
        }
    }


    private fun makePayment() {
        _state.update { it.copy(isLoading = true) }
//        update the available slots
//        add user to the slot
        // update the provider
        // update the slot
        _state.update {
            it.copy(
                providers = _state.value.providers.map { provider ->
                    if (provider.id == state.value.provider!!.id) {
                        provider.copy(
                            slots = provider.slots.map { slot ->
                                if (slot.id == state.value.selectedSlot!!.id) {
                                    slot.copy(
                                        isOccupied = true,
                                        occupiedBy = Occupier(
                                            hours = state.value.hoursToPark,
                                            amount = state.value.cashAmount,
                                            user = FirebaseAuth.getInstance().currentUser!!.uid
                                        )
                                    )
                                } else {
                                    slot
                                }
                            }.toMutableList()
                        )
                    } else {
                        provider
                    }
                }
            )
        }.also {
            saveData(app, state.value.providers.toMutableList())
        }
        try {
            val output = MpesaPay.sendPush(
                TransactionDetails(
                    state.value.phoneNumber,
                    state.value.passKey,
                    state.value.cashAmount,
                    state.value.payBill,
                    state.value.accReference,
                    state.value.callBackUrl,
                    state.value.partyA,
                    state.value.partyB,
                    state.value.description,
                    state.value.type
                )
            )
            ParkingProvider.providers.find {
                it.id == state.value.provider!!.id
            }?.let { provider ->
                provider.slots.find { it.id == state.value.selectedSlot!!.id }?.copy(
                    isOccupied = true,
                    occupiedBy = Occupier(
                        hours = state.value.hoursToPark,
                        amount = state.value.cashAmount,
                        user = FirebaseAuth.getInstance().currentUser!!.uid
                    )
                )?.let { slot ->
                    provider.slots[provider.slots.indexOfFirst { it.id == slot.id }] = slot
                }
                println("Provider is $provider and has changed")

            }
            _state.update { it.copy(stkPushResponse = output) }
            firestore.collection("transactions").document(firebaseAuth.uid!!).set(output)
            val batch: WriteBatch = firestore.batch()
            val providerRef =
                firestore.collection("providers").document(state.value.provider!!.id)
            println("Provider ref is $providerRef")
            batch.update(
                providerRef,
                "availableSlots",
                state.value.provider!!.availableSlots - 1
            )
            // Create a document reference for the slot
            val slotRef =
                providerRef.collection("slots").document(state.value.selectedSlot!!.id)
            // Update the slot's occupancy status
            batch.update(slotRef, "isOccupied", true)
            batch.commit().addOnSuccessListener {
                _state.update { it.copy(navigate = true, isLoading = false) }
            }.addOnFailureListener { ex ->
                _state.update {
                    it.copy(
                        error = UiText.DynamicString(
                            ex.message ?: "An error occurred"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    error = UiText.DynamicString(
                        e.message ?: "An error occurred"
                    )
                )
            }
        }
    }


    private fun validatePhoneNumber(phoneNumber: String): String? {
        return if (phoneNumber.isNotEmpty() &&
            phoneNumber.length == 13 && phoneNumber.startsWith("+254")
        ) {
            phoneNumber.replace("^+".toRegex(), "")
        } else if (
            phoneNumber.isNotEmpty() &&
            phoneNumber.length == 10 && (phoneNumber.startsWith("07") || phoneNumber.startsWith("01"))
        ) {
            phoneNumber.replaceFirst("^0".toRegex(), "254")
        } else {
            null
        }

    }

    private fun loadProviders() {
        val data = loadData(context = app)
        if (data != null) {
            ParkingProvider.providers.clear()
            ParkingProvider.providers.addAll(data)
            _state.update { it.copy(providers = ParkingProvider.providers) }
        }
    }

    fun getOccupiedSlotsByCurrentUser(): Map<ParkingProvider, List<Slot>> {
        val currentUserUid = firebaseAuth.currentUser?.uid
        return state.value.providers.mapNotNull { provider ->
            provider.slots.filter { it.occupiedBy?.user == currentUserUid }
                .takeIf { it.isNotEmpty() }?.let { slots ->
                provider to slots
            }
        }.toMap()
    }


    data class PaymentState(
        val phoneNumber: String = "",
        val passKey: String = Constants.PASSKEY,
        val cashAmount: Int = 1,
        val payBill: String = Constants.PARTYB,
        val accReference: String = Constants.ACCOUNT_REFERENCE,
        val callBackUrl: String = Constants.CALLBACKURL,
        val partyA: String = Constants.PARTYA,
        val partyB: String = Constants.PARTYB,
        val description: String = Constants.DESCRIPTION,
        val type: String = Constants.TRANSACTION_TYPE,
        val error: UiText? = null,
        val isLoading: Boolean = false,
        val payButtonEnabled: Boolean = false,
        val phoneNumberError: String? = null,
        val stkPushQueryResponse: STKPushQueryResponse? = null,
        val stkPushResponse: STKPushResponse? = null,
        val transactionStatus: String? = null,
        val hoursToPark: Int = 1,
        val selectedSlot: Slot? = null,
        val provider: ParkingProvider? = null,
        val navigate: Boolean = false,
        val providers: List<ParkingProvider> = emptyList()
    )


    sealed interface PaymentEvent {
        data class PhoneNumberChanged(val phoneNumber: String) : PaymentEvent
        data class CashAmountChanged(val cashAmount: Int) : PaymentEvent
        data class HoursToParkChanged(val hoursToPark: Int) : PaymentEvent
        data class SelectedSlot(val provider: ParkingProvider, val selectedSlot: Slot) :
            PaymentEvent

        data object MakePayment : PaymentEvent
//        data object QueryStatus : PaymentEvent

    }
}


