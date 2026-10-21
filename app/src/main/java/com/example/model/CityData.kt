package com.example.model

object CityData {
    // City Center: Jammu (32.73, 74.86)
    val JAMMU_CENTER = GeoPoint(32.7300, 74.8600)

    val INITIAL_FIRE_NODES = listOf(
        FireNode(
            id = "FN-01",
            name = "Jewel Chowk Transit Substation",
            location = "Jewel Chowk",
            geo = GeoPoint(32.7230, 74.8580),
            temperatureC = 28.4,
            smokePpm = 22.0,
            gasLevelPpm = 14.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        ),
        FireNode(
            id = "FN-02",
            name = "Talab Tillo Wood Market Lane",
            location = "Talab Tillo",
            geo = GeoPoint(32.7240, 74.8320),
            temperatureC = 31.0,
            smokePpm = 28.0,
            gasLevelPpm = 18.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        ),
        FireNode(
            id = "FN-03",
            name = "Janipur Main Market Complex",
            location = "Janipur",
            geo = GeoPoint(32.7560, 74.8450),
            temperatureC = 27.8,
            smokePpm = 19.5,
            gasLevelPpm = 12.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        ),
        FireNode(
            id = "FN-04",
            name = "Golepulli Industrial Flammables Yard",
            location = "Golepulli",
            geo = GeoPoint(32.7150, 74.8190),
            temperatureC = 29.5,
            smokePpm = 34.0,
            gasLevelPpm = 24.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        ),
        FireNode(
            id = "FN-05",
            name = "Sanjay Nagar Sector 3 Grid",
            location = "Sanjay Nagar",
            geo = GeoPoint(32.6950, 74.8720),
            temperatureC = 26.5,
            smokePpm = 16.0,
            gasLevelPpm = 10.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        ),
        FireNode(
            id = "FN-06",
            name = "Roop Nagar Housing Colony",
            location = "Roop Nagar",
            geo = GeoPoint(32.7680, 74.8210),
            temperatureC = 25.0,
            smokePpm = 14.0,
            gasLevelPpm = 9.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        ),
        FireNode(
            id = "FN-07",
            name = "Raghunath Bazar Heritage Alleys",
            location = "Old City",
            geo = GeoPoint(32.7315, 74.8640),
            temperatureC = 30.2,
            smokePpm = 31.0,
            gasLevelPpm = 21.0,
            isAlert = false,
            isAiVerified = true,
            flameDetected = false
        )
    )

    val INITIAL_BUILDING_NODES = listOf(
        BuildingNode(
            id = "BLD-01",
            name = "Mubarak Mandi Palace Complex (East Wing)",
            location = "Old City / Panjtirthi",
            ageYears = 195,
            geo = GeoPoint(32.7410, 74.8720),
            tiltAngleDeg = 1.4,
            crackWidthMm = 3.8,
            vibrationMms2 = 0.42,
            riskLevel = RiskLevel.AMBER,
            structureType = "Brick Masonry & Lime Plaster",
            occupantCount = 45
        ),
        BuildingNode(
            id = "BLD-02",
            name = "Talab Tillo Old Grain Silo & Mill",
            location = "Talab Tillo",
            ageYears = 74,
            geo = GeoPoint(32.7210, 74.8350),
            tiltAngleDeg = 0.5,
            crackWidthMm = 1.2,
            vibrationMms2 = 0.18,
            riskLevel = RiskLevel.GREEN,
            structureType = "Reinforced Concrete / Brick Infill",
            occupantCount = 12
        ),
        BuildingNode(
            id = "BLD-03",
            name = "Janipur Old Municipal Quarters",
            location = "Janipur",
            ageYears = 62,
            geo = GeoPoint(32.7530, 74.8480),
            tiltAngleDeg = 1.9,
            crackWidthMm = 4.6,
            vibrationMms2 = 0.65,
            riskLevel = RiskLevel.AMBER,
            structureType = "Unreinforced Masonry",
            occupantCount = 80
        ),
        BuildingNode(
            id = "BLD-04",
            name = "Jewel Chowk Commercial Arcade",
            location = "Jewel Chowk",
            ageYears = 48,
            geo = GeoPoint(32.7250, 74.8560),
            tiltAngleDeg = 0.3,
            crackWidthMm = 0.8,
            vibrationMms2 = 0.22,
            riskLevel = RiskLevel.GREEN,
            structureType = "RCC Frame",
            occupantCount = 150
        ),
        BuildingNode(
            id = "BLD-05",
            name = "Bahu Fort Hillside Retaining Structure",
            location = "Bahu Fort",
            ageYears = 210,
            geo = GeoPoint(32.7280, 74.8820),
            tiltAngleDeg = 2.4,
            crackWidthMm = 5.9,
            vibrationMms2 = 0.82,
            riskLevel = RiskLevel.AMBER,
            structureType = "Stone Masonry & Hill Slope",
            occupantCount = 30
        ),
        BuildingNode(
            id = "BLD-06",
            name = "Sanjay Nagar Primary School Annex",
            location = "Sanjay Nagar",
            ageYears = 55,
            geo = GeoPoint(32.6980, 74.8700),
            tiltAngleDeg = 0.4,
            crackWidthMm = 0.9,
            vibrationMms2 = 0.14,
            riskLevel = RiskLevel.GREEN,
            structureType = "Brick & Slab",
            occupantCount = 120
        ),
        BuildingNode(
            id = "BLD-07",
            name = "Golepulli Canal Bridge Abutment",
            location = "Golepulli",
            ageYears = 68,
            geo = GeoPoint(32.7170, 74.8210),
            tiltAngleDeg = 1.1,
            crackWidthMm = 2.4,
            vibrationMms2 = 0.35,
            riskLevel = RiskLevel.GREEN,
            structureType = "Stone Arch & Concrete Cap",
            occupantCount = 0
        ),
        BuildingNode(
            id = "BLD-08",
            name = "Roop Nagar Hillside Tower Block C",
            location = "Roop Nagar",
            ageYears = 32,
            geo = GeoPoint(32.7650, 74.8250),
            tiltAngleDeg = 0.2,
            crackWidthMm = 0.5,
            vibrationMms2 = 0.11,
            riskLevel = RiskLevel.GREEN,
            structureType = "Modern RCC",
            occupantCount = 95
        ),
        BuildingNode(
            id = "BLD-09",
            name = "Peer Kho Historic Vaults",
            location = "Old City / Circular Rd",
            ageYears = 180,
            geo = GeoPoint(32.7360, 74.8730),
            tiltAngleDeg = 1.6,
            crackWidthMm = 3.9,
            vibrationMms2 = 0.48,
            riskLevel = RiskLevel.AMBER,
            structureType = "Ancient Cave & Masonry Vault",
            occupantCount = 25
        ),
        BuildingNode(
            id = "BLD-10",
            name = "Gandhi Nagar Old Dispensary",
            location = "Gandhi Nagar",
            ageYears = 58,
            geo = GeoPoint(32.7090, 74.8610),
            tiltAngleDeg = 0.6,
            crackWidthMm = 1.4,
            vibrationMms2 = 0.20,
            riskLevel = RiskLevel.GREEN,
            structureType = "Masonry & Steel Joists",
            occupantCount = 40
        )
    )

    val INITIAL_DRAIN_NODES = listOf(
        DrainNode(
            id = "DR-01",
            name = "Talab Tillo Main Nullah Outfall",
            location = "Talab Tillo",
            geo = GeoPoint(32.7225, 74.8280),
            waterLevelCm = 85.0,
            maxCapacityCm = 220.0,
            flowRateM3s = 4.2,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-02",
            name = "Janipur Chatha Nullah Intake",
            location = "Janipur",
            geo = GeoPoint(32.7520, 74.8420),
            waterLevelCm = 110.0,
            maxCapacityCm = 200.0,
            flowRateM3s = 5.8,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-03",
            name = "Jewel Chowk Sump Basin",
            location = "Jewel Chowk",
            geo = GeoPoint(32.7215, 74.8550),
            waterLevelCm = 95.0,
            maxCapacityCm = 180.0,
            flowRateM3s = 3.6,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-04",
            name = "Golepulli Irrigation Branch Canal",
            location = "Golepulli",
            geo = GeoPoint(32.7135, 74.8230),
            waterLevelCm = 70.0,
            maxCapacityCm = 240.0,
            flowRateM3s = 6.1,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-05",
            name = "Sanjay Nagar Deep Storm Sewer",
            location = "Sanjay Nagar",
            geo = GeoPoint(32.6930, 74.8690),
            waterLevelCm = 78.0,
            maxCapacityCm = 190.0,
            flowRateM3s = 2.9,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-06",
            name = "Roop Nagar Ridge Drainage Channel",
            location = "Roop Nagar",
            geo = GeoPoint(32.7710, 74.8180),
            waterLevelCm = 65.0,
            maxCapacityCm = 210.0,
            flowRateM3s = 4.8,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-07",
            name = "Tawi River Promenade Interceptor Drain",
            location = "Tawi Bank",
            geo = GeoPoint(32.7290, 74.8690),
            waterLevelCm = 130.0,
            maxCapacityCm = 300.0,
            flowRateM3s = 14.5,
            isAtRisk = false
        ),
        DrainNode(
            id = "DR-08",
            name = "Bahu Fort Gully Runoff Basin",
            location = "Bahu Fort",
            geo = GeoPoint(32.7260, 74.8850),
            waterLevelCm = 52.0,
            maxCapacityCm = 160.0,
            flowRateM3s = 2.1,
            isAtRisk = false
        )
    )

    val SPONGE_ZONES = listOf(
        SpongeZone(
            id = "SZ-01",
            name = "Talab Tillo Rain Garden & Wetland Park",
            areaSqM = 14500,
            type = "Rain Garden & Bioswales",
            geo = GeoPoint(32.7260, 74.8300),
            normalWaterlogHours = 7.5,
            spongeWaterlogHours = 0.8,
            absorptionRateLPerHr = 42000
        ),
        SpongeZone(
            id = "SZ-02",
            name = "Jewel Chowk Permeable Aquifer Pit",
            areaSqM = 8200,
            type = "Aquifer Recharge Wells",
            geo = GeoPoint(32.7245, 74.8530),
            normalWaterlogHours = 6.2,
            spongeWaterlogHours = 1.1,
            absorptionRateLPerHr = 28000
        ),
        SpongeZone(
            id = "SZ-03",
            name = "Janipur Valley Bioswale Buffer",
            areaSqM = 11200,
            type = "Engineered Bioswale Corridor",
            geo = GeoPoint(32.7540, 74.8400),
            normalWaterlogHours = 8.0,
            spongeWaterlogHours = 1.4,
            absorptionRateLPerHr = 36000
        ),
        SpongeZone(
            id = "SZ-04",
            name = "Sanjay Nagar Green Detention Pond",
            areaSqM = 18000,
            type = "Retention Wetland Basin",
            geo = GeoPoint(32.6960, 74.8670),
            normalWaterlogHours = 9.2,
            spongeWaterlogHours = 1.2,
            absorptionRateLPerHr = 55000
        )
    )

    val FIRE_STATIONS = listOf(
        Pair("Gandhi Nagar Fire Station", GeoPoint(32.7050, 74.8580)),
        Pair("City Chowk Fire Sub-Station", GeoPoint(32.7330, 74.8620)),
        Pair("Roop Nagar Fire Brigade Unit", GeoPoint(32.7630, 74.8290))
    )

    val PRESET_CRACK_PHOTOS = listOf(
        PresetCrack(
            id = "preset_1",
            title = "Diagonal Shear Crack",
            type = "Diagonal Shear (Load-bearing)",
            location = "Old City / Raghunath Bazar",
            description = "Deep 5.8mm diagonal shear crack through old brick masonry wall.",
            severity = RiskLevel.RED,
            confidence = 96,
            explanation = "Severe diagonal shear cracking indicating differential foundation settlement and active masonry shear failure.",
            action = "Immediate structural shoring required. Restrict upper floor occupancy within 6 hours."
        ),
        PresetCrack(
            id = "preset_2",
            title = "Hairline Plaster Cracks",
            type = "Thermal / Shrinkage Hairline",
            location = "Janipur Quarters",
            description = "Surface hairline network cracking across exterior plaster coating.",
            severity = RiskLevel.GREEN,
            confidence = 91,
            explanation = "Superficial thermal expansion cracks confined to plaster layer with no structural masonry displacement.",
            action = "Routine elastomeric sealant re-coating during regular maintenance cycle."
        ),
        PresetCrack(
            id = "preset_3",
            title = "Foundation Settlement Seam",
            type = "Vertical Foundation Shift",
            location = "Jewel Chowk Commercial Arcade",
            description = "Vertical separation along column joint measuring 3.4mm.",
            severity = RiskLevel.AMBER,
            confidence = 89,
            explanation = "Differential settlement along exterior structural column. Crack width exceeds aesthetic threshold.",
            action = "Install crack displacement gauge pins and survey ground water table within 48 hours."
        ),
        PresetCrack(
            id = "preset_4",
            title = "Mortar Delamination & Bulge",
            type = "Heritage Wall Delamination",
            location = "Mubarak Mandi Palace Complex",
            description = "Historic lime mortar erosion with 4.5mm outward parapet bulge.",
            severity = RiskLevel.RED,
            confidence = 94,
            explanation = "Lime mortar deterioration under moisture saturation causing exterior wall bulge and risk of parapet collapse.",
            action = "Cordon off ground perimeter immediately. Deploy conservation engineering scaffolding."
        )
    )

    data class PresetCrack(
        val id: String,
        val title: String,
        val type: String,
        val location: String,
        val description: String,
        val severity: RiskLevel,
        val confidence: Int,
        val explanation: String,
        val action: String
    )

    fun getEmergencySmsMessage(lang: Language, location: String, alertDetails: String): String {
        return when (lang) {
            Language.EN ->
                "🚨 JAMMU RAKSHAK EMERGENCY ALERT:\nCritical Hazard detected at $location: $alertDetails.\nImmediate emergency response initiated. All civic units on alert. Evacuate 200m zone."
            Language.HI ->
                "🚨 जम्मू रक्षक आपातकालीन चेतावनी:\n$location पर गंभीर खतरा: $alertDetails।\nत्वरित आपदा प्रबंधन दल रवाना। सभी नागरिक सतर्क रहें। 200 मीटर का दायरा तुरंत खाली करें।"
            Language.UR ->
                "🚨 جموں رکشاک ہنگامی الرٹ:\n$location پر شدید خطرہ: $alertDetails۔\nفوری امدادی ٹیمیں روانہ ہو چکی ہیں۔ تمام شہری محتاط رہیں۔ 200 میٹر کا علاقہ خالی کریں۔"
            Language.DOGRI ->
                "🚨 जम्मू रक्षक संकट चेतौनी:\n$location पर बड्डा खतरा: $alertDetails।\nराहत मोचन टीम मौक़े पर पूजी गेई ऐ। सारे लोक सावधान रोह्न। 200 मीटर दायरा खाली करो।"
        }
    }
}
