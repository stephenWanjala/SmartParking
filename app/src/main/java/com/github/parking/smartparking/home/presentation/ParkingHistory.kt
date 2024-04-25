package com.github.parking.smartparking.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ParkingHistoryScreen(
    navigator: DestinationsNavigator,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val providerSlots = viewModel.getOccupiedSlotsByCurrentUser()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    Text(text = "Parking History")
                },
                    navigationIcon = {
                        IconButton(onClick = navigator::navigateUp) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                        }
                    })

            }) { paddingValues: PaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn {
                    providerSlots.forEach { providerSlot ->
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Provider: ${providerSlot.key.name}")
                                Text(text = "HourlY Rate: Kesh ${providerSlot.key.hourlyRate}")
                                OutlinedCard(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    providerSlot.value.forEach { slot ->
                                        Card(
                                            shape = MaterialTheme.shapes.medium,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text(text = "Slot: ${slot.id}")
                                                Text(text = "SlotNumber: ${slot.number}")
                                                Text(text = "Floor: ${slot.floor}")
                                                Text(text = "Booked For : ${slot.occupiedBy?.hours} hrs")
                                                Text(text = "Pay : ${slot.occupiedBy?.amount} hrs")
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

}
