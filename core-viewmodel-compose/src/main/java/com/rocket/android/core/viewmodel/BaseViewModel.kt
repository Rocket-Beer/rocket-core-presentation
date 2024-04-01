package com.rocket.android.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e(
                "BaseViewModel",
                "CoroutineExceptionHandler handled crash $exception \n ${exception.cause?.message}"
            )
        }

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private var _scope =
        CoroutineScope(dispatcher + SupervisorJob() + coroutineExceptionHandler)
    protected val scope
        get() = _scope

    private fun reinitScope() {
        if (!isJobActive()) {
            _scope = CoroutineScope(dispatcher + SupervisorJob() + coroutineExceptionHandler)
        }
    }

    private fun isJobActive() = _scope.isActive

    override fun onCleared() {
        super.onCleared()
        _scope.cancel()
    }

    protected fun cancelJob() = _scope.cancel()

    protected fun launch(block: suspend () -> Unit): Job {
        reinitScope()
        return _scope.launch(context = dispatcher) {
            block()
        }
    }

    protected fun <T> async(block: suspend () -> T): Deferred<T> {
        reinitScope()
        return _scope.async(context = dispatcher) {
            block()
        }
    }
}
