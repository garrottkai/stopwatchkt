package com.kaigarrott.stopwatchkt

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TaskExecutors private constructor(val db: Executor) {

    companion object {

        val instance = TaskExecutors(Executors.newSingleThreadExecutor())

    }
}
