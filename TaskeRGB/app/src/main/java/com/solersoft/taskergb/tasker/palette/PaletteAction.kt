package com.solersoft.taskergb.tasker.palette

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import coil.api.get


object PaletteAction {

    /**
     * Runs the Palette action.
     */
   suspend fun run(context: Context, input: PaletteInput): PaletteEx {

        // Validate input
        input.validate()

        // // Parse default color
        // @ColorInt val defaultColor = Color.parseColor(input.defaultColor)

        // Get the image path
        val imagePath = input.imagePath!!

        // Time to load the image using Coil, but we can't allow hardware bitmaps
        // because the palette builder needs access to the pixels.
        // Create the loader without hardware access
        val loader = coil.ImageLoader(context) {
            allowHardware(false)
        }

        // Have the loader get the image in its own coroutine thread
        val image = loader.get(imagePath)

        // Get the bitmap from the drawable
        val bitmap = (image as BitmapDrawable).bitmap

        // Build the PaletteEx with a variant threshold of 70% match to target.
        return PaletteEx.generate(bitmap, input.colorCount, 0.70F)
    }
}