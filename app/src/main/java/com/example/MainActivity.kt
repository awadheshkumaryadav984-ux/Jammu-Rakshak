package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AlertItem
import com.example.service.AudioAlertService
import com.example.service.SimulationEngine
import com.example.ui.components.AlertFeed
import com.example.ui.components.ArchitectureDialog
import com.example.ui.components.AutoDemoCaptionOverlay
import com.example.ui.components.CitizenCrackReportDialog
import com.example.ui.components.DemoControlsDialog
import com.example.ui.components.EmergencyBroadcastDialog
import com.example.ui.components.KpiRow
import com.example.ui.components.ShieldTabs
import com.example.ui.components.TacticalMap
import com.example.ui.components.TopBar
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.JammuRakshakTheme

class MainActivity : ComponentActivity() {

    private lateinit var audioService: AudioAlertService
    private lateinit var simulationEngine: SimulationEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        audioService = AudioAlertService()
        simulationEngine = SimulationEngine(audioService)

        setContent {
            JammuRakshakTheme {
                val scope = rememberCoroutineScope()

                DisposableEffect(Unit) {
                    simulationEngine.start(scope)
                    onDispose {
                        simulationEngine.release()
                    }
                }

                DashboardScreen(simulationEngine = simulationEngine)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simulationEngine.release()
    }
}

@Composable
fun DashboardScreen(simulationEngine: SimulationEngine) {
    val scope = rememberCoroutineScope()

    // State collections
    val fireNodes by simulationEngine.fireNodes.collectAsState()
    val buildingNodes by simulationEngine.buildingNodes.collectAsState()
    val drainNodes by simulationEngine.drainNodes.collectAsState()
    val spongeZones by simulationEngine.spongeZones.collectAsState()
    val alerts by simulationEngine.alerts.collectAsState()
    val citizenReports by simulationEngine.citizenReports.collectAsState()
    val activeRoute by simulationEngine.activeRoute.collectAsState()
    val selectedLanguage by simulationEngine.selectedLanguage.collectAsState()
    val soundEnabled by simulationEngine.soundEnabled.collectAsState()
    val spongeLayerVisible by simulationEngine.spongeLayerVisible.collectAsState()
    val autoDemoState by simulationEngine.autoDemo.collectAsState()
    val selectedNode by simulationEngine.selectedInspectorNode.collectAsState()
    val smsAlert by simulationEngine.smsEmergencyDialogAlert.collectAsState()

    // Dialog visibility states
    var showDemoControls by remember { mutableStateOf(false) }
    var showCrackReport by remember { mutableStateOf(false) }
    var showArchitecture by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("jammu_rakshak_main_scaffold"),
        containerColor = CyberNavyDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CyberNavyDark)
        ) {
            // 1. Top Bar with Live Clock, Branding, Pulse, Language, Sound
            TopBar(
                currentLanguage = selectedLanguage,
                soundEnabled = soundEnabled,
                onLanguageSelected = { simulationEngine.selectLanguage(it) },
                onToggleSound = { simulationEngine.toggleSound() },
                onOpenCrackReport = { showCrackReport = true },
                onOpenArchitecture = { showArchitecture = true },
                onOpenDemoControls = { showDemoControls = true }
            )

            // Auto Demo Narrative Caption Banner (when active)
            AutoDemoCaptionOverlay(
                autoDemoState = autoDemoState,
                onStop = { simulationEngine.stopAutoDemo() }
            )

            // 2. Top Row KPI Cards
            KpiRow(
                fireNodes = fireNodes,
                buildingNodes = buildingNodes,
                drainNodes = drainNodes,
                activeRoute = activeRoute
            )

            // 3. Middle Area: Tactical Map + Alert Feed (Responsive layout)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                val isWideScreen = maxWidth > 720.dp

                if (isWideScreen) {
                    // Landscape / Tablet Split: Map on Left, Alert Feed on Right
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        TacticalMap(
                            fireNodes = fireNodes,
                            buildingNodes = buildingNodes,
                            drainNodes = drainNodes,
                            spongeZones = spongeZones,
                            citizenReports = citizenReports,
                            activeRoute = activeRoute,
                            spongeLayerVisible = spongeLayerVisible,
                            onToggleSpongeLayer = { simulationEngine.toggleSpongeLayer() },
                            selectedNode = selectedNode,
                            onSelectNode = { simulationEngine.selectInspectorNode(it) },
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxSize()
                        )

                        AlertFeed(
                            alerts = alerts,
                            onSelectAlert = { alert ->
                                alert.geo?.let {
                                    val match = fireNodes.find { fn -> fn.location == alert.location }
                                        ?: buildingNodes.find { bn -> bn.location == alert.location }
                                        ?: drainNodes.find { dn -> dn.location == alert.location }
                                    simulationEngine.selectInspectorNode(match)
                                }
                            },
                            onOpenSmsDialog = { simulationEngine.openSmsDialogForAlert(it) },
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxSize()
                        )
                    }
                } else {
                    // Portrait / Handheld: Tactical Map on Top, Compact Alert Feed underneath
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp)
                    ) {
                        TacticalMap(
                            fireNodes = fireNodes,
                            buildingNodes = buildingNodes,
                            drainNodes = drainNodes,
                            spongeZones = spongeZones,
                            citizenReports = citizenReports,
                            activeRoute = activeRoute,
                            spongeLayerVisible = spongeLayerVisible,
                            onToggleSpongeLayer = { simulationEngine.toggleSpongeLayer() },
                            selectedNode = selectedNode,
                            onSelectNode = { simulationEngine.selectInspectorNode(it) },
                            modifier = Modifier
                                .weight(1.35f)
                                .fillMaxWidth()
                        )

                        AlertFeed(
                            alerts = alerts,
                            onSelectAlert = { alert ->
                                alert.geo?.let {
                                    val match = fireNodes.find { fn -> fn.location == alert.location }
                                        ?: buildingNodes.find { bn -> bn.location == alert.location }
                                        ?: drainNodes.find { dn -> dn.location == alert.location }
                                    simulationEngine.selectInspectorNode(match)
                                }
                            },
                            onOpenSmsDialog = { simulationEngine.openSmsDialogForAlert(it) },
                            modifier = Modifier
                                .weight(0.95f)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Bottom Row: Three Shield Tabs with Telemetry Charts
            ShieldTabs(
                fireNodes = fireNodes,
                buildingNodes = buildingNodes,
                drainNodes = drainNodes,
                onSelectNode = { simulationEngine.selectInspectorNode(it) },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // --- Dialogs ---
        DemoControlsDialog(
            isOpen = showDemoControls,
            autoDemoState = autoDemoState,
            onDismiss = { showDemoControls = false },
            onTriggerFire = { simulationEngine.triggerFireEmergency(scope) },
            onSimulateRain = { simulationEngine.simulateHeavyRain(scope) },
            onSimulateTremor = { simulationEngine.simulateEarthquakeTremor(scope) },
            onReset = { simulationEngine.resetSimulation() },
            onStartAutoDemo = { simulationEngine.startAutoDemoMode(scope) },
            onStopAutoDemo = { simulationEngine.stopAutoDemo() }
        )

        CitizenCrackReportDialog(
            isOpen = showCrackReport,
            onDismiss = { showCrackReport = false },
            onSubmitReport = { report -> simulationEngine.addCitizenCrackReport(report) }
        )

        EmergencyBroadcastDialog(
            alert = smsAlert,
            currentLanguage = selectedLanguage,
            onDismiss = { simulationEngine.dismissSmsDialog() }
        )

        ArchitectureDialog(
            isOpen = showArchitecture,
            onDismiss = { showArchitecture = false }
        )
    }
}
