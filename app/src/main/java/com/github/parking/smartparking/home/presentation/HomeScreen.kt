package com.github.parking.smartparking.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.github.parking.smartparking.R
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                SmartParkingApBar()
            },

            ) { paddingValues ->

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
                    .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                    .clip(CircleShape),
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Rounded.Person, contentDescription = "User",
                    tint=MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        })
}