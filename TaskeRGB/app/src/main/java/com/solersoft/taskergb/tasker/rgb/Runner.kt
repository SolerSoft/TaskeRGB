package com.solersoft.taskergb.tasker.rgb

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.R


// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    companion object {
        const val ERR_CONFIG = -1
        const val ERR_DEVICE_UNAVAILABLE = -2
        const val GATT_SERVICE = "0000FFE5-0000-1000-8000-00805F9B34FB"
        const val GATT_CHARACTERISTIC = "0000FFE9-0000-1000-8000-00805F9B34FB"
    }

    // Add our custom plugin icon
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    // Performs the real work of the action
    override fun run(context: Context, input: TaskerInput<RGBWInput>): TaskerPluginResult<RGBWOutput> {

        // Create shortcuts to inputs
        var device = input.regular.device
        var value = input.regular.value

        // Make sure we have valid inputs
        require(device.isValid().success && value.isValid().success) {
            context.getString(R.string.rgbwErrConfig)
        }

        // TODO: Actually do some work!
        return TaskerPluginResultSucess(RGBWOutput(input.regular.value))
    }
}