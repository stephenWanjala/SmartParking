package com.github.parking.smartparking

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.github.parking.smartparking.destinations.Destination
import com.github.parking.smartparking.destinations.HomeScreenDestination
import com.github.parking.smartparking.destinations.ParkingHistoryScreenDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec


enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val label: Int
) {
    HOME(
        direction = HomeScreenDestination,
        selectedIcon = Icons.Outlined.Home,
        unselectedIcon = Icons.Filled.Home,
        label = R.string.home
    ),
    PARKING_HISTORY(
        direction = ParkingHistoryScreenDestination,
        selectedIcon = Icons.Outlined.History,
        unselectedIcon = Icons.Filled.History,
        label = R.string.parking_history
    ),
}


@Composable
fun BottomBar(
    navController: NavController,
    items: List<BottomBarDestination>
) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: HomeScreenDestination

    NavigationBar {
        items.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction, fun NavOptionsBuilder.() {
                        launchSingleTop = true
                        restoreState = true
                    })
                },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == destination.direction) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}