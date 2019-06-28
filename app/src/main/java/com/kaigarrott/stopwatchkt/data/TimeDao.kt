package com.kaigarrott.stopwatchkt.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface TimeDao {

    @Query("SELECT * FROM time ORDER BY timestamp ASC")
    fun getAll(): LiveData<List<TimeEntry>>

    @Query("SELECT * FROM time WHERE id = :id")
    fun getById(id: Int): LiveData<TimeEntry>

    @Insert
    fun insert(timeEntry: TimeEntry)

    @Update
    fun update(timeEntry: TimeEntry)

    @Delete
    fun delete(timeEntry: TimeEntry)

}
