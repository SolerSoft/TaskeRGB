package com.solersoft.taskergb.devices;

import androidx.palette.graphics.Palette;

import com.solersoft.taskergb.BR;
import com.solersoft.taskergb.R;
import com.solersoft.taskergb.tasker.palette.ColorTargetResult;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


/****************************************
 * Bindings
 ****************************************/
/**
 * Bindings used for device objects.
 */
public class DeviceBindings {
    static public ItemBinding<DeviceInfo> device = ItemBinding.of(BR.device, R.layout.fragment_deviceinfo);
}