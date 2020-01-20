package com.solersoft.taskergb.tasker.palette

import android.graphics.Bitmap
import androidx.databinding.Bindable
import com.solersoft.taskergb.BR
import com.solersoft.taskergb.R
import com.solersoft.taskergb.binding.ViewModel
import com.solersoft.taskergb.binding.bindDelegate
import kotlinx.android.synthetic.main.activity_config_palette.view.*
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.lang.Exception

/**
 * A ViewModel for the Palette Configuration Activity.
 */
class PaletteViewModel : ViewModel() {

    val allColorsBinding = ItemBinding.of<Int>(BR.color, R.layout.fragment_palettesimple)

    // region Input Fields
    @get:Bindable
    var input : PaletteInput by bindDelegate(PaletteInput())
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