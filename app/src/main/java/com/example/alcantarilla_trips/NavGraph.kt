package com.example.alcantarilla_trips
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alcantarilla_trips.Configuracion
import com.example.alcantarilla_trips.Configuracion.CambiarIdioma
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("agenda") {
            Agenda(navController = navController)
        }
        composable("configuracion") {
            Configuracion(navController = navController)
        }
        composable("cambiar_idioma") {
            CambiarIdioma(navController = navController)
        }
    }
}
