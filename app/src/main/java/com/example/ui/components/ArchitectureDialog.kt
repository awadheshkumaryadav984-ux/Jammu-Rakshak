package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberNavyCard
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchitectureDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("architecture_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberNavyDark)
                .border(1.5.dp, NeonCyan, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Hub, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "SYSTEM ARCHITECTURE & DESIGN",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Jammu Smart City Hackathon • Theme 06 • Team ENDLIFERZ",
                                fontSize = 9.sp,
                                color = NeonCyan
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hackathon Executive Summary
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberNavyCard)
                        .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Jammu Rakshak: The 3-Shield Urban Resilience Framework",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningAmber
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "A single live unified dashboard consolidating three defense shields across Jammu city: Fire Shield (detect thermal & toxic spikes), Structure Shield (monitor ancient masonry tilt & seismic vibration), and Water Shield (absorb urban runoff via sponge infrastructure).",
                            fontSize = 9.5.sp,
                            color = TextSecondary,
                            lineHeight = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Architecture Pipeline Diagram
                Text(
                    text = "END-TO-END TELEMETRY PIPELINE:",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                val stages = listOf(
                    Triple("1. Sensors (LoRaWAN Mesh)", "DHT22 (Temp), MQ-2 (Smoke/Gas), MPU6050 (Tilt/Vib), Ultrasonic Water Depth", NeonCyan),
                    Triple("2. Telemetry Ingestion (MQTT Broker)", "Low-power gateways at Jewel Chowk, Janipur, Bahu Fort transmitting packet frames", TextPrimary),
                    Triple("3. High-Throughput Geo Engine", "GeoJSON indexing, spatial risk matrix, nearest fire station route solver", WarningAmber),
                    Triple("4. AI Inference Engine (Gemini 3.5 Flash)", "Edge noise filtering, false alarm verification, multimodal crack forensic assessment", PurpleAccent),
                    Triple("5. Jammu Rakshak Dashboard", "Real-time tactical GIS canvas, live charts, 3 shields telemetry stream", NeonEmerald),
                    Triple("6. Automated Multi-Agency Dispatch", "Instant emergency SMS in Dogri, Hindi, Urdu & English to JMC, Fire & SDMA", Color(0xFFFF5252))
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    stages.forEachIndexed { index, (stageTitle, stageDesc, stageColor) ->
                        ArchStageCard(title = stageTitle, description = stageDesc, color = stageColor)
                        if (index < stages.size - 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Team & Theme Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "DEVELOPED BY: TEAM ENDLIFERZ", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(text = "Jammu Smart City Hackathon 2026", fontSize = 9.sp, color = TextSecondary)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.height(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("CLOSE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ArchStageCard(title: String, description: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberNavyCard)
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(7.dp)
    ) {
        Column {
            Text(text = title, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, fontSize = 9.sp, color = TextSecondary)
        }
    }
}
