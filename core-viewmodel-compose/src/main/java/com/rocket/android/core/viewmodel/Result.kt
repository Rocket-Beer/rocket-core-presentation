package com.rocket.android.core.viewmodel

import com.rocket.core.domain.error.Failure

/**
 * It´s called to get a response from a service and returns a different value depending on the status of the service response,
 * which can be [Success], [Loading] or [Failure].
 */
sealed class Result<out R> {
    /**
     * Data class [Success] receives a parameter and returns [Result]
     */
    data class Success<out T>(val data: T) : Result<T>()
    /**
     * Data class [Error] receives a [Failure] parameter and returns [Result]
     */
    data class Error<out T>(val error: Failure) : Result<T>()

    /**
     * Data class [Loading] receives a parameter and returns [Result]
     */
    data class Loading<out T>(val data: T? = null) : Result<T>()

    /**
     * Returns a different String depending on the data class being called.
     */
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
            is Loading<*> -> "Loading[data=$data]"
        }
    }
    /**
     * The value shall be true when the status is [Success] and the content of data is not null.
     */
    val succeeded
        get() = this is Success && data != null
}
