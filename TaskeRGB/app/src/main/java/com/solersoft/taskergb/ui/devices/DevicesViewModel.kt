package com.solersoft.taskergb.ui.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solersoft.taskergb.binding.SSViewModel
import com.solersoft.taskergb.devices.DeviceScanner

class DevicesViewModel : SSViewModel() {

    init {
        DeviceScanner.startScanning()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is Devices Fragment"
    }
    val text: LiveData<String> = _text
}