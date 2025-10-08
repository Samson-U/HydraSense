package com.example.hydrasense.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hydrasense.ui.theme.HydraSenseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController = rememberNavController()) {
    // State management for all settings
    var pushNotifications by remember { mutableStateOf(true) }
    var emailAlerts by remember { mutableStateOf(true) }
    var locationSharing by remember { mutableStateOf(false) }
    var autoSubmit by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsGroupCard(icon = Icons.Default.Notifications, title = "Notifications") {
                    SettingsSwitchItem(
                        title = "Push Notifications",
                        subtitle = "Receive alerts about water quality changes",
                        checked = pushNotifications,
                        onCheckedChange = { pushNotifications = it }
                    )
                    Divider(Modifier.padding(horizontal = 16.dp))
                    SettingsSwitchItem(
                        title = "Email Alerts",
                        subtitle = "Get water quality reports via email",
                        checked = emailAlerts,
                        onCheckedChange = { emailAlerts = it }
                    )
                    Divider(Modifier.padding(horizontal = 16.dp))
                    SettingsDropDownItem(
                        title = "Alert Radius",
                        options = listOf("1 km", "5 km", "10 km", "25 km"),
                        initialSelection = "5 km"
                    )
                }
            }

            item {
                SettingsGroupCard(icon = Icons.Default.Shield, title = "Privacy & Security") {
                    SettingsSwitchItem(
                        title = "Location Sharing",
                        subtitle = "Share your location for better water quality tracking",
                        checked = locationSharing,
                        onCheckedChange = { locationSharing = it }
                    )
                    Divider(Modifier.padding(horizontal = 16.dp))
                    SettingsSwitchItem(
                        title = "Automatic Reports",
                        subtitle = "Auto-submit basic water quality data when detected",
                        checked = autoSubmit,
                        onCheckedChange = { autoSubmit = it }
                    )
                }
            }

            item {
                SettingsGroupCard(icon = Icons.Default.Palette, title = "Appearance") {
                    SettingsDropDownItem(
                        title = "Theme",
                        options = listOf("Light", "Dark", "System Default"),
                        initialSelection = "Light"
                    )
                    Divider(Modifier.padding(horizontal = 16.dp))
                    SettingsDropDownItem(
                        title = "Language",
                        options = listOf("English", "Spanish", "French"),
                        initialSelection = "English"
                    )
                }
            }

            item {
                SettingsGroupCard(icon = Icons.Default.Info, title = "About & Support") {
                    AboutInfoItem(label = "App Version", value = "1.0.0")
                    AboutInfoItem(label = "Last Updated", value = "January 2024")
                    Spacer(Modifier.height(8.dp))
                    SettingsClickableItem(text = "Privacy Policy")
                    SettingsClickableItem(text = "Terms of Service")
                    SettingsClickableItem(text = "Contact Support")
                    SettingsClickableItem(text = "Report a Bug")
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Reset settings to default */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reset to Default")
                    }
                    Button(
                        onClick = { /* TODO: Save settings */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
                    ) {
                        Text("Save Settings")
                    }
                }
            }
        }
    }
}


// --- Reusable Components for the Settings Screen ---

@Composable
private fun SettingsGroupCard(
    icon: ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(16.dp))
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
            content()
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, lineHeight = 18.sp)
        }
        Spacer(Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = null) // Null lambda as Row is clickable
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropDownItem(
    title: String,
    options: List<String>,
    initialSelection: String
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initialSelection) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .width(150.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SettingsClickableItem(text: String) {
    OutlinedButton(
        onClick = { /* TODO: Navigate or perform action */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SettingsScreenPreview() {
    HydraSenseTheme {
        SettingsScreen()
    }
}