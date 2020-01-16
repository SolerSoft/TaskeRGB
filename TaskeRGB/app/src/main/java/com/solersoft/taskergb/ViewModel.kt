package com.solersoft.taskergb

import android.util.Log
import androidx.databinding.BaseObservable
import java.lang.Exception

open class ViewModel : BaseObservable() {

    // region Member Variables
    private var onError: ((error: Exception) -> Unit)? = null // Optional error handler
    protected val TAG = this.javaClass.canonicalName // Tag used for logging
    // endregion

    // region Protected Methods
    protected fun handleError(e: Exception) {
        // First log
        Log.e(this.TAG, e.message)

        // Pass on to handler, if available
        onError?.invoke(e)
    }
    // endregion

    // region Public Methods
    /**
     * Sets the handler for errors raised by the ViewModel.
     * @param handler - The method that handles errors.
     */
    fun setErrorHandler(handler: ((error: Exception) -> Unit)?) {
        onError = handler
    }
    // endregion
}