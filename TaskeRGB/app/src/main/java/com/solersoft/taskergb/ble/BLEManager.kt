package com.solersoft.taskergb.ble

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.solersoft.taskergb.R

// A class for managing BLE access and connecting to devices
class BLEManager(val context: Context) {
    /**
     * Check the Location Permission before calling the BLE API's
     */
    private fun checkLocationPermission() {
        if (isAboveMarshmallow()) {
            when {
                isLocationPermissionEnabled() -> initBLEModule()
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) -> displayRationale()
                else -> requestLocationPermission()
            }
        } else {
            initBLEModule()
        }
    }
    /**
     * Request Location API
     * If the request go to Android system and the System will throw a dialog message
     * user can accept or decline the permission from there
     */
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
    }
    /**
     * If the user decline the Permission request and tick the never ask again message
     * Then the application can't proceed further steps
     * In such situation- App need to prompt the user to do the change form Settings Manually
     */
    private fun displayRationale() {
        AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.location_permission_disabled))
                .setPositiveButton(context.getString(R.string.ok)
                ) { _, _ -> requestLocationPermission() }
                .setNegativeButton(context.getString(R.string.cancel)
                ) { _, _ -> }
                .show()
    }
    /**
     * If the user either accept or reject the Permission- The requested App will get a callback
     * Form the call back we can filter the user response with the help of request key
     * If the user accept the same- We can proceed further steps
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (permissions.size != 1 || grantResults.size != 1) {
                    throw RuntimeException("Error on requesting location permission.")
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initBLEModule()
                } else {
                    Toast.makeText(context, R.string.location_permission_not_granted,
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    /**
     * Check with the system- If the permission already enabled or not
     */
    private fun isLocationPermissionEnabled(): Boolean {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }
    /**
     * The location permission is incorporated in Marshmallow and Above
     */
    private fun isAboveMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}