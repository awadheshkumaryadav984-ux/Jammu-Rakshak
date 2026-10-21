package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlertItem
import com.example.model.CityData
import com.example.model.Language
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyBroadcastDialog(
    alert: AlertItem?,
    currentLanguage: Language,
    onDismiss: () -> Unit
) {
    if (alert == null) return

    var activeLanguage by remember(currentLanguage) { mutableStateOf(currentLanguage) }

    val smsMessage = remember(activeLanguage, alert) {
        CityData.getEmergencySmsMessage(activeLanguage, alert.location, alert.title)
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("emergency_broadcast_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CyberNavyDark)
                .border(2.dp, CriticalRed, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Phone simulation header bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jammu CivAlert 5G • SDMA",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Alert title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CriticalRed.copy(alpha = 0.2f))
                            .border(1.dp, CriticalRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = CriticalRed, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "CRITICAL EMERGENCY BROADCAST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = CriticalRed
                        )
                        Text(
                            text = "Simulated Citizen Emergency Notification & Inter-Agency Dispatch",
                            fontSize = 9.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Language Selector Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Language.values().forEach { lang ->
                        val isSelected = activeLanguage == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) CriticalRed else CyberNavyElevated)
                                .clickable { activeLanguage = lang }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lang.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mock SMS Bubble
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF16213E))
                        .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MOCK SMS: +91 (JK-DISASTER-ALERT)",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = NeonCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = smsMessage,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Multi-Agency Dispatch Log
                Text(
                    text = "INTER-AGENCY NOTIFICATION DISPATCH LOG:",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DispatchAgencyRow(
                        agency = "Fire & Emergency Services J&K",
                        status = "Nearest Unit Dispatched (Route line plotted on map, ETA active)",
                        isDispatched = true
                    )
                    DispatchAgencyRow(
                        agency = "Jammu Municipal Corporation (JMC)",
                        status = "Structural & Drainage quick-response teams notified",
                        isDispatched = true
                    )
                    DispatchAgencyRow(
                        agency = "State Disaster Management Authority (SDMA)",
                        status = "Command log recorded, 200m perimeter advisory broadcast",
                        isDispatched = true
                    )
                    DispatchAgencyRow(
                        agency = "Traffic Police Jammu",
                        status = "Priority green corridor cleared on arterial routes",
                        isDispatched = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("dismiss_broadcast_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberNavyElevated,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("ACKNOWLEDGE & CLOSE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DispatchAgencyRow(agency: String, status: String, isDispatched: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(CyberNavyCard)
            .border(1.dp, CyberBorder, RoundedCornerShape(4.dp))
            .padding(6.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = NeonEmerald,
                modifier = Modifier
                    .size(13.dp)
                    .padding(top = 1.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(text = agency, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = status, fontSize = 9.sp, color = TextSecondary)
            }
        }
    }
}
