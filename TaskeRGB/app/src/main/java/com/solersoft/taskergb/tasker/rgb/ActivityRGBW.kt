package com.solersoft.taskergb.tasker.rgb

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager
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
}

// Activity class to handle UI for Tasker action configuration
class ActivityConfigRGBW : ActivityConfigTasker<RGBWInput, RGBWOutput, RGBWRunner, RGBWHelper>() {

    private val prefName = RGBWRunner::class.simpleName;

    /**
     * Presents a color picker that allows the user to select a color.
     * This method uses @see https://github.com/skydoves/ColorPickerView
     */
    private fun pickColor(){

        // TODO: Do we need to set a starting color?
        val manager = ColorPickerPreferenceManager.getInstance(this)
        manager.setColor(prefName, Color.RED); // manipulates the saved color data.

        // Present the dialog
        ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle(R.string.color_picker_title)
                .setPreferenceName(prefName)
                .setPositiveButton(getString(R.string.ok),
                        ColorEnvelopeListener { envelope, fromUser -> onColorSelected(envelope, fromUser) })
                .setNegativeButton(getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss()})
                .attachAlphaSlideBar(false) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true) // default is true. If false, do not show the BrightnessSlideBar.
                .show()
    }

    open fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {

    }

    // Handle activity creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Subscribe to control events
        // editTextTimes.setOnClickListener { showVariableDialog() }
        colorPickButton.setOnClickListener { pickColor() }
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