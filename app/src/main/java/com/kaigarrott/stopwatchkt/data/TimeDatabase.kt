package com.kaigarrott.stopwatchkt.data

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.content.Context

@Database(entities = [TimeEntry::class], version = 1, exportSchema = false)
abstract class TimeDatabase : RoomDatabase() {

    abstract fun timeDao(): TimeDao

    companion object {

        private var sInstance: TimeDatabase? = null
        private val LOCK = Any()
        private val DB_NAME = "times"

        fun getInstance(context: Context): TimeDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room.databaseBuilder(context.applicationContext, TimeDatabase::class.java, DB_NAME).build()
                }
            }
            return sInstance
        }
    }
}
