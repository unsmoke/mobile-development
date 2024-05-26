package com.cpp.unsmoke.utils.helper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class ObtainViewModelFactory {
    companion object {
        inline fun <reified T : ViewModel> obtain(owner: Context): T {
            val factory = ViewModelFactory.getInstance(owner)
            return ViewModelProvider(owner as ViewModelStoreOwner, factory)[T::class.java]
        }
    }
}