package com.solersoft.taskergb.devices

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import com.solersoft.taskergb.binding.observableProperty
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.util.*

/**
 * Provides information about a controllable device.
 */
class DeviceInfo : BaseObservable {
    private val ONLINE_TIME: Long = 5000

    val id: UUID
    val connectionType: ConnectionType
    @get:Bindable var name: String by observableProperty("")
    @get:Bindable var address: String by observableProperty("")
    @get:Bindable var lastSeen: Long by observableProperty(0, ::lastSeenChanged)
    @get:Bindable var online: Boolean by observableProperty(false)

    /**
     * Constructs the instance.
     * @param id A unique ID for the device that will not change over time.
     * @param name A name for the device. This must be unique across all devices.
     * @param address The address where the device can be reached.
     * @param connectionType The method by which the device can be connected.
     */
    constructor(id : UUID, name: String, address: String, connectionType: ConnectionType = ConnectionType.BLE, lastSeen: Long = 0)
    {
        this.id = id
        this.name = name
        this.address = address
        this.connectionType = connectionType
        this.lastSeen = lastSeen
    }

    private fun lastSeenChanged(oldValue: Long, newValue: Long) {
        // Make sure it's changing
        if (newValue != oldValue) {
            updateOnline()
        }
    }

    /**
     * Updates the Online status based on when the device was last seen.
     */
    fun updateOnline() {
        // Only online if seen recently
        online = (System.currentTimeMillis() - lastSeen < ONLINE_TIME)
    }
}

/**
 * A group of controllable devices.
 */
class DeviceGroup : BaseObservable {
    val id: UUID
    @get:Bindable var name: String by observableProperty("")
    @get:Bindable var devices: ObservableArrayList<DeviceInfo> by observableProperty(ObservableArrayList<DeviceInfo>())

    /**
     * Constructs the instance.
     * @param id A unique ID for the group that will not change over time.
     * @param name A name for the group. This name must be unique across all groups.
     * @param devices A collection of devices that are part of the group.
     */
    constructor(id: UUID, name: String, devices: ObservableArrayList<DeviceInfo>? = null) {
        this.id = id
        this.name = name
        if (devices != null) this.devices = devices
    }
}