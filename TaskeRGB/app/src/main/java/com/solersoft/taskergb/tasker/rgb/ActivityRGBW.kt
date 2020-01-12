package com.solersoft.taskergb.tasker.rgb

import android.graphics.Color
import android.os.Bundle
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.solersoft.taskergb.R
import com.solersoft.taskergb.tasker.ActivityConfigTasker
import kotlinx.android.synthetic.main.activity_config_rgb.*


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

    override fun addToStringBlurb(input: TaskerInput<RGBWInput>, blurbBuilder: StringBuilder) {
        blurbBuilder.append("${context.getString(R.string.redLabel)}: ${input.regular.value.r}\n")
        blurbBuilder.append("${context.getString(R.string.greenLabel)}: ${input.regular.value.g}\n")
        blurbBuilder.append("${context.getString(R.string.blueLabel)}: ${input.regular.value.b}\n")
        blurbBuilder.append("${context.getString(R.string.whiteLabel)}: ${input.regular.value.w}")
        super.addToStringBlurb(input, blurbBuilder)
    }
}

// Activity class to handle UI for Tasker action configuration
class ActivityConfigRGBW : ActivityConfigTasker<RGBWInput, RGBWOutput, RGBWRunner, RGBWHelper>() {

    private val prefName = RGBWRunner::class.simpleName;
    private val input = RGBWInput()

    /**
     * Presents a color picker that allows the user to select a color.
     * This method uses @see https://github.com/AzeeSoft/AndroidPhotoshopColorPicker
     */
    private fun pickRGB() {

        // Create the dialog
        val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this, ColorPickerDialog.DARK_THEME)

        // Configure the dialog
        colorPickerDialog.hideOpacityBar();

        // TODO: Do we need to set a starting color?
        colorPickerDialog.setInitialColor(Color.RED)

        // Subscribe to events
        colorPickerDialog.setOnColorPickedListener { color, hexVal -> onRGBPicked(color, hexVal) }

        // Show the dialog
        colorPickerDialog.show()
    }

    /**
     * Called when the user picks a RGB color.
     * @param color Color picked by the user
     * @param hexVal Color picked by the user in hexadecimal form
     */
    open fun onRGBPicked(color: Int, hexVal: String) {

        // Store value
        input.value.rgb = color

        // Convert to hex
        editColor.setText(input.value.toHex())
    }

    // Handle activity creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Subscribe to control events
        // editTextTimes.setOnClickListener { showVariableDialog() }
        colorPickButton.setOnClickListener { pickRGB() }
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

        // Use class-level data
        return TaskerInput(input)
    }

    /*
    private fun showVariableDialog() {
        val relevantVariables = taskerHelper.relevantVariables.toList()
        if (relevantVariables.isEmpty()) return "No variables to select.\n\nCreate some local variables in Tasker to show here.".toToast(this)

        selectOne("Select a Tasker variable", relevantVariables) { editTextTimes.setText(it) }

    }*/
}