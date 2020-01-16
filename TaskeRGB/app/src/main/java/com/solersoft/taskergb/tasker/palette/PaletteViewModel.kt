package com.solersoft.taskergb.tasker.palette

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.solersoft.taskergb.BR
import com.solersoft.taskergb.ViewModel
import com.solersoft.taskergb.util.bindDelegate
import kotlinx.android.synthetic.main.activity_config_palette.*
import java.lang.Exception
import kotlin.properties.Delegates

/**
 * A ViewModel for the Palette Configuration Activity.
 */
class PaletteViewModel : ViewModel() {

    // region Input Fields
    @get:Bindable
    var input : PaletteInput by bindDelegate(PaletteInput())

    @get:Bindable
    var loadedImage: Bitmap? by bindDelegate(null)
    // endregion

    // region Output Fields
    @get:Bindable
    var erroMessage: String? by bindDelegate(null)

    @get:Bindable
    var result: PaletteResult? by bindDelegate(null)
    // endregion

    // region Public Methods
    /**
     * Runs a test with the current input values.
     */
    fun runTest() {
        try{
            // Process dynamic variables
            // TODO: Actually use file
            input.imagePath = "file:///storage/emulated/0/Android/data/com.joaomgcd.autonotification/files/com.spotify.music_-102440555"

            // Run the task and set the output
            result = PaletteAction.run(input)
        }
        catch (e: Exception) {
            // Some problem. Clear previous results, if any.
            result = null

            // Pass on to handler
            handleError(e)
        }
    }
    // endregion
}