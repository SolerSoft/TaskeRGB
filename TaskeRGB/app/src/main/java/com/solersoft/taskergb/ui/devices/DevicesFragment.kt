package com.solersoft.taskergb.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.solersoft.taskergb.R
import com.solersoft.taskergb.databinding.ActivityConfigPaletteBinding
import com.solersoft.taskergb.databinding.FragmentDevicesBinding

class DevicesFragment : Fragment() {

    // region Member Variables
    private lateinit var binding : FragmentDevicesBinding
    private lateinit var vm: DevicesViewModel
    // endregion

    // Overrides and Event Handlers
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the fragment and get the binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_devices, container, false)

        // Set lifecycle owner for LiveData bindings
        binding.lifecycleOwner = this

        // Get the ViewModel
        vm = ViewModelProviders.of(this).get(DevicesViewModel::class.java)

        // Bind to the ViewModel
        binding.vm = vm

        // Root is the inflated view
        return binding.root
    }
    // endregion
}