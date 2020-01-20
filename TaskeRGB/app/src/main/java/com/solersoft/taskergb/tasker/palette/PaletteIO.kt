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
 * Inputs
 ****************************************/

/**
 * Input for the Palette action.
 */
@TaskerInputRoot
class PaletteInput @JvmOverloads constructor(
        @field:TaskerInputField(VAR_IMAGE_PATH, R.string.imagePathLabel, R.string.imagePathDescription) var imagePath: String? = null,
        @field:TaskerInputField(VAR_COLOR_COUNT, R.string.defaultColorLabel, R.string.defaultColorDescription, ignoreInStringBlurb = true) var colorCount: Int = 256,
        @field:TaskerInputField(VAR_DEFAULT_COLOR, R.string.defaultColorLabel, R.string.defaultColorDescription) var defaultColor: String = Color.BLACK.toTaskerColor()
) {

    companion object {
        const val VAR_IMAGE_PATH = VAR_PREFIX + "imagepath"
        const val VAR_COLOR_COUNT = VAR_PREFIX + "colorcount"
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
        requireRange(colorCount, min = 1, max = 255) { "$VAR_COLOR_COUNT must be between 1 and 255" }
        requireTaskerColor(defaultColor) { "$VAR_DEFAULT_COLOR is not a valid color" }
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
 * Tasker output for the Palette action.
 */
@TaskerOutputObject()
class PaletteOutput @JvmOverloads constructor(
        // Named Values
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

        // Collections of Values
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

/**
 * Converts a {@link PaletteResult} to a {@link PaletteOutput}
 */
inline fun PaletteResult.toTasker() : PaletteOutput {
    return PaletteOutput(
            darkMuted =  this.darkMuted.toTaskerColor(),
            darkVibrant = this.darkVibrant.toTaskerColor(),
            dominant = this.dominant.toTaskerColor(),
            lightMuted = this.lightMuted.toTaskerColor(),
            lightVibrant = this.lightVibrant.toTaskerColor(),
            muted = this.muted.toTaskerColor(),
            vibrant = this.vibrant.toTaskerColor(),
            allColors = this.allColors.map { c -> c.toTaskerColor() }.toTypedArray()
    )
}