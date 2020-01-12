package com.solersoft.taskergb.tasker.palette

import android.graphics.Color
import androidx.annotation.ColorInt
import com.joaomgcd.taskerpluginlibrary.SimpleResult
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.*

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

@TaskerOutputObject()
class PaletteEntry(
        @get:TaskerOutputVariable(VAR_NAME, R.string.entryNameLabel, R.string.entryNameDescription) val name: String,
        @get:TaskerOutputVariable(VAR_COLOR, R.string.entryColorLabel, R.string.entryColorDescription) @ColorInt val color: Int = Color.BLACK
){
    companion object {
        const val VAR_NAME = "name"
        const val VAR_COLOR = "color"
    }
}

/**
 * Output for the Palette action.
 */
@TaskerOutputObject()
class PaletteOutput @JvmOverloads constructor(
        @get:TaskerOutputVariable(VAR_DARK_MUTED, R.string.darkMutedLabel, R.string.darkMutedDescription)
        var darkMuted: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_DARK_VIBRANT, R.string.darkVibrantLabel, R.string.darkVibrantDescription)
        var darkVibrant: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_DOMINANT, R.string.dominantLabel, R.string.dominantDescription)
        var dominant: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_LIGHT_MUTED, R.string.lightMutedLabel, R.string.lightMutedDescription)
        var lightMuted: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_LIGHT_VIBRANT, R.string.lightVibrantLabel, R.string.lightVibrantDescription)
        var lightVibrant: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_MUTED, R.string.mutedLabel, R.string.mutedDescription)
        var muted: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_VIBRANT, R.string.vibrantLabel, R.string.vibrantDescription)
        var vibrant: String = DEFAULT_COLOR,

        @get:TaskerOutputVariable(VAR_ALL_COLORS, R.string.allColorsLabel, R.string.allColorsDescription)
        var allColors: Array<String> = arrayOf<String>()
    ) {
    companion object {
        const val VAR_DARK_MUTED = VAR_PREFIX + "darkmuted"
        const val VAR_DARK_VIBRANT = VAR_PREFIX + "darkvibrant"
        const val VAR_DOMINANT = VAR_PREFIX + "dominant"
        const val VAR_LIGHT_MUTED = VAR_PREFIX + "lightmuted"
        const val VAR_LIGHT_VIBRANT = VAR_PREFIX + "lightvibrant"
        const val VAR_MUTED = VAR_PREFIX + "muted"
        const val VAR_VIBRANT = VAR_PREFIX + "vibrant"
        const val VAR_ALL_COLORS = VAR_PREFIX + "allcolors"
        const val DEFAULT_COLOR = "#FF000000"
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
                ColorTargetType.DarkMuted -> requireTaskerColor(darkMuted) { "$VAR_DARK_MUTED is not a valid color" }
                ColorTargetType.DarkVibrant -> requireTaskerColor(darkVibrant) { "$VAR_DARK_VIBRANT is not a valid color" }
                ColorTargetType.Dominant -> requireTaskerColor(dominant) { "$VAR_DOMINANT is not a valid color" }
                ColorTargetType.LightMuted -> requireTaskerColor(lightMuted) { "$VAR_LIGHT_MUTED is not a valid color" }
                ColorTargetType.LightVibrant -> requireTaskerColor(lightVibrant) { "$VAR_LIGHT_MUTED is not a valid color" }
                ColorTargetType.Muted -> requireTaskerColor(muted) { "$VAR_MUTED is not a valid color" }
                ColorTargetType.Vibrant -> requireTaskerColor(vibrant) { "$VAR_VIBRANT is not a valid color" }
            }
        }
        allColors.forEach { requireTaskerColor(it) { "$VAR_ALL_COLORS contains an invalid entry"} }
    }
}