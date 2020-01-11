package com.solersoft.taskergb.tasker.palette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.R
import com.solersoft.taskergb.ble.deviceFor
import com.solersoft.taskergb.ble.useBasic
import com.solersoft.taskergb.byIndex
import com.solersoft.taskergb.devices.DeviceInfo
import com.solersoft.taskergb.devices.DeviceManager
import com.solersoft.taskergb.devices.RGBWDeviceBLE
import kotlinx.coroutines.runBlocking


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
        return BitmapFactory.decodeFile(path)
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
            ColorTargetType.None -> defaultColor
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

        // Load the image
        val bmp = loadImage(pi.imagePath!!)

        // Create the palette builder
        val builder = Palette.from(bmp)

        // Configure the palette builder
        // TODO: Is this necessary?

        // Build the palette
        val palette = builder.generate()

        // Create the output
        val po = PaletteOutput()

        // Read the desired input targets from the palette and assign them to outputs
        po.color1 = palette.getColorForTarget(enumValueOf<ColorTargetType>(pi.color1Target))
        po.color2 = palette.getColorForTarget(enumValueOf<ColorTargetType>(pi.color2Target))
        po.color3 = palette.getColorForTarget(enumValueOf<ColorTargetType>(pi.color3Target))
        po.color4 = palette.getColorForTarget(enumValueOf<ColorTargetType>(pi.color4Target))

        // Done!
        return TaskerPluginResultSucess(po)
    }
}