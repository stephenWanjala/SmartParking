package com.github.parking.smartparking.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.parking.smartparking.auth.core.presentation.AButton
import com.github.parking.smartparking.destinations.LoginScreenDestination
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle


@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun ProfileScreen(navigator: DestinationsNavigator) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    Dialog(onDismissRequest = navigator::popBackStack) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(min=600.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Profile Screen ",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = navigator::popBackStack) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Email: ${user?.email}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                AButton(
                    text = "Logout",
                    onClick = {
                        auth.signOut()
                        navigator.navigate(LoginScreenDestination)
                    },
                    modifier = Modifier,
                    buttonEnabled = { true })
            }
        }
    }
}