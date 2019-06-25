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
        val model: TimesViewModel = ViewModelProviders.of(this).get(TimesViewModel.class)
        model.getTimes().observe(this, new Observer<List<TimeEntry>>() {
            @Override
            public void onChanged( List<TimeEntry> timeEntries) {
                mTimesAdapter.setData(timeEntries);
            }
        });
    }

    private void start() {
        mRunning = true;
        mButton.setImageResource(R.drawable.ic_stop);
        mThread = new StopwatchThread();
        mThread.start();
    }

    private void stop() {
        mRunning = false;
        mButton.setImageResource(R.drawable.ic_start);
        if(mThread != null) mThread.interrupt();
        mThread = null;
        final TimeEntry newEntry = new TimeEntry(mBegin, mElapsed);
        TaskExecutors.getInstance().db().execute(new Runnable() {
            @Override
            public void run() {
                mDb.timeDao().insert(newEntry);
            }
        });
    }

    public void toggle(View v) {
        if(mRunning) {
            stop();
        } else {
            start();
        }
    }

    private class StopwatchThread extends Thread {
        @Override
        public void run() {

            super.run();
            mBegin = new Date().getTime();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mElapsed = 0;
                    mOutputView.setText(getString(R.string.default_time_value));
                }
            });

            try {
                while(!this.isInterrupted()) {

                    final long diff = new Date().getTime() - mBegin;
                    final String out = Utils.format(diff);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mElapsed = diff;
                            mOutputView.setText(out);
                        }
                    });

                    Thread.sleep(10);

                }
            } catch (InterruptedException e) {
            }
        }
    }
}
