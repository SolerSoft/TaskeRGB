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

class RGBRunner : TaskerPluginRunnerAction<RGBInput, RGBOutput>() {
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    //User has the option to name the output variable for the time, so you add that rename here
    override fun addOutputVariableRenames(context: Context, input: TaskerInput<RGBInput>, renames: TaskerOutputRenames) {
        super.addOutputVariableRenames(context, input, renames)
        renames.add(TaskerOutputRename(RGBOutput.VAR_FORMATTED_TIME, input.regular.variableName))
    }

    override fun shouldAddOutput(context: Context, input: TaskerInput<RGBInput>?, ouput: TaskerOuputBase): Boolean {
        if (input == null) return true
        if (input.regular.getSeconds) return true;
        return ouput.nameNoSuffix != RGBOutput.VAR_SECONDS
    }

    override fun run(context: Context, input: TaskerInput<RGBInput>): TaskerPluginResult<RGBOutput> {
        //Notice how you get the dynamic input by key here
        val dateMs = input.dynamic.getByKey(KEY_RGBW)?.valueAs<Long?>()
                ?: throw RuntimeException("Time was not configured!!")
        val formatted = input.regular.format(dateMs)
        var result = formatted
        input.regular.times?.let { times ->
            for (count in 1 until times) {
                result += " $formatted"
            }
        }
        ActivityConfigGotTime::class.java.requestQuery(context, GotTimeUpdate(Date().time))
        return TaskerPluginResultSucess(RGBOutput(result, "Not for below Oreo 8.1!!"))
    }

}