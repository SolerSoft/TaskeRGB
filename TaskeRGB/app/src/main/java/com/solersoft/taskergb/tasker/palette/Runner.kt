package com.solersoft.taskergb.tasker.palette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.*
import java.io.File


/**
 * Represents a Tasker action
 */
class PaletteRunner : TaskerPluginRunnerAction<PaletteInput, PaletteOutput>() {

    // Add our custom plugin icon
    override val notificationProperties get() = NotificationProperties(iconResId = R.drawable.plugin)

    /**
     * Loads the image at the specified path
     */
    private fun loadImage(path: String) : Bitmap {
        // TODO: Support URLs

        // Remove file prefix if it's there
        val fpath = path.replace("file://", "")

        var f1 = File(fpath)
        var f1e = f1.exists()

        val ap = f1.absolutePath

        // Attempt to load and make sure we got a value
        return BitmapFactory.decodeFile(ap)
                ?: throw IllegalArgumentException("File not found: $ap")
    }

    /**
     * Gets the specified {@link ColorTargetType} from the {@link Palette} or returns {@link defaultColor}.
     * @param target The {@link ColorTargetType} to obtain.
     * @param defaultColor the default color to return if {@link target} isn't found.
     */
    @ColorInt
    private fun Palette.getColorForTarget(target: ColorTargetType, @ColorInt defaultColor: Int = Color.BLACK) : Int {
        return when (target) {
            ColorTargetType.DarkMuted -> this.getDarkMutedColor(defaultColor)
            ColorTargetType.DarkVibrant -> this.getDarkVibrantColor(defaultColor)
            ColorTargetType.Dominant -> this.getDominantColor(defaultColor)
            ColorTargetType.LightMuted -> this.getLightMutedColor(defaultColor)
            ColorTargetType.LightVibrant -> this.getLightVibrantColor(defaultColor)
            ColorTargetType.Muted -> this.getMutedColor(defaultColor)
            ColorTargetType.Vibrant -> this.getVibrantColor(defaultColor)
        }
    }

    /**
     * Performs the real work of the action
     */
    override fun run(context: Context, input: TaskerInput<PaletteInput>): TaskerPluginResult<PaletteOutput> {

        // Validate input
        val pi = input.regular
        pi.validate()

        // De-dynamic the image path
        val imagePath = input.deDynamic(pi.imagePath!!)

        // Load the image
        val bmp = loadImage(imagePath)

        // Create the palette builder
        val builder = Palette.from(bmp)

        // Configure the palette builder
        // TODO: Is this necessary?
        ColorTargetType.values().forEach {
            when (it) {
                ColorTargetType.DarkMuted -> builder.addTarget(Target.DARK_MUTED)
                ColorTargetType.DarkVibrant -> builder.addTarget(Target.DARK_VIBRANT)
                ColorTargetType.Dominant -> Unit
                ColorTargetType.LightMuted -> builder.addTarget(Target.LIGHT_MUTED)
                ColorTargetType.LightVibrant -> builder.addTarget(Target.LIGHT_VIBRANT)
                ColorTargetType.Muted -> builder.addTarget(Target.MUTED)
                ColorTargetType.Vibrant -> builder.addTarget(Target.VIBRANT)
            }
        }

        // Add more colors (default is 16)
        builder.maximumColorCount(32)

        // Build the palette
        val palette = builder.generate()

        // Create output arrays
        val allColors = ArrayList<String>()
        val darkColors = ArrayList<String>()
        val lightColors = ArrayList<String>()
        val mutedColors = ArrayList<String>()
        val vibrantColors = ArrayList<String>()

        // Create the output object
        val po = PaletteOutput()

        // Read the desired input targets from the palette and assign them to outputs
        ColorTargetType.values().forEach {

            // Get the color for the palette entry
            var color = palette.getColorForTarget(it, pi.defaultColor)
            var tcolor = color.toTaskerColor()

            // Parse entry into various arrays
            when (it) {
                ColorTargetType.DarkMuted -> {
                    po.darkMuted = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                        darkColors.add(tcolor)
                        mutedColors.add(tcolor)
                    }
                }
                ColorTargetType.DarkVibrant -> {
                    po.darkVibrant = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                        darkColors.add(tcolor)
                        vibrantColors.add(tcolor)
                    }
                }
                ColorTargetType.Dominant -> {
                    po.dominant = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                    }
                }
                ColorTargetType.LightMuted -> {
                    po.lightMuted = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                        lightColors.add(tcolor)
                        mutedColors.add(tcolor)
                    }
                }
                ColorTargetType.LightVibrant -> {
                    po.lightVibrant = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                        lightColors.add(tcolor)
                        vibrantColors.add(tcolor)
                    }
                }
                ColorTargetType.Muted -> {
                    po.muted = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                        mutedColors.add(tcolor)
                    }
                }
                ColorTargetType.Vibrant -> {
                    po.vibrant = tcolor
                    if (color != pi.defaultColor) {
                        allColors.add(tcolor)
                        vibrantColors.add(tcolor)
                    }
                }
            }
        }

        // Assign output arrays
        po.allColors = allColors.toTypedArray()

        // Done!
        return TaskerPluginResultSucess(po)
    }
}