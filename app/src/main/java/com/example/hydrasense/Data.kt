package com.example.hydrasense

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// --- Navigation Destinations ---
object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val HOME_ROUTE = "home"
    const val NEW_REPORT_ROUTE = "new_report"
    const val MY_REPORTS_ROUTE = "my_reports"
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val PROFILE_ROUTE = "profile"
    const val SETTINGS_ROUTE = "settings"
}

// --- Data class for Drawer Items ---
data class AppDrawerItemInfo(
    val route: String,
    val icon: ImageVector,
    val title: String
)

// --- List of Drawer Items (only for post-login) ---
val drawerItems = listOf(
    AppDrawerItemInfo(AppDestinations.HOME_ROUTE, Icons.Default.Home, "Dashboard"),
    AppDrawerItemInfo(AppDestinations.NEW_REPORT_ROUTE, Icons.Default.Add, "Report Water"),
    AppDrawerItemInfo(AppDestinations.MY_REPORTS_ROUTE, Icons.Default.List, "My Reports"),
    AppDrawerItemInfo(AppDestinations.NOTIFICATIONS_ROUTE, Icons.Default.Notifications, "Notifications"),
    AppDrawerItemInfo(AppDestinations.PROFILE_ROUTE, Icons.Default.Person, "Profile"),
    AppDrawerItemInfo(AppDestinations.SETTINGS_ROUTE, Icons.Default.Settings, "Settings")
)

// --- Data Models for Water Reports ---
enum class SafetyStatus {
    SAFE,
    POLLUTED
}

data class WaterReport(
    val locationName: String,
    val pH: Double,
    val status: SafetyStatus
)

// --- Dummy Data ---
val dummyReports = listOf(
    WaterReport("Central Park Lake", 7.2, SafetyStatus.SAFE),
    WaterReport("Hudson River - Pier 45", 6.1, SafetyStatus.POLLUTED),
    WaterReport("Brooklyn Bridge Fountain", 7.5, SafetyStatus.SAFE)
)

// --- App Colors ---
val SafeColor = Color(0xFF4CAF50)
val PollutedColor = Color(0xFFF44336)
val BackgroundColor = Color(0xFFF0F0F0)
