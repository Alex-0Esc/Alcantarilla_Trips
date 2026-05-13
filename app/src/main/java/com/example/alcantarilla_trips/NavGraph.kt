package com.example.alcantarilla_trips

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alcantarilla_trips.ui.viewmodels.ActivityViewModel
import com.example.alcantarilla_trips.ui.viewmodels.TripListViewModel
import com.example.alcantarilla_trips.ui.viewmodels.AuthViewModel
import com.example.alcantarilla_trips.ui.viewmodels.AuthState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alcantarilla_trips.ui.viewmodels.HotelViewModel
import com.example.alcantarilla_trips.ui.viewmodels.TripImageViewModel
import androidx.compose.runtime.collectAsState

val bottomNavRoutes = setOf("mis_viajes", "album", "configuracion")

@Composable
fun NavGraph(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    // Instancias compartidas
    val tripViewModel: TripListViewModel = viewModel()
    val activityViewModel: ActivityViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val imageViewModel: TripImageViewModel = viewModel()

    // Observamos el estado global de autenticación
    val authState by authViewModel.authState.collectAsState()

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash", // El splash decide a dónde ir
            modifier = Modifier.padding(paddingValues)
        ) {
            // --- SPLASH SCREEN (T2.1: Control de sesión activa) ---
            composable("splash") {
                SplashScreen(onFinished = {
                    // Verificamos si ya hay un usuario logueado en Firebase
                    if (authViewModel.currentUser != null) {
                        navController.navigate("mis_viajes") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                })
            }

            // --- LOGIN (T2.3: Implementación de acciones) ---
            composable("login") {
                LoginScreen(navController = navController, authViewModel = authViewModel)
            }

            // --- REGISTRO ---
            composable("registro") {
                RegisterScreen(navController = navController, authViewModel = authViewModel)
            }

            // --- RECUPERAR CONTRASEÑA (T3.3) ---
            composable("recuperar_password") {
                ForgotPasswordScreen(navController = navController, authViewModel = authViewModel)
            }

            // --- PANTALLA PRINCIPAL ---
            composable("mis_viajes") {
                TripsScreen(navController = navController, viewModel = tripViewModel)
            }

            // --- CONFIGURACIÓN (T2.4: Logout) ---
            composable("configuracion") {
                Configuracion(
                    navController = navController,
                    themeViewModel = themeViewModel,
                    authViewModel = authViewModel
                )
            }

            // --- RESTO DE RUTAS (Se mantienen igual) ---
            composable("itinerario") { ItineraryScreen(navController = navController) }
            composable("album") {
                // Default album tab: navigate to first trip or show empty
                val trips = tripViewModel.trips.collectAsState().value
                val firstId = trips.firstOrNull()?.tripId ?: -1
                PhotoAlbumScreen(navController = navController, tripId = firstId, imageViewModel = imageViewModel, tripViewModel = tripViewModel)
            }
            composable(
                route = "album/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
                PhotoAlbumScreen(navController = navController, tripId = tripId, imageViewModel = imageViewModel, tripViewModel = tripViewModel)
            }
            composable("cambiar_idioma") { CambiarIdioma(navController = navController) }
            composable("informacion") { Informacion(navController = navController) }
            composable("terminos") { Terminos(navController = navController) }
            composable("about") { About(navController = navController) }
            composable("create_trip") { CreateTripScreen(navController = navController, viewModel = tripViewModel) }
            composable("reservations") {
                val hotelViewModel: HotelViewModel = hiltViewModel()
                ReservationsScreen(navController = navController, hotelViewModel = hotelViewModel, tripViewModel = tripViewModel)
            }
            composable(
                route = "edit_trip/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
                EditTripScreen(navController = navController, tripId = tripId, viewModel = tripViewModel)
            }

            composable(
                route = "trip_detail/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
                TripDetailScreen(navController, tripId, tripViewModel, activityViewModel)
            }

            composable(
                route = "add_activity/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
                AddActivityScreen(navController, tripId, tripViewModel, activityViewModel)
            }

            composable(
                route = "edit_activity/{activityId}/{tripId}",
                arguments = listOf(
                    navArgument("activityId") { type = NavType.IntType },
                    navArgument("tripId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val activityId = backStackEntry.arguments?.getInt("activityId") ?: return@composable
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
                EditActivityScreen(navController, activityId, tripId, tripViewModel, activityViewModel)
            }
        }
    }
}