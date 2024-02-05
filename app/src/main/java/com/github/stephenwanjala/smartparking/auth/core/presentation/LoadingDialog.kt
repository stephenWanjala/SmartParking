package com.github.stephenwanjala.smartparking.auth.core.presentation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.LoadingDialog() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(50.dp)
            .align(Alignment.Center),
        color = MaterialTheme.colorScheme.primary
    )
}