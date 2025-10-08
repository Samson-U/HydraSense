package com.example.hydrasense.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hydrasense.AppDestinations
import com.example.hydrasense.ui.theme.HydraSenseTheme

// --- Data Models for this Screen ---

enum class ReportStatus {
    APPROVED, PENDING, REJECTED, ALL
}

data class ReportHistory(
    val locationName: String,
    val locationAddress: String,
    val ph: Double,
    val turbidity: Int,
    val temperature: Int,
    val submittedDate: String,
    val reviewedDate: String?,
    val status: ReportStatus,
    val reviewComments: String?
)

// --- Dummy Data for Preview ---

val dummyHistoryReports = listOf(
    ReportHistory(
        locationName = "Central Park Lake",
        locationAddress = "New York, NY",
        ph = 7.2,
        turbidity = 3,
        temperature = 18,
        submittedDate = "Jan 15, 2024, 04:00 PM",
        reviewedDate = "Jan 15, 2024, 07:50 PM",
        status = ReportStatus.APPROVED,
        reviewComments = "Report verified. Good water quality indicators."
    ),
    ReportHistory(
        locationName = "Hudson River - Pier 45",
        locationAddress = "New York, NY",
        ph = 6.1,
        turbidity = 8,
        temperature = 16,
        submittedDate = "Jan 12, 2024, 02:45 PM",
        reviewedDate = "Jan 13, 2024, 09:00 AM",
        status = ReportStatus.APPROVED,
        reviewComments = "pH level is slightly acidic but within acceptable range."
    ),
    ReportHistory(
        locationName = "Brooklyn Bridge Fountain",
        locationAddress = "Brooklyn, NY",
        ph = 7.5,
        turbidity = 1,
        temperature = 22,
        submittedDate = "Jan 11, 2024, 11:10 AM",
        reviewedDate = null,
        status = ReportStatus.PENDING,
        reviewComments = null
    ),
    ReportHistory(
        locationName = "East River Park",
        locationAddress = "New York, NY",
        ph = 8.8,
        turbidity = 12,
        temperature = 15,
        submittedDate = "Jan 10, 2024, 08:00 AM",
        reviewedDate = "Jan 10, 2024, 10:30 AM",
        status = ReportStatus.REJECTED,
        reviewComments = "pH level is too high (alkaline). Requires further investigation."
    )
)

// --- Colors for Status Badges ---
val ApprovedGreen = Color(0xFF34A853)
val PendingOrange = Color(0xFFFBBC05)
val RejectedRed = Color(0xFFEA4335)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReportsScreen(navController: NavController = rememberNavController()) {
    var selectedStatus by remember { mutableStateOf(ReportStatus.ALL) }

    val filteredReports = remember(selectedStatus, dummyHistoryReports) {
        if (selectedStatus == ReportStatus.ALL) {
            dummyHistoryReports
        } else {
            dummyHistoryReports.filter { it.status == selectedStatus }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Reports", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { navController.navigate(AppDestinations.NEW_REPORT_ROUTE) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("New Report")
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
            // Summary Cards Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SummaryStatCard("5", "Total Reports", Modifier.weight(1f))
                        SummaryStatCard("3", "Approved", Modifier.weight(1f), ApprovedGreen)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SummaryStatCard("1", "Pending", Modifier.weight(1f), PendingOrange)
                        SummaryStatCard("1", "Rejected", Modifier.weight(1f), RejectedRed)
                    }
                }
            }

            // Report History Section
            item {
                Column {
                    Text(
                        "Report History",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    FilterChips(
                        selectedStatus = selectedStatus,
                        onStatusSelected = { selectedStatus = it }
                    )
                }
            }

            // Report List
            items(filteredReports.size) { index ->
                ReportHistoryItem(report = filteredReports[index])
            }
        }
    }
}

@Composable
private fun SummaryStatCard(value: String, label: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Card(modifier = modifier.height(100.dp), shape = RoundedCornerShape(12.dp)) { // Set a fixed height for consistency
        Column(
            modifier = Modifier
                .fillMaxSize() // ✅ Add this to make the Column fill the Card
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = color.takeIf { it != Color.Unspecified } ?: MaterialTheme.colorScheme.onSurface)
            Text(label, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChips(selectedStatus: ReportStatus, onStatusSelected: (ReportStatus) -> Unit) {
    // ✅ Add horizontalScroll and rememberScrollState to the Row modifier
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), // <-- ADD THIS LINE
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val statuses = mapOf(
            ReportStatus.ALL to dummyHistoryReports.size,
            ReportStatus.PENDING to dummyHistoryReports.count { it.status == ReportStatus.PENDING },
            ReportStatus.APPROVED to dummyHistoryReports.count { it.status == ReportStatus.APPROVED },
            ReportStatus.REJECTED to dummyHistoryReports.count { it.status == ReportStatus.REJECTED }
        )

        statuses.forEach { (status, count) ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = { Text("${status.name.replaceFirstChar { it.uppercase() }} ($count)") }
            )
        }
    }
}


@Composable
private fun ReportHistoryItem(report: ReportHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Location and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(report.locationName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        if (report.status == ReportStatus.APPROVED) {
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = "Approved", tint = ApprovedGreen, modifier = Modifier.size(18.dp))
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(report.locationAddress, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
                StatusBadge(status = report.status)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Metrics: pH, Turbidity, Temp
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                MetricItem("pH", report.ph.toString())
                MetricItem("Turbidity", "${report.turbidity} NTU")
                MetricItem("Temp", "${report.temperature}°C")
            }

            Spacer(Modifier.height(16.dp))

            // Timestamps
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TimestampItem(icon = Icons.Default.CalendarMonth, text = "Submitted: ${report.submittedDate}")
                if (report.reviewedDate != null) {
                    TimestampItem(icon = Icons.Default.Schedule, text = "Reviewed: ${report.reviewedDate}")
                }
            }

            // Review Comments
            if (report.reviewComments != null) {
                Spacer(Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Review Comments:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Text(report.reviewComments, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TimestampItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
private fun StatusBadge(status: ReportStatus) {
    val (color, text) = when (status) {
        ReportStatus.APPROVED -> ApprovedGreen to "Approved"
        ReportStatus.PENDING -> PendingOrange to "Pending"
        ReportStatus.REJECTED -> RejectedRed to "Rejected"
        else -> Color.Gray to "Unknown"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun MyReportsScreenPreview() {
    HydraSenseTheme {
        MyReportsScreen()
    }
}