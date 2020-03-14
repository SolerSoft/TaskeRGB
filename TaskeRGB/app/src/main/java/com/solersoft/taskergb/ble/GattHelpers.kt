package com.solersoft.taskergb.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

const val GATT_HELPERS_TAG = "GattHelpers"

@RequiresApi(18)
fun deviceFor(macAddress: String): BluetoothDevice {
    return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress)
}

@ObsoleteCoroutinesApi
fun GattConnection.logConnectionChanges() {
    GlobalScope.launch(Dispatchers.Main) {
        stateChangeChannel.consumeEach {
            Log.i(GATT_HELPERS_TAG,"Connection state changed: $it")
        }
    }
}

/**
 * Connects to the device, discovers services, executes [block] and finally closes the connection.
 */
@RequiresApi(18)
@UseExperimental(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
suspend inline fun BluetoothDevice.useBasic(
        connectionTimeoutInMillis: Long = 5000L,
        block: (GattConnection, List<BluetoothGattService>) -> Unit
) {
    val deviceConnection = GattConnection(this)
    try {
        deviceConnection.logConnectionChanges()
        withTimeout(connectionTimeoutInMillis) {
            deviceConnection.connect()
        }
        Log.i(GATT_HELPERS_TAG, "Connected!")
        val services = deviceConnection.discoverServices()
        Log.i(GATT_HELPERS_TAG,"Services discovered!")
        block(deviceConnection, services)
    } catch (e: TimeoutCancellationException) {
        Log.e(GATT_HELPERS_TAG, "Connection timed out after $connectionTimeoutInMillis milliseconds!")
        throw e
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e(GATT_HELPERS_TAG, "Unknown BLE error", e)
    } finally {
        deviceConnection.close()
        Log.i(GATT_HELPERS_TAG,"Closed!")
    }
}