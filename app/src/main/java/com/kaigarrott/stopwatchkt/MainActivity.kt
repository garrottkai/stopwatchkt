package com.kaigarrott.stopwatchkt

import android.arch.lifecycle.Observer
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.arch.lifecycle.ViewModelProviders

import com.kaigarrott.stopwatchkt.data.TimeDatabase
import com.kaigarrott.stopwatchkt.data.TimeEntry
import java.util.*

class MainActivity : AppCompatActivity () {

    private var mThread: StopwatchThread? = null
    private var mRunning: Boolean = false
    private var mDb: TimeDatabase? = null
    private var mBegin: Long = 0
    private var mElapsed: Long = 0

    private var mOutputView: TextView? = null
    private var mButton: FloatingActionButton? = null
    private var mTimesView: RecyclerView? = null
    private var mTimesAdapter: TimesAdapter? = null;
    private var mTimesLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDb = TimeDatabase.getInstance(this.applicationContext)

        mButton = findViewById(R.id.fab)
        mOutputView = findViewById(R.id.output)
        mTimesView = findViewById(R.id.times_view)
        mTimesView?.setHasFixedSize(true)
        mTimesLayoutManager = LinearLayoutManager(this)
        mTimesView?.layoutManager = mTimesLayoutManager
        mTimesAdapter = TimesAdapter(this)
        mTimesView?.adapter = mTimesAdapter
        mTimesView?.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        initViewModel()
    }

    private fun initViewModel() {
        val model = ViewModelProviders.of(this).get(TimesViewModel::class.java)
        model.times?.observe(this, Observer<List<TimeEntry>> { timeEntries -> mTimesAdapter?.setData(timeEntries) })
    }

    private fun start() {
        mRunning = true
        mButton?.setImageResource(R.drawable.ic_stop)
        mThread = StopwatchThread()
        mThread?.start()
    }

    private fun stop() {
        mRunning = false
        mButton?.setImageResource(R.drawable.ic_start)
        if (mThread != null) mThread?.interrupt()
        mThread = null
        val newEntry = TimeEntry(mBegin, mElapsed)
        TaskExecutors.instance.db.execute { mDb?.timeDao()?.insert(newEntry) }
    }

    fun toggle(v: View) {
        if (mRunning) {
            stop()
        } else {
            start()
        }
    }

    private inner class StopwatchThread : Thread() {
        override fun run() {

            super.run()
            mBegin = Date().time

            runOnUiThread {
                mElapsed = 0
                mOutputView?.text = getString(R.string.default_time_value)
            }

            try {
                while (!this.isInterrupted) {

                    val diff = Date().time - mBegin
                    val out = Utils.format(diff)

                    runOnUiThread {
                        mElapsed = diff
                        mOutputView?.text = out
                    }

                    sleep(10)

                }
            } catch (e: InterruptedException) {
            }

        }
    }
}
