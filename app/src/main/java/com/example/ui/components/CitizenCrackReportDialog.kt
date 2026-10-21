package com.example.ui.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CitizenCrackReport
import com.example.model.CityData
import com.example.model.CrackAssessmentResult
import com.example.model.GeoPoint
import com.example.model.RiskLevel
import com.example.service.GeminiCrackService
import com.example.ui.theme.CriticalRed
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
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenCrackReportDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSubmitReport: (CitizenCrackReport) -> Unit
) {
    if (!isOpen) return

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val geminiService = remember { GeminiCrackService(context) }

    var selectedPresetIndex by remember { mutableIntStateOf(0) }
    var userLocation by remember { mutableStateOf("Old City / Raghunath Bazar") }
    var uploadedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var assessmentResult by remember { mutableStateOf<CrackAssessmentResult?>(null) }

    // Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val bitmap = geminiService.loadBitmapFromUri(uri)
            if (bitmap != null) {
                uploadedBitmap = bitmap
                assessmentResult = null
            }
        }
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("crack_report_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberNavyDark)
                .border(1.5.dp, PurpleAccent, RoundedCornerShape(12.dp))
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
                                .background(PurpleAccent.copy(alpha = 0.2f))
                                .border(1.dp, PurpleAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = PurpleAccent, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "CITIZEN CRACK AI INSPECTION",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Powered by Gemini Vision Multimodal AI",
                                fontSize = 9.sp,
                                color = PurpleAccent
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Location Input Field
                OutlinedTextField(
                    value = userLocation,
                    onValueChange = { userLocation = it },
                    label = { Text("Building Location / Ward / Landmark", fontSize = 10.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("crack_location_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = TextSecondary,
                        focusedContainerColor = CyberNavyCard,
                        unfocusedContainerColor = CyberNavyCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Preset Crack Photos Selector
                Text(
                    text = "SELECT CRACK SAMPLE OR UPLOAD PHOTO:",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CityData.PRESET_CRACK_PHOTOS.forEachIndexed { index, preset ->
                        val isSelected = selectedPresetIndex == index && uploadedBitmap == null
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) PurpleAccent.copy(alpha = 0.25f) else CyberNavyCard)
                                .border(
                                    1.dp,
                                    if (isSelected) PurpleAccent else CyberBorder,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedPresetIndex = index
                                    uploadedBitmap = null
                                    userLocation = preset.location
                                    assessmentResult = null
                                }
                                .padding(6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = preset.title,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = preset.severity.name,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = when (preset.severity) {
                                        RiskLevel.RED -> CriticalRed
                                        RiskLevel.AMBER -> WarningAmber
                                        RiskLevel.GREEN -> NeonEmerald
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Photo Picker Upload Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyberNavyElevated)
                            .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("upload_photo_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uploadedBitmap != null) "Photo Selected (Change)" else "Pick Custom Photo",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        }
                    }

                    if (uploadedBitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(1.dp, NeonEmerald, RoundedCornerShape(4.dp))
                        ) {
                            Image(
                                bitmap = uploadedBitmap!!.asImageBitmap(),
                                contentDescription = "Uploaded crack preview",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Analyze Button
                Button(
                    onClick = {
                        isAnalyzing = true
                        scope.launch {
                            val result = geminiService.analyzeCrackImage(
                                bitmap = uploadedBitmap,
                                presetIndex = selectedPresetIndex,
                                location = userLocation
                            )
                            assessmentResult = result
                            isAnalyzing = false
                        }
                    },
                    enabled = !isAnalyzing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("analyze_crack_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Analyzing with Gemini Vision...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "ANALYZE CRACK SEVERITY", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }

                // AI Result Display
                assessmentResult?.let { res ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberNavyCard)
                            .border(
                                1.5.dp,
                                when (res.severity) {
                                    RiskLevel.RED -> CriticalRed
                                    RiskLevel.AMBER -> WarningAmber
                                    RiskLevel.GREEN -> NeonEmerald
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                            .testTag("ai_assessment_result")
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
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when (res.severity) {
                                                    RiskLevel.RED -> CriticalRed
                                                    RiskLevel.AMBER -> WarningAmber
                                                    RiskLevel.GREEN -> NeonEmerald
                                                }
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${res.severity.name} RISK",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${res.confidence}% AI Confidence",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = NeonEmerald
                                    )
                                }

                                Text(
                                    text = "Width: ${res.estimatedWidthMm}mm",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Diagnosis: ${res.crackType}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = res.explanation,
                                fontSize = 10.sp,
                                color = TextSecondary,
                                lineHeight = 13.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Action: ${res.recommendedAction}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan,
                                lineHeight = 13.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Submit to Map Button
                            Button(
                                onClick = {
                                    val approxGeo = when {
                                        userLocation.contains("Old City", ignoreCase = true) -> GeoPoint(32.7320, 74.8630)
                                        userLocation.contains("Janipur", ignoreCase = true) -> GeoPoint(32.7550, 74.8460)
                                        userLocation.contains("Jewel", ignoreCase = true) -> GeoPoint(32.7240, 74.8570)
                                        userLocation.contains("Talab", ignoreCase = true) -> GeoPoint(32.7230, 74.8340)
                                        else -> GeoPoint(32.7300, 74.8550)
                                    }
                                    val report = CitizenCrackReport(
                                        id = UUID.randomUUID().toString().take(6),
                                        locationName = userLocation,
                                        geo = approxGeo,
                                        presetImageIndex = selectedPresetIndex,
                                        assessment = res
                                    )
                                    onSubmitReport(report)
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(34.dp)
                                    .testTag("submit_report_to_map_btn"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonEmerald,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "ADD TO COMMAND MAP & ALERTS", fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
