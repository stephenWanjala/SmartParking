package com.github.parking.smartparking.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.parking.smartparking.auth.core.domain.repository.AuthRepository
import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.domain.PaymentRepository
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import com.github.parking.smartparking.home.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
                if (state.value.cashAmount > 0 && state.value.phoneNumber.isNotEmpty()
                    && state.value.phoneNumber.length == 10
                    && state.value.phoneNumber.startsWith("0")
                    && state.value.phoneNumber.all { it.isDigit() }
                    ) {
                    _state.update { it.copy(payButtonEnabled = true) }
                } else {
                    _state.update { it.copy(payButtonEnabled = false) }
                }
            }

            PaymentEvent.MakePayment ->{
                makePayment()
            }
            is PaymentEvent.PhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }
                val phoneNumber = validatePhoneNumber(state.value.phoneNumber)
                if(phoneNumber != null ) {
                    _state.update { it.copy(phoneNumberError = null) }
                    if (state.value.cashAmount > 0 && state.value.phoneNumber.isNotEmpty()
                        ) {
                        _state.update { it.copy(payButtonEnabled = true) }
                    }
                } else {
                    _state.update { it.copy(phoneNumberError = "Invalid phone number", payButtonEnabled = false) }

                    }
            }
        }
    }


    private fun makePayment(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val phoneNumber = validatePhoneNumber(state.value.phoneNumber)
            if(phoneNumber != null) {
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
                repository.sendSTKPush(request).collect { stkPushResponseResource ->
                    when(stkPushResponseResource){
                        is Resource.Success -> {
                            _state.update { it.copy(isLoading = false) }
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




    private fun validatePhoneNumber(phoneNumber: String): String? {
        return if (phoneNumber.isNotEmpty() &&
            phoneNumber.length == 13 && phoneNumber.startsWith("+254")
        ) {
            phoneNumber.replace("^+".toRegex(), "")
        } else if (
            phoneNumber.isNotEmpty() &&
            phoneNumber.length < 11 && phoneNumber.startsWith("0")
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
    )


    sealed interface PaymentEvent {
        data class PhoneNumberChanged(val phoneNumber: String) : PaymentEvent
        data class CashAmountChanged(val cashAmount: Int) : PaymentEvent
        data object MakePayment : PaymentEvent

    }
}

