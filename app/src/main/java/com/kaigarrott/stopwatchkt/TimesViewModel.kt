package com.kaigarrott.stopwatchkt

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.kaigarrott.stopwatchkt.data.TimeDatabase
import com.kaigarrott.stopwatchkt.data.TimeEntry

class TimesViewModel(
        application: Application,
        var db: TimeDatabase = TimeDatabase.getInstance(application),
        val times: LiveData<List<TimeEntry>> = db.timeDao().getAll()
) : AndroidViewModel (application)

