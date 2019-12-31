package com.solersoft.taskergb.tasker.rgb

import com.joaomgcd.taskerpluginlibrary.SimpleResult
import com.joaomgcd.taskerpluginlibrary.SimpleResultSuccess
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputObject
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.R
import com.solersoft.taskergb.requireRange

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
        @field:TaskerInputField(VAR_RED, R.string.redLabel, R.string.redDescription)
        @get:TaskerOutputVariable(VAR_RED, R.string.redLabel, R.string.redDescription)
        var red: Int = 255,

        @field:TaskerInputField(VAR_GREEN, R.string.greenLabel, R.string.greenDescription)
        @get:TaskerOutputVariable(VAR_GREEN, R.string.greenLabel, R.string.greenDescription)
        var green: Int = 255,

        @field:TaskerInputField(VAR_BLUE, R.string.blueLabel, R.string.blueDescription)
        @get:TaskerOutputVariable(VAR_BLUE, R.string.blueLabel, R.string.blueDescription)
        var blue: Int = 255,

        @field:TaskerInputField(VAR_WHITE, R.string.whiteLabel, R.string.whiteDescription)
        @get:TaskerOutputVariable(VAR_WHITE, R.string.whiteLabel, R.string.whiteDescription)
        var white: Int = 255
) {
    companion object {
        const val KEY = "rgbwValue"
        const val VAR_RED = "red"
        const val VAR_GREEN = "green"
        const val VAR_BLUE = "blue"
        const val VAR_WHITE = "white"
    }

    // Validates that the RGBWValue contains valid data.
    public fun isValid() : SimpleResult {
        return SimpleResult.get {
            requireRange(red, 255) {"Red must be between 0 and 255"}
            requireRange(green, 255) {"Green must be between 0 and 255"}
            requireRange(blue, 255) {"Blue must be between 0 and 255"}
            requireRange(white, 255) {"White must be between 0 and 255"}
        }
    }
}