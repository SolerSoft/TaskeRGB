package com.solersoft.taskergb.tasker.rgb

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
import com.solersoft.taskergb.requireRange
import com.solersoft.taskergb.tasker.palette.ColorTargetType
import com.solersoft.taskergb.tasker.palette.PaletteInput
import com.solersoft.taskergb.withPrefix
import java.util.*

/****************************************
 * Enums
 ****************************************/
enum class TargetType {
    Device, Group
}

/****************************************
 * Inputs
 ****************************************/

// Main Input
@TaskerInputRoot
class RGBWInput @JvmOverloads constructor(
        @field:TaskerInputField(VAR_TARGET_TYPE, R.string.targetLabel, R.string.targetDescription) var targetType: Int = TargetType.Device.ordinal,
        @field:TaskerInputField(VAR_TARGET_NAME, R.string.targetNameLabel, R.string.targetNameDescription) var targetName: String? = null,
        @field:TaskerInputObject(RGBWValue.KEY, R.string.valueLabel, R.string.valueDescription) var value: RGBWValue = RGBWValue()
) {

    companion object {
        const val VAR_TARGET_TYPE = VAR_PREFIX + "targettype"
        const val VAR_TARGET_NAME = VAR_PREFIX + "targetname"
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
        // We actually allow a misconfigured device because otherwise the
        // user will never be able to leave the configuration screen.
        /*
        var r = device.isValid()
        if (!r.success) { return r; } */

        // Check value
        value.validate()
    }
}


/****************************************
 * Outputs
 ****************************************/

@TaskerOutputObject()
class RGBWOutput(val value: RGBWValue)



/****************************************
 * Input / Outputs
 ****************************************/

@TaskerInputObject(RGBWValue.KEY, R.string.valueLabel, R.string.valueDescription)
@TaskerOutputObject()
class RGBWValue @JvmOverloads constructor(
        @field:TaskerInputField(VAR_RGB, R.string.rgbLabel, R.string.rgbDescription, ignoreInStringBlurb = true)
        @get:TaskerOutputVariable(VAR_RGB, R.string.rgbLabel, R.string.rgbDescription)
        @ColorInt
        var rgb: Int = Color.BLACK,

        @field:TaskerInputField(VAR_WHITE, R.string.whiteLabel, R.string.whiteDescription, ignoreInStringBlurb = true)
        @get:TaskerOutputVariable(VAR_WHITE, R.string.whiteLabel, R.string.whiteDescription)
        var white: Int = 0
) {
    companion object {
        const val KEY = VAR_PREFIX + "rgbw"
        const val VAR_RGB = VAR_PREFIX + "rgb"
        const val VAR_WHITE = VAR_PREFIX + "white"
    }

    /**
     * Indicates if there is valid data.
     */
    fun isValid() : SimpleResult {
        return SimpleResult.get {
            validate()
        }
    }

    /**
     * Converts the RGB value to string hex code.
     */
    fun rgbToHex() : String {

        return String.format(Locale.getDefault(), "%02X%02X%02X", r, g, b);
    }

    /**
     * Converts entire RGBW value to string hex code.
     */
    fun toHex() : String {

        return String.format(Locale.getDefault(), "%02X%02X%02X%02X", r, g, b, w);
    }

    /**
     * Converts the white value to string hex code.
     */
    fun wToHex() : String {

        return String.format(Locale.getDefault(), "%02X", w);
    }

    /**
     * Validates the data or throws an exception describing the error.
     */
    fun validate() {
        requireRange(rgb, min= Color.BLACK, max= Color.WHITE) {"RGB is not a valid color"}
        requireRange(white, 255) {"White is not between 0 and 255"}
    }

    /**
     * Gets the Red component of the RGBW value.
     */
    val r: Byte get() = ((rgb shr 16) and 0xff).toByte()

    /**
     * Gets the Green component of the RGBW value.
     */
    val g: Byte get() = ((rgb shr 8) and 0xff).toByte()

    /**
     * Gets the Blue component of the RGBW value.
     */
    val b: Byte get() = ((rgb) and 0xff).toByte()

    /**
     * Gets the White component of the RGBW value.
     */
    val w: Byte get() = white.toByte()
}