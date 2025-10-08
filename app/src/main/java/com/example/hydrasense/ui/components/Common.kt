package com.example.hydrasense.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hydrasense.AppDestinations
import com.example.hydrasense.AppDrawerItemInfo
import com.example.hydrasense.drawerItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    navController: NavController,
    currentRoute: String,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = Color.White,
        drawerContentColor = Color.Black
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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

            // Navigation Section
            Text(
                text = "NAVIGATION",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            drawerItems.take(4).forEach { item ->
                DrawerItem(item, navController, currentRoute, closeDrawer)
            }

            // Account Section
            Text(
                text = "ACCOUNT",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
            )
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

@Composable
private fun DrawerItem(
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
            if (!isSelected) {
                navController.navigate(item.route) {
                    popUpTo(AppDestinations.HOME_ROUTE) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}