package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PPEStatusRow(

    helmet: String,
    vest: String,
    gloves: String,
    shoes: String,
    score: String

) {

    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        shape = RoundedCornerShape(16.dp),

        color = Color.Black.copy(alpha = 0.70f)

    ) {

        Column(

            modifier = Modifier.padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {

            Text(

                text = "Worker Safety Status",

                color = Color.White,

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold

            )

            PPEItem("Helmet", helmet)

            PPEItem("Vest", vest)

            PPEItem("Gloves", gloves)

            PPEItem("Shoes", shoes)

            Text(

                text = "Compliance Score : $score",

                color = Color.White,

                fontWeight = FontWeight.Bold

            )

        }

    }

}

@Composable
fun PPEItem(

    label: String,

    value: String

) {

    Row(

        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,

        verticalAlignment = Alignment.CenterVertically

    ) {

        Text(

            text = label,

            color = Color.White

        )

        Text(

            text = value,

            color = Color.White,

            fontWeight = FontWeight.Bold

        )

    }

}