package com.rocket.android.core.viewmodel

import com.rocket.core.domain.error.Failure

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val error: Failure) : Result<T>()
    data class Loading<out T>(val data: T? = null) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
            is Loading<*> -> "Loading[data=$data]"
        }
    }

    val succeeded
        get() = this is Success && data != null
}