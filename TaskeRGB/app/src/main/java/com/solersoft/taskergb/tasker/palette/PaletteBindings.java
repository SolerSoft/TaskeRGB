package com.solersoft.taskergb.tasker.palette;

import androidx.palette.graphics.Palette;

import com.solersoft.taskergb.BR;
import com.solersoft.taskergb.R;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


/****************************************
 * Bindings
 ****************************************/
/**
 * Bindings used for palette objects.
 */
public class PaletteBindings {
    static public ItemBinding<ColorTargetResult> colorTargetResult = ItemBinding.of(BR.colorTargetResult, R.layout.fragment_color_target_result);
    static public ItemBinding<Palette.Swatch> swatch = ItemBinding.of(BR.swatch, R.layout.fragment_palettesimple);
}