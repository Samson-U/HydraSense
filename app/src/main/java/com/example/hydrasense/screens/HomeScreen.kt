package com.example.hydrasense.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hydrasense.AppDestinations
import com.example.hydrasense.BackgroundColor
import com.example.hydrasense.PollutedColor
import com.example.hydrasense.SafeColor
import com.example.hydrasense.SafetyStatus
import com.example.hydrasense.WaterReport
import com.example.hydrasense.dummyReports
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(navController: NavController, openDrawer: () -> Unit) {
    Scaffold(
        // ✅ FIXED: Restored your custom TopBar
        topBar = { TopBar(openDrawer) },
        floatingActionButton = {
            AddReportFab {
                navController.navigate(AppDestinations.NEW_REPORT_ROUTE)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {
            item { WaterSourcesMap() }
            item { SummaryCardsRow() }
            item { RecentReportsSection(dummyReports) }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// --- HomeScreen Specific Composables ---

@Composable
private fun TopBar(openDrawer: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = openDrawer) {
            Icon(Icons.Default.Menu, "Menu", modifier = Modifier.size(24.dp))
        }
        Text("Welcome Samson", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Icon(Icons.Default.Notifications, "Notifications", modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun WaterSourcesMap() {
    // Define locations for the map markers
    val centralPark = LatLng(40.785091, -73.968285)
    val hudsonRiver = LatLng(40.7489, -74.0094)
    val brooklynFountain = LatLng(40.7000, -73.9920)

    // Set the initial camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centralPark, 11f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // ✅ FIXED: The GoogleMap should be the main content of the Card
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(state = MarkerState(position = centralPark), title = "Central Park Lake")
            Marker(state = MarkerState(position = hudsonRiver), title = "Hudson River - Pier 45")
            Marker(state = MarkerState(position = brooklynFountain), title = "Brooklyn Bridge Fountain")
        }
    }
}


// --- The rest of your file is correct and doesn't need changes ---

@Composable
private fun SummaryCardsRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryCard("Nearby", "5", Color.Black, Modifier.weight(1f))
        SummaryCard("Safe", "3", SafeColor, Modifier.weight(1f))
        SummaryCard("Polluted", "2", PollutedColor, Modifier.weight(1f))
    }
}

@Composable
private fun SummaryCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, color = color, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun RecentReportsSection(reports: List<WaterReport>) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "Recent Water Quality Reports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.background(Color.White)) {
                reports.forEachIndexed { index, report ->
                    ReportItem(report)
                    if (index < reports.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFE0E0E0))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportItem(report: WaterReport) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, "Location", tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text(report.locationName, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                Text("pH: ${report.pH}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
        StatusBadge(report.status)
    }
}

@Composable
private fun StatusBadge(status: SafetyStatus) {
    val color = if (status == SafetyStatus.SAFE) SafeColor else PollutedColor
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            status.name.lowercase(),
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun AddReportFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = SafeColor,
        shape = CircleShape
    ) {
        Icon(Icons.Filled.Add, "Add new report", tint = Color.White)
    }
}