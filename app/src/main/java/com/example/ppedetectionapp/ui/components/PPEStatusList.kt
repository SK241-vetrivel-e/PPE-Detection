package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.ppedetectionapp.model.PersonStatus

@Composable
fun PPEStatusList(
    workers: List<PersonStatus>
) {

    LazyColumn {

        itemsIndexed(workers) { index, worker ->

            PPEStatusCard(
                workerId = index + 1,
                status = worker
            )

        }

    }

}