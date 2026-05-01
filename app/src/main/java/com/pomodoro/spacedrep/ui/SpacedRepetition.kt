package com.pomodoro.spacedrep.ui

import com.pomodoro.spacedrep.data.MasteryLevel
import java.util.concurrent.TimeUnit

// Base intervals in days for spaced repetition
private val BASE_INTERVALS = listOf(1, 3, 5, 7, 14, 30, 45, 60, 90)

// For weak mastery, add extra early reviews
private val FAIBLE_INTERVALS = listOf(1, 2, 3, 5, 7, 14, 30, 45, 60, 90)

// For excellent mastery, skip J1 and J3
private val EXCELLENT_INTERVALS = listOf(5, 7, 14, 30, 45, 60, 90)

data class ReviewDate(val label: String, val dateMillis: Long)

fun computeReviews(startMillis: Long, mastery: MasteryLevel): List<ReviewDate> {
    val intervals = when (mastery) {
        MasteryLevel.FAIBLE -> FAIBLE_INTERVALS
        MasteryLevel.EXCELLENT -> EXCELLENT_INTERVALS
        else -> BASE_INTERVALS
    }
    return intervals.map { days ->
        val millis = startMillis + TimeUnit.DAYS.toMillis(days.toLong())
        ReviewDate("J$days", millis)
    }
}
