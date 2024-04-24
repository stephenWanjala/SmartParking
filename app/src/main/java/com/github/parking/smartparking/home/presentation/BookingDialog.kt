package com.github.parking.smartparking.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.parking.smartparking.auth.core.presentation.AButton
import com.github.parking.smartparking.auth.core.presentation.LoadingDialog
import com.github.parking.smartparking.auth.signin.presentation.componets.InputTextField
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

object NonDismissableDialog : DestinationStyle.Dialog {
    override val properties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
    )
}

@Composable
@Destination(
    style = NonDismissableDialog::class
)
fun BookingDialog(
    navigator: DestinationsNavigator,
    provider: ParkingProvider,
    selectedSlotNumber: Int,
    viewmodel: PaymentViewModel = hiltViewModel()
) {
    val selectedSlot = provider.slots.find { it.number == selectedSlotNumber }!!
    val state = viewmodel.state.collectAsState()
    viewmodel.onEvent(PaymentViewModel.PaymentEvent.SelectedSlot(provider, selectedSlot))



    Dialog(
        onDismissRequest = navigator::navigateUp,
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 600.dp),
//                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {


            LaunchedEffect(state.value.hoursToPark) {
                viewmodel.onEvent(PaymentViewModel.PaymentEvent.CashAmountChanged((state.value.hoursToPark * provider.hourlyRate).toInt()))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Booking Details",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = navigator::navigateUp) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
//                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Booking Parking Slot ${selectedSlot.number} at ${provider.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Provider: ${provider.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Location: ${provider.location}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Price: Kes ${provider.hourlyRate} per hour",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InputTextField(
                            textValue = state.value.hoursToPark.toString(),
                            labelText = "Time to park in Hrs",
                            onValueChange = {
                                if (it.toIntOrNull() != null) {
                                    viewmodel.onEvent(
                                        PaymentViewModel.PaymentEvent.HoursToParkChanged(
                                            it.toInt()
                                        )
                                    )
                                } else {
                                    viewmodel.onEvent(
                                        PaymentViewModel.PaymentEvent.HoursToParkChanged(
                                            1
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = androidx.compose.ui.text.input.ImeAction.Next
                            ),

                            )

                        InputTextField(
                            textValue = state.value.cashAmount.toString(),
                            labelText = "Cost",
                            onValueChange = {
                               viewmodel.onEvent(PaymentViewModel.PaymentEvent.CashAmountChanged(it.toInt()))
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = androidx.compose.ui.text.input.ImeAction.Done
                            ),
                            enabled = false
                        )
                    }

                    InputTextField(
                        textValue = state.value.phoneNumber,
                        labelText = "Phone Number",
                        onValueChange = {
                            viewmodel.onEvent(PaymentViewModel.PaymentEvent.PhoneNumberChanged(it))
                        },
                        modifier = Modifier,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        ),
                        isError = state.value.phoneNumberError != null,
                        supportText = state.value.phoneNumberError
                    )

                    AButton(
                        text = "Reserve Slot",
                        onClick = { viewmodel.onEvent(PaymentViewModel.PaymentEvent.MakePayment) },
                        modifier = Modifier,
                        buttonEnabled = { state.value.payButtonEnabled && !state.value.isLoading })
                }

                if (state.value.isLoading) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            LoadingDialog(modifier = Modifier.align(Alignment.Center))
                            Text(
                                text = "Processing, Waiting for Payment...",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }


        }

    }
}