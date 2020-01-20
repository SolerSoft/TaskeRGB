package com.solersoft.taskergb.tasker.palette

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.R
import com.solersoft.taskergb.databinding.ActivityConfigPaletteBinding
import com.solersoft.taskergb.selectOne
import com.solersoft.taskergb.tasker.ActivityConfigTasker
import com.solersoft.taskergb.toToast
import kotlinx.android.synthetic.main.activity_config_palette.*
import kotlinx.android.synthetic.main.activity_config_rgb.colorPickButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


/**
 * Represents a Tasker action
 */
class PaletteRunner : TaskerPluginRunnerAction<PaletteInput, PaletteOutput>() {

    // Add our custom plugin icon
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    /**
     * Performs the real work of the action
     */
    override fun run(context: Context, input: TaskerInput<PaletteInput>): TaskerPluginResult<PaletteOutput> {

        // PaletteAction is a suspend function but Tasker doesn't support that so we'll have to run blocking
        val result = runBlocking {

            // Run the action and get a result
            PaletteAction.run(context, input.regular)
        }

        // Return success with converted value
        return TaskerPluginResultSucess(result.toTasker())
    }
}

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

    /*
    override fun addToStringBlurb(input: TaskerInput<PaletteInput>, blurbBuilder: StringBuilder) {
        super.addToStringBlurb(input, blurbBuilder)
        blurbBuilder.append("\n${context.getString(R.string.defaultColorLabel)}: ${Integer.toHexString(input.regular.defaultColor)}")
    }*/
}

// Activity class to handle UI for Tasker action configuration
class ActivityConfigPalette : ActivityConfigTasker<PaletteInput, PaletteOutput, PaletteRunner, PaletteHelper>() {

    // region Override Base Class Values
    override val layoutResId = R.layout.activity_config_palette // This is our layout resource ID
    override val shouldSetContentView = false // Do not load the content view since we'll be using binding
    // endregion

    // region Member Variables
    lateinit var binding : ActivityConfigPaletteBinding
    lateinit var vm : PaletteViewModel

    // endregion

    // Handle activity creation
    override fun onCreate(savedInstanceState: Bundle?) {

        // Create binding
        binding = DataBindingUtil.setContentView(this, layoutResId)

        // Create the ViewModel
        vm = PaletteViewModel(this)

        // Set error handler
        vm.setErrorHandler(::onError)

        // Bind to the ViewModel
        binding.vm = vm

        // Subscribe to control events
        colorPickButton.setOnClickListener { showVariableDialog() }

        // Pass on to super for the rest of creation
        super.onCreate(savedInstanceState)
    }

    // Create custom helper
    override fun getNewHelper(config: TaskerPluginConfig<PaletteInput>) = PaletteHelper(config)

    // Fill controls from Input data
    override fun assignFromInput(input: TaskerInput<PaletteInput>) = input.regular.run {
        // Fill VM with incoming data
        vm.input = input.regular
    }

    // Convert controls to Input data
    override val inputForTasker : TaskerInput<PaletteInput> get() {
        // Set output to VM data
        return TaskerInput(vm.input)
    }

    private fun onError(e: Exception) {
        with(AlertDialog.Builder(this)) {
            setTitle("Error")
            setMessage(e.message)
            create().show()
        }
    }

    private fun showVariableDialog() {
        // TODO: How do we handle this in the ViewModel?
        val relevantVariables = taskerHelper.relevantVariables.toList()
        if (relevantVariables.isEmpty()) return "No variables to select.\n\nCreate some local variables in Tasker to show here.".toToast(this)
        selectOne("Select a Tasker variable", relevantVariables) { filePathEdit.setText(it) }
    }
}