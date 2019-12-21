package com.solersoft.taskergb.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.content.Context

// A helper class for reading and writing GATT attributes
object GattUtil {
    public fun WriteAttribute(context: Context, address: String) {

        // Validate parameters
        require(!address.isNullOrEmpty()) {"Device address is invalid."}

        // Start by getting the device
        var device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address)

        // Connect to the device
        device.connectGatt(context, false) { gatt: BluetoothGatt, status: Int, newState: Int ->

        }
    }
}