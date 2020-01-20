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

object PaletteAction {
    /**
     * Contains all custom targets for the {@link PaletteRunner} action.
     */
    object CustomTargets {

        // Create all custom targets
        val dominant: Target = Target.Builder().setPopulationWeight(1f)
                .setSaturationWeight(0f)
                .setLightnessWeight(0f)
                .setExclusive(false)
                .build()

        val all = arrayOf(dominant)
    }

    /*
    /**
     * Loads the image at the specified path
     */
    private fun loadImage(context: Context, path: String): Bitmap {
        /*
        // TODO: Support URLs

        // Remove file prefix if it's there
        val fpath = path.replace("file://", "")

        var f1 = File(fpath)
        var f1e = f1.exists()

        val ap = f1.absolutePath

        // Attempt to load and make sure we got a value
        return BitmapFactory.decodeFile(ap)
                ?: throw IllegalArgumentException("File not found: $ap")
         */
    }

     */

    /**
     * Gets the specified {@link ColorTargetType} from the {@link Palette} or returns {@link defaultColor}.
     * @param target The {@link ColorTargetType} to obtain.
     * @param defaultColor the default color to return if {@link target} isn't found.
     */
    @ColorInt
    private fun Palette.getColorForTarget(target: ColorTargetType, @ColorInt defaultColor: Int = Color.BLACK): Int {
        return when (target) {
            ColorTargetType.DarkMuted -> this.getDarkMutedColor(defaultColor)
            ColorTargetType.DarkVibrant -> this.getDarkVibrantColor(defaultColor)
            ColorTargetType.Dominant -> this.getColorForTarget(CustomTargets.dominant, defaultColor)
            ColorTargetType.LightMuted -> this.getLightMutedColor(defaultColor)
            ColorTargetType.LightVibrant -> this.getLightVibrantColor(defaultColor)
            ColorTargetType.Muted -> this.getMutedColor(defaultColor)
            ColorTargetType.Vibrant -> this.getVibrantColor(defaultColor)
        }
    }

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
        CustomTargets.all.forEach { builder.addTarget(it) }

        // Build the palette in its own coroutine thread
        val palette = withContext(Dispatchers.Default) {
            builder.generate()
        }

        // Build the palette variants with a threshold of 70% match to target.
        val variant = PaletteVariant.generate(palette, 0.70F)

        // Create the output object
        val output = PaletteResult(bmp)

        // Copy all palette RGB results to the 'all' collection
        // output.allColors.addAll(palette.swatches.map { s -> s.rgb })
        output.allColors.addAll(variant.getSwatches(Target.VIBRANT).map { s -> s.rgb })

        // Map named targets to named output variables
        ColorTargetType.values().forEach {

            // Get the color for the palette entry
            var color = palette.getColorForTarget(it, defaultColor)
            var included = ((includeDefault) || (color != defaultColor))
            if (included) {
                output.targetColors[it] = color

                // Parse entry into various arrays
                when (it) {
                    ColorTargetType.DarkMuted -> {
                        output.darkMuted = color
                        if (included) {
                            output.darkColors.addUnique(color)
                            output.mutedColors.addUnique(color)
                        }
                    }
                    ColorTargetType.DarkVibrant -> {
                        output.darkVibrant = color
                        if (included) {
                            output.darkColors.addUnique(color)
                            output.vibrantColors.addUnique(color)
                        }
                    }
                    ColorTargetType.Dominant -> {
                        output.dominant = color
                    }
                    ColorTargetType.LightMuted -> {
                        output.lightMuted = color
                        if (included) {
                            output.lightColors.addUnique(color)
                            output.mutedColors.addUnique(color)
                        }
                    }
                    ColorTargetType.LightVibrant -> {
                        output.lightVibrant = color
                        if (included) {
                            output.lightColors.addUnique(color)
                            output.vibrantColors.addUnique(color)
                        }
                    }
                    ColorTargetType.Muted -> {
                        output.muted = color
                        if (included) {
                            output.mutedColors.addUnique(color)
                        }
                    }
                    ColorTargetType.Vibrant -> {
                        output.vibrant = color
                        if (included) {
                            output.vibrantColors.addUnique(color)
                        }
                    }
                }
            }
        }

        // Done
        return output
    }
}