package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AutoDemoState
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberNavyCard
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WaterBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoControlsDialog(
    isOpen: Boolean,
    autoDemoState: AutoDemoState,
    onDismiss: () -> Unit,
    onTriggerFire: () -> Unit,
    onSimulateRain: () -> Unit,
    onSimulateTremor: () -> Unit,
    onReset: () -> Unit,
    onStartAutoDemo: () -> Unit,
    onStopAutoDemo: () -> Unit
) {
    if (!isOpen) return

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("demo_controls_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberNavyDark)
                .border(1.5.dp, WarningAmber, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "INCIDENT & HAZARD OPERATIONS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = WarningAmber
                        )
                        Text(
                            text = "Operational multi-hazard response and emergency coordination",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Auto Scenario Banner / Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (autoDemoState.isRunning) CriticalRed.copy(alpha = 0.2f) else Color(0xFF1B3B2B))
                        .border(
                            1.dp,
                            if (autoDemoState.isRunning) CriticalRed else NeonEmerald,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (autoDemoState.isRunning) {
                                onStopAutoDemo()
                            } else {
                                onStartAutoDemo()
                                onDismiss()
                            }
                        }
                        .padding(10.dp)
                        .testTag("auto_demo_toggle_btn")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (autoDemoState.isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (autoDemoState.isRunning) CriticalRed else NeonEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (autoDemoState.isRunning) "STOP SCENARIO RUNNER" else "RUN 60s RESILIENCE SCENARIO",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (autoDemoState.isRunning) CriticalRed else NeonEmerald
                                )
                                Text(
                                    text = if (autoDemoState.isRunning) "Running: ${autoDemoState.timeRemainingSeconds}s remaining" else "Coordinated live scenario across all 3 shields",
                                    fontSize = 9.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyberNavyDark)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "60 SEC",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonEmerald
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 1. Trigger Fire
                DemoActionButton(
                    title = "TRIGGER FIRE (LANE INCIDENT)",
                    subtitle = "Spawns emergency fire, plots rapid response route & ETA countdown",
                    icon = Icons.Default.LocalFireDepartment,
                    accentColor = Color(0xFFFF6D00),
                    onClick = {
                        onTriggerFire()
                        onDismiss()
                    },
                    tag = "trigger_fire_btn"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Simulate Heavy Rain
                DemoActionButton(
                    title = "SIMULATE HEAVY RAIN (MONSOON)",
                    subtitle = "Surges drain levels >70%, increases building saturation risk",
                    icon = Icons.Default.Thunderstorm,
                    accentColor = WaterBlue,
                    onClick = {
                        onSimulateRain()
                        onDismiss()
                    },
                    tag = "simulate_rain_btn"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Earthquake Tremor
                DemoActionButton(
                    title = "EARTHQUAKE TREMOR (SEISMIC SHOCK)",
                    subtitle = "Spikes vibration across heritage buildings & triggers red tilt warnings",
                    icon = Icons.Default.Vibration,
                    accentColor = WarningAmber,
                    onClick = {
                        onSimulateTremor()
                        onDismiss()
                    },
                    tag = "simulate_tremor_btn"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 4. Reset
                DemoActionButton(
                    title = "RESET COMMAND BASELINE",
                    subtitle = "Normalizes all telemetry nodes and clears routes",
                    icon = Icons.Default.Refresh,
                    accentColor = NeonCyan,
                    onClick = {
                        onReset()
                        onDismiss()
                    },
                    tag = "reset_simulation_btn"
                )
            }
        }
    }
}

@Composable
fun DemoActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavyCard)
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp)
            .testTag(tag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    color = TextSecondary,
                    lineHeight = 12.sp
                )
            }
        }
    }
}

// Persistent Caption Overlay when Auto Demo is running
@Composable
fun AutoDemoCaptionOverlay(
    autoDemoState: AutoDemoState,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = autoDemoState.isRunning,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xE6070C1B))
                .border(1.5.dp, NeonEmerald, RoundedCornerShape(8.dp))
                .padding(10.dp)
                .testTag("auto_demo_caption_bar")
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NeonEmerald)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AUTO DEMO MODE (${autoDemoState.currentStep}/5)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonEmerald,
                            letterSpacing = 1.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${autoDemoState.timeRemainingSeconds}s",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CriticalRed.copy(alpha = 0.25f))
                                .border(1.dp, CriticalRed, RoundedCornerShape(4.dp))
                                .clickable { onStop() }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "STOP",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CriticalRed
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = autoDemoState.caption,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { ((60 - autoDemoState.timeRemainingSeconds) / 60f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(CircleShape),
                    color = NeonEmerald,
                    trackColor = CyberNavyCard
                )
            }
        }
    }
}
