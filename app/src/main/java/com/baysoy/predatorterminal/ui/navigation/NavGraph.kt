package com.baysoy.predatorterminal.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baysoy.predatorterminal.ui.chart.ChartScreen
import com.baysoy.predatorterminal.ui.signal.SignalScreen
import com.baysoy.predatorterminal.ui.watchlist.WatchlistScreen
import com.baysoy.predatorterminal.ui.settings.SettingsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Chart : Screen("chart", "GRAFİK", Icons.Default.BarChart)
    object Signal : Screen("signals", "SİNYAL", Icons.Default.Notifications)
    object Watchlist : Screen("watchlist", "TAKİP", Icons.Default.Star)
    object Settings : Screen("settings", "AYAR", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredatorNavGraph() {
    val navController = rememberNavController()
    val screens = listOf(Screen.Chart, Screen.Signal, Screen.Watchlist, Screen.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Chart.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Chart.route) { ChartScreen() }
            composable(Screen.Signal.route) { SignalScreen() }
            composable(Screen.Watchlist.route) { WatchlistScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}