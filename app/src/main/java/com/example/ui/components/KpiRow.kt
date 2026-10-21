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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BuildingNode
import com.example.model.DrainNode
import com.example.model.FireDispatchRoute
import com.example.model.FireNode
import com.example.model.RiskLevel
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberNavyCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WaterBlue

@Composable
fun KpiRow(
    fireNodes: List<FireNode>,
    buildingNodes: List<BuildingNode>,
    drainNodes: List<DrainNode>,
    activeRoute: FireDispatchRoute?,
    modifier: Modifier = Modifier
) {
    val activeFireAlerts = fireNodes.count { it.isAlert }
    val greenBuildings = buildingNodes.count { it.riskLevel == RiskLevel.GREEN }
    val amberBuildings = buildingNodes.count { it.riskLevel == RiskLevel.AMBER }
    val redBuildings = buildingNodes.count { it.riskLevel == RiskLevel.RED }
    val atRiskDrains = drainNodes.count { it.isAtRisk }
    val maxDrainPct = drainNodes.maxOfOrNull { it.capacityPercent } ?: 0

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // KPI 1: Active Fire Alerts
        KpiCard(
            modifier = Modifier.weight(1f),
            title = "FIRE SHIELD",
            metric = "$activeFireAlerts ACTIVE",
            subtitle = if (activeFireAlerts > 0) "Edge AI Verified" else "All Sectors Nominal",
            icon = Icons.Default.LocalFireDepartment,
            accentColor = if (activeFireAlerts > 0) CriticalRed else NeonEmerald,
            tag = "kpi_fire"
        )

        // KPI 2: Buildings by Risk
        KpiCard(
            modifier = Modifier.weight(1.2f),
            title = "STRUCTURE RISK",
            metric = "$greenBuildings G • $amberBuildings A • $redBuildings R",
            subtitle = if (redBuildings > 0) "Critical Alert ($redBuildings)" else "${buildingNodes.size} Heritage/High-Rise",
            icon = Icons.Default.Apartment,
            accentColor = if (redBuildings > 0) CriticalRed else if (amberBuildings > 0) WarningAmber else NeonEmerald,
            tag = "kpi_structure"
        )

        // KPI 3: Drains at Risk
        KpiCard(
            modifier = Modifier.weight(1f),
            title = "WATER SHIELD",
            metric = "$atRiskDrains / ${drainNodes.size} DRAINS",
            subtitle = "Peak: $maxDrainPct% cap",
            icon = Icons.Default.WaterDamage,
            accentColor = if (atRiskDrains > 0) CriticalRed else WaterBlue,
            tag = "kpi_water"
        )

        // KPI 4: Response Time & Unit Status
        KpiCard(
            modifier = Modifier.weight(1.1f),
            title = "RESPONSE TIME",
            metric = if (activeRoute != null) "${activeRoute.remainingEtaSeconds}s ETA" else "3.8 min AVG",
            subtitle = if (activeRoute != null) "Rapid Unit En Route" else "-18% vs Baseline",
            icon = Icons.Default.Speed,
            accentColor = if (activeRoute != null) WarningAmber else NeonCyan,
            tag = "kpi_response"
        )
    }
}

@Composable
fun KpiCard(
    title: String,
    metric: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    tag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavyCard)
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .testTag(tag)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = metric,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = if (accentColor == CriticalRed) CriticalRed else TextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = accentColor,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}
