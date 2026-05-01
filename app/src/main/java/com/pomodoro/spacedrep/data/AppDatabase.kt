package com.pomodoro.spacedrep.data

import android.content.Context
import androidx.room.*

@Database(entities = [LearningSession::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): LearningSessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "pomodoro.db")
                    .build().also { INSTANCE = it }
            }
    }
}

class Converters {
    @TypeConverter fun fromMastery(value: MasteryLevel): String = value.name
    @TypeConverter fun toMastery(value: String): MasteryLevel = MasteryLevel.valueOf(value)
}
