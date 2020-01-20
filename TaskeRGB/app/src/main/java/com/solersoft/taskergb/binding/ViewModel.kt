package com.solersoft.taskergb.binding

import android.util.Log
import androidx.databinding.BaseObservable
import java.lang.Exception

// Aliases
typealias ErrorHandler = (error: Exception) -> Unit
// endregion


open class ViewModel : BaseObservable() {

    // region Member Variables
    private var errorHandler: ErrorHandler? = null // Optional error handler
    protected val TAG = this.javaClass.canonicalName // Tag used for logging
    // endregion

    // region Protected Methods
    protected fun handleError(e: Exception) {
        // First log
        Log.e(this.TAG, e.message)

        // Pass on to handler, if available
        errorHandler?.invoke(e)
    }
    // endregion

    // region Public Methods
    /**
     * Sets the handler for errors raised by the ViewModel.
     * @param handler - The method that handles errors.
     */
    fun setErrorHandler(handler: ErrorHandler?) {
        errorHandler = handler
    }
    // endregion
}