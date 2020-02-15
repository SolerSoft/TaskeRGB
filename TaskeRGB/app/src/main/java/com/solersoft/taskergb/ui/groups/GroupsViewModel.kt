package com.solersoft.taskergb.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solersoft.taskergb.binding.SSViewModel

class GroupsViewModel : SSViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Groups Fragment"
    }
    val text: LiveData<String> = _text
}