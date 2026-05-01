package com.pomodoro.spacedrep.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MasteryLevel(val label: String) {
    FAIBLE("Faible"),
    MOYEN("Moyen"),
    FORT("Fort"),
    EXCELLENT("Excellent")
}

@Entity(tableName = "learning_sessions")
data class LearningSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookName: String,
    val pagesLearned: Int,
    val repetitions: Int,
    val masteryLevel: MasteryLevel,
    val startDateMillis: Long = System.currentTimeMillis()
)
