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
import com.solersoft.taskergb.VAR_PREFIX
import com.solersoft.taskergb.requireName
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
     * A target which has the characteristics of a color which shows up most frequently.
     * @see <a href="https://developer.android.com/reference/kotlin/androidx/palette/graphics/Palette#getDominantSwatch()">getDominantSwatch</a>
     */
    Dominant,

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
    Vibrant
}

/****************************************
 * Inputs
 ****************************************/

/**
 * Input for the Palette action.
 */
@TaskerInputRoot
class PaletteInput @JvmOverloads constructor(
        @field:TaskerInputField(VAR_IMAGE_PATH, R.string.imagePathLabel, R.string.imagePathDescription) var imagePath: String? = null,
        @field:TaskerInputField(VAR_DEFAULT_COLOR, R.string.defaultColorLabel, R.string.defaultColorDescription, ignoreInStringBlurb = true) @ColorInt var defaultColor: Int = Color.BLACK
) {

    companion object {
        const val VAR_IMAGE_PATH = VAR_PREFIX + "imagepath"
        const val VAR_DEFAULT_COLOR = VAR_PREFIX + "defaultcolor"
    }

    /**
     * Indicates if there is a valid configuration.
     */
    fun isValid() : SimpleResult {
        return SimpleResult.get {
            validate()
        }
    }

    /**
     * Validates the data or throws an exception describing the error.
     */
    fun validate() {
        require(!imagePath.isNullOrBlank()) {"$VAR_IMAGE_PATH is not valid."}
        requireRange(defaultColor, min = Color.BLACK, max = Color.WHITE) { "$VAR_DEFAULT_COLOR is not a valid color" }
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
        @get:TaskerOutputVariable(VAR_DARK_MUTED, R.string.darkMutedLabel, R.string.darkMutedDescription)
        @ColorInt
        var darkMuted: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_DARK_VIBRANT, R.string.darkVibrantLabel, R.string.darkVibrantDescription)
        @ColorInt
        var darkVibrant: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_DOMINANT, R.string.dominantLabel, R.string.dominantDescription)
        @ColorInt
        var dominant: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_LIGHT_MUTED, R.string.lightMutedLabel, R.string.lightMutedDescription)
        @ColorInt
        var lightMuted: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_LIGHT_VIBRANT, R.string.lightVibrantLabel, R.string.lightVibrantDescription)
        @ColorInt
        var lightVibrant: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_MUTED, R.string.mutedLabel, R.string.mutedDescription)
        @ColorInt
        var muted: Int = Color.BLACK,

        @get:TaskerOutputVariable(VAR_VIBRANT, R.string.vibrantLabel, R.string.vibrantDescription)
        @ColorInt
        var vibrant: Int = Color.BLACK
    ) {
    companion object {
        const val VAR_DARK_MUTED = VAR_PREFIX + "darkmuted"
        const val VAR_DARK_VIBRANT = VAR_PREFIX + "darkvibrant"
        const val VAR_DOMINANT = VAR_PREFIX + "dominant"
        const val VAR_LIGHT_MUTED = VAR_PREFIX + "lightmuted"
        const val VAR_LIGHT_VIBRANT = VAR_PREFIX + "lightvibrant"
        const val VAR_MUTED = VAR_PREFIX + "muted"
        const val VAR_VIBRANT = VAR_PREFIX + "vibrant"
    }

    /**
     * Indicates if there is valid data.
     */
    fun isValid(): SimpleResult {
        return SimpleResult.get {
            validate()
        }
    }

    /**
     * Validates the data or throws an exception describing the error.
     */
    fun validate() {
        ColorTargetType.values().forEach {
            when (it) {
                ColorTargetType.DarkMuted -> requireRange(darkMuted, min = Color.BLACK, max = Color.WHITE) { "$VAR_DARK_MUTED is not a valid color" }
                ColorTargetType.DarkVibrant -> requireRange(darkVibrant, min = Color.BLACK, max = Color.WHITE) { "$VAR_DARK_VIBRANT is not a valid color" }
                ColorTargetType.Dominant -> requireRange(dominant, min = Color.BLACK, max = Color.WHITE) { "$VAR_DOMINANT is not a valid color" }
                ColorTargetType.LightMuted -> requireRange(lightMuted, min = Color.BLACK, max = Color.WHITE) { "$VAR_LIGHT_MUTED is not a valid color" }
                ColorTargetType.LightVibrant -> requireRange(lightVibrant, min = Color.BLACK, max = Color.WHITE) { "$VAR_LIGHT_MUTED is not a valid color" }
                ColorTargetType.Muted -> requireRange(muted, min = Color.BLACK, max = Color.WHITE) { "$VAR_MUTED is not a valid color" }
                ColorTargetType.Vibrant -> requireRange(vibrant, min = Color.BLACK, max = Color.WHITE) { "$VAR_VIBRANT is not a valid color" }
            }
        }
    }
}