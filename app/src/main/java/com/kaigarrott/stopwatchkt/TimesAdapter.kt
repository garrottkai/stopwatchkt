package com.kaigarrott.stopwatchkt

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.kaigarrott.stopwatchkt.data.TimeEntry


class TimesAdapter(private val mContext: Context) : RecyclerView.Adapter<TimesAdapter.TimeViewHolder> () {

    private var mDataSet: List<TimeEntry>? = null

    class TimeViewHolder(view: View, val textView: TextView = view.findViewById(R.id.times_textview)) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TimeViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.times_view, viewGroup, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: TimeViewHolder, position: Int) {
        viewHolder.textView.text = Utils.format(mDataSet?.get(position)?.value)
    }

    fun setData(timeEntries: List<TimeEntry>) {
        mDataSet = timeEntries
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mDataSet?.size ?: 0
    }
}
