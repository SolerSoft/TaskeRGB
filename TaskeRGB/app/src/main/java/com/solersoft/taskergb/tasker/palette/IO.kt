package com.solersoft.taskergb.tasker.palette

import android.graphics.Color
import androidx.annotation.ColorInt
import com.joaomgcd.taskerpluginlibrary.SimpleResult
import com.joaomgcd.taskerpluginlibrary.SimpleResultSuccess
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputObject
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.R
import com.solersoft.taskergb.requireRange
import java.util.*

/****************************************
 * Enums
 ****************************************/

/**
 * Defines the types of color targets that can be detected in an image.
 */
enum class ColorTargetType {
    /**
     * No color target selected. The color will be Black.
     */
    None,

    /**
     * A target which has the characteristics of a muted color which is dark in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#DARK_MUTED">DARK_MUTED</a>
     */
    DarkMuted,

    /**
     * A target which has the characteristics of a vibrant color which is dark in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#DARK_VIBRANT">DARK_VIBRANT</a>
     */
    DarkVibrant,

    /**
     * A target which has the characteristics of a muted color which is light in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#LIGHT_MUTED">LIGHT_MUTED</a>
     */
    LightMuted,

    /**
     * A target which has the characteristics of a vibrant color which is light in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#LIGHT_VIBRANT">LIGHT_VIBRANT</a>
     */
    LightVibrant,

    /**
     * A target which has the characteristics of a muted color which is neither light or dark.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#MUTED">MUTED</a>
     */
    Muted,

    /**
     * A target which has the characteristics of a vibrant color which is neither light or dark.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#VIBRANT">VIBRANT</a>
     */
    Light
}

/****************************************
 * Inputs
 ****************************************/

/**
 * Input for the Palette action.
 */
@TaskerInputRoot
class PaletteInput @JvmOverloads constructor(
        @field:TaskerInputObject(VAR_IMAGE_PATH, R.string.imagePathLabel, R.string.imagePathDescription) var imagePath: String? = null,
        @field:TaskerInputObject(VAR_COLOR1_TARGET, R.string.color1TargetLabel, R.string.color1TargetDescription) var color1Target: Int = ColorTargetType.LightVibrant.ordinal,
        @field:TaskerInputObject(VAR_COLOR2_TARGET, R.string.color2TargetLabel, R.string.color2TargetDescription) var color2Target: Int = ColorTargetType.Light.ordinal,
        @field:TaskerInputObject(VAR_COLOR3_TARGET, R.string.color3TargetLabel, R.string.color3TargetDescription) var color3Target: Int = ColorTargetType.LightMuted.ordinal,
        @field:TaskerInputObject(VAR_COLOR4_TARGET, R.string.color4TargetLabel, R.string.color4TargetDescription) var color4Target: Int = ColorTargetType.DarkVibrant.ordinal
) {

    companion object {
        const val VAR_IMAGE_PATH = "imagePath"
        const val VAR_COLOR1_TARGET = "color1Target"
        const val VAR_COLOR2_TARGET = "color2Target"
        const val VAR_COLOR3_TARGET = "color3Target"
        const val VAR_COLOR4_TARGET = "color4Target"
    }

    /**
     * Indicates if the input has a valid configuration.
     */
    public fun isValid() : SimpleResult {
        return SimpleResult.get {
            requireRange<ColorTargetType>(color1Target) {"$VAR_COLOR1_TARGET is not valid."}
            requireRange<ColorTargetType>(color2Target) {"$VAR_COLOR2_TARGET is not valid."}
            requireRange<ColorTargetType>(color3Target) {"$VAR_COLOR3_TARGET is not valid."}
            requireRange<ColorTargetType>(color4Target) {"$VAR_COLOR4_TARGET is not valid."}
        }
    }
}


/****************************************
 * Outputs
 ****************************************/

/**
 * Output for the Palette action.
 */
@TaskerOutputObject()
class PaletteOutput @JvmOverloads constructor(
        @get:TaskerOutputVariable(VAR_COLOR1, R.string.color1Label, R.string.color1Description)
        @ColorInt
        var color1: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_COLOR2, R.string.color2Label, R.string.color2Description)
        @ColorInt
        var color2: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_COLOR3, R.string.color3Label, R.string.color3Description)
        @ColorInt
        var color3: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_COLOR4, R.string.color4Label, R.string.color4Description)
        @ColorInt
        var color4: Int = Color.BLACK
) {
    companion object {
        const val VAR_COLOR1 = "color1"
        const val VAR_COLOR2 = "color2"
        const val VAR_COLOR3 = "color3"
        const val VAR_COLOR4 = "color4"
    }

    /**
     * Indicates if the output has a valid data.
     */
    public fun isValid() : SimpleResult {
        return SimpleResult.get {
            requireRange(color1, min= Color.BLACK, max= Color.WHITE) {"$VAR_COLOR1 is not a valid color"}
            requireRange(color2, min= Color.BLACK, max= Color.WHITE) {"$VAR_COLOR2 is not a valid color"}
            requireRange(color3, min= Color.BLACK, max= Color.WHITE) {"$VAR_COLOR3 is not a valid color"}
            requireRange(color4, min= Color.BLACK, max= Color.WHITE) {"$VAR_COLOR4 is not a valid color"}
        }
    }
}