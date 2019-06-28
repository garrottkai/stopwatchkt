package com.kaigarrott.stopwatchkt.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey


//@Entity(tableName = "time")
//class TimeEntry(@PrimaryKey(autoGenerate = true) val id: Int, val timestamp: Long, val value: Long)

@Entity(tableName = "time")
class TimeEntry {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var timestamp: Long = 0
    var value: Long = 0

    @Ignore
    constructor(timestamp: Long, value: Long) {
        this.timestamp = timestamp
        this.value = value
    }

    constructor(id: Int, timestamp: Long, value: Long) {
        this.id = id
        this.timestamp = timestamp
        this.value = value
    }
}
