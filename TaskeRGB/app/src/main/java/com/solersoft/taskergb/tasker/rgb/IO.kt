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
import java.text.SimpleDateFormat
import java.util.*

/****************************************
 * Inputs
 ****************************************/

// Device Info
@TaskerInputObject("device", R.string.deviceInfo)
class RGBDeviceInfo @JvmOverloads constructor(
        @field:TaskerInputField(R.string.addressKey, R.string.address) var address: String? = null,
        @field:TaskerInputField("name", R.string.name) var name: String? = null
) {
    // Validates that the DeviceInfo contains valid data.
    public fun isValid() : SimpleResult {
        return SimpleResult.get {
            require(!address.isNullOrEmpty()) { "Device address must be defined" }
        }
    }
}

// Main Input
@TaskerInputRoot
class RGBInput @JvmOverloads constructor(
        @field:TaskerInputObject("device") var device: RGBDeviceInfo = RGBDeviceInfo(),
        @field:TaskerInputObject("value") var value: RGBWValue = RGBWValue()
) {
    // Validates that the DeviceInfo contains valid data.
    public fun isValid() : SimpleResult {
        // Check device first
        var r = device.isValid()
        if (!r.success) { return r; }

        // Check value
        r = value.isValid()
        if (!r.success) { return r; }

        // All good
        return SimpleResultSuccess()
    }
}


/****************************************
 * Outputs
 ****************************************/

@TaskerOutputObject()
class RGBOutput(
        @get:TaskerOutputVariable("value", R.string.rgbwValue, R.string.time_description) var time: Long = Date().time,
        //nested output. Notice how DateParts also has the @TaskerOutputObject annotation
        var dateParts: DateParts = DateParts(time)

){
    companion object {
        const val VAR_SECONDS = "seconds"
        const val VAR_FORMATTED_TIME = "formatted_time"
    }
}

@TaskerOutputObject()
class DateParts(time: Long) {
    private val date = Date(time)

    @get:TaskerOutputVariable("hours", R.string.hours, R.string.hours_description)
    val hours: String = SimpleDateFormat("HH").format(date)
    @get:TaskerOutputVariable("minutes", R.string.minutes, R.string.minutes_description)
    val minutes: String = SimpleDateFormat("mm").format(date)
    @get:TaskerOutputVariable(RGBOutput.VAR_SECONDS, R.string.seconds, R.string.seconds_description)
    val seconds: String = SimpleDateFormat("ss").format(date)
}


/****************************************
 * Input / Outputs
 ****************************************/

@TaskerInputObject("rgbwValue", R.string.rgbwValue)
@TaskerOutputObject()
class RGBWValue @JvmOverloads constructor(
        @field:TaskerInputField(VAR_RED, R.string.red_label)
        @get:TaskerOutputVariable(VAR_RED, R.string.red_label, R.string.red_html_label)
        var red: Int = 255,

        @field:TaskerInputField(VAR_GREEN, R.string.green_label)
        @get:TaskerOutputVariable(VAR_GREEN, R.string.green_label, R.string.green_html_label)
        var green: Int = 255,

        @field:TaskerInputField(VAR_BLUE, R.string.blue_label)
        @get:TaskerOutputVariable(VAR_BLUE, R.string.blue_label, R.string.blue_html_label)
        var blue: Int = 255,

        @field:TaskerInputField(VAR_WHITE, R.string.white_label)
        @get:TaskerOutputVariable(VAR_WHITE, R.string.white_label, R.string.white_html_label)
        var white: Int = 255
) {
    // Validates that the RGBWValue contains valid data.
    public fun isValid() : SimpleResult {
        return SimpleResult.get {
            requireRange(red, 255) {"Red must be between 0 and 255"}
            requireRange(green, 255) {"Green must be between 0 and 255"}
            requireRange(blue, 255) {"Blue must be between 0 and 255"}
            requireRange(white, 255) {"White must be between 0 and 255"}
        }
    }

    companion object {
        const val VAR_RED = "red"
        const val VAR_GREEN = "green"
        const val VAR_BLUE = "blue"
        const val VAR_WHITE = "white"
    }
}