package com.github.parking.smartparking.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.parking.smartparking.home.data.ParkingProviderRepository
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingProvidersViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ProviderState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        loadProviders()
    }


    private fun loadProviders(){
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            ParkingProviderRepository.fetchParkingProvidersWithSlots(
                onSuccess = { providers ->
                    _state.value = state.value.copy(providers = providers, isLoading = false)
                },
                onFailure = { error ->
                    _state.value = state.value.copy(error = error, isLoading = false)
                }
            )
        }
    }
}


data class ProviderState(
    val providers: List<ParkingProvider> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)