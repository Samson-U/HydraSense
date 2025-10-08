package com.example.hydrasense.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hydrasense.R // Make sure to add a dummy avatar image in your res/drawable folder
import com.example.hydrasense.ui.theme.HydraSenseTheme

// --- Data Models for Profile Screen ---

data class UserProfile(
    val name: String,
    val email: String,
    val avatarResId: Int,
    val verifiedReports: Int,
    val pendingReports: Int
)

enum class UserReportStatus {
    VERIFIED, PENDING
}

data class UserReport(
    val locationName: String,
    val locationAddress: String,
    val date: String,
    val phLevel: Double,
    val status: UserReportStatus
)

// --- Dummy Data ---

val dummyProfile = UserProfile(
    name = "John Doe",
    email = "john.doe@gmail.com",
    avatarResId = R.drawable.avatar_placeholder, // Add a placeholder image named 'avatar_placeholder.png' to res/drawable
    verifiedReports = 2,
    pendingReports = 1
)

val dummyUserReports = listOf(
    UserReport("Central Park Lake", "New York, NY", "2024-01-15", 7.2, UserReportStatus.VERIFIED),
    UserReport("Brooklyn Bridge Fountain", "Brooklyn, NY", "2024-01-12", 7.5, UserReportStatus.PENDING),
    UserReport("Queens Borough Well", "Queens, NY", "2024-01-08", 7.3, UserReportStatus.VERIFIED)
)

// --- Colors for this screen ---
val VerifiedBlue = Color(0xFF0D47A1)
val PendingGray = Color(0xFF757575)
val LogoutRed = Color(0xFFD32F2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.SemiBold) },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileHeader(profile = dummyProfile)
                Spacer(Modifier.height(24.dp))
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(
                        value = dummyProfile.verifiedReports.toString(),
                        label = "Verified Reports",
                        color = VerifiedBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = dummyProfile.pendingReports.toString(),
                        label = "Pending Reports",
                        color = PendingGray,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "My Reports",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        dummyUserReports.forEachIndexed { index, report ->
                            UserReportItem(report = report)
                            if (index < dummyUserReports.lastIndex) {
                                Divider(Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                Button(
                    onClick = { /* TODO: Handle Logout Logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LogoutRed)
                ) {
                    Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = profile.avatarResId),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(profile.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(profile.email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { /* TODO: Navigate to Edit Profile */ },
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Edit Profile", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun UserReportItem(report: UserReport) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(report.locationName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(8.dp))
                StatusPill(status = report.status)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(icon = Icons.Default.LocationOn, text = report.locationAddress)
                InfoChip(icon = null, text = report.date)
            }
            Spacer(Modifier.height(4.dp))
            Text("pH Level: ${report.phLevel}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun StatusPill(status: UserReportStatus) {
    val (text, color) = when (status) {
        UserReportStatus.VERIFIED -> "verified" to VerifiedBlue
        UserReportStatus.PENDING -> "pending" to PendingGray
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = color,
        contentColor = Color.White
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun InfoChip(icon: ImageVector?, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
            Spacer(Modifier.width(4.dp))
        }
        Text(text, fontSize = 12.sp, color = Color.Gray)
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun ProfileScreenPreview() {
    // Remember to add a placeholder image named 'avatar_placeholder.png' in your res/drawable folder for this preview to work.
    HydraSenseTheme {
        ProfileScreen()
    }
}