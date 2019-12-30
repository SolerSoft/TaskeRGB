package com.solersoft.taskergb.ui.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DevicesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Devices Fragment"
    }
    val text: LiveData<String> = _text
}