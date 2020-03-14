package com.solersoft.taskergb.binding

import android.util.Log
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import java.lang.Exception

// Aliases
typealias ErrorHandler = (error: Exception) -> Unit
typealias HelpHandler = (markupId : Int) -> Unit
// endregion


open class SSViewModel : ObservableViewModel() {

    // region Common ViewModel Fields
    @get:Bindable
    var erroMessage: String? by observableProperty(null)

    @get:Bindable
    var busy: Boolean by observableProperty(false)
    // endregion


    // region Member Variables
    private var errorHandler: ErrorHandler? = null // Optional error handler
    private var helpHandler: HelpHandler? = null // Optional help handler
    protected val TAG = this.javaClass.canonicalName // Tag used for logging
    // endregion

    // region Protected Methods
    protected open fun showError(e: Exception) {
        // First log
        Log.e(this.TAG, e.message)

        // Display
        erroMessage = e.message

        // Pass on to handler, if available
        errorHandler?.invoke(e)
    }

    protected open fun showHelp(@StringRes markupId : Int) {

        // If there's no handler, log it
        if (helpHandler == null) {
            // First log
            Log.e(this.TAG, "No help handler available to display help ID $markupId.")
        }
        else {
            helpHandler!!.invoke(markupId)
        }
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

    /**
     * Sets the handler for help messages raised by the ViewModel.
     * @param handler - The method that handles errors.
     */
    fun setHelpHandler(handler: HelpHandler?) {
        helpHandler = handler
    }
    // endregion
}