package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BuildingNode
import com.example.model.CityData
import com.example.model.DrainNode
import com.example.model.FireNode
import com.example.model.RiskLevel
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
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ShieldTabs(
    fireNodes: List<FireNode>,
    buildingNodes: List<BuildingNode>,
    drainNodes: List<DrainNode>,
    onSelectNode: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavyDark)
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
            .padding(6.dp)
            .testTag("shield_tabs_container")
    ) {
        // Tab Navigation
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = CyberNavyCard,
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = when (selectedTab) {
                        0 -> Color(0xFFFF9100)
                        1 -> WarningAmber
                        else -> WaterBlue
                    },
                    height = 2.dp
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .height(34.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = if (selectedTab == 0) Color(0xFFFF9100) else TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "FIRE SHIELD (DETECT)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 0) Color(0xFFFF9100) else TextSecondary
                        )
                    }
                }
            )

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = if (selectedTab == 1) WarningAmber else TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "STRUCTURE SHIELD (MONITOR)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) WarningAmber else TextSecondary
                        )
                    }
                }
            )

            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDamage,
                            contentDescription = null,
                            tint = if (selectedTab == 2) WaterBlue else TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "WATER SHIELD (ABSORB)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 2) WaterBlue else TextSecondary
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            when (selectedTab) {
                0 -> FireShieldContent(fireNodes = fireNodes, onSelectNode = onSelectNode)
                1 -> StructureShieldContent(buildingNodes = buildingNodes, onSelectNode = onSelectNode)
                2 -> WaterShieldContent(drainNodes = drainNodes, onSelectNode = onSelectNode)
            }
        }
    }
}

// ==================== FIRE SHIELD ====================
@Composable
fun FireShieldContent(
    fireNodes: List<FireNode>,
    onSelectNode: (Any) -> Unit
) {
    val tempHistory = remember { mutableStateListOf(28.0f, 29.2f, 27.5f, 31.0f, 30.4f, 29.8f, 32.1f, 31.5f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            val avgTemp = fireNodes.map { it.temperatureC.toFloat() }.average().toFloat()
            if (tempHistory.size >= 12) tempHistory.removeAt(0)
            tempHistory.add(avgTemp)
        }
    }

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Left: Live Temperature & Smoke Chart
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
                .background(CyberNavyCard)
                .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ROLLING THERMAL & SMOKE TELEMETRY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = "LIVE SENSORS",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFFF9100)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    TelemetryMiniChart(
                        dataPoints = tempHistory,
                        lineColor = Color(0xFFFF9100),
                        fillColor = Color(0x33FF9100),
                        minValue = 20f,
                        maxValue = 85f,
                        unit = "°C"
                    )
                }
            }
        }

        // Right: Node Telemetry List
        LazyColumn(
            modifier = Modifier
                .weight(1.8f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(fireNodes, key = { it.id }) { node ->
                FireNodeRow(node = node, onClick = { onSelectNode(node) })
            }
        }
    }
}

@Composable
fun FireNodeRow(node: FireNode, onClick: () -> Unit) {
    val isHot = node.isAlert
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(if (isHot) CriticalRed.copy(alpha = 0.15f) else CyberNavyCard)
            .border(
                1.dp,
                if (isHot) CriticalRed else CyberBorder,
                RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = node.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isHot) CriticalRed else TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${node.location} • Gas: ${node.gasLevelPpm} ppm",
                    fontSize = 9.sp,
                    color = TextSecondary
                )
                if (node.isAiVerified) {
                    Text(
                        text = "✓ Edge AI verified, false alarm filtered",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${node.temperatureC}°C",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = if (isHot) CriticalRed else Color(0xFFFF9100)
                )
                Text(
                    text = "${node.smokePpm} ppm",
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }
        }
    }
}

// ==================== STRUCTURE SHIELD ====================
@Composable
fun StructureShieldContent(
    buildingNodes: List<BuildingNode>,
    onSelectNode: (Any) -> Unit
) {
    val vibHistory = remember { mutableStateListOf(0.35f, 0.42f, 0.38f, 0.45f, 0.40f, 0.52f, 0.44f, 0.48f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            val avgVib = buildingNodes.map { it.vibrationMms2.toFloat() }.average().toFloat()
            if (vibHistory.size >= 12) vibHistory.removeAt(0)
            vibHistory.add(avgVib)
        }
    }

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Left: Structural Vibration & Tilt Chart
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
                .background(CyberNavyCard)
                .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "HERITAGE SEISMIC / VIBRATION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = "ACCEL LOG",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = WarningAmber
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    TelemetryMiniChart(
                        dataPoints = vibHistory,
                        lineColor = WarningAmber,
                        fillColor = Color(0x33FFB300),
                        minValue = 0f,
                        maxValue = 3.0f,
                        unit = "mm/s²"
                    )
                }
            }
        }

        // Right: Buildings List
        LazyColumn(
            modifier = Modifier
                .weight(1.8f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(buildingNodes, key = { it.id }) { bld ->
                BuildingNodeRow(bld = bld, onClick = { onSelectNode(bld) })
            }
        }
    }
}

@Composable
fun BuildingNodeRow(bld: BuildingNode, onClick: () -> Unit) {
    val riskColor = when (bld.riskLevel) {
        RiskLevel.RED -> CriticalRed
        RiskLevel.AMBER -> WarningAmber
        RiskLevel.GREEN -> NeonEmerald
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(CyberNavyCard)
            .border(1.dp, riskColor.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bld.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${bld.structureType} • ${bld.ageYears} yrs",
                    fontSize = 9.sp,
                    color = TextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Tilt: ${bld.tiltAngleDeg}° | Crack: ${bld.crackWidthMm}mm",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary
                    )
                    Text(
                        text = "${bld.vibrationMms2} mm/s²",
                        fontSize = 8.5.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(riskColor.copy(alpha = 0.2f))
                        .border(1.dp, riskColor, RoundedCornerShape(3.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = bld.riskLevel.name,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = riskColor
                    )
                }
            }
        }
    }
}

// ==================== WATER SHIELD ====================
@Composable
fun WaterShieldContent(
    drainNodes: List<DrainNode>,
    onSelectNode: (Any) -> Unit
) {
    val levelHistory = remember { mutableStateListOf(45f, 48f, 52f, 50f, 55f, 58f, 62f, 60f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            val avgCap = drainNodes.map { it.capacityPercent.toFloat() }.average().toFloat()
            if (levelHistory.size >= 12) levelHistory.removeAt(0)
            levelHistory.add(avgCap)
        }
    }

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Left: Drains Capacity Chart & Sponge Comparison
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CyberNavyCard)
                    .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                    .padding(6.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "DRAINAGE SATURATION",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Text(
                            text = "THRESHOLD 70%",
                            fontSize = 8.5.sp,
                            fontFamily = FontFamily.Monospace,
                            color = WaterBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Box(modifier = Modifier.fillMaxSize()) {
                        TelemetryMiniChart(
                            dataPoints = levelHistory,
                            lineColor = WaterBlue,
                            fillColor = Color(0x332979FF),
                            minValue = 0f,
                            maxValue = 100f,
                            unit = "%"
                        )
                    }
                }
            }

            // Sponge Neighbourhood Impact Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0A261E))
                    .border(1.dp, NeonEmerald.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = NeonEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "SPONGE CITY ACTIVE MITIGATION",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonEmerald
                        )
                        Text(
                            text = "Waterlogging reduced: 7.5 hrs → 1.1 hrs (-85%)",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Right: Drains List with capacity progress bar
        LazyColumn(
            modifier = Modifier
                .weight(1.8f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(drainNodes, key = { it.id }) { drain ->
                DrainNodeRow(drain = drain, onClick = { onSelectNode(drain) })
            }
        }
    }
}

@Composable
fun DrainNodeRow(drain: DrainNode, onClick: () -> Unit) {
    val isAlert = drain.isAtRisk
    val pct = drain.capacityPercent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(CyberNavyCard)
            .border(
                1.dp,
                if (isAlert) CriticalRed else CyberBorder,
                RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = drain.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAlert) CriticalRed else TextPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "${drain.location} • Flow: ${drain.flowRateM3s} m³/s",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$pct%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = if (isAlert) CriticalRed else WaterBlue
                    )
                    Text(
                        text = "${drain.waterLevelCm} / ${drain.maxCapacityCm} cm",
                        fontSize = 8.5.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { (pct / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape),
                color = if (isAlert) CriticalRed else WaterBlue,
                trackColor = CyberNavyDark
            )
        }
    }
}

// ==================== SHARED SPARKLINE CHART ====================
@Composable
fun TelemetryMiniChart(
    dataPoints: List<Float>,
    lineColor: Color,
    fillColor: Color,
    minValue: Float,
    maxValue: Float,
    unit: String
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (dataPoints.isEmpty()) return@Canvas

        val w = size.width
        val h = size.height

        // Subtle grid lines
        drawLine(
            color = Color(0x1AFFFFFF),
            start = Offset(0f, h * 0.25f),
            end = Offset(w, h * 0.25f),
            strokeWidth = 1f
        )
        drawLine(
            color = Color(0x1AFFFFFF),
            start = Offset(0f, h * 0.75f),
            end = Offset(w, h * 0.75f),
            strokeWidth = 1f
        )

        val stepX = w / (dataPoints.size - 1).coerceAtLeast(1)
        val range = (maxValue - minValue).coerceAtLeast(1f)

        val strokePath = Path()
        val fillPath = Path()

        val points = dataPoints.mapIndexed { idx, value ->
            val normY = 1.0f - ((value - minValue) / range).coerceIn(0f, 1f)
            Offset(idx * stepX, normY * (h - 10f) + 5f)
        }

        strokePath.moveTo(points[0].x, points[0].y)
        fillPath.moveTo(points[0].x, h)
        fillPath.lineTo(points[0].x, points[0].y)

        for (i in 1 until points.size) {
            val prev = points[i - 1]
            val curr = points[i]
            val midX = (prev.x + curr.x) / 2f
            strokePath.cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
            fillPath.cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
        }

        fillPath.lineTo(points.last().x, h)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillColor, Color.Transparent),
                startY = 0f,
                endY = h
            )
        )

        drawPath(
            path = strokePath,
            color = lineColor,
            style = Stroke(width = 2.2f, cap = StrokeCap.Round)
        )

        // Draw last data point marker dot
        val lastPt = points.last()
        drawCircle(
            color = lineColor,
            radius = 4f,
            center = lastPt
        )
        drawCircle(
            color = Color.White,
            radius = 2f,
            center = lastPt
        )
    }
}
