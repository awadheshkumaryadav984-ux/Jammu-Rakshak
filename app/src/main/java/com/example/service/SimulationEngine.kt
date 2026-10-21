package com.example.service

import com.example.model.AlertItem
import com.example.model.BuildingNode
import com.example.model.CitizenCrackReport
import com.example.model.CityData
import com.example.model.DrainNode
import com.example.model.FireDispatchRoute
import com.example.model.FireNode
import com.example.model.GeoPoint
import com.example.model.Language
import com.example.model.RiskLevel
import com.example.model.ShieldType
import com.example.model.SpongeZone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

data class AutoDemoState(
    val isRunning: Boolean = false,
    val currentStep: Int = 0,
    val totalSteps: Int = 5,
    val caption: String = "",
    val timeRemainingSeconds: Int = 60
)

class SimulationEngine(
    private val audioService: AudioAlertService
) {
    private val _fireNodes = MutableStateFlow(CityData.INITIAL_FIRE_NODES)
    val fireNodes: StateFlow<List<FireNode>> = _fireNodes.asStateFlow()

    private val _buildingNodes = MutableStateFlow(CityData.INITIAL_BUILDING_NODES)
    val buildingNodes: StateFlow<List<BuildingNode>> = _buildingNodes.asStateFlow()

    private val _drainNodes = MutableStateFlow(CityData.INITIAL_DRAIN_NODES)
    val drainNodes: StateFlow<List<DrainNode>> = _drainNodes.asStateFlow()

    private val _spongeZones = MutableStateFlow(CityData.SPONGE_ZONES)
    val spongeZones: StateFlow<List<SpongeZone>> = _spongeZones.asStateFlow()

    private val _alerts = MutableStateFlow<List<AlertItem>>(emptyList())
    val alerts: StateFlow<List<AlertItem>> = _alerts.asStateFlow()

    private val _citizenReports = MutableStateFlow<List<CitizenCrackReport>>(emptyList())
    val citizenReports: StateFlow<List<CitizenCrackReport>> = _citizenReports.asStateFlow()

    private val _activeRoute = MutableStateFlow<FireDispatchRoute?>(null)
    val activeRoute: StateFlow<FireDispatchRoute?> = _activeRoute.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(Language.EN)
    val selectedLanguage: StateFlow<Language> = _selectedLanguage.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _spongeLayerVisible = MutableStateFlow(true)
    val spongeLayerVisible: StateFlow<Boolean> = _spongeLayerVisible.asStateFlow()

    private val _isRainActive = MutableStateFlow(false)
    val isRainActive: StateFlow<Boolean> = _isRainActive.asStateFlow()

    private val _isTremorActive = MutableStateFlow(false)
    val isTremorActive: StateFlow<Boolean> = _isTremorActive.asStateFlow()

    private val _autoDemo = MutableStateFlow(AutoDemoState())
    val autoDemo: StateFlow<AutoDemoState> = _autoDemo.asStateFlow()

    private val _selectedInspectorNode = MutableStateFlow<Any?>(null)
    val selectedInspectorNode: StateFlow<Any?> = _selectedInspectorNode.asStateFlow()

    private val _smsEmergencyDialogAlert = MutableStateFlow<AlertItem?>(null)
    val smsEmergencyDialogAlert: StateFlow<AlertItem?> = _smsEmergencyDialogAlert.asStateFlow()

    private var simulationJob: Job? = null
    private var etaCountdownJob: Job? = null
    private var autoDemoJob: Job? = null

    fun start(scope: CoroutineScope) {
        initBaselineAlerts()
        simulationJob?.cancel()
        simulationJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(2500)
                performRandomWalkTick(scope)
            }
        }
    }

    private fun initBaselineAlerts() {
        val initialAlerts = listOf(
            AlertItem(
                id = "alt-0",
                timestamp = System.currentTimeMillis() - 45000,
                title = "Edge AI Guard Active",
                description = "LoRaWAN gateway heartbeat verified across 7 city sectors. Noise filtering calibrated.",
                shield = ShieldType.ALL,
                risk = RiskLevel.GREEN,
                location = "Central Command Hub",
                isAiVerified = true
            ),
            AlertItem(
                id = "alt-1",
                timestamp = System.currentTimeMillis() - 25000,
                title = "Heritage Vibration Sensor Online",
                description = "Mubarak Mandi Palace sensor BLD-01 report: Ambient vibration 0.42 mm/s² within buffer.",
                shield = ShieldType.STRUCTURE,
                risk = RiskLevel.AMBER,
                location = "Old City / Panjtirthi",
                geo = GeoPoint(32.7410, 74.8720)
            )
        )
        _alerts.value = initialAlerts
    }

    private fun performRandomWalkTick(scope: CoroutineScope) {
        // Random walk for fire nodes
        _fireNodes.update { currentList ->
            currentList.map { node ->
                if (node.isAlert) {
                    node // Keep fire emergency hot
                } else {
                    val dTemp = (Random.nextDouble() - 0.48) * 0.4
                    val dSmoke = (Random.nextDouble() - 0.48) * 0.8
                    val dGas = (Random.nextDouble() - 0.48) * 0.6
                    val newTemp = ((node.temperatureC + dTemp) * 10).roundToInt() / 10.0
                    val newSmoke = ((node.smokePpm + dSmoke) * 10).roundToInt() / 10.0
                    val newGas = ((node.gasLevelPpm + dGas) * 10).roundToInt() / 10.0
                    node.copy(
                        temperatureC = newTemp.coerceIn(22.0, 38.0),
                        smokePpm = newSmoke.coerceIn(10.0, 45.0),
                        gasLevelPpm = newGas.coerceIn(6.0, 30.0),
                        lastUpdated = System.currentTimeMillis()
                    )
                }
            }
        }

        // Random walk for building nodes (unless tremor active)
        if (!_isTremorActive.value) {
            _buildingNodes.update { currentList ->
                currentList.map { bld ->
                    val dVib = (Random.nextDouble() - 0.5) * 0.04
                    val newVib = ((bld.vibrationMms2 + dVib) * 100).roundToInt() / 100.0
                    val computedRisk = calculateBuildingRisk(bld.tiltAngleDeg, bld.crackWidthMm, newVib.coerceAtLeast(0.05))
                    bld.copy(
                        vibrationMms2 = newVib.coerceIn(0.08, 1.2),
                        riskLevel = computedRisk
                    )
                }
            }
        }

        // Random walk for drain nodes
        _drainNodes.update { currentList ->
            currentList.map { drain ->
                val delta = if (_isRainActive.value) {
                    Random.nextDouble(2.0, 5.0)
                } else {
                    (Random.nextDouble() - 0.52) * 1.5
                }
                val newLevel = ((drain.waterLevelCm + delta) * 10).roundToInt() / 10.0
                val capped = newLevel.coerceIn(40.0, drain.maxCapacityCm * 0.98)
                val atRisk = (capped / drain.maxCapacityCm) >= 0.70
                drain.copy(
                    waterLevelCm = capped,
                    isAtRisk = atRisk
                )
            }
        }
    }

    private fun calculateBuildingRisk(tilt: Double, crackWidth: Double, vibration: Double): RiskLevel {
        return when {
            tilt >= 2.0 || crackWidth >= 5.0 || vibration >= 0.75 -> RiskLevel.RED
            tilt >= 1.2 || crackWidth >= 3.0 || vibration >= 0.38 -> RiskLevel.AMBER
            else -> RiskLevel.GREEN
        }
    }

    fun triggerFireEmergency(scope: CoroutineScope) {
        val targetIndex = Random.nextInt(_fireNodes.value.size)
        val targetNode = _fireNodes.value[targetIndex]

        val updatedNode = targetNode.copy(
            temperatureC = 78.4,
            smokePpm = 412.0,
            gasLevelPpm = 138.0,
            isAlert = true,
            isAiVerified = true,
            flameDetected = true,
            lastUpdated = System.currentTimeMillis()
        )

        _fireNodes.update { list ->
            list.mapIndexed { idx, item -> if (idx == targetIndex) updatedNode else item }
        }

        val alert = AlertItem(
            id = "fire-${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis(),
            title = "🔥 CRITICAL FIRE ALERT: ${updatedNode.location}",
            description = "Rapid thermal spike (${updatedNode.temperatureC}°C, ${updatedNode.smokePpm} ppm smoke) in dense sector. Edge AI verified (false alarm filtered). Rapid unit dispatched.",
            shield = ShieldType.FIRE,
            risk = RiskLevel.RED,
            location = updatedNode.location,
            isAiVerified = true,
            geo = updatedNode.geo
        )

        addAlert(alert)
        audioService.playSiren(scope)
        _smsEmergencyDialogAlert.value = alert

        // Find nearest fire station and dispatch route
        val station = CityData.FIRE_STATIONS.minByOrNull { (_, stGeo) ->
            val dLat = stGeo.lat - targetNode.geo.lat
            val dLng = stGeo.lng - targetNode.geo.lng
            dLat * dLat + dLng * dLng
        } ?: CityData.FIRE_STATIONS[0]

        // Create realistic multi-segment road route
        val midLat = (station.second.lat + targetNode.geo.lat) / 2 + 0.003
        val midLng = (station.second.lng + targetNode.geo.lng) / 2 - 0.002
        val waypoints = listOf(
            station.second,
            GeoPoint(station.second.lat + 0.004, station.second.lng + 0.001),
            GeoPoint(midLat, midLng),
            GeoPoint(targetNode.geo.lat - 0.002, targetNode.geo.lng + 0.001),
            targetNode.geo
        )

        val route = FireDispatchRoute(
            fireNodeId = updatedNode.id,
            stationName = station.first,
            startGeo = station.second,
            destinationGeo = targetNode.geo,
            waypointPath = waypoints,
            initialEtaSeconds = 180,
            remainingEtaSeconds = 180,
            isActive = true
        )
        _activeRoute.value = route

        // Start ETA countdown
        etaCountdownJob?.cancel()
        etaCountdownJob = scope.launch(Dispatchers.Default) {
            while (isActive && (_activeRoute.value?.remainingEtaSeconds ?: 0) > 0) {
                delay(1000)
                _activeRoute.update { curr ->
                    curr?.copy(remainingEtaSeconds = (curr.remainingEtaSeconds - 1).coerceAtLeast(0))
                }
            }
        }
    }

    fun simulateHeavyRain(scope: CoroutineScope) {
        _isRainActive.value = true
        audioService.playWarning()

        _drainNodes.update { list ->
            list.map { drain ->
                val surge = Random.nextDouble(55.0, 95.0)
                val newLevel = (drain.waterLevelCm + surge).coerceAtMost(drain.maxCapacityCm * 0.94)
                drain.copy(
                    waterLevelCm = ((newLevel * 10).roundToInt() / 10.0),
                    isAtRisk = (newLevel / drain.maxCapacityCm) >= 0.70
                )
            }
        }

        // Soil saturation increases structural vulnerability
        _buildingNodes.update { list ->
            list.map { bld ->
                if (bld.riskLevel == RiskLevel.AMBER) {
                    bld.copy(
                        tiltAngleDeg = bld.tiltAngleDeg + 0.4,
                        crackWidthMm = bld.crackWidthMm + 0.8,
                        riskLevel = RiskLevel.RED
                    )
                } else {
                    bld
                }
            }
        }

        val alert = AlertItem(
            id = "rain-${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis(),
            title = "🌧️ MONSOON SURGE & FLOOD WARNING",
            description = "Heavy precipitation (48mm/hr) detected across Tawi catchment. 5 of 8 drainage collectors operating above 70% capacity threshold. Sponge bioswales engaged.",
            shield = ShieldType.WATER,
            risk = RiskLevel.RED,
            location = "City-Wide Drainage Grid",
            isAiVerified = true
        )
        addAlert(alert)
    }

    fun simulateEarthquakeTremor(scope: CoroutineScope) {
        _isTremorActive.value = true
        audioService.playSiren(scope)

        _buildingNodes.update { list ->
            list.map { bld ->
                val tremorVib = Random.nextDouble(1.4, 2.6)
                val newTilt = bld.tiltAngleDeg + Random.nextDouble(0.3, 0.7)
                val newCrack = bld.crackWidthMm + Random.nextDouble(0.5, 1.4)
                val risk = calculateBuildingRisk(newTilt, newCrack, tremorVib)
                bld.copy(
                    tiltAngleDeg = ((newTilt * 10).roundToInt() / 10.0),
                    crackWidthMm = ((newCrack * 10).roundToInt() / 10.0),
                    vibrationMms2 = ((tremorVib * 100).roundToInt() / 100.0),
                    riskLevel = risk
                )
            }
        }

        val alert = AlertItem(
            id = "tremor-${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis(),
            title = "⚠️ SEISMIC ACCELERATION ALERT",
            description = "Seismic tremor peak detected (2.4 mm/s²). Mubarak Mandi Palace, Janipur Quarters & Bahu Fort transitioned to critical risk status. Structural shoring protocol active.",
            shield = ShieldType.STRUCTURE,
            risk = RiskLevel.RED,
            location = "Old City / Mubarak Mandi / Bahu Fort",
            isAiVerified = true
        )
        addAlert(alert)
        _smsEmergencyDialogAlert.value = alert
    }

    fun resetSimulation() {
        autoDemoJob?.cancel()
        etaCountdownJob?.cancel()
        _autoDemo.value = AutoDemoState()
        _activeRoute.value = null
        _isRainActive.value = false
        _isTremorActive.value = false
        _fireNodes.value = CityData.INITIAL_FIRE_NODES
        _buildingNodes.value = CityData.INITIAL_BUILDING_NODES
        _drainNodes.value = CityData.INITIAL_DRAIN_NODES
        _smsEmergencyDialogAlert.value = null

        val resetAlert = AlertItem(
            id = "reset-${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis(),
            title = "System Baseline Restored",
            description = "All simulated hazards cleared. Telemetry normalized to nominal baseline.",
            shield = ShieldType.ALL,
            risk = RiskLevel.GREEN,
            location = "Jammu Command Center",
            isAiVerified = true
        )
        addAlert(resetAlert)
        audioService.playBeep()
    }

    fun startAutoDemoMode(scope: CoroutineScope) {
        autoDemoJob?.cancel()
        resetSimulation()

        autoDemoJob = scope.launch(Dispatchers.Default) {
            _autoDemo.value = AutoDemoState(
                isRunning = true,
                currentStep = 1,
                caption = "Step 1/5: Jammu Rakshak baseline online. LoRaWAN sensors transmitting nominal telemetry across 7 sectors.",
                timeRemainingSeconds = 60
            )

            // Step 1: 0 to 12s
            for (sec in 60 downTo 49) {
                _autoDemo.update { it.copy(timeRemainingSeconds = sec) }
                delay(1000)
            }

            // Step 2: Fire Alert at Jewel Chowk (12s to 25s)
            triggerFireEmergency(scope)
            _autoDemo.update {
                it.copy(
                    currentStep = 2,
                    caption = "Step 2/5: 🔥 FIRE ALERT at Jewel Chowk! Edge AI verified (false alarm filtered). Route plotted & fire unit dispatched."
                )
            }
            for (sec in 48 downTo 36) {
                _autoDemo.update { it.copy(timeRemainingSeconds = sec) }
                delay(1000)
            }

            // Step 3: Heavy Rain (25s to 38s)
            simulateHeavyRain(scope)
            _autoDemo.update {
                it.copy(
                    currentStep = 3,
                    caption = "Step 3/5: 🌧️ MONSOON SURGE: Heavy cloudburst over Tawi basin. Drains rising to 85% capacity. Sponge zones absorbing runoff."
                )
            }
            for (sec in 35 downTo 23) {
                _autoDemo.update { it.copy(timeRemainingSeconds = sec) }
                delay(1000)
            }

            // Step 4: Structural Tremor & Red Alert (38s to 50s)
            simulateEarthquakeTremor(scope)
            _autoDemo.update {
                it.copy(
                    currentStep = 4,
                    caption = "Step 4/5: ⚠️ SEISMIC TREMOR: Vibration spike! Mubarak Mandi heritage wing tilt increases. Red structural alert active."
                )
            }
            for (sec in 22 downTo 11) {
                _autoDemo.update { it.copy(timeRemainingSeconds = sec) }
                delay(1000)
            }

            // Step 5: Multi-Agency Coordinated Dispatch (50s to 60s)
            _autoDemo.update {
                it.copy(
                    currentStep = 5,
                    caption = "Step 5/5: 🚨 INTER-AGENCY DISPATCH: Emergency broadcast generated in Dogri, Hindi & English. SDMA & JMC deployed."
                )
            }
            for (sec in 10 downTo 1) {
                _autoDemo.update { it.copy(timeRemainingSeconds = sec) }
                delay(1000)
            }

            // Finish
            _autoDemo.update {
                it.copy(
                    isRunning = false,
                    caption = "✅ Auto Demo Completed: All 3 Shields (Fire, Structure, Water) successfully demonstrated."
                )
            }
        }
    }

    fun stopAutoDemo() {
        autoDemoJob?.cancel()
        _autoDemo.value = AutoDemoState(isRunning = false)
    }

    fun addCitizenCrackReport(report: CitizenCrackReport) {
        _citizenReports.update { listOf(report) + it }
        val alert = AlertItem(
            id = "citizen-${report.id}",
            timestamp = report.reportedAt,
            title = "📸 CITIZEN CRACK AI REPORT: ${report.locationName}",
            description = "${report.assessment.crackType} (${report.assessment.estimatedWidthMm}mm) analyzed by Gemini AI (${report.assessment.confidence}% conf). ${report.assessment.explanation}",
            shield = ShieldType.STRUCTURE,
            risk = report.assessment.severity,
            location = report.locationName,
            isAiVerified = true,
            geo = report.geo
        )
        addAlert(alert)
        audioService.playWarning()
    }

    private fun addAlert(item: AlertItem) {
        _alerts.update { current ->
            (listOf(item) + current).take(25) // Keep latest 25
        }
    }

    fun selectLanguage(language: Language) {
        _selectedLanguage.value = language
        audioService.playBeep()
    }

    fun toggleSound() {
        val next = !_soundEnabled.value
        _soundEnabled.value = next
        audioService.isEnabled = next
        if (next) audioService.playBeep()
    }

    fun toggleSpongeLayer() {
        _spongeLayerVisible.value = !_spongeLayerVisible.value
        audioService.playBeep()
    }

    fun selectInspectorNode(node: Any?) {
        _selectedInspectorNode.value = node
        if (node != null) audioService.playBeep()
    }

    fun dismissSmsDialog() {
        _smsEmergencyDialogAlert.value = null
    }

    fun openSmsDialogForAlert(alert: AlertItem) {
        _smsEmergencyDialogAlert.value = alert
        audioService.playBeep()
    }

    fun release() {
        simulationJob?.cancel()
        etaCountdownJob?.cancel()
        autoDemoJob?.cancel()
        audioService.release()
    }
}
