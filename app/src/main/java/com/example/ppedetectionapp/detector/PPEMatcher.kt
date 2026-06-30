package com.example.ppedetectionapp.detector

import android.util.Log
import com.example.ppedetectionapp.model.PersonStatus

object PPEMatcher {

    fun match(boxes: List<BoundingBox>): List<PersonStatus> {

        Log.d("PPE", "========================")
        Log.d("PPE", "Total Boxes = ${boxes.size}")

        // Print every detected class
        for (box in boxes) {
            Log.d("PPE", "Raw Class = '${box.className}'")
        }

        // Find all persons
        val persons = boxes.filter {

            Log.d(
                "PPE",
                "Comparing '${it.className}'"
            )

            it.classId == 6
        }

        Log.d("PPE", "Persons Found = ${persons.size}")

        val results = mutableListOf<PersonStatus>()

        for (person in persons) {

            Log.d(
                "PPE",
                "Person Box = (${person.x1}, ${person.y1}) -> (${person.x2}, ${person.y2})"
            )

            val status = PersonStatus(person)

            for (box in boxes) {

                if (box == person)
                    continue

                val itemClass = box.className
                    .trim()
                    .lowercase()

                Log.d(
                    "PPE",
                    "Checking $itemClass"
                )

                if (!isInside(person, box)) {

                    Log.d(
                        "PPE",
                        "$itemClass OUTSIDE Person"
                    )

                    continue
                }

                Log.d(
                    "PPE",
                    "$itemClass INSIDE Person"
                )

                when (itemClass) {

                    "helmet" -> status.helmet = true

                    "vest" -> status.vest = true

                    "gloves" -> status.gloves = true

                    "boots" -> status.boots = true

                    "goggles" -> status.goggles = true
                }
            }

            Log.d(
                "PPE",
                "Helmet=${status.helmet}"
            )

            Log.d(
                "PPE",
                "Vest=${status.vest}"
            )

            Log.d(
                "PPE",
                "Gloves=${status.gloves}"
            )

            Log.d(
                "PPE",
                "Boots=${status.boots}"
            )

            Log.d(
                "PPE",
                "Goggles=${status.goggles}"
            )

            results.add(status)
        }

        Log.d("PPE", "Workers Found = ${results.size}")
        Log.d("PPE", "========================")

        return results
    }

    private fun isInside(
        person: BoundingBox,
        item: BoundingBox
    ): Boolean {

        val centerX = (item.x1 + item.x2) / 2f
        val centerY = (item.y1 + item.y2) / 2f

        return centerX >= person.x1 &&
                centerX <= person.x2 &&
                centerY >= person.y1 &&
                centerY <= person.y2
    }
}