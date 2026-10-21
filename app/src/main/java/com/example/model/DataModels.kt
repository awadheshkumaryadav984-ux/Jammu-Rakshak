package com.example.model

enum class ShieldType {
    FIRE, STRUCTURE, WATER, ALL
}

enum class RiskLevel {
    GREEN, AMBER, RED
}

enum class Language(val code: String, val label: String) {
    EN("en", "English"),
    HI("hi", "हिंदी"),
    UR("ur", "اردو"),
    DOGRI("doi", "डोगरी")
}

data class GeoPoint(
    val lat: Double,
    val lng: Double
)

data class FireNode(
    val id: String,
    val name: String,
    val location: String,
    val geo: GeoPoint,
    val temperatureC: Double,
    val smokePpm: Double,
    val gasLevelPpm: Double,
    val isAlert: Boolean,
    val isAiVerified: Boolean,
    val flameDetected: Boolean,
    val lastUpdated: Long = System.currentTimeMillis()
)

data class BuildingNode(
    val id: String,
    val name: String,
    val location: String,
    val ageYears: Int,
    val geo: GeoPoint,
    val tiltAngleDeg: Double,
    val crackWidthMm: Double,
    val vibrationMms2: Double,
    val riskLevel: RiskLevel,
    val structureType: String,
    val occupantCount: Int
)

data class DrainNode(
    val id: String,
    val name: String,
    val location: String,
    val geo: GeoPoint,
    val waterLevelCm: Double,
    val maxCapacityCm: Double,
    val flowRateM3s: Double,
    val isAtRisk: Boolean // > 70%
) {
    val capacityPercent: Int
        get() = ((waterLevelCm / maxCapacityCm) * 100).toInt().coerceIn(0, 100)
}

data class SpongeZone(
    val id: String,
    val name: String,
    val areaSqM: Int,
    val type: String, // "Rain Garden", "Bioswale", "Aquifer Recharge Pit"
    val geo: GeoPoint,
    val normalWaterlogHours: Double,
    val spongeWaterlogHours: Double,
    val absorptionRateLPerHr: Int
)

data class AlertItem(
    val id: String,
    val timestamp: Long,
    val title: String,
    val description: String,
    val shield: ShieldType,
    val risk: RiskLevel,
    val location: String,
    val isAiVerified: Boolean = false,
    val geo: GeoPoint? = null
)

data class CrackAssessmentResult(
    val severity: RiskLevel,
    val confidence: Int,
    val crackType: String,
    val estimatedWidthMm: Double,
    val explanation: String,
    val recommendedAction: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CitizenCrackReport(
    val id: String,
    val locationName: String,
    val geo: GeoPoint,
    val photoUri: String? = null,
    val presetImageIndex: Int = 0,
    val assessment: CrackAssessmentResult,
    val reportedAt: Long = System.currentTimeMillis()
)

data class FireDispatchRoute(
    val fireNodeId: String,
    val stationName: String,
    val startGeo: GeoPoint,
    val destinationGeo: GeoPoint,
    val waypointPath: List<GeoPoint>,
    val initialEtaSeconds: Int,
    val remainingEtaSeconds: Int,
    val isActive: Boolean
)
