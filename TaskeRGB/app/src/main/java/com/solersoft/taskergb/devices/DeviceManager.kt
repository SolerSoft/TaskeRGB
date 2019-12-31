package com.solersoft.taskergb.devices

object DeviceManager {

    // region Member Variables
    val devices = HashMap<String, DeviceInfo>()
    val groups = HashMap<String, DeviceGroup>()
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
        val device = DeviceInfo(name = deviceName, address = "7C:01:0A:E8:9B:D7", connectionType = ConnectionType.BLE)
        devices[deviceName] = device

        // Create fake group for now
        val group = DeviceGroup(groupName)
        group.devices.add(device)
        groups[groupName] = group
    }
    // endregion

    /**
     * Gets the device with the specified name or throws an exception if the named device is not found.
     * @param name The name of the device.
     */
    fun getDevice(name: String) : DeviceInfo {

        // Validate
        require(devices.containsKey(name)) { "No device could be found with the name '$name'" }

        // Return device
        return devices[name]!!
    }

    /**
     * Gets the group with the specified name or throws an exception if the named group is not found.
     * @param name The name of the group.
     */
    fun getGroup(name: String) : DeviceGroup {

        // Validate
        require(groups.containsKey(name)) { "No group could be found with the name '$name'" }

        // Return device
        return groups[name]!!
    }
}