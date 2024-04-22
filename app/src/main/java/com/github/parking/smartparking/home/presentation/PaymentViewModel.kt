package com.github.parking.smartparking.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.parking.smartparking.auth.core.domain.repository.AuthRepository
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.domain.PaymentRepository
import com.github.parking.smartparking.home.domain.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), PaymentState())

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
    val error: UiText? = null
)
