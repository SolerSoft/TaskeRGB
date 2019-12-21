package com.solersoft.taskergb.tasker.rgb

import android.app.PendingIntent
import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.output.TaskerOuputBase
import com.joaomgcd.taskerpluginlibrary.runner.TaskerOutputRename
import com.joaomgcd.taskerpluginlibrary.runner.TaskerOutputRenames
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.R
import com.solersoft.taskergb.tasker.gottime.ActivityConfigGotTime
import com.solersoft.taskergb.tasker.gottime.GotTimeUpdate
import java.util.*

// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    // Add our custom plugin icon
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    // Performs the real work of the action
    override fun run(context: Context, input: TaskerInput<RGBWInput>): TaskerPluginResult<RGBWOutput> {

        // TODO: Actually do some work!
        return TaskerPluginResultSucess(RGBWOutput(input.regular.value))
    }
}