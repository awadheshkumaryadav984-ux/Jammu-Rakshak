package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlertItem
import com.example.model.RiskLevel
import com.example.model.ShieldType
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

@Composable
fun AlertFeed(
    alerts: List<AlertItem>,
    onSelectAlert: (AlertItem) -> Unit,
    onOpenSmsDialog: (AlertItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(ShieldType.ALL) }

    val filteredAlerts = remember(alerts, selectedFilter) {
        if (selectedFilter == ShieldType.ALL) {
            alerts
        } else {
            alerts.filter { it.shield == selectedFilter }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavyDark)
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .testTag("alert_feed_container")
    ) {
        // Feed Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = CriticalRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LIVE INCIDENT STREAM",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CriticalRed.copy(alpha = 0.2f))
                    .border(1.dp, CriticalRed.copy(alpha = 0.5f), CircleShape)
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${alerts.size} EVENTS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = CriticalRed
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Shield Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                ShieldType.ALL to "All",
                ShieldType.FIRE to "Fire",
                ShieldType.STRUCTURE to "Structure",
                ShieldType.WATER to "Water"
            ).forEach { (type, label) ->
                FilterChip(
                    selected = selectedFilter == type,
                    onClick = { selectedFilter = type },
                    label = { Text(label, fontSize = 9.sp, color = if (selectedFilter == type) Color.Black else TextSecondary) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        containerColor = CyberNavyElevated
                    ),
                    modifier = Modifier.height(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Alert Items List
        if (filteredAlerts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No active warnings in selected sector filter.",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredAlerts, key = { it.id }) { alert ->
                    AlertItemRow(
                        alert = alert,
                        onTap = { onSelectAlert(alert) },
                        onOpenSms = { onOpenSmsDialog(alert) }
                    )
                }
            }
        }
    }
}

@Composable
fun AlertItemRow(
    alert: AlertItem,
    onTap: () -> Unit,
    onOpenSms: () -> Unit
) {
    val accentColor = when (alert.risk) {
        RiskLevel.RED -> CriticalRed
        RiskLevel.AMBER -> WarningAmber
        RiskLevel.GREEN -> NeonEmerald
    }

    val shieldLabel = when (alert.shield) {
        ShieldType.FIRE -> "FIRE SHIELD"
        ShieldType.STRUCTURE -> "STRUCTURE"
        ShieldType.WATER -> "WATER DRAIN"
        ShieldType.ALL -> "SYS CORE"
    }

    val timeAgo = remember(alert.timestamp) {
        val diffSec = ((System.currentTimeMillis() - alert.timestamp) / 1000).coerceAtLeast(0)
        when {
            diffSec < 15 -> "Just now"
            diffSec < 60 -> "${diffSec}s ago"
            diffSec < 3600 -> "${diffSec / 60}m ago"
            else -> "${diffSec / 3600}h ago"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberNavyCard)
            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .clickable { onTap() }
            .padding(8.dp)
            .testTag("alert_item_${alert.id}")
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
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = shieldLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = timeAgo,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = alert.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = alert.description,
                fontSize = 10.sp,
                color = TextSecondary,
                lineHeight = 13.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (alert.isAiVerified) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Edge AI Verified",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeonEmerald
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // If Red Risk alert, show SMS / Dispatch trigger button
                if (alert.risk == RiskLevel.RED) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CriticalRed.copy(alpha = 0.2f))
                            .border(1.dp, CriticalRed, RoundedCornerShape(4.dp))
                            .clickable { onOpenSms() }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Message,
                                contentDescription = null,
                                tint = CriticalRed,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SMS / DISPATCH LOG",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = CriticalRed
                            )
                        }
                    }
                }
            }
        }
    }
}
