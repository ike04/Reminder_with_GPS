package com.google.codelab.reminderwithgps.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.activity.AddRemindActivity

class EditRemindFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.remind_list)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return inflater.inflate(R.layout.fragment_edit_remind, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_button -> {
                parentFragmentManager.popBackStack()
                return true
            }
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
