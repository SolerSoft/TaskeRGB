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
import kotlinx.android.synthetic.main.activity_config_rgb.*
import java.util.*

/**
 * Activity for controlling RGB and RGBW lighting.
 */

// Helper class to wire everything up, AKA config class
class RGBWHelper(config: TaskerPluginConfig<RGBWInput>) : TaskerPluginConfigHelper<RGBWInput, RGBWOutput, RGBWRunner>(config) {
    override val runnerClass = RGBWRunner::class.java
    override val inputClass = RGBWInput::class.java
    override val outputClass = RGBWOutput::class.java
    override fun isInputValid(input: TaskerInput<RGBWInput>) = input.regular.isValid()
    override val defaultInput = RGBWInput()
}

// Activity class to handle UI for Tasker action configuration
class ActivityConfigRGBW : ActivityConfigTasker<RGBWInput, RGBWOutput, RGBWRunner, RGBWHelper>() {

    // Handle activity creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Subscribe to control events
        // editTextTimes.setOnClickListener { showVariableDialog() }
    }

    // Specify UI resource ID
    override val layoutResId = R.layout.activity_config_rgb

    // Create custom helper
    override fun getNewHelper(config: TaskerPluginConfig<RGBWInput>) = RGBWHelper(config)

    // Fill controls from Input data
    override fun assignFromInput(input: TaskerInput<RGBWInput>) = input.regular.run {

        // TODO: Fill controls with data
        /*
        editTextFormat.setText(format)
        times?.let { editTextTimes.setText(it.toString()) }
        editTextVariable.setText(variableName)
        checkBoxGetSeconds.isChecked = input.regular.getSeconds
         */
    }

    // Convert controls to Input data
    override val inputForTasker : TaskerInput<RGBWInput> get() {

        // TODO: Convert controls to input data
        // TaskerInput(RGBWInput(editTextFormat.text?.toString(), editTextTimes.text?.toString()?.toIntOrNull(), editTextVariable.text?.toString(), checkBoxGetSeconds.isChecked))

        // HACK: Just do new data for now
        return TaskerInput(RGBWInput())
    }

    /*
    private fun showVariableDialog() {
        val relevantVariables = taskerHelper.relevantVariables.toList()
        if (relevantVariables.isEmpty()) return "No variables to select.\n\nCreate some local variables in Tasker to show here.".toToast(this)

        selectOne("Select a Tasker variable", relevantVariables) { editTextTimes.setText(it) }

    }*/
}