package com.github.parking.smartparking.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.parking.smartparking.auth.core.domain.repository.AuthRepository
import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.data.remote.MpesaPay.toSTkQuery
import com.github.parking.smartparking.home.domain.PaymentRepository
import com.github.parking.smartparking.home.domain.model.STKPushQueryResponse
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import com.github.parking.smartparking.home.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), PaymentState())

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

            PaymentEvent.QueryStatus -> {
                queryStatus()

            }
        }
    }


    private fun makePayment() {
        _state.update { it.copy(isLoading = true) }
        println("make payment called ")
        println("The state is ${state.value}")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val phoneNumber = validatePhoneNumber(state.value.phoneNumber)
            if (phoneNumber != null) {
                val request = TransactionDetails(
                    phoneNumber = phoneNumber,
                    passKey = state.value.passKey,
                    cashAmount = state.value.cashAmount,
                    payBill = state.value.payBill,
                    accReference = state.value.accReference,
                    callBackUrl = state.value.callBackUrl,
                    partyA = state.value.partyA,
                    partyB = state.value.partyB,
                    description = state.value.description,
                    type = state.value.type
                )
                repository.sendSTKPush(request).collectLatest { stkPushResponseResource ->
                    when (stkPushResponseResource) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    stkPushResponse = stkPushResponseResource.data,
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false, error = it.error) }
                        }

                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }

                        }
                    }
                }
            }
        }
    }

    private fun queryStatus() {
        viewModelScope.launch {
            delay(3000)  // delay for 3 second
            if (state.value.stkPushResponse != null) {
                repository.querySTKPush(request = state.value.stkPushResponse!!.toSTkQuery()).collectLatest { response->
                    when(response){
                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false, error = response.uiText, transactionStatus = null) }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true, transactionStatus = null) }
                        }
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false, transactionStatus = response.data?.ResultCode) }
                        }
                    }
                }
            } else {
                return@launch
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
        val transactionStatus: String? = null
    )


    sealed interface PaymentEvent {
        data class PhoneNumberChanged(val phoneNumber: String) : PaymentEvent
        data class CashAmountChanged(val cashAmount: Int) : PaymentEvent
        data object MakePayment : PaymentEvent
        data object QueryStatus : PaymentEvent

    }
}

