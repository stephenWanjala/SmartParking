package com.github.parking.smartparking.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.parking.smartparking.R
import com.github.parking.smartparking.auth.core.presentation.LoadingDialog
import com.github.parking.smartparking.destinations.ParkingProviderScreenDestination
import com.github.parking.smartparking.home.ParkingProvidersViewModel
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: ParkingProvidersViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        val state = viewModel.state.collectAsState().value


        Scaffold(
            topBar = {
                SmartParkingApBar()
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
            ) { paddingValues ->

            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.padding(paddingValues),
                    columns = StaggeredGridCells.Adaptive(200.dp)
                ) {
                    items(state.providers) { provider ->
                        ParkingProviderCard(provider = provider, onclick = {
                            navigator.navigate(ParkingProviderScreenDestination(it))
                        })
                    }

                }

                AnimatedVisibility(state.isLoading) {
                    LoadingDialog(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

        }
        LaunchedEffect(key1 = state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(it.message ?: "An error occurred")
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartParkingApBar(
    modifier: Modifier = Modifier,

    ) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Rounded.Person, contentDescription = "User",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        })
}


@Composable
fun ParkingProviderCard(
    modifier: Modifier = Modifier,
    provider: ParkingProvider,
    onclick: (ParkingProvider) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onclick(provider) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = provider.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = provider.name,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = provider.location,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Available slots: ${provider.availableSlots}/${provider.totalSlots}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hourly rate:Kesh ${provider.hourlyRate}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}