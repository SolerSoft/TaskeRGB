package com.solersoft.taskergb.devices

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import com.solersoft.taskergb.ble.deviceFor
import com.solersoft.taskergb.tasker.rgb.RGBWValue
import kotlinx.coroutines.withTimeout
import java.lang.Exception
import java.util.*

/**
 * Abstract base class for a RGB device.
 */
open abstract class RGBDevice(
        /**
         * The address of the device, if {@link connectionState} is Connected.
         */
        val address: String
) {

    // region Constants

    private val TAG = this::class.simpleName

    // endregion


    // region Internal Methods

    /**
     * Raises an exception if the connection state isn't Connected.
     */
    protected fun ensureConnected() {
        if (connectionState != ConnectionState.Connected) { throw IllegalStateException("Not connected to device.") }
    }

    // endregion


    // region Public Methods

    /**
     * Attempts to connect to the device at {@link #address}.
     *
     * @param timeoutMS Duration in milliseconds before the connection will timeout. Default is 5 seconds.
     */
    abstract suspend fun connect(timeoutMS: Long = 5000L);

    /**
     * Writes the specified color value to the device.
     *
     * @param value The color value to write to the device.
     */
    abstract suspend fun writeValue(value: RGBWValue);

    /**
     * Closes any open connection to the underlying device.
     */
    abstract fun close();

    // endregion


    // region Public Properties

    /**
     * Gets a value that indicates the state of the underlying connection to the device.
     */
    var connectionState: ConnectionState = ConnectionState.Disconnected
        protected set

    // endregion
}

/**
 * A RGB device that is controlled over BLE.
 */
class BLEDevice(val context: Context, address: String) : RGBDevice(address) {

    // region Constants

    private val TAG = this::class.simpleName

    companion object {
        val RGBW_SERVICE_UUID = UUID.fromString("0000FFE5-0000-1000-8000-00805F9B34FB")
        val RGBW_CHARACTERISTIC = UUID.fromString("0000FFE9-0000-1000-8000-00805F9B34FB")
    }

    // endregion


    // region Internal Methods

    /**
     * Creates the characteristic byte payload for setting the specified value.
     */
    private fun createPayload(value: RGBWValue) : ByteArray {

        // Create the payload
        val payload = ByteArray(7)

        // Header and footer
        payload[0] = 0x56.toByte()
        payload[6] = 0xAA.toByte()

        // TODO: How do we combine RGB and brightness

        // RGB Mode
        payload[1] = value.r
        payload[2] = value.g
        payload[3] = value.b
        payload[4] = 0x00.toByte() // Brightness not used
        payload[5] = 0xF0.toByte() // RGB mode specifier

        // Brightness Mode
        /*
        payload[4] = value.w
        payload[5] = 0x0F.toByte() // Brightness mode specifier
        */

        // Done
        return payload
    }

    // endregion


    // region Public Methods

    /**
     * Attempts to connect to the device at {@link #address}.
     *
     * @param timeoutMS Duration in milliseconds before the connection will timeout. Default is 5 seconds.
     */
    override suspend fun connect(timeoutMS: Long) {

        // If already connected or connecting, ignore duplicate request
        if (connectionState != ConnectionState.Disconnected) { return }

        // Connecting
        connectionState = ConnectionState.Connected

        // Any of the following may fail
        try {
            // Get the device
            val localDevice = deviceFor(context, address)
            device = localDevice

            // Create the device connection
            val localConnection = GattConnection(localDevice)
            connection = localConnection

            // Attempt to connect with timeout
            Log.d(TAG,"Attempting to connect to $address ...")
            withTimeout(timeoutMS) {
                // Attempt to connect
                localConnection.connect()

                // Discover services
                Log.d(TAG,"Connection established. Discovering...")
                localConnection.discoverServices()

                // Connected
                Log.d(TAG, "Connected!")
                connectionState = ConnectionState.Connected
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "Unable to complete connection", e)
            device = null
            connection = null
            connectionState = ConnectionState.Disconnected
            throw e
        }
    }

    /**
     * Writes the specified color value to the device.
     *
     * @param value The color value to write to the device.
     */
    override suspend fun writeValue(value: RGBWValue) {

        // First, make sure we're connected
        ensureConnected()

        // Attempt to get the RGBW service
        val service = connection!!.getService(RGBW_SERVICE_UUID)
                ?: throw NoSuchElementException("RGBW Service not found")

        // Attempt to get the RGBW characteristic
        var characteristic = service.getCharacteristic(RGBW_CHARACTERISTIC)

        // Set the characteristic value to the payload for the specified color values
        characteristic.value = createPayload(value)

        // Write the  characteristic
        characteristic = connection!!.writeCharacteristic(characteristic)
    }

    /**
     * Closes any open connection to the underlying device.
     */
    override fun close() {

        // If not connected, ignore duplicate request
        if (connectionState == ConnectionState.Disconnected) { return }

        // If still connecting, we can't disconnect
        if (connectionState == ConnectionState.Connecting) { throw IllegalStateException("Can't close while connecting.") }

        // Close the connection
        Log.d(TAG,"Closing connection...")
        connection?.close()

        // Clear variables
        connection = null
        device = null;

        // Disconnected
        connectionState = ConnectionState.Disconnected
        Log.d(TAG,"Closed.")
    }

    // endregion


    // region Public Properties

    var connection: GattConnection? = null
        private set

    /**
     * Gets the underlying bluetooth device, if {@link connectionState} is Connected.
     */
    var device: BluetoothDevice? = null
        private set

    // endregion
}