package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel

val bottomNavRoutes = setOf("mis_viajes", "itinerario", "configuracion", "album")

@Composable
fun NavGraph(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    // Una sola instancia compartida por todas las pantallas
    val tripViewModel: TripListViewModel = viewModel()

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash") {
                SplashScreen(onFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("registro") {
                RegisterScreen(navController = navController)
            }
            composable("mis_viajes") {
                TripsScreen(navController = navController, viewModel = tripViewModel)
            }
            composable("itinerario") {
                ItineraryScreen(navController = navController)
            }
            composable("album") {
                PhotoAlbumScreen(navController = navController)
            }
            composable("configuracion") {
                Configuracion(navController = navController, themeViewModel = themeViewModel)
            }
            composable("cambiar_idioma") {
                CambiarIdioma(navController = navController)
            }
            composable("informacion") {
                Informacion(navController = navController)
            }
            composable("terminos") {
                Terminos(navController = navController)
            }
            composable("about") {
                About(navController = navController)
            }
            composable("create_trip") {
                CreateTripScreen(navController = navController, viewModel = tripViewModel)
            }
            composable(
                route = "valorar/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
                RateTripScreen(navController = navController, tripId = tripId)
            }
        }
    }
}