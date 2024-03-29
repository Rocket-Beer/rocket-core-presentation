package com.rocket.android.core.navigation.extension

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Calls [observeViewLifecycleOwner] with [liveData] and [block], using the activity as the [LifecycleOwner]
 * @param liveData *liveData* parameter of the [observeViewLifecycleOwner] method
 * @param block *block* parameter of the [observeViewLifecycleOwner] method
 */
fun <T : Any, L : LiveData<T>> AppCompatActivity.observe(liveData: L, block: (T) -> Unit) {
    try {
        observeViewLifecycleOwner(liveData, block, this)
    } catch (exception: IllegalStateException) {
        observeViewLifecycleOwner(liveData, block, this)
    }
}

/**
 * Calls [observeViewLifecycleOwner] with [liveData] and [block], using the [Fragment.getViewLifecycleOwner] as the
 * [LifecycleOwner]. If the fragment's view is null, the fragment will be used as the [LifecycleOwner]
 * @param liveData *liveData* parameter of the [observeViewLifecycleOwner] method
 * @param block *block* parameter of the [observeViewLifecycleOwner] method
 */
fun <T : Any?, L : LiveData<T>> Fragment.observe(liveData: L, block: (T) -> Unit) {
    try {
        observeViewLifecycleOwner(liveData, block, viewLifecycleOwner)
    } catch (exception: IllegalStateException) {
        observeViewLifecycleOwner(liveData, block, this)
    }
}

/**
 * Calls [observeViewLifecycleOwner] with [liveData] and [block], using [lifecycleOwner] as the [LifecycleOwner]
 * @param liveData *liveData* parameter of the [observeViewLifecycleOwner] method
 * @param lifecycleOwner *lifecycleOwner* parameter of the [observeViewLifecycleOwner] method
 * @param block *block* parameter of the [observeViewLifecycleOwner] method
 */
fun <T : Any, L : LiveData<T>> View.observe(liveData: L, lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    observeViewLifecycleOwner(liveData, block, lifecycleOwner)
}

/**
 * Calls [LiveData.removeObservers] and [LiveData.observe] methods successively on [liveData]
 * @param liveData [LiveData] used to perform the methods
 * @param block type parameter for the *observer* parameter of the [LiveData.observe] method
 * @param lifecycleOwner *owner* parameter of the [LiveData.removeObservers] and [LiveData.observe] methods
 */
private fun <T : Any?, L : LiveData<T>> observeViewLifecycleOwner(
    liveData: L,
    block: (T) -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    liveData.removeObservers(lifecycleOwner)
    liveData.observe(lifecycleOwner, Observer(block))
}
