package com.kaigarrott.stopwatchkt

import java.util.concurrent.TimeUnit

class Utils {

    companion object {
        fun format(value: Long?): String {
            if(value !is Long) return ""
            val h: Long = TimeUnit.MILLISECONDS.toHours(value)
            val m: Long = TimeUnit.MILLISECONDS.toMinutes(value - TimeUnit.MILLISECONDS.toMillis(h))
            val s: Long = TimeUnit.MILLISECONDS.toSeconds(value - TimeUnit.MILLISECONDS.toMillis(h) - TimeUnit.MILLISECONDS.toMillis(m))
            val cs: Long = Math.floor(TimeUnit.MILLISECONDS.toMillis(value - TimeUnit.MILLISECONDS.toMillis(h) - TimeUnit.MILLISECONDS.toMillis(m) - TimeUnit.MILLISECONDS.toMillis(s)) / 10.0).toLong()
            return String.format("%02d:%02d:%02d.%02d", h, m, s, cs)
        }
    }

}
