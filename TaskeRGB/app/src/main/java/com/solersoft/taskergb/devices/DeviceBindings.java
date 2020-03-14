package com.solersoft.taskergb.devices;

import com.solersoft.taskergb.BR;
import com.solersoft.taskergb.R;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


/****************************************
 * Bindings
 ****************************************/
/**
 * Bindings used for device objects.
 */
public class DeviceBindings {
    static public ItemBinding<DeviceInfo> foundDevice = ItemBinding.of(BR.device, R.layout.fragment_founddeviceinfo);
    static public ItemBinding<DeviceInfo> savedDevice = ItemBinding.of(BR.device, R.layout.fragment_saveddeviceinfo);
}