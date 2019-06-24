package com.kaigarrott.stopwatchkt;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.arch.lifecycle.ViewModelProviders;

import com.kaigarrott.stopwatchkt.data.TimeDatabase;
import com.kaigarrott.stopwatchkt.data.TimeEntry;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StopwatchThread mThread;
    private boolean mRunning = false;
    private TimeDatabase mDb;
    private long mBegin;
    private long mElapsed;

    private TextView mOutputView;
    private FloatingActionButton mButton;
    private RecyclerView mTimesView;
    private TimesAdapter mTimesAdapter;
    private RecyclerView.LayoutManager mTimesLayoutManager;

    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = TimeDatabase.getInstance(getApplicationContext());

        mButton = findViewById(R.id.fab);
        mOutputView = findViewById(R.id.output);
        mTimesView = findViewById(R.id.times_view);
        mTimesView.setHasFixedSize(true);
        mTimesLayoutManager = new LinearLayoutManager(this);
        mTimesView.setLayoutManager(mTimesLayoutManager);
        //String[] testData = new String[]{"01:02:03.04", "02:02:03.04", "03:02:03.04", "04:02:03.04", "05:02:03.04", "06:02:03.04", "07:02:03.04", "08:02:03.04"};
        mTimesAdapter = new TimesAdapter(this);
        mTimesView.setAdapter(mTimesAdapter);
        mTimesView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        initViewModel();
    }

    private void initViewModel() {
        TimesViewModel model = ViewModelProviders.of(this).get(TimesViewModel.class);
        model.getTimes().observe(this, new Observer<List<TimeEntry>>() {
            @Override
            public void onChanged(@Nullable List<TimeEntry> timeEntries) {
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
