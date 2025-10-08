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
import com.example.hydrasense.NotificationsScreen
import com.example.hydrasense.ProfileScreen
import com.example.hydrasense.SettingsScreen
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

    // Define the list of routes that should have the drawer enabled
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
        // Lock the swipe gesture on login/signup screens
        gesturesEnabled = isDrawerEnabled
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                // The starting page is the login route
                startDestination = AppDestinations.LOGIN_ROUTE,
                modifier = Modifier.padding(innerPadding)
            ) {
                // --- Authentication Routes ---
                composable(AppDestinations.LOGIN_ROUTE) {
                    val auth = FirebaseAuth.getInstance()
                    LoginScreen(
                        auth = auth,
                        navController = navController
                    )
                }

                // ✅ Uncommented and activated the SignUp route
//                composable(AppDestinations.SIGNUP_ROUTE) {
//                    val auth = FirebaseAuth.getInstance()
//                    // You will need to create the SignUpScreen composable
//                    SignUpScreen(auth = auth, navController = navController)
//                }

                // --- Main App Routes ---
                composable(AppDestinations.HOME_ROUTE) {
                    HomeScreen(
                        navController = navController,
                        openDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(AppDestinations.NEW_REPORT_ROUTE) {
                    ReportWaterSourceScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                // ✅ Passed the NavController to MyReportsScreen
                composable(AppDestinations.MY_REPORTS_ROUTE) {
                    MyReportsScreen(navController = navController)
                }
                composable(AppDestinations.PROFILE_ROUTE) {
                    ProfileScreen(navController = navController)
                }

                composable(AppDestinations.NOTIFICATIONS_ROUTE) { NotificationsScreen() }

                composable(AppDestinations.SETTINGS_ROUTE) {
                    SettingsScreen(navController = navController)
                }
            }
        }
    }
}