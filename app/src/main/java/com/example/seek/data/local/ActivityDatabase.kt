package com.example.seek.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.seek.data.model.ActivityDao
import com.example.seek.data.model.Activity

@Database(entities = [Activity::class], version = 1, exportSchema = false)
abstract class ActivityDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var instance: ActivityDatabase? = null

        fun getDatabase(context: Context): ActivityDatabase {
            return instance
                ?: synchronized(this) {
                    buildDatabase(
                        context
                    )
                }
        }

        private fun buildDatabase(context: Context): ActivityDatabase {
            val newInstance = Room.databaseBuilder(
                context.applicationContext,
                ActivityDatabase::class.java,
                "activity_database"
            ).build()

            instance = newInstance
            return newInstance
        }
    }
}