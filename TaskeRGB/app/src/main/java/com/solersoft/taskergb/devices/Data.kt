package com.solersoft.taskergb.devices

/**
 * Provides information about a controllable device.
 * @param name A name for the device. This must be unique across all devices.
 * @param address The address where the device can be reached.
 * @param connectionType The method by which the device can be conencted.
 */
class DeviceInfo ( var name: String, var address: String, var connectionType: ConnectionType = ConnectionType.BLE) {

}

/**
 * A group of controllable devices.
 * @param name A name for the group. This name must be unique across all groups.
 * @param devices A collection of devices that are part of the group.
 */
class DeviceGroup ( var name: String, var devices: ArrayList<DeviceInfo> = ArrayList<DeviceInfo>()) {

}