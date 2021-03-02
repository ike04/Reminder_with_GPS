package com.google.codelab.reminderwithgps.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.model.Remind
import com.google.codelab.reminderwithgps.RemindListCellRecyclerViewAdapter
import com.google.codelab.reminderwithgps.activity.AddRemindActivity
import java.lang.Math.random
import java.util.*

class RemindListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_remind_list, container, false)

        requireActivity().setTitle(R.string.remind_list)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        view.findViewById<RecyclerView>(R.id.recycler_remind_list).apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = RemindListCellRecyclerViewAdapter(
                createTestData(),
                object : RemindListCellRecyclerViewAdapter.ListListener {
                    override fun onClickRow(tappedView: View, selectedRemind: Remind) {
                        setFragmentResult(
                            "selected_remind", bundleOf(
                                "remind_title" to selectedRemind.title,
                                "remind_memo" to selectedRemind.memo,
                                "remind_lat" to selectedRemind.lat,
                                "remind_lng" to selectedRemind.lng,
                                "remind_date" to selectedRemind.dateTime,
                                "remind_done" to selectedRemind.isDone
                            )
                        )
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.activity_main, EditRemindFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
            )
            val separateLine = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(separateLine)
        }

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(activity, AddRemindActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTestData(): List<Remind> {
        val dataSet: MutableList<Remind> = ArrayList()
        var i = 1

        while (i <= 10) {
            val data = Remind()
            data.title = "テストデータ$i"
            data.memo = ""
            data.lat = random()
            data.lng = random()
            data.dateTime = Date()
            data.isDone = i % 2 ==0

            dataSet.add(data)
            i += 1
        }
        return dataSet
    }
}
