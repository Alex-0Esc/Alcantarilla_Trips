package com.example.alcantarilla_trips

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavGraph(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
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
        // "registro" — coincide con navController.navigate("registro") del LoginScreen
        composable("registro") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
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
        // "mis_viajes" — coincide con navController.navigate("mis_viajes") del HomeScreen
        composable("mis_viajes") {
            TripsScreen(navController = navController)
        }
        composable("create_trip") {
            CreateTripScreen(navController = navController)
        }
        composable(
            route = "valorar/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId") ?: return@composable
            // ✅ Solo pasa el Int — RateTripScreen busca el Trip internamente
            RateTripScreen(navController = navController, tripId = tripId)
        }
    }
}