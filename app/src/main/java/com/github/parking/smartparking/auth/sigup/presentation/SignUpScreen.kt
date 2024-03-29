package com.github.parking.smartparking.auth.sigup.presentation

import androidx.compose.runtime.Composable
import com.github.parking.smartparking.auth.sigup.presentation.componets.SignUpSection
import com.github.parking.smartparking.destinations.HomeScreenDestination
import com.github.parking.smartparking.destinations.LoginScreenDestination
import com.github.parking.smartparking.destinations.SignUpScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo


@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {

    SignUpSection(
        onNavigate = {
            navigator.navigate(HomeScreenDestination) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true 
                }
            }
        },
        onNavigateToLoginScreen = {
            navigator.navigate(LoginScreenDestination) {
                popUpTo(SignUpScreenDestination) {
                    inclusive = true
                }
            }
        },

        )
}