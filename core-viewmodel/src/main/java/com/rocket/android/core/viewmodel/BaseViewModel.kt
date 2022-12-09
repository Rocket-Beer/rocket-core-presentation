package com.rocket.android.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Contains the generic functions that can be used in any viewModel
 * @param coroutineExceptionHandler instance of [CoroutineExceptionHandler]
 * @param dispatcher instance of [CoroutineDispatcher]
 */
abstract class BaseViewModel(
    private val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(
            "BaseViewModel",
            "CoroutineExceptionHandler handled crash $exception \n ${exception.cause?.message}"
        )
    },
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    /**
     * Instance of [CoroutineScope], the context contains [dispatcher], [SupervisorJob] and [CoroutineExceptionHandler]
     */
    private var _scope =
        CoroutineScope(dispatcher + SupervisorJob() + coroutineExceptionHandler)

    /**
     * Gets [_scope] value
     */
    protected val scope
        get() = _scope

    /**
     * Check if [isJobActive] function is false, in this case restarts the scope
     */
    private fun reinitScope() {
        if (!isJobActive()) {
            _scope = CoroutineScope(dispatcher + SupervisorJob() + coroutineExceptionHandler)
        }
    }

    /**
     * Gets the current value of the Job,
     */
    private fun isJobActive() = _scope.isActive

    /**
     * Calls [onCleared] to destroy the viewmodel and [cancel] function to cancel the scope
     */
    override fun onCleared() {
        super.onCleared()
        _scope.cancel()
    }

    /**
     * Calls [cancel] function to cancel the scope
     */
    protected fun cancelJob() = _scope.cancel()

    /**
     * Calls [reinitScope] function and returns a new coroutine with the context of the dispatcher
     * @param dispatcher instance of [CoroutineDispatcher]
     * @param block calls a lambda function to execute in the coroutine
     */
    protected fun launch(dispatcher: CoroutineContext = this.dispatcher, block: suspend () -> Unit): Job {
        reinitScope()
        return _scope.launch(context = dispatcher) {
            block()
        }
    }

    /**
     * Calls [reinitScope] function and returns a new [async] cororutine, when it has received a response from the await method
     * @param dispatcher instance of [CoroutineDispatcher]
     * @param block calls a lambda function to execute in the coroutine
     */
    protected fun <T> async(dispatcher: CoroutineContext = this.dispatcher, block: suspend () -> T): Deferred<T> {
        reinitScope()
        return _scope.async(context = dispatcher) {
            block()
        }
    }
}
