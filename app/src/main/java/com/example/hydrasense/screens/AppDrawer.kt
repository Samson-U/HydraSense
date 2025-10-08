package com.example.hydrasense.screens

// File: AppDrawer.kt (or placed in HomeScreen.kt for simplicity)

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch // Used by popUpTo/launchSingleTop arguments

// NOTE: Since your MainActivity uses these from the main package,
// these placeholders must be defined/imported correctly:
// We define them here for a clean, compilable file:

// --- Placeholder for AppDestinations (MUST MATCH definition in MainActivity) ---
object AppDestinations {
    const val HOME_ROUTE = "home"
    const val NEW_REPORT_ROUTE = "new_report"
    const val MY_REPORTS_ROUTE = "my_reports"
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val PROFILE_ROUTE = "profile"
    const val SETTINGS_ROUTE = "settings"
}

// --- Placeholder for AppDrawerItemInfo (MUST MATCH definition in MainActivity) ---
data class AppDrawerItemInfo(
    val route: String,
    val icon: ImageVector,
    val title: String
)


// The list of all items shown in the drawer
val drawerItems = listOf(
    // NAVIGATION Section
    AppDrawerItemInfo(AppDestinations.HOME_ROUTE, Icons.Default.Home, "Dashboard"),
    AppDrawerItemInfo(AppDestinations.NEW_REPORT_ROUTE, Icons.Default.Add, "Report Water"),
    AppDrawerItemInfo(AppDestinations.MY_REPORTS_ROUTE, Icons.Default.List, "My Reports"),
    AppDrawerItemInfo(AppDestinations.NOTIFICATIONS_ROUTE, Icons.Default.Notifications, "Notifications"),

    // ACCOUNT Section (Using placeholders for separation)
    AppDrawerItemInfo(AppDestinations.PROFILE_ROUTE, Icons.Default.Person, "Profile"),
    AppDrawerItemInfo(AppDestinations.SETTINGS_ROUTE, Icons.Default.Settings, "Settings"),
)

// --- AppDrawer Composable ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    navController: NavController,
    currentRoute: String,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp) // Set a fixed width for the drawer
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {

            // Header: Water Quality and Close Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Water Quality",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = closeDrawer) {
                    Icon(Icons.Default.Close, contentDescription = "Close Menu")
                }
            }

            // NAVIGATION Section Header
            Text(
                text = "NAVIGATION",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            // Navigation Items
            drawerItems.take(4).forEach { item ->
                DrawerItem(item, navController, currentRoute, closeDrawer)
            }

            // ACCOUNT Section Header
            Text(
                text = "ACCOUNT",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
            )

            // Account Items
            drawerItems.drop(4).forEach { item ->
                DrawerItem(item, navController, currentRoute, closeDrawer)
            }

            // Footer
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Water Quality App v1.0",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// --- DrawerItem Composable ---

@Composable
fun DrawerItem(
    item: AppDrawerItemInfo,
    navController: NavController,
    currentRoute: String,
    closeDrawer: () -> Unit
) {
    val isSelected = currentRoute == item.route

    NavigationDrawerItem(
        label = { Text(item.title) },
        icon = { Icon(item.icon, contentDescription = item.title) },
        selected = isSelected,
        onClick = {
            // Navigate only if not already on this route
            if (!isSelected) {
                navController.navigate(item.route) {
                    // Navigate back to the home route and clear all other back stack entries
                    popUpTo(AppDestinations.HOME_ROUTE) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            closeDrawer() // Close the drawer after navigation
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}