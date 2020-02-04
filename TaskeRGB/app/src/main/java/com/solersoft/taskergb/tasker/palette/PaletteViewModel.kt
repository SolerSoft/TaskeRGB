package com.solersoft.taskergb.tasker.palette

import android.content.Context
import androidx.databinding.Bindable
import com.solersoft.taskergb.R
import com.solersoft.taskergb.binding.ObservableViewModel
import com.solersoft.taskergb.binding.SSViewModel
import com.solersoft.taskergb.binding.bindDelegate
import java.lang.Exception
import kotlinx.coroutines.*

/**
 * A ViewModel for the Palette Configuration Activity.
 */
class PaletteViewModel() : SSViewModel() {

    // region Input Fields
    @get:Bindable
    var input : PaletteInput by bindDelegate(PaletteInput())
    // endregion

    // region Output Fields
    @get:Bindable
    var palette: PaletteEx? by bindDelegate(null)
    // endregion

    // region State Fields
    @get:Bindable
    var showingResults: Boolean by bindDelegate(false)
    // endregion

    // region Public Methods
    /**
     * Runs a test with the current input values.
     * @param context The context used for loading resources.
     */
    fun runTest(context: Context) {
        // Check for variables first
        var path = input.imagePath?.trim()
        if (path != null && path.startsWith('%')) {

            // Show help for variables in test
            showHelp(R.string.helpVarsInTest)

            // Bail
            return
        }

        // Run the following on main thread since we need to do binding
        // Note: PaletteAction does dispatch to other threads
        GlobalScope.launch(Dispatchers.Main) {
            try{
                // Stop showing results if currently showing
                showingResults = false

                // We're busy now
                busy = true

                // Run the task and set the output
                palette = PaletteAction.run(context, input)

                // Show results
                showingResults = true
            }
            catch (e: Exception) {
                // Some problem. Clear previous results, if any.
                palette = null

                // Pass on to handler
                showError(e)
            }
            finally {
                // No longer busy
                busy = false
            }
        }
    }
    // endregion
}