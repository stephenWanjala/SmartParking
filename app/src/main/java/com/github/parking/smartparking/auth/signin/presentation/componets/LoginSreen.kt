package com.github.parking.smartparking.auth.signin.presentation.componets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.parking.smartparking.auth.signin.presentation.LoginViewModel
import com.github.parking.smartparking.destinations.HomeScreenDestination
import com.github.parking.smartparking.destinations.LoginScreenDestination
import com.github.parking.smartparking.destinations.SignUpScreenDestination
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo


@Destination(
    start = true
)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val auth = FirebaseAuth.getInstance()
    var isAuthenticated by remember { mutableStateOf(auth.currentUser != null) }

    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            isAuthenticated = firebaseAuth.currentUser != null
        }
        auth.addAuthStateListener(authStateListener)
        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }





    if (isAuthenticated) {
        navigator.navigate(HomeScreenDestination) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }

    } else {
        LoginScreenUi(navigator = navigator, viewModel = viewModel)
    }
}


@Composable
fun LoginScreenUi(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                ),

            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            LoginSection(
                onNavigate = { },
                onNavigateToSignUpScreen = {
                    navigator.navigate(SignUpScreenDestination) {
                        popUpTo(LoginScreenDestination) {
                            inclusive = true
                        }
                    }
                },
                viewModel = viewModel
            )
        }
    }
}