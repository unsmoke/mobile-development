package com.cpp.unsmoke.ui.main.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is plan Fragment"
    }

    val text: LiveData<String> = _text
}