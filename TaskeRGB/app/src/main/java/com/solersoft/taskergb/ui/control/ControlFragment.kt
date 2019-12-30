package com.solersoft.taskergb.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.solersoft.taskergb.R

class ControlFragment : Fragment() {

    private lateinit var controlViewModel: ControlViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        controlViewModel =
                ViewModelProviders.of(this).get(ControlViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_control, container, false)
        val textView: TextView = root.findViewById(R.id.text_control)
        controlViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        view.findViewById<View>(R.id.button_home).setOnClickListener {
            val action = HomeFragmentDirections
                    .actionHomeFragmentToHomeSecondFragment("From HomeFragment")
            NavHostFragment.findNavController(this@ControlFragment)
                    .navigate(action)
        }
         */
    }
}