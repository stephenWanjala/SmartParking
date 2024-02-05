package com.github.stephenwanjala.smartparking.auth.signin.presentation.componets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.stephenwanjala.smartparking.auth.destinations.SignUpScreenDestination
import com.github.stephenwanjala.smartparking.auth.signin.presentation.LoginViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(
    start = true
)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val isUserExist = viewModel.isCurrentUserExist.collectAsState(initial = true)
    LaunchedEffect(isUserExist.value) {
        if (isUserExist.value) {

        }
    }

    if (!isUserExist.value) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
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
                    onNavigate = {  },
                    onNavigateToSignUpScreen = {
                            navigator.navigate(SignUpScreenDestination)
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}