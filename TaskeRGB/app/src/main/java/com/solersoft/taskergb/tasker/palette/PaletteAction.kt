package com.solersoft.taskergb.tasker.palette

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import coil.Coil
import coil.DefaultRequestOptions
import coil.api.get
import coil.api.load
import coil.request.GetRequest
import coil.request.GetRequestBuilder
import coil.request.LoadRequest
import coil.request.LoadRequestBuilder
import com.solersoft.taskergb.addUnique
import kotlinx.coroutines.*
import androidx.palette.graphics.Palette.Swatch


object PaletteAction {

    /**
     * Runs the Palette action.
     */
   suspend fun run(context: Context, input: PaletteInput): PaletteResult {

        // Validate input
        input.validate()

        // Parse input color
        @ColorInt val defaultColor = Color.parseColor(input.defaultColor)

        // Include default in arrays?
        val includeDefault = false

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
        val bmp = (image as BitmapDrawable).bitmap

        // Create the palette builder from the bitmap
        val builder = Palette.from(bmp)

        // Set configurable color count (default is 16)
        builder.maximumColorCount(input.colorCount)

        // Add custom targets to builder
        ColorTargetType.CustomTargets.all.forEach { builder.addTarget(it) }

        // Build the palette in its own coroutine thread
        val palette = withContext(Dispatchers.Default) {
            builder.generate()
        }

        // Build the palette variants with a threshold of 70% match to target.
        val variant = PaletteVariant.generate(palette, 0.70F)

        // Create the output object
        val output = PaletteResult(bmp)

        // Map named targets to result variables
        ColorTargetType.values().forEach {

            // Set the primary swatch from the AndroidX Palette
            output.setPrimary(it, palette.getSwatchForTarget(it.target))

            // Set the variants from our PaletteVariant
            output.setVariants(it, variant.getSwatches(it.target))
        }

        // Done
        return output
    }
}