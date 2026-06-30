package com.example.ppedetectionapp.detector

import com.example.ppedetectionapp.model.GearStatus
import com.example.ppedetectionapp.model.WorkerResult
import com.example.ppedetectionapp.utils.PPEConstants
import java.util.ArrayDeque
import kotlin.math.pow
import kotlin.math.sqrt

class PPEProcessor {

    private val smoother = GearSmoother(PPEConstants.SMOOTH_WINDOW)

    fun process(detections: List<BoundingBox>): List<WorkerResult> {
        val persons = detections.filter { it.classId == 6 }
        val gear = detections.filter { it.classId !in PPEConstants.SKIP_CLASSES }
        val noneDets = detections.filter { it.classId == 5 }

        val workerResults = mutableListOf<WorkerResult>()

        if (persons.isEmpty()) {
            for (nd in noneDets) {
                val statuses = PPEConstants.PPE_PAIRS.keys.map { gearName ->
                    GearStatus(gearName, "absent")
                }
                workerResults.add(WorkerResult(nd, statuses))
            }
        } else {
            for (personDet in persons) {
                val associated = associateGearToPerson(personDet, gear)
                val wr = evaluateWorker(personDet, associated)
                workerResults.add(wr)
            }
        }

        return smoother.update(workerResults)
    }

    private fun associateGearToPerson(
        personBox: BoundingBox,
        gearDetections: List<BoundingBox>
    ): List<BoundingBox> {
        val associated = mutableListOf<BoundingBox>()
        for (det in gearDetections) {
            val center = boxCenter(det)
            if (boxContainsPoint(personBox, center)) {
                associated.add(det)
                continue
            }
            if (iou(personBox, det) > 0.15f) {
                associated.add(det)
            }
        }
        return associated
    }

    private fun boxCenter(box: BoundingBox): Pair<Float, Float> {
        return Pair((box.x1 + box.x2) / 2f, (box.y1 + box.y2) / 2f)
    }

    private fun boxContainsPoint(box: BoundingBox, point: Pair<Float, Float>): Boolean {
        val marginX = (box.x2 - box.x1) * 0.15f
        val marginY = (box.y2 - box.y1) * 0.10f
        return (box.x1 - marginX) <= point.first && point.first <= (box.x2 + marginX) &&
                (box.y1 - marginY) <= point.second && point.second <= (box.y2 + marginY)
    }

    private fun iou(a: BoundingBox, b: BoundingBox): Float {
        val left = Math.max(a.x1, b.x1)
        val top = Math.max(a.y1, b.y1)
        val right = Math.min(a.x2, b.x2)
        val bottom = Math.min(a.y2, b.y2)

        val width = Math.max(0f, right - left)
        val height = Math.max(0f, bottom - top)

        val intersection = width * height
        val areaA = (a.x2 - a.x1) * (a.y2 - a.y1)
        val areaB = (b.x2 - b.x1) * (b.y2 - b.y1)

        val union = areaA + areaB - intersection
        if (union <= 0f) return 0f
        return intersection / union
    }

    private fun evaluateWorker(personBox: BoundingBox, workerDets: List<BoundingBox>): WorkerResult {
        val detected = workerDets.associateBy { it.classId }
        val statuses = mutableListOf<GearStatus>()

        for ((gearName, pair) in PPEConstants.PPE_PAIRS) {
            val (posId, negId) = pair
            if (detected.containsKey(posId)) {
                statuses.add(GearStatus(gearName, "present", detected[posId]!!.confidence))
            } else if (negId != null && detected.containsKey(negId)) {
                statuses.add(GearStatus(gearName, "absent", detected[negId]!!.confidence))
            } else {
                statuses.add(GearStatus(gearName, "absent", 0.0f))
            }
        }
        return WorkerResult(personBox = personBox, gearStatuses = statuses)
    }

    private class GearSmoother(private val window: Int) {
        private val history = mutableMapOf<Int, ArrayDeque<Map<String, String>>>()
        private val lastCenters = mutableMapOf<Int, Pair<Float, Float>>()

        fun update(workerResults: List<WorkerResult>): List<WorkerResult> {
            val usedSlots = mutableSetOf<Int>()
            val smoothedResults = mutableListOf<WorkerResult>()

            // Sort results so we match them consistently
            val sortedResults = workerResults.sortedBy { it.personBox.x1 }

            for (wr in sortedResults) {
                val center = Pair(
                    (wr.personBox.x1 + wr.personBox.x2) / 2f,
                    (wr.personBox.y1 + wr.personBox.y2) / 2f
                )
                
                // Use a smaller threshold for normalized coordinates (e.g., 15% of screen)
                var slot = findSlot(center, usedSlots, 0.15f)
                if (slot == null) {
                    slot = nextSlot()
                }

                usedSlots.add(slot)
                lastCenters[slot] = center

                if (!history.containsKey(slot)) {
                    history[slot] = ArrayDeque(window)
                }

                val frameStatuses = wr.gearStatuses.associate { it.name to it.status }
                val slotHistory = history[slot]!!
                if (slotHistory.size >= window) {
                    slotHistory.removeFirst()
                }
                slotHistory.addLast(frameStatuses)

                val smoothStatuses = mutableListOf<GearStatus>()
                for (gearName in PPEConstants.PPE_PAIRS.keys) {
                    val votes = slotHistory.map { it[gearName] ?: "absent" }
                    val presentVotes = votes.count { it == "present" }
                    
                    // Slightly bias toward 'present' to avoid false alarms (40% threshold)
                    val smoothedStatus = if (presentVotes >= slotHistory.size * 0.4) "present" else "absent"

                    val currentGear = wr.gearStatuses.find { it.name == gearName }
                    val conf = currentGear?.confidence ?: 0.0f

                    smoothStatuses.add(GearStatus(gearName, smoothedStatus, conf))
                }

                smoothedResults.add(WorkerResult(wr.personBox, smoothStatuses))
            }

            // Evict stale slots
            val stale = history.keys.filter { it !in usedSlots }
            for (s in stale) {
                // Decay the slot history - only remove if it's been empty/stale for a while
                if (history[s]!!.isNotEmpty()) {
                    history[s]!!.removeFirst()
                }
                if (history[s]!!.isEmpty()) {
                    history.remove(s)
                    lastCenters.remove(s)
                }
            }

            return smoothedResults
        }

        private fun findSlot(center: Pair<Float, Float>, usedSlots: Set<Int>, threshold: Float): Int? {
            var bestSlot: Int? = null
            var bestDist = threshold
            for ((slot, sc) in lastCenters) {
                if (slot in usedSlots) continue
                val dist = sqrt((center.first - sc.first).pow(2) + (center.second - sc.second).pow(2))
                if (dist < bestDist) {
                    bestDist = dist
                    bestSlot = slot
                }
            }
            return bestSlot
        }

        private fun nextSlot(): Int {
            return (history.keys.maxOrNull() ?: -1) + 1
        }
    }
}
