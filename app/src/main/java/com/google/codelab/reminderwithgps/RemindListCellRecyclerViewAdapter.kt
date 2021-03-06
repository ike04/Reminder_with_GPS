package com.google.codelab.reminderwithgps

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.codelab.reminderwithgps.model.Remind

class RemindListCellRecyclerViewAdapter(
    private val remindList: List<Remind>,
    private val listener: ListListener
) : RecyclerView.Adapter<RemindListCellRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_remind_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val remind = remindList[position]
        holder.titleText.text = remind.title
        holder.date.text =
            DateFormat.format("yyyy/MM/dd kk:mm", remind.dateTime).toString()
        holder.memoText.text = remind.memo
        holder.isDone.isChecked = remind.isDone

        holder.itemView.setOnClickListener {
            listener.onClickRow(it, remindList[position])
        }
    }

    override fun getItemCount(): Int = remindList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleTextView)
        val memoText: TextView = view.findViewById(R.id.memoTextView)
        val date: TextView = view.findViewById(R.id.dateTextView)
        val isDone: CheckBox = view.findViewById(R.id.doneCheckBox)
    }

    interface ListListener {
        fun onClickRow(tappedView: View, selectedRemind: Remind)
    }
}
