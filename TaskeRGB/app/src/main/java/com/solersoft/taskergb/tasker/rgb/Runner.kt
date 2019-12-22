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
import com.solersoft.taskergb.devices.RGBWDeviceBLE
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*


// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    private val TAG = this::class.simpleName
    private val ERR_CONFIG = -1
    private val ERR_DEVICE_UNAVAILABLE = -2


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
        var deviceInfo = input.regular.device
        var value = input.regular.value

        // Temporarily override values
        deviceInfo = RGBWDeviceInfo(defaultDeviceMacAddress, "My Bulb")
        // value = RGBWValue(0, 0, 255, 255)

        // Make sure we have valid inputs
        require(deviceInfo.isValid().success && value.isValid().success) {
            context.getString(R.string.rgbwErrConfig)
        }

        // This would normally be a coroutine, but Tasker doesn't support that
        // so we have to run it blocking
        runBlocking {
            // Placeholder
            var device: RGBWDeviceBLE? = null

            // Following may fail
            try {
                // Create the device
                device = RGBWDeviceBLE(context, deviceInfo.address!!)

                // Connect to the device
                device.connect()

                // Write the value to the device
                device.writeValue(value)
            }
            finally {
                // Always make sure the device is closed
                device?.close()
            }
        }

        Log.d(TAG, "Plugin Done!!")

        // TODO: Actually do some work!
        return TaskerPluginResultSucess(RGBWOutput(input.regular.value))
    }
}