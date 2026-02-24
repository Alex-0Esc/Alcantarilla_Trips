package com.example.alcantarilla_trips

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alcantarilla_trips.Configuracion.CambiarIdioma

@Composable
fun NavGraph(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(onFinished = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("agenda") {
            Agenda(navController = navController)
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
    }
}