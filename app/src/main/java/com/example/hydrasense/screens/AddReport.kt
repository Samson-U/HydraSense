package com.example.hydrasense.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hydrasense.ui.theme.HydraSenseTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DecimalFormat

val SubmitGreen = Color(0xFF34A853)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportWaterSourceScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    // Input states
    var waterSourceName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var phLevel by remember { mutableStateOf(7f) }
    var turbidity by remember { mutableStateOf(5f) }
    var temperature by remember { mutableStateOf(20f) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Water Source", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Text(
                text = "Water Quality Report",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Water Source Name
            OutlinedTextField(
                value = waterSourceName,
                onValueChange = { waterSourceName = it },
                label = { Text("Water Source Name") },
                placeholder = { Text("e.g., Central Park Lake") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Location input
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = if (latitude != 0.0) latitude.toString() else "",
                    onValueChange = { latitude = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Latitude") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = if (longitude != 0.0) longitude.toString() else "",
                    onValueChange = { longitude = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Longitude") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    // TODO: Add FusedLocationProviderClient to auto-detect GPS
                    Toast.makeText(context, "Auto-detect location not implemented yet", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Auto-detect")
            }

            Spacer(Modifier.height(24.dp))

            // Sliders
            InfoSlider("pH Level", Icons.Default.Science, phLevel, { phLevel = it }, 0f..14f, 13, "Acidic (0)", "Neutral (7)", "Basic (14)", "0.0")
            Spacer(Modifier.height(24.dp))
            InfoSlider("Turbidity", Icons.Default.WaterDrop, turbidity, { turbidity = it }, 0f..10f, 9, "Clear (0)", "Moderate (5)", "Cloudy (10)", unit = " NTU")
            Spacer(Modifier.height(24.dp))
            InfoSlider("Temperature", Icons.Default.Thermostat, temperature, { temperature = it }, 0f..40f, 39, "Cold (0°C)", "Room (20°C)", "Hot (40°C)", unit = "°C")

            Spacer(Modifier.height(24.dp))

            // Photo picker
            Text("Photo (Optional)", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .clickable { photoPickerLauncher.launch("image/*") }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(if (photoUri != null) "Photo Selected" else "Attach Photo", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(32.dp))

            // Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(onClick = onNavigateBack, modifier = Modifier.weight(1f)) { Text("Cancel") }
                Button(
                    onClick = {
                        val userId = Firebase.auth.currentUser?.uid ?: "anonymous"

                        fun saveReport(photoUrl: String) {
                            val reportData = hashMapOf(
                                "UserId" to userId,
                                "WaterSourceName" to waterSourceName,
                                "Location" to mapOf("latitude" to latitude, "longitude" to longitude),
                                "ph" to phLevel,
                                "turbidity" to turbidity,
                                "temperature" to temperature,
                                "imageProof" to photoUrl
                            )
                            Firebase.firestore.collection("WaterSources")
                                .add(reportData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Report submitted!", Toast.LENGTH_SHORT).show()
                                    // Reset fields
                                    waterSourceName = ""
                                    latitude = 0.0
                                    longitude = 0.0
                                    phLevel = 7f
                                    turbidity = 5f
                                    temperature = 20f
                                    photoUri = null
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }

                        if (photoUri != null) {
                            val storageRef = Firebase.storage.reference.child("images/${System.currentTimeMillis()}.jpg")
                            storageRef.putFile(photoUri!!)
                                .addOnSuccessListener {
                                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                                        saveReport(uri.toString())
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Photo upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            saveReport("")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SubmitGreen)
                ) {
                    Text("Submit Report")
                }
            }
        }
    }
}

@Composable
private fun InfoSlider(
    label: String,
    icon: ImageVector,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    startLabel: String,
    midLabel: String? = null,
    endLabel: String,
    valueFormat: String = "0",
    unit: String = ""
) {
    val decimalFormat = remember(valueFormat) { DecimalFormat(valueFormat) }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text("$label: ${decimalFormat.format(value)}$unit", fontWeight = FontWeight.Medium)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange, steps = steps, modifier = Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth()) {
            Text(startLabel, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
            if (midLabel != null) Text(midLabel, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(endLabel, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6") // Added a device for better previewing
@Composable
fun ReportWaterSourceScreenPreview() {
    HydraSenseTheme {
        ReportWaterSourceScreen(onNavigateBack = {}) // ✅ Correct: Passes an empty function
    }
}
