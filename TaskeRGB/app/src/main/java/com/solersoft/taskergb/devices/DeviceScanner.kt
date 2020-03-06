package com.solersoft.taskergb.devices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanFilter
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import splitties.permissions.hasPermission
import splitties.systemservices.bluetoothManager
import java.util.*
import kotlin.collections.ArrayList
import android.bluetooth.le.ScanCallback as BLEScanCallback
import android.bluetooth.le.ScanResult as BLEScanResult
import android.bluetooth.le.ScanSettings as BLEScanSettings

/**
 * Scans for well-known devices.
 */
object DeviceScanner {

    // region Internal Variables
    private val TAG = this::class.simpleName
    // endregion

    // region Nested Types
    /**
     * Handles callbacks for BLE scan results.
     */
    private val bleScanCallback = object : BLEScanCallback() {

        override fun onBatchScanResults(results: List<BLEScanResult?>?) {

        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "Scan failed. ErrorCode: $errorCode")
        }

        override fun onScanResult(callbackType : Int, result : BLEScanResult) {
            when (callbackType) {
                BLEScanSettings.CALLBACK_TYPE_ALL_MATCHES,
                BLEScanSettings.CALLBACK_TYPE_FIRST_MATCH -> {
                    handleResult(result)
                }
            }
        }
    }
    // endregion

    // region Internal Methods
    /**
     * Handles a new BLE scan result
     * @param result The result to handle.
     */
    private fun handleResult(result : BLEScanResult) {
        // Get the device itself
        val device = result.device

        // See if we already have this device
        var info = devices.firstOrNull() { it.address == device.address }

        // If not, create and add it
        if (info == null) {
            info = DeviceInfo(
                    id = UUID.randomUUID(),
                    name = device.name,
                    address = device.address, 
                    connectionType = ConnectionType.BLE)
            devices.add(info)
        }
    }
    // endregion

    // Public Methods
    /**
     * Starts scanning for known devices.
     */
    fun startScanning() {
        Log.d(TAG, "Preparing for scan...");

        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            throw UnsupportedOperationException("User has not granted coarse location permission.")
        }

        // Get the adapter
        val adapter = BluetoothAdapter.getDefaultAdapter() ?: throw UnsupportedOperationException("BLE adapter was not found.")

        // Make sure the adapter is enabled
        if (!adapter.isEnabled) { throw UnsupportedOperationException("BLE adapter is disabled.") }

        // Get the scanner
        val scanner = adapter.bluetoothLeScanner ?: throw UnsupportedOperationException("BLE scanner not found")

        // Create scan settings
        val scanSettings = BLEScanSettings.Builder().apply {

            // Only notify us once per device found
            setCallbackType(BLEScanSettings.CALLBACK_TYPE_FIRST_MATCH)

            // Causes results to be batched
            // setReportDelay(4000) // Causes results to be batched
            // setScanMode(BLEScanSettings.SCAN_MODE_LOW_POWER)

            // Causes results to be immediate
            setScanMode(BLEScanSettings.SCAN_MODE_LOW_LATENCY)

            // Match all devices, even with low signal strength
            setMatchMode(BLEScanSettings.MATCH_MODE_AGGRESSIVE)

            // Make sure the device advertises a couple of times
            setNumOfMatches(BLEScanSettings.MATCH_NUM_FEW_ADVERTISEMENT)

        }.build();

        // Create filters for our known device types
        val filters = ArrayList<ScanFilter>();
        filters.add(ScanFilter.Builder().setServiceUuid(ParcelUuid(BLEDevice.RGBW_SERVICE_UUID)).build());

        // Start scanning
        scanner.startScan(filters, scanSettings, bleScanCallback)
    }

    /**
     * Stops scanning if it has been started.
     */
    fun stopScanning() {
        // Get the adapter
        val adapter = BluetoothAdapter.getDefaultAdapter()

        // Stop scanning
        adapter?.bluetoothLeScanner?.stopScan(bleScanCallback)
    }
    // endregion

    // region Public Properties
    /**
     * Gets the observable list of devices found by the scanner.
     */
    val devices: ObservableList<DeviceInfo> = ObservableArrayList<DeviceInfo>()
    // endregion
}