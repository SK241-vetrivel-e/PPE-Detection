package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ppedetectionapp.model.PersonStatus

@Composable
fun PPEStatusCard(
    workerId: Int,
    status: PersonStatus
) {

    val score =
        listOf(
            status.helmet,
            status.vest,
            status.gloves,
            status.boots,
            status.goggles
        ).count { it } * 20

    val compliant = score >= 80

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )

    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Worker $workerId",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            PPERow("Helmet", status.helmet)
            PPERow("Vest", status.vest)
            PPERow("Gloves", status.gloves)
            PPERow("Boots", status.boots)
            PPERow("Goggles", status.goggles)

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Compliance Score",
                color = Color.LightGray
            )

            Text(
                text = "$score%",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (compliant)
                    "✅ COMPLIANT"
                else
                    "⚠ NON-COMPLIANT",

                color = if (compliant)
                    Color.Green
                else
                    Color.Red,

                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

        }

    }

}

@Composable
private fun PPERow(
    title: String,
    detected: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        horizontalArrangement = Arrangement.SpaceBetween,

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            color = Color.White
        )

        if (detected) {

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.Green
            )

        } else {

            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = null,
                tint = Color.Red
            )

        }

    }

}