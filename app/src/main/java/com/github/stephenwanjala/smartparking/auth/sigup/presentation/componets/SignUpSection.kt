package com.github.stephenwanjala.smartparking.auth.sigup.presentation.componets

import android.app.Application
import android.content.res.Configuration
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.stephenwanjala.smartparking.R
import com.github.stephenwanjala.smartparking.auth.core.presentation.AButton
import com.github.stephenwanjala.smartparking.auth.core.util.asString
import com.github.stephenwanjala.smartparking.auth.signin.presentation.componets.InputTextField
import com.github.stephenwanjala.smartparking.auth.signin.presentation.componets.LogoSection
import com.github.stephenwanjala.smartparking.auth.signin.presentation.componets.PasswordTextField
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.SignUpState
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.SignUpViewModel
import com.github.stephenwanjala.smartparking.auth.sigup.presentation.SignupEvent
import com.wantech.gdsc_msu.core.presentation.LoadingDialog
import kotlinx.coroutines.launch


@Composable
fun SignUpSection(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigate: () -> Unit,
    onNavigateToLoginScreen: () -> Unit,
    application: Application = LocalContext.current.applicationContext as Application,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val signUpUiState = viewModel.signUpIState.collectAsState(initial = SignUpState())
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                LogoSection()
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 32.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        LoginTextInputFields(
                            onClickLoginButton = {

                                onNavigateToLoginScreen()

                            },
                            onClickToSignUp = {
                                viewModel.onEvent(SignupEvent.Signup)

                            },
                        )
                        if (signUpUiState.value.isLoading) {
                            LoadingDialog()
                        }
                    }

                }


            }
        }
        if (signUpUiState.value.error != null) {
            LaunchedEffect(true) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = signUpUiState.value.error!!.asString(context = application),
                        actionLabel = "Dismiss"
                    )
                }
            }
        }
    }

    if (signUpUiState.value.signUp != null) {
        onNavigate()

    }

}


@Composable
fun LoginTextInputFields(
    onClickLoginButton: () -> Unit, onClickToSignUp: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    var orientation by remember {
        mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT)
    }
    val configuration = LocalConfiguration.current

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InputTextField(
                        textValue = state.userName,
                        labelText = "UserName",
                        onValueChange = { viewModel.onEvent(SignupEvent.EnteredUsername(it)) },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    InputTextField(
                        textValue = state.email,
                        labelText = "Email",
                        onValueChange = { viewModel.onEvent(SignupEvent.EnteredEmail(it)) },
                        modifier = Modifier.weight(0.5f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                    )
                }
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = state.password,
                    labelText = "Password",

                    placeHolder = "Your Password",
                    onValueChange = {
                        viewModel.onEvent(SignupEvent.EnteredPassword(it))

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    passwordModifier = Modifier.fillMaxWidth(0.9f)

                )


                AButton(text = stringResource(id = R.string.sign_up),
                    onClick = { onClickToSignUp() },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    buttonEnabled = {
                        state.email.isNotBlank() && state.password.isNotBlank() &&
                                state.userName.isNotBlank() && state.password.length > 6 && state.userName.length > 3 &&
                                state.password.isNotBlank() && ((state.password.length >= 8) && state.email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(
                            state.email
                        ).matches())
                    }

                )

                TextButton(
                    onClick = {
                        onClickLoginButton()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.already_have_Account),
                        color = MaterialTheme.colorScheme.surface,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(4.dp),
                    )
                }

            }

        }

        else -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
//                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create_Account),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                InputTextField(
                    textValue = state.userName,
                    labelText = "UserName",
                    onValueChange = { viewModel.onEvent(SignupEvent.EnteredUsername(it)) },
                )

                InputTextField(
                    textValue = state.email,
                    labelText = "Email",
                    onValueChange = { viewModel.onEvent(SignupEvent.EnteredEmail(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )

                )


                PasswordTextField(
                    textValue = state.password,
                    labelText = "Password",
                    placeHolder = "Your Password",

                    onValueChange = {
                        viewModel.onEvent(SignupEvent.EnteredPassword(it))

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                )

                AButton(text = stringResource(id = R.string.sign_up),
                    onClick = { onClickToSignUp() },
                    modifier = Modifier.wrapContentSize(),
                    buttonEnabled = {
                        state.email.isNotBlank() && state.password.isNotBlank() &&
                                state.userName.isNotBlank() && state.password.length > 6
                                && state.userName.length > 3 &&
                                state.password.isNotBlank() && ((state.password.length >= 8)
                                && state.email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(
                            state.email
                        ).matches())
                    }

                )

                TextButton(
                    onClick = {
                        onClickLoginButton()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.already_have_Account),
                        color = MaterialTheme.colorScheme.surface,

                        )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.sign_in),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(4.dp)
                    )
                }

            }
        }

    }


}





