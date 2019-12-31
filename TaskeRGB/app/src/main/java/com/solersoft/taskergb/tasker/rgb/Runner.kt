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
import com.solersoft.taskergb.byIndex
import com.solersoft.taskergb.devices.DeviceInfo
import com.solersoft.taskergb.devices.DeviceManager
import com.solersoft.taskergb.devices.RGBWDeviceBLE
import kotlinx.coroutines.runBlocking


// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    private val TAG = this::class.simpleName
    private val ERR_CONFIG = -1
    private val ERR_DEVICE_UNAVAILABLE = -2


    // Add our custom plugin icon
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    /*
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
     */

    // Performs the real work of the action
    override fun run(context: Context, input: TaskerInput<RGBWInput>): TaskerPluginResult<RGBWOutput> {

        // Parse inputs
        var targetType = TargetType::class.byIndex(input.regular.targetType)
        var targetName = input.regular.targetName
        var value = input.regular.value

        // Validate input data
        requireNotNull(targetName) { "${RGBWInput.VAR_TARGET_NAME} must be supplied."}
        require(value.isValid().success) { "${RGBWValue.KEY} is not valid." }

        // Obtain list of devices
        var devices: ArrayList<DeviceInfo>
        if (targetType == TargetType.Device) {
            // Single device
            devices = ArrayList<DeviceInfo>();
            devices.add(DeviceManager.getDevice(targetName))
        } else {
            // Group of devices
            devices = DeviceManager.getGroup(targetName).devices
        }

        // This would normally be a coroutine, but Tasker doesn't support that
        // so we have to run it blocking
        runBlocking {

            // Repeat for each device
            devices.forEach {

                // Placeholder
                var device: RGBWDeviceBLE? = null

                // Following may fail
                try {
                    // Create the device
                    device = RGBWDeviceBLE(context, it.address)

                    // Connect to the device
                    device.connect()

                    // Write the value to the device
                    device.writeValue(value)
                } finally {
                    // Always make sure the device is closed
                    device?.close()
                }
            }
        }

        Log.d(TAG, "Plugin Done!!")

        // TODO: Actually do some work!
        return TaskerPluginResultSucess(RGBWOutput(input.regular.value))
    }
}