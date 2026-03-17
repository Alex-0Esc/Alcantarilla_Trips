package com.example.alcantarilla_trips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.dp

data class BottomNavDestination(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int
)

val bottomNavDestinations = listOf(
    BottomNavDestination("mis_viajes",    Icons.Default.Luggage,       R.string.nav_viajes),
    BottomNavDestination("album",         Icons.Default.Album, R.string.album_titulo),
    BottomNavDestination("configuracion", Icons.Default.Settings,      R.string.nav_configuracion)
)

@Composable
fun BottomNavBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        bottomNavDestinations.forEach { dest ->
            val selected = currentRoute == dest.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(dest.route) {
                            popUpTo("mis_viajes") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(dest.icon, contentDescription = stringResource(dest.labelRes)) },
                label = {
                    Text(
                        stringResource(dest.labelRes),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = MaterialTheme.colorScheme.primary,
                    selectedTextColor   = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}