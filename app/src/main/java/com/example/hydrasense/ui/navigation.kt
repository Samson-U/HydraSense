package com.example.hydrasense.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hydrasense.AppDestinations
import com.example.hydrasense.screens.*
import com.example.hydrasense.ui.components.AppDrawer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val startDestination = if (currentUser != null) {
        AppDestinations.HOME_ROUTE
    } else {
        AppDestinations.LOGIN_ROUTE
    }

    val drawerEnabledRoutes = listOf(
        AppDestinations.HOME_ROUTE,
        AppDestinations.NEW_REPORT_ROUTE,
        AppDestinations.MY_REPORTS_ROUTE,
        AppDestinations.NOTIFICATIONS_ROUTE,
        AppDestinations.PROFILE_ROUTE,
        AppDestinations.SETTINGS_ROUTE
    )
    val isDrawerEnabled = currentRoute in drawerEnabledRoutes

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                navController = navController,
                currentRoute = currentRoute ?: AppDestinations.HOME_ROUTE,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = isDrawerEnabled
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Authentication
                composable(AppDestinations.LOGIN_ROUTE) { LoginScreen(auth, navController) }
                composable(AppDestinations.SIGNUP_ROUTE) { SignUpScreen(auth, navController) }

                // Main App
                composable(AppDestinations.HOME_ROUTE) {
                    HomeScreen(navController) { scope.launch { drawerState.open() } }
                }
                composable(AppDestinations.NEW_REPORT_ROUTE) {
                    ReportWaterSourceScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(AppDestinations.MY_REPORTS_ROUTE) { MyReportsScreen(navController) }
                composable(AppDestinations.PROFILE_ROUTE) { ProfileScreen(navController) }
                composable(AppDestinations.NOTIFICATIONS_ROUTE) { MyReportsScreen(navController) }
                composable(AppDestinations.SETTINGS_ROUTE) { SettingsScreen(navController) }
            }
        }
    }
}
