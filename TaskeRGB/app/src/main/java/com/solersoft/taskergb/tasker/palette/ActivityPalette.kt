package com.solersoft.taskergb.tasker.palette

import android.graphics.Color
import android.os.Bundle
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.solersoft.taskergb.R
import com.solersoft.taskergb.selectOne
import com.solersoft.taskergb.tasker.ActivityConfigTasker
import com.solersoft.taskergb.toToast
import kotlinx.android.synthetic.main.activity_config_gettime.*
import kotlinx.android.synthetic.main.activity_config_palette.*
import kotlinx.android.synthetic.main.activity_config_rgb.*
import kotlinx.android.synthetic.main.activity_config_rgb.colorPickButton


/**
 * Activity for generating color palettes.
 */

// Helper class to wire everything up, AKA config class
class PaletteHelper(config: TaskerPluginConfig<PaletteInput>) : TaskerPluginConfigHelper<PaletteInput, PaletteOutput, PaletteRunner>(config) {
    override val runnerClass = PaletteRunner::class.java
    override val inputClass = PaletteInput::class.java
    override val outputClass = PaletteOutput::class.java
    override fun isInputValid(input: TaskerInput<PaletteInput>) = input.regular.isValid()
    override val defaultInput = PaletteInput()
}

// Activity class to handle UI for Tasker action configuration
class ActivityConfigPalette : ActivityConfigTasker<PaletteInput, PaletteOutput, PaletteRunner, PaletteHelper>() {

    // Handle activity creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Subscribe to control events
        colorPickButton.setOnClickListener { showVariableDialog() }
    }

    // Specify UI resource ID
    override val layoutResId = R.layout.activity_config_palette

    // Create custom helper
    override fun getNewHelper(config: TaskerPluginConfig<PaletteInput>) = PaletteHelper(config)

    // Fill controls from Input data
    override fun assignFromInput(input: TaskerInput<PaletteInput>) = input.regular.run {

        // Fill controls with data
        editFilePath.setText(input.regular.imagePath?.trim())
    }

    // Convert controls to Input data
    override val inputForTasker : TaskerInput<PaletteInput> get() {

        // Create input value
        val input = PaletteInput()

        // Convert controls to input data
        input.imagePath = editFilePath.text?.toString()?.trim()

        // Use class-level data
        return TaskerInput(input)
    }

    private fun showVariableDialog() {
        val relevantVariables = taskerHelper.relevantVariables.toList()
        if (relevantVariables.isEmpty()) return "No variables to select.\n\nCreate some local variables in Tasker to show here.".toToast(this)
        selectOne("Select a Tasker variable", relevantVariables) { editFilePath.setText(it) }
    }
}