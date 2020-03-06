package com.solersoft.taskergb.tasker.rgb

import android.content.Context
import android.util.Log
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.R
import com.solersoft.taskergb.byIndex
import com.solersoft.taskergb.devices.*
import com.solersoft.taskergb.toUUID
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


// Represents a Tasker action
class RGBWRunner : TaskerPluginRunnerAction<RGBWInput, RGBWOutput>() {

    private val TAG = this::class.simpleName
    private val ERR_CONFIG = -1
    private val ERR_DEVICE_UNAVAILABLE = -2


    /**
     * Add meta properties, like our custom plugin icon
     */
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

    /**
     * Creates the right runtime device for the specified {@link DeviceInfo}.
     * @param context The context which may be used to create the device.
     * @param deviceInfo Information about the device to instantiate.
     */
    private fun createDevice(context: Context, deviceInfo: DeviceInfo): RGBDevice {
        return when (deviceInfo.connectionType){
            ConnectionType.BLE -> BLEDevice(context, deviceInfo.address)
            else -> throw UnsupportedOperationException("Connection type ${deviceInfo.connectionType} is not yet supported.")
        };
    }


    /**
     * Performs the real work of the action
     * @param context The context for the action.
     * @param input The Tasker input data for the action.
     * @return The Tasker result for the action.
     */
    override fun run(context: Context, input: TaskerInput<RGBWInput>): TaskerPluginResult<RGBWOutput> {

        // Parse inputs
        var targetType = TargetType::class.byIndex(input.regular.targetType)
        var targetID = input.regular.targetID.toUUID();
        var value = input.regular.value

        // TODO: Remove this
        targetID = UUID(0, 1)

        // Validate input data
        requireNotNull(targetID) { "${RGBWInput.VAR_TARGET_ID} must be supplied."}
        require(value.isValid().success) { "${RGBWValue.KEY} is not valid." }

        // Obtain list of devices
        var devices: ArrayList<DeviceInfo>
        if (targetType == TargetType.Device) {
            // Single device
            devices = ArrayList<DeviceInfo>();
            devices.add(DeviceManager.getDevice(targetID))
        } else {
            // Group of devices
            devices = DeviceManager.getGroup(targetID).devices
        }

        // This would normally be a coroutine, but Tasker doesn't support that
        // so we have to run it blocking
        runBlocking {

            // Repeat for each device
            devices.forEach {

                // Placeholder
                var device: RGBDevice? = null

                // Following may fail
                try {
                    // Create the device
                    device = createDevice(context, it)

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
        }

        Log.d(TAG, "Plugin Done!!")

        // TODO: Actually do some work!
        return TaskerPluginResultSucess(RGBWOutput(input.regular.value))
    }
}