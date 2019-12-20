package com.solersoft.taskergb.tasker.rgb

import android.os.Bundle
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputInfo
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputInfos
import com.solersoft.taskergb.R
import com.solersoft.taskergb.selectOne
import com.solersoft.taskergb.tasker.ActivityConfigTasker
import com.solersoft.taskergb.toToast
import kotlinx.android.synthetic.main.activity_config_gettime.*
import java.util.*

/**
 * Activity for controlling RGB and RGBW lighting.
 */


class RGBHelper(config: TaskerPluginConfig<RGBInput>) : TaskerPluginConfigHelper<RGBInput, RGBOutput, RGBRunner>(config) {
    override val runnerClass = RGBRunner::class.java
    override val inputClass = RGBInput::class.java
    override val outputClass = RGBOutput::class.java
    override fun isInputValid(input: TaskerInput<RGBInput>) = input.regular.isValid()
    override val defaultInput = RGBInput()
}

class ActivityConfigRGB : ActivityConfigTasker<RGBInput, RGBOutput, RGBRunner, RGBHelper>() {
    //Overrides
    override fun getNewHelper(config: TaskerPluginConfig<RGBInput>) = RGBHelper(config)

    override fun assignFromInput(input: TaskerInput<RGBInput>) = input.regular.run {
        editTextFormat.setText(format)
        times?.let { editTextTimes.setText(it.toString()) }
        editTextVariable.setText(variableName)
        checkBoxGetSeconds.isChecked = input.regular.getSeconds
    }

    override val inputForTasker get() = TaskerInput(RGBInput(editTextFormat.text?.toString(), editTextTimes.text?.toString()?.toIntOrNull(), editTextVariable.text?.toString(), checkBoxGetSeconds.isChecked))
    override val layoutResId = R.layout.activity_config_rgb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editTextTimes.setOnClickListener { showVariableDialog() }
    }

    private fun showVariableDialog() {
        val relevantVariables = taskerHelper.relevantVariables.toList()
        if (relevantVariables.isEmpty()) return "No variables to select.\n\nCreate some local variables in Tasker to show here.".toToast(this)

        selectOne("Select a Tasker variable", relevantVariables) { editTextTimes.setText(it) }

    }
}