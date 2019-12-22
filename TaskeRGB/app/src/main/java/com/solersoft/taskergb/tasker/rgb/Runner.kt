package com.solersoft.taskergb.tasker.rgb

import android.content.Context
import android.util.Log
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.R
import com.solersoft.taskergb.ble.deviceFor
import com.solersoft.taskergb.ble.useBasic
import kotlinx.coroutines.runBlocking


// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    companion object {
        val TAG = this::class.simpleName
        const val ERR_CONFIG = -1
        const val ERR_DEVICE_UNAVAILABLE = -2
        const val GATT_SERVICE = "0000FFE5-0000-1000-8000-00805F9B34FB"
        const val GATT_CHARACTERISTIC = "0000FFE9-0000-1000-8000-00805F9B34FB"
    }

    // Add our custom plugin icon
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    private val defaultDeviceMacAddress = "7C:01:0A:E8:9B:D7"

    suspend fun logNameAndAppearance(context: Context, deviceMacAddress: String = defaultDeviceMacAddress) {
        deviceFor(context, deviceMacAddress).useBasic { device, services ->
            services.forEach { Log.d(TAG,"Service found with UUID: ${it.uuid}") }
            with(GenericAccess) {
                device.readAppearance()
                Log.d(TAG,"Device appearance: ${device.appearance}")
                device.readDeviceName()
                Log.d(TAG,"Device name: ${device.deviceName}")
            }
        }
    }

    // Performs the real work of the action
    override fun run(context: Context, input: TaskerInput<RGBWInput>): TaskerPluginResult<RGBWOutput> {

        // Create shortcuts to inputs
        var device = input.regular.device
        var value = input.regular.value

        // Make sure we have valid inputs
        /*
        require(device.isValid().success && value.isValid().success) {
            context.getString(R.string.rgbwErrConfig)
        }*/

        // This would normally be a coroutine, but Tasker doesn't support that
        // so we have to run blocking
        runBlocking {
            logNameAndAppearance(context)
        }

        Log.d(TAG, "Plugin Done!!")

        // TODO: Actually do some work!
        return TaskerPluginResultSucess(RGBWOutput(input.regular.value))
    }
}