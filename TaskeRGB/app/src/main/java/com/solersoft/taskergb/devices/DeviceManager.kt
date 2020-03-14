package com.solersoft.taskergb.devices

import androidx.databinding.ObservableArrayMap
import java.util.*
import kotlin.collections.HashMap

object DeviceManager {

    // region Member Variables
    val devices = ObservableArrayMap<UUID, DeviceInfo>()
    val groups = HashMap<UUID, DeviceGroup>()
    // endregion

    // region Initialization
    init {
        loadData()
    }
    // endregion

    // region Internal Methods
    private fun loadData() {

        val deviceName = "Test"
        val groupName = "Main"

        // Create fake device for now
        val device = DeviceInfo(
                id = UUID(0, 1),
                name = deviceName,
                address = "7C:01:0A:E8:9B:D7",
                connectionType = ConnectionType.BLE)
        devices[device.id] = device

        // Create fake group for now
        val group = DeviceGroup(
                id = UUID(0, 1),
                name = groupName)
        group.devices.add(device)
        groups[group.id] = group
    }
    // endregion

    /**
     * Gets the device with the specified id or throws an exception if the device is not found.
     * @param id The id of the device.
     */
    fun getDevice(id: UUID) : DeviceInfo {

        // Validate
        require(devices.containsKey(id)) { "No device could be found with the id '$id'" }

        // Return device
        return devices[id]!!
    }

    /**
     * Gets the group with the specified id or throws an exception if the group is not found.
     * @param id The id of the group.
     */
    fun getGroup(id: UUID) : DeviceGroup {

        // Validate
        require(groups.containsKey(id)) { "No group could be found with the id '$id'" }

        // Return device
        return groups[id]!!
    }
}