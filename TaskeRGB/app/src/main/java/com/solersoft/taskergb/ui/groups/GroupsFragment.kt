package com.solersoft.taskergb.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.solersoft.taskergb.R

class GroupsFragment : Fragment() {

    private lateinit var groupsViewModel: GroupsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        groupsViewModel =
                ViewModelProviders.of(this).get(GroupsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_groups, container, false)
        val textView: TextView = root.findViewById(R.id.text_groups)
        groupsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}