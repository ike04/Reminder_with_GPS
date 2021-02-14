package com.google.codelab.reminderwithgps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RemindListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_remind_list, container, false)

        requireActivity().setTitle(R.string.remind_list)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_remind_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = RemindListCellRecyclerViewAdapter(
            createTestData(),
            object : RemindListCellRecyclerViewAdapter.ListListener {
                override fun onClickRow(tappedView: View, selectedRemind: Remind) {}
            }
        )

        val separateLine = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(separateLine)
    }


    private fun createTestData(): List<Remind> {
        val dataSet: MutableList<Remind> = ArrayList()
        var i = 1

        while (i <= 10) {
            val data = Remind()
            data.title = "テストデータ$i"
            data.lat = 35.6578976
            data.lng = 139.9070041
            data.dateTime = Date()
            data.isDone = true
            i += 1
        }
        return dataSet
    }
}
