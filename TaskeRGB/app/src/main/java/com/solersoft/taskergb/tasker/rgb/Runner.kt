package com.solersoft.taskergb.tasker.rgb

import android.app.PendingIntent
import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.output.TaskerOuputBase
import com.joaomgcd.taskerpluginlibrary.runner.*
import com.solersoft.taskergb.R
import com.solersoft.taskergb.tasker.gottime.ActivityConfigGotTime
import com.solersoft.taskergb.tasker.gottime.GotTimeUpdate
import java.util.*

// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    companion object {
        const val ERR_CONFIG = -1
        const val ERR_DEVICE_UNAVAILABLE = -2
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