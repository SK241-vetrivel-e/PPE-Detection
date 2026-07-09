package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(

    onHomeClick: () -> Unit = {},

    onSettingsClick: () -> Unit = {},

    onAboutClick: () -> Unit = {}

) {

    ModalDrawerSheet {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "PPE Detection",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider()

        NavigationDrawerItem(
            label = { Text("Home") },
            selected = false,
            onClick = onHomeClick
        )

        NavigationDrawerItem(
            label = { Text("Settings") },
            selected = false,
            onClick = onSettingsClick
        )

        NavigationDrawerItem(
            label = { Text("About") },
            selected = false,
            onClick = onAboutClick
        )

    }

}