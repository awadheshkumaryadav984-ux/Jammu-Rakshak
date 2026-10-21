package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BuildingNode
import com.example.model.CitizenCrackReport
import com.example.model.DrainNode
import com.example.model.FireDispatchRoute
import com.example.model.FireNode
import com.example.model.GeoPoint
import com.example.model.RiskLevel
import com.example.model.SpongeZone
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
import kotlin.math.roundToInt

private const val MIN_LAT = 32.6850
private const val MAX_LAT = 32.7800
private const val MIN_LNG = 74.8100
private const val MAX_LNG = 74.8950

private fun projectGeoToOffset(
    geo: GeoPoint,
    width: Float,
    height: Float,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float
): Offset {
    val normX = (geo.lng - MIN_LNG) / (MAX_LNG - MIN_LNG)
    val normY = 1.0 - (geo.lat - MIN_LAT) / (MAX_LAT - MIN_LAT)
    val centerX = width / 2f
    val centerY = height / 2f
    val basePosX = (normX * width).toFloat()
    val basePosY = (normY * height).toFloat()
    return Offset(
        centerX + (basePosX - centerX) * zoomScale + panOffsetX,
        centerY + (basePosY - centerY) * zoomScale + panOffsetY
    )
}

private fun performMapHitTest(
    tapOffset: Offset,
    w: Float,
    h: Float,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    showFire: Boolean,
    showStructure: Boolean,
    showWater: Boolean,
    showSponge: Boolean,
    fireNodes: List<FireNode>,
    buildingNodes: List<BuildingNode>,
    drainNodes: List<DrainNode>,
    spongeZones: List<SpongeZone>,
    citizenReports: List<CitizenCrackReport>
): Any? {
    if (showFire) {
        for (fn in fireNodes) {
            val pos = projectGeoToOffset(fn.geo, w, h, zoomScale, panOffsetX, panOffsetY)
            if ((pos - tapOffset).getDistance() <= 28f * zoomScale.coerceAtLeast(1f)) return fn
        }
    }
    if (showStructure) {
        for (bn in buildingNodes) {
            val pos = projectGeoToOffset(bn.geo, w, h, zoomScale, panOffsetX, panOffsetY)
            if ((pos - tapOffset).getDistance() <= 26f * zoomScale.coerceAtLeast(1f)) return bn
        }
    }
    if (showWater) {
        for (dn in drainNodes) {
            val pos = projectGeoToOffset(dn.geo, w, h, zoomScale, panOffsetX, panOffsetY)
            if ((pos - tapOffset).getDistance() <= 26f * zoomScale.coerceAtLeast(1f)) return dn
        }
    }
    if (showSponge) {
        for (sz in spongeZones) {
            val pos = projectGeoToOffset(sz.geo, w, h, zoomScale, panOffsetX, panOffsetY)
            if ((pos - tapOffset).getDistance() <= 38f * zoomScale.coerceAtLeast(1f)) return sz
        }
    }
    for (cr in citizenReports) {
        val pos = projectGeoToOffset(cr.geo, w, h, zoomScale, panOffsetX, panOffsetY)
        if ((pos - tapOffset).getDistance() <= 26f * zoomScale.coerceAtLeast(1f)) return cr
    }
    return null
}

@Composable
fun TacticalMap(
    fireNodes: List<FireNode>,
    buildingNodes: List<BuildingNode>,
    drainNodes: List<DrainNode>,
    spongeZones: List<SpongeZone>,
    citizenReports: List<CitizenCrackReport>,
    activeRoute: FireDispatchRoute?,
    spongeLayerVisible: Boolean,
    onToggleSpongeLayer: () -> Unit,
    selectedNode: Any?,
    onSelectNode: (Any?) -> Unit,
    modifier: Modifier = Modifier
) {
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    var showFireLayer by remember { mutableStateOf(true) }
    var showStructureLayer by remember { mutableStateOf(true) }
    var showWaterLayer by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "mapRadar")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )

    val dashPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dashPhase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavyDark)
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
            .testTag("tactical_map_container")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        zoomScale = (zoomScale * zoom).coerceIn(0.7f, 3.5f)
                        panOffsetX += pan.x
                        panOffsetY += pan.y
                    }
                }
                .pointerInput(showFireLayer, showStructureLayer, showWaterLayer, spongeLayerVisible) {
                    detectTapGestures { tapOffset ->
                        val hit = performMapHitTest(
                            tapOffset = tapOffset,
                            w = size.width.toFloat(),
                            h = size.height.toFloat(),
                            zoomScale = zoomScale,
                            panOffsetX = panOffsetX,
                            panOffsetY = panOffsetY,
                            showFire = showFireLayer,
                            showStructure = showStructureLayer,
                            showWater = showWaterLayer,
                            showSponge = spongeLayerVisible,
                            fireNodes = fireNodes,
                            buildingNodes = buildingNodes,
                            drainNodes = drainNodes,
                            spongeZones = spongeZones,
                            citizenReports = citizenReports
                        )
                        onSelectNode(hit)
                    }
                }
        ) {
            val width = size.width
            val height = size.height

            // 1. Draw Tactical Cyber Grid
            val gridStep = 50f * zoomScale
            var gx = (panOffsetX % gridStep)
            while (gx < width) {
                drawLine(
                    color = Color(0x1200E5FF),
                    start = Offset(gx, 0f),
                    end = Offset(gx, height),
                    strokeWidth = 1f
                )
                gx += gridStep
            }
            var gy = (panOffsetY % gridStep)
            while (gy < height) {
                drawLine(
                    color = Color(0x1200E5FF),
                    start = Offset(0f, gy),
                    end = Offset(width, gy),
                    strokeWidth = 1f
                )
                gy += gridStep
            }

            // 2. Draw Tawi River Ribbon
            val riverPath = Path()
            val riverWaypoints = listOf(
                GeoPoint(32.7750, 74.8820),
                GeoPoint(32.7600, 74.8780),
                GeoPoint(32.7480, 74.8720),
                GeoPoint(32.7350, 74.8690),
                GeoPoint(32.7210, 74.8650),
                GeoPoint(32.7050, 74.8610),
                GeoPoint(32.6880, 74.8580)
            )
            val firstPt = projectGeoToOffset(riverWaypoints[0], width, height, zoomScale, panOffsetX, panOffsetY)
            riverPath.moveTo(firstPt.x, firstPt.y)
            for (i in 1 until riverWaypoints.size) {
                val pt = projectGeoToOffset(riverWaypoints[i], width, height, zoomScale, panOffsetX, panOffsetY)
                riverPath.lineTo(pt.x, pt.y)
            }
            drawPath(
                path = riverPath,
                color = Color(0x330077B6),
                style = Stroke(width = 18f * zoomScale, cap = StrokeCap.Round)
            )
            drawPath(
                path = riverPath,
                color = Color(0x990096C7),
                style = Stroke(width = 8f * zoomScale, cap = StrokeCap.Round)
            )

            // 3. Draw Major Road Network (Arterial connecting lines)
            val roadConnections = listOf(
                Pair(GeoPoint(32.7560, 74.8450), GeoPoint(32.7680, 74.8210)),
                Pair(GeoPoint(32.7560, 74.8450), GeoPoint(32.7230, 74.8580)),
                Pair(GeoPoint(32.7240, 74.8320), GeoPoint(32.7230, 74.8580)),
                Pair(GeoPoint(32.7240, 74.8320), GeoPoint(32.7150, 74.8190)),
                Pair(GeoPoint(32.7230, 74.8580), GeoPoint(32.6950, 74.8720)),
                Pair(GeoPoint(32.7230, 74.8580), GeoPoint(32.7315, 74.8640)),
                Pair(GeoPoint(32.7315, 74.8640), GeoPoint(32.7280, 74.8820))
            )
            for ((from, to) in roadConnections) {
                val p1 = projectGeoToOffset(from, width, height, zoomScale, panOffsetX, panOffsetY)
                val p2 = projectGeoToOffset(to, width, height, zoomScale, panOffsetX, panOffsetY)
                drawLine(
                    color = Color(0x331E3A5F),
                    start = p1,
                    end = p2,
                    strokeWidth = 3f * zoomScale
                )
            }

            // 4. Draw Sponge Neighbourhood Zones
            if (spongeLayerVisible) {
                for (sz in spongeZones) {
                    val pos = projectGeoToOffset(sz.geo, width, height, zoomScale, panOffsetX, panOffsetY)
                    val radius = 32f * zoomScale

                    drawCircle(
                        color = Color(0x2800E676),
                        radius = radius,
                        center = pos
                    )
                    drawCircle(
                        color = Color(0x8800E676),
                        radius = radius,
                        center = pos,
                        style = Stroke(
                            width = 1.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                        )
                    )
                    drawCircle(
                        color = NeonEmerald,
                        radius = 4.5f * zoomScale,
                        center = pos
                    )
                }
            }

            // 5. Draw Active Emergency Dispatch Route
            activeRoute?.let { route ->
                if (route.isActive && route.waypointPath.size >= 2) {
                    val routePath = Path()
                    val pStart = projectGeoToOffset(route.waypointPath[0], width, height, zoomScale, panOffsetX, panOffsetY)
                    routePath.moveTo(pStart.x, pStart.y)
                    for (i in 1 until route.waypointPath.size) {
                        val pt = projectGeoToOffset(route.waypointPath[i], width, height, zoomScale, panOffsetX, panOffsetY)
                        routePath.lineTo(pt.x, pt.y)
                    }

                    drawPath(
                        path = routePath,
                        color = Color(0x44FF3D00),
                        style = Stroke(width = 10f * zoomScale, cap = StrokeCap.Round)
                    )

                    drawPath(
                        path = routePath,
                        color = CriticalRed,
                        style = Stroke(
                            width = 3.5f * zoomScale,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), dashPhase)
                        )
                    )

                    val progressFraction = 1f - (route.remainingEtaSeconds.toFloat() / route.initialEtaSeconds.toFloat()).coerceIn(0f, 1f)
                    val totalSegments = (route.waypointPath.size - 1).coerceAtLeast(1)
                    val segIndex = (progressFraction * totalSegments).toInt().coerceIn(0, totalSegments - 1)
                    val segFraction = (progressFraction * totalSegments) - segIndex
                    val segA = projectGeoToOffset(route.waypointPath[segIndex], width, height, zoomScale, panOffsetX, panOffsetY)
                    val segB = projectGeoToOffset(route.waypointPath[segIndex + 1], width, height, zoomScale, panOffsetX, panOffsetY)
                    val unitPos = Offset(
                        segA.x + (segB.x - segA.x) * segFraction,
                        segA.y + (segB.y - segA.y) * segFraction
                    )

                    drawCircle(
                        color = Color(0x66FF3D00),
                        radius = (14f + 8f * radarPulse) * zoomScale,
                        center = unitPos
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6f * zoomScale,
                        center = unitPos
                    )
                    drawCircle(
                        color = CriticalRed,
                        radius = 4f * zoomScale,
                        center = unitPos
                    )
                }
            }

            // 6. Draw Drain Nodes (Water Shield)
            if (showWaterLayer) {
                for (dn in drainNodes) {
                    val pos = projectGeoToOffset(dn.geo, width, height, zoomScale, panOffsetX, panOffsetY)
                    val radius = 7f * zoomScale
                    val color = if (dn.isAtRisk) CriticalRed else WaterBlue

                    if (dn.isAtRisk) {
                        drawCircle(
                            color = CriticalRed.copy(alpha = 0.35f * (1f - radarPulse)),
                            radius = (radius + 16f * radarPulse) * zoomScale,
                            center = pos
                        )
                    }

                    drawCircle(color = color, radius = radius, center = pos)
                    drawCircle(color = Color.White, radius = radius * 0.45f, center = pos)
                }
            }

            // 7. Draw Building Nodes (Structure Shield)
            if (showStructureLayer) {
                for (bn in buildingNodes) {
                    val pos = projectGeoToOffset(bn.geo, width, height, zoomScale, panOffsetX, panOffsetY)
                    val sizeDp = 12f * zoomScale
                    val color = when (bn.riskLevel) {
                        RiskLevel.RED -> CriticalRed
                        RiskLevel.AMBER -> WarningAmber
                        RiskLevel.GREEN -> NeonEmerald
                    }

                    if (bn.riskLevel == RiskLevel.RED) {
                        drawCircle(
                            color = CriticalRed.copy(alpha = 0.4f * (1f - radarPulse)),
                            radius = (10f + 16f * radarPulse) * zoomScale,
                            center = pos
                        )
                    }

                    drawRect(
                        color = color,
                        topLeft = Offset(pos.x - sizeDp / 2, pos.y - sizeDp / 2),
                        size = androidx.compose.ui.geometry.Size(sizeDp, sizeDp)
                    )
                    drawRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        topLeft = Offset(pos.x - sizeDp / 4, pos.y - sizeDp / 4),
                        size = androidx.compose.ui.geometry.Size(sizeDp / 2, sizeDp / 2)
                    )
                }
            }

            // 8. Draw Fire Nodes (Fire Shield)
            if (showFireLayer) {
                for (fn in fireNodes) {
                    val pos = projectGeoToOffset(fn.geo, width, height, zoomScale, panOffsetX, panOffsetY)
                    val radius = 8f * zoomScale
                    val color = if (fn.isAlert) CriticalRed else Color(0xFFFF9100)

                    if (fn.isAlert) {
                        drawCircle(
                            color = CriticalRed.copy(alpha = 0.6f * (1f - radarPulse)),
                            radius = (radius + 24f * radarPulse) * zoomScale,
                            center = pos
                        )
                        drawCircle(
                            color = CriticalRed.copy(alpha = 0.3f * (1f - radarPulse)),
                            radius = (radius + 40f * radarPulse) * zoomScale,
                            center = pos
                        )
                    }

                    drawCircle(color = color, radius = radius, center = pos)
                    drawCircle(color = Color.White, radius = radius * 0.4f, center = pos)
                }
            }

            // 9. Draw Citizen Crack Reports
            for (cr in citizenReports) {
                val pos = projectGeoToOffset(cr.geo, width, height, zoomScale, panOffsetX, panOffsetY)
                val radius = 7.5f * zoomScale
                val color = when (cr.assessment.severity) {
                    RiskLevel.RED -> CriticalRed
                    RiskLevel.AMBER -> WarningAmber
                    RiskLevel.GREEN -> NeonEmerald
                }
                drawCircle(color = color, radius = radius, center = pos)
                drawCircle(color = Color(0xFF9D4EDD), radius = radius * 0.5f, center = pos)
            }
        }

        // --- MAP HUD CONTROLS OVERLAY ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = showFireLayer,
                onClick = { showFireLayer = !showFireLayer },
                label = { Text("Fire", fontSize = 10.sp, color = if (showFireLayer) Color.Black else TextPrimary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (showFireLayer) Color.Black else Color(0xFFFF9100)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFF9100),
                    containerColor = CyberNavyElevated.copy(alpha = 0.85f)
                ),
                modifier = Modifier.height(28.dp)
            )

            FilterChip(
                selected = showStructureLayer,
                onClick = { showStructureLayer = !showStructureLayer },
                label = { Text("Structure", fontSize = 10.sp, color = if (showStructureLayer) Color.Black else TextPrimary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (showStructureLayer) Color.Black else WarningAmber
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WarningAmber,
                    containerColor = CyberNavyElevated.copy(alpha = 0.85f)
                ),
                modifier = Modifier.height(28.dp)
            )

            FilterChip(
                selected = showWaterLayer,
                onClick = { showWaterLayer = !showWaterLayer },
                label = { Text("Water", fontSize = 10.sp, color = if (showWaterLayer) Color.Black else TextPrimary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.WaterDamage,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (showWaterLayer) Color.Black else WaterBlue
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WaterBlue,
                    containerColor = CyberNavyElevated.copy(alpha = 0.85f)
                ),
                modifier = Modifier.height(28.dp)
            )

            FilterChip(
                selected = spongeLayerVisible,
                onClick = onToggleSpongeLayer,
                label = { Text("Sponge", fontSize = 10.sp, color = if (spongeLayerVisible) Color.Black else TextPrimary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (spongeLayerVisible) Color.Black else NeonEmerald
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonEmerald,
                    containerColor = CyberNavyElevated.copy(alpha = 0.85f)
                ),
                modifier = Modifier.height(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = { zoomScale = (zoomScale * 1.25f).coerceAtMost(3.5f) },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CyberNavyElevated)
                    .border(1.dp, CyberBorder, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = NeonCyan, modifier = Modifier.size(16.dp))
            }

            IconButton(
                onClick = { zoomScale = (zoomScale / 1.25f).coerceAtLeast(0.7f) },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CyberNavyElevated)
                    .border(1.dp, CyberBorder, CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = NeonCyan, modifier = Modifier.size(16.dp))
            }

            IconButton(
                onClick = {
                    zoomScale = 1.0f
                    panOffsetX = 0f
                    panOffsetY = 0f
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CyberNavyElevated)
                    .border(1.dp, CyberBorder, CircleShape)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Reset Center", tint = NeonEmerald, modifier = Modifier.size(16.dp))
            }
        }

        activeRoute?.let { route ->
            if (route.isActive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberNavyDark.copy(alpha = 0.92f))
                        .border(1.dp, CriticalRed, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CriticalRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "RAPID RESPONSE DISPATCHED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = CriticalRed
                            )
                            Text(
                                text = "Unit: ${route.stationName} • ETA: ${route.remainingEtaSeconds}s",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        selectedNode?.let { node ->
            MapNodeInspectorCard(
                node = node,
                onClose = { onSelectNode(null) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 45.dp, end = 8.dp)
                    .width(260.dp)
            )
        }
    }
}

@Composable
fun MapNodeInspectorCard(
    node: Any,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNavyCard.copy(alpha = 0.95f))
            .border(1.dp, NeonCyan, RoundedCornerShape(8.dp))
            .padding(10.dp)
            .testTag("map_inspector_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (node) {
                        is FireNode -> "FIRE SHIELD SENSOR"
                        is BuildingNode -> "STRUCTURE SENSOR"
                        is DrainNode -> "WATER LEVEL SENSOR"
                        is SpongeZone -> "SPONGE ZONE"
                        is CitizenCrackReport -> "CITIZEN AI REPORT"
                        else -> "TELEMETRY NODE"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                IconButton(onClick = onClose, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            when (node) {
                is FireNode -> {
                    Text(text = node.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Sector: ${node.location}", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TelemetryBadge("Temp", "${node.temperatureC}°C", if (node.isAlert) CriticalRed else TextPrimary)
                        TelemetryBadge("Smoke", "${node.smokePpm} ppm", if (node.isAlert) CriticalRed else TextPrimary)
                        TelemetryBadge("Gas", "${node.gasLevelPpm} ppm", TextPrimary)
                    }
                    if (node.isAiVerified) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "✓ Edge AI Verified (False Alarm Filtered)",
                            fontSize = 10.sp,
                            color = NeonEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                is BuildingNode -> {
                    Text(text = node.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "${node.structureType} • ${node.ageYears} yrs", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TelemetryBadge("Tilt", "${node.tiltAngleDeg}°", when (node.riskLevel) {
                            RiskLevel.RED -> CriticalRed
                            RiskLevel.AMBER -> WarningAmber
                            RiskLevel.GREEN -> NeonEmerald
                        })
                        TelemetryBadge("Crack", "${node.crackWidthMm}mm", when (node.riskLevel) {
                            RiskLevel.RED -> CriticalRed
                            RiskLevel.AMBER -> WarningAmber
                            RiskLevel.GREEN -> NeonEmerald
                        })
                        TelemetryBadge("Vib", "${node.vibrationMms2} mm/s²", TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Status: ${node.riskLevel.name} Risk (${node.occupantCount} occupants)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (node.riskLevel) {
                            RiskLevel.RED -> CriticalRed
                            RiskLevel.AMBER -> WarningAmber
                            RiskLevel.GREEN -> NeonEmerald
                        }
                    )
                }
                is DrainNode -> {
                    Text(text = node.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Sector: ${node.location}", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TelemetryBadge("Level", "${node.waterLevelCm} cm", if (node.isAtRisk) CriticalRed else WaterBlue)
                        TelemetryBadge("Capacity", "${node.capacityPercent}%", if (node.isAtRisk) CriticalRed else WaterBlue)
                        TelemetryBadge("Flow", "${node.flowRateM3s} m³/s", TextPrimary)
                    }
                }
                is SpongeZone -> {
                    Text(text = node.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "${node.type} (${node.areaSqM} m²)", fontSize = 11.sp, color = NeonEmerald)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Waterlogging: ${node.normalWaterlogHours} hrs → ${node.spongeWaterlogHours} hrs (-${((node.normalWaterlogHours - node.spongeWaterlogHours) / node.normalWaterlogHours * 100).roundToInt()}%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                    Text(
                        text = "Absorption: ${node.absorptionRateLPerHr} Litres / hr",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
                is CitizenCrackReport -> {
                    Text(text = "Citizen Report @ ${node.locationName}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "${node.assessment.crackType} • ${node.assessment.estimatedWidthMm}mm", fontSize = 11.sp, color = WarningAmber)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = node.assessment.explanation, fontSize = 10.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Action: ${node.assessment.recommendedAction}", fontSize = 10.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TelemetryBadge(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(CyberNavyDark)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(text = label, fontSize = 8.sp, color = TextMuted)
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
