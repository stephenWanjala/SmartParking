package com.github.parking.smartparking.auth.core.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun AButton(
    text: String, onClick: () -> Unit, modifier: Modifier, buttonEnabled: () -> Boolean,
    trailingIcon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            disabledElevation = 0.dp,
            pressedElevation = 4.dp
        ),
        enabled = buttonEnabled(),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (trailingIcon != null) {
            Icon(imageVector = trailingIcon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text, style = MaterialTheme.typography.bodySmall
        )

    }
}