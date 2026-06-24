package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(

    onMenuClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}

) {

    Column {

        CenterAlignedTopAppBar(

            navigationIcon = {

                IconButton(
                    onClick = onMenuClick
                ) {

                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )

                }

            },

            title = {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "PPE Detection",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Construction Safety Monitoring",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                }

            },

            actions = {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "LIVE",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onSettingsClick
                    ) {

                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )

                    }

                }

            }

        )

        HorizontalDivider()

    }

}