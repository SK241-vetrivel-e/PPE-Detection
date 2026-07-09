package com.example.ppedetectionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ppedetectionapp.ui.components.TopBar
import com.example.ppedetectionapp.viewmodel.PPEViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit = {},
    viewModel: PPEViewModel = viewModel()
) {
    val workerResults = viewModel.workerResults
    val personDetected = workerResults.isNotEmpty()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Safety Dashboard",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DrawerSectionHeader("Safety Guidelines", Icons.Default.Shield)
                    SafetyItem("Always wear Helmet", "Protects against falling objects.")
                    SafetyItem("Wear High-Vis Vest", "Ensures visibility in low light.")
                    SafetyItem("Protective Gloves", "Prevents hand injuries.")
                    
                    HorizontalDivider()

                    DrawerSectionHeader("App Information", Icons.Default.Info)
                    InfoRow("Version", "1.0.0")
                    InfoRow("Model", "YOLOv8-Small")
                    InfoRow("Mode", "Real-time AI Detection")

                    HorizontalDivider()

                    DrawerSectionHeader("Session Stats", Icons.Default.Analytics)
                    InfoRow("Inference", "${viewModel.inferenceTime} ms")
                    InfoRow("Workers Found", "${workerResults.size}")
                    val violations = workerResults.count { !it.compliant }
                    InfoRow("Active Violations", "$violations")
                }
            }
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onSettingsClick = onSettingsClick
                )
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CameraScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel
                )

                // HUD Panel (Muted/Mild colors)
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "PPE MONITOR",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Workers: ${workerResults.size}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 11.sp
                    )
                    val violations = workerResults.count { !it.compliant }
                    Text(
                        text = "Violations: $violations",
                        color = if (violations > 0) MaterialTheme.colorScheme.error else Color(0xFF43A047),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Inference: ${viewModel.inferenceTime} ms",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (!personDetected) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "Waiting for Person...",
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            title, 
            style = MaterialTheme.typography.titleMedium, 
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SafetyItem(title: String, description: String) {
    Column(modifier = Modifier.padding(start = 32.dp)) {
        Text(
            title, 
            fontWeight = FontWeight.SemiBold, 
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            description, 
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
            fontSize = 12.sp
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label, 
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
            fontSize = 14.sp
        )
        Text(
            value, 
            fontWeight = FontWeight.Bold, 
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
