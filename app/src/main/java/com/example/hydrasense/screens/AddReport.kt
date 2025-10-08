package com.example.hydrasense.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
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
import com.example.hydrasense.ui.theme.HydraSenseTheme
import java.text.DecimalFormat

// A custom green color to match the submit button in the image
val SubmitGreen = Color(0xFF34A853)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportWaterSourceScreen(onNavigateBack: () -> Unit) {
    // State variables for all the input fields
    var waterSourceName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phLevel by remember { mutableStateOf(7.0f) }
    var turbidity by remember { mutableStateOf(5f) }
    var temperature by remember { mutableStateOf(20f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Water Source", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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

            // Location Input
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                placeholder = { Text("Enter location or coordinates") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { /* TODO: Auto-detect location logic */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Auto-detect")
            }

            Spacer(Modifier.height(24.dp))

            // Sliders for water quality parameters
            InfoSlider(
                label = "pH Level",
                icon = Icons.Default.Science,
                value = phLevel,
                onValueChange = { phLevel = it },
                valueRange = 0f..14f,
                steps = 13, // 14 steps (0 to 14) means 13 divisions
                startLabel = "Acidic (0)",
                midLabel = "Neutral (7)",
                endLabel = "Basic (14)",
                valueFormat = "0.0"
            )

            Spacer(Modifier.height(24.dp))

            InfoSlider(
                label = "Turbidity",
                icon = Icons.Default.WaterDrop,
                value = turbidity,
                onValueChange = { turbidity = it },
                valueRange = 0f..100f,
                steps = 99,
                startLabel = "Clear (0)",
                midLabel = "Moderate (50)",
                endLabel = "Cloudy (100)",
                unit = " NTU"
            )

            Spacer(Modifier.height(24.dp))

            InfoSlider(
                label = "Temperature",
                icon = Icons.Default.Thermostat,
                value = temperature,
                onValueChange = { temperature = it },
                valueRange = 0f..40f,
                steps = 39,
                startLabel = "Cold (0°C)",
                midLabel = "Room (20°C)",
                endLabel = "Hot (40°C)",
                unit = "°C"
            )

            Spacer(Modifier.height(24.dp))

            // Photo Attachment
            Text("Photo (Optional)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .clickable { /* TODO: Photo picker logic */ }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Attach Photo", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { /* TODO: Submit report logic */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SubmitGreen)
                ) {
                    Text("Submit Report")
                }
            }
        }
    }
}

/**
 * A reusable Composable for the labeled sliders with icons and range labels.
 */
@Composable
private fun InfoSlider(
    label: String,
    icon: ImageVector,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    startLabel: String,
    midLabel: String?,
    endLabel: String,
    valueFormat: String = "0",
    unit: String = ""
) {
    val decimalFormat = remember(valueFormat) { DecimalFormat(valueFormat) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "$label: ${decimalFormat.format(value)}$unit",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.fillMaxWidth()) {
            Text(startLabel, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
            if (midLabel != null) {
                Text(midLabel, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }
            Text(endLabel, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun ReportWaterSourceScreenPreview() {
    HydraSenseTheme {
        ReportWaterSourceScreen(onNavigateBack = {})
    }
}