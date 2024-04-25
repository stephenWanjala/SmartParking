package com.github.parking.smartparking

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.parking.smartparking.destinations.BookingDialogDestination
import com.github.parking.smartparking.destinations.ChekoutSheetDestination
import com.github.parking.smartparking.destinations.HomeScreenDestination
import com.github.parking.smartparking.destinations.ParkingHistoryScreenDestination
import com.github.parking.smartparking.destinations.ParkingProviderScreenDestination
import com.github.parking.smartparking.destinations.ProfileScreenDestination
import com.github.parking.smartparking.home.data.ParkingProviderDataInitializer
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.github.parking.smartparking.ui.theme.SmartParkingTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ParkingProviderDataInitializer.initializeParkingProviders(this)
        val providers = loadData(this)
        if (providers == null) {
            ParkingProviderDataInitializer.initializeParkingProviders(this)
            saveData(this, ParkingProvider.providers )
        } else {
            ParkingProvider.providers = providers
        }
        setContent {
            SmartParkingTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navHostEngine = rememberAnimatedNavHostEngine(
                    navHostContentAlignment = Alignment.TopCenter,
                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
                        enterTransition = {
                            scaleIn(transformOrigin = TransformOrigin.Center)
                        },
                        exitTransition = {
                            scaleOut(transformOrigin = TransformOrigin.Center)
                        }
                    )
                )
                val navController = navHostEngine.rememberNavController(bottomSheetNavigator)
                val bottomNavDestinations: List<BottomBarDestination> = listOf(
                    BottomBarDestination.HOME,
                    BottomBarDestination.PARKING_HISTORY
                )

                navHostEngine.rememberNavController(bottomSheetNavigator)

                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination
                val showBottomBar = currentDestination?.route in listOf(
                    HomeScreenDestination.route,
                    ParkingHistoryScreenDestination.route,
                    ParkingProviderScreenDestination.route,
                    BookingDialogDestination.route,
                    ChekoutSheetDestination.route,
                    ProfileScreenDestination.route,
                )
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(visible = showBottomBar) {
                            BottomBar(
                                navController = navController,
                                items = bottomNavDestinations
                            )
                        }
                    }
                ) { paddingValues ->
                    val unUsedPadding = paddingValues
                    ModalBottomSheetLayout(
                        bottomSheetNavigator = bottomSheetNavigator,
                        sheetShape = RoundedCornerShape(16.dp),
                    ) {
                        DestinationsNavHost(
                            navController = navController,
                            navGraph = NavGraphs.root,
                            engine = navHostEngine
                        )
                    }
                }

            }
        }
    }
}


fun saveData(context: Context, providers: MutableList<ParkingProvider>) {
    val fileOutputStream = context.openFileOutput("providers.data", Context.MODE_PRIVATE)
    val objectOutputStream = ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(providers)
    objectOutputStream.close()
    fileOutputStream.close()
}

fun loadData(context: Context): MutableList<ParkingProvider>? {
    try {
        val fileInputStream = context.openFileInput("providers.data")
        val objectInputStream = ObjectInputStream(fileInputStream)
        val providers = objectInputStream.readObject() as MutableList<ParkingProvider>
        objectInputStream.close()
        fileInputStream.close()
        return providers
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}




