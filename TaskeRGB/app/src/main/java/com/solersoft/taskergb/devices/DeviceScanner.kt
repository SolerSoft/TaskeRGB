package com.solersoft.taskergb.devices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import splitties.permissions.hasPermission
import java.util.*
import android.bluetooth.le.ScanCallback as BLEScanCallback
import android.bluetooth.le.ScanResult as BLEScanResult

/**
 * Scans for well-known devices.
 */
object DeviceScanner {

    // region Internal Variables
    private val TAG = this::class.simpleName
    private var lastOnlineUpdate: Long = 0

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
            // HACK: Just handle basic result now. No filtering.
            handleResult(result)

            // Update online status?
            val now = System.currentTimeMillis()
            if (now - lastOnlineUpdate > 10000) {
                lastOnlineUpdate = now
                updateOnlineDevices()
            }

            /*
            when (callbackType) {
                BLEScanSettings.CALLBACK_TYPE_ALL_MATCHES,
                BLEScanSettings.CALLBACK_TYPE_FIRST_MATCH -> {
                    handleResult(result, online = true)
                }
                BLEScanSettings.CALLBACK_TYPE_MATCH_LOST -> {
                    handleResult(result, online = false)
                }
            }
             */
        }
    }
    // endregion

    // region Internal Methods
    /**
     * Handles a new BLE scan result
     * @param result The result to handle.
     */
    private fun handleResult(result: BLEScanResult) {
        // Get the device itself
        val device = result.device

        // HACK: Since UUID filters don't work, we have to make sure the device name starts with 'LED' :(
        if (device.name == null || !device.name.startsWith(prefix = "LED", ignoreCase = true)) { return }

        // See if we already have this device
        var info = devices.firstOrNull() { it.address == device.address }

        // Already existing?
        if (info == null)
        {
            // Not existing. Create and add it.
            info = DeviceInfo(
                    id = UUID.randomUUID(),
                    name = device.name ?: device.address,
                    address = device.address,
                    connectionType = ConnectionType.BLE,
                    lastSeen = System.currentTimeMillis())
            devices.add(info)
        }
        else
        {
            // Already existing. Just update last seen
            info.lastSeen = System.currentTimeMillis()
        }
    }

    private fun updateOnlineDevices() {
        devices?.forEach() {
            it.updateOnline()
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
        /*
        val scanSettings = BLEScanSettings.Builder().apply {

            // Only notify us once per device found; also notify if device lost
            setCallbackType(BLEScanSettings.CALLBACK_TYPE_FIRST_MATCH or BLEScanSettings.CALLBACK_TYPE_MATCH_LOST)

            // Causes results to be batched
            // setReportDelay(4000) // Causes results to be batched
            // setScanMode(BLEScanSettings.SCAN_MODE_LOW_POWER)

            // Causes results to be immediate
            setScanMode(BLEScanSettings.SCAN_MODE_LOW_LATENCY)

            // Match all devices, even with low signal strength
            setMatchMode(BLEScanSettings.MATCH_MODE_AGGRESSIVE)

            // Take any matching device, even if it only advertises once
            setNumOfMatches(BLEScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)

        }.build();
         */

        // Create filters for our known device types
        // HACK: Android does NOT allow filter by partial name, and some LED devices do not
        // include their service UUIDs in their advertising packet.
        // val filters = ArrayList<ScanFilter>();
        // filters.add(ScanFilter.Builder().setServiceUuid(ParcelUuid(BLEDevice.RGBW_SERVICE_UUID)).build());

        // Start scanning
        // scanner.startScan(filters, scanSettings, bleScanCallback)
        scanner.startScan(bleScanCallback)
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