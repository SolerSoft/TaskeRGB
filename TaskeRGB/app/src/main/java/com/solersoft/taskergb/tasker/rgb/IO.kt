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
import com.solersoft.taskergb.requireRange
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
        @field:TaskerInputObject(VAR_TARGET_TYPE, R.string.targetLabel, R.string.targetDescription) var targetType: Int = TargetType.Device.ordinal,
        @field:TaskerInputObject(VAR_TARGET_NAME, R.string.targetNameLabel, R.string.targetNameDescription) var targetName: String? = null,
        @field:TaskerInputObject(RGBWValue.KEY, R.string.valueLabel, R.string.valueDescription) var value: RGBWValue = RGBWValue()
) {

    companion object {
        const val VAR_TARGET_TYPE = "targetType"
        const val VAR_TARGET_NAME = "targetName"
    }

    // Validates that the DeviceInfo contains valid data.
    public fun isValid() : SimpleResult {

        // We actually allow a misconfigured device because otherwise the
        // user will never be able to leave the configuration screen.
        /*
        var r = device.isValid()
        if (!r.success) { return r; } */

        // Check value
        var r = value.isValid()
        if (!r.success) { return r; }

        // All good
        return SimpleResultSuccess()
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
        const val KEY = "rgbwValue"
        const val VAR_RGB = "rgb"
        const val VAR_WHITE = "white"
    }

    /**
     * Validates that the RGBWValue contains valid data.
     */
    public fun isValid() : SimpleResult {
        return SimpleResult.get {
            requireRange(rgb, min= Color.BLACK, max= Color.WHITE) {"RGB must be a valid color"}
            requireRange(white, 255) {"White must be between 0 and 255"}
        }
    }

    /**
     * Converts the RGB value to string hex code.
     */
    public fun rgbToHex() : String {

        return String.format(Locale.getDefault(), "%02X%02X%02X", r, g, b);
    }

    /**
     * Converts entire RGBW value to string hex code.
     */
    public fun toHex() : String {

        return String.format(Locale.getDefault(), "%02X%02X%02X%02X", r, g, b, w);
    }

    /**
     * Converts the white value to string hex code.
     */
    public fun wToHex() : String {

        return String.format(Locale.getDefault(), "%02X", w);
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