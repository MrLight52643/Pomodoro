package com.pomodoro.spacedrep.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LearningSessionDao {
    @Query("SELECT * FROM learning_sessions ORDER BY startDateMillis DESC")
    fun getAll(): LiveData<List<LearningSession>>

    @Insert
    suspend fun insert(session: LearningSession)

    @Delete
    suspend fun delete(session: LearningSession)
}
