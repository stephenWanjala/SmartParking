package com.github.stephenwanjala.smartparking.auth.sigup.presentation

import androidx.compose.runtime.Composable
import com.github.stephenwanjala.smartparking.auth.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.componets.SignUpSection


@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {

    SignUpSection(
        onNavigate = {  },
        onNavigateToLoginScreen = { navigator.navigate(LoginScreenDestination) },

    )
}