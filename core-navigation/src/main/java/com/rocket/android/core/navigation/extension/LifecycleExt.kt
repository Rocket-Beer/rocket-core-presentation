package com.rocket.android.core.navigation.extension

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any, L : LiveData<T>> AppCompatActivity.observe(liveData: L, block: (T) -> Unit) {
    try {
        observeViewLifecycleOwner(liveData, block, this)
    } catch (exception: IllegalStateException) {
        observeViewLifecycleOwner(liveData, block, this)
    }
}

fun <T : Any?, L : LiveData<T>> Fragment.observe(liveData: L, block: (T) -> Unit) {
    try {
        observeViewLifecycleOwner(liveData, block, viewLifecycleOwner)
    } catch (exception: IllegalStateException) {
        observeViewLifecycleOwner(liveData, block, this)
    }
}

fun <T : Any, L : LiveData<T>> View.observe(liveData: L, lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    observeViewLifecycleOwner(liveData, block, lifecycleOwner)
}

private fun <T : Any?, L : LiveData<T>> observeViewLifecycleOwner(
    liveData: L,
    block: (T) -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    liveData.removeObservers(lifecycleOwner)
    liveData.observe(lifecycleOwner, Observer(block))
}
