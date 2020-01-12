package com.solersoft.taskergb.tasker.palette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.view.OneShotPreDrawListener.add
import androidx.palette.graphics.Palette
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.beepiz.bluetooth.gattcoroutines.OperationFailedException
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.output.runner.TaskerOutputForRunner
import com.joaomgcd.taskerpluginlibrary.output.runner.TaskerOutputsForRunner
import com.joaomgcd.taskerpluginlibrary.output.runner.TaskerValueGetterDirect
import com.joaomgcd.taskerpluginlibrary.output.runner.TaskerValueGetterMethod
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.solersoft.taskergb.*
import com.solersoft.taskergb.ble.deviceFor
import com.solersoft.taskergb.ble.useBasic
import com.solersoft.taskergb.devices.DeviceInfo
import com.solersoft.taskergb.devices.DeviceManager
import com.solersoft.taskergb.devices.RGBWDeviceBLE
import kotlinx.coroutines.runBlocking
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

        // Build the palette
        val palette = builder.generate()

        // Create the output
        val po = PaletteOutput()

        // Read the desired input targets from the palette and assign them to outputs
        ColorTargetType.values().forEach {
            when (it) {
                ColorTargetType.DarkMuted -> po.darkMuted = palette.getDarkMutedColor(pi.defaultColor)
                ColorTargetType.DarkVibrant -> po.darkVibrant = palette.getDarkVibrantColor(pi.defaultColor)
                ColorTargetType.Dominant -> po.dominant = palette.getDominantColor(pi.defaultColor)
                ColorTargetType.LightMuted -> po.lightMuted = palette.getLightMutedColor(pi.defaultColor)
                ColorTargetType.LightVibrant -> po.lightVibrant = palette.getLightVibrantColor(pi.defaultColor)
                ColorTargetType.Muted -> po.muted = palette.getMutedColor(pi.defaultColor)
                ColorTargetType.Vibrant -> po.vibrant = palette.getVibrantColor(pi.defaultColor)
            }
        }

        // Done!
        return TaskerPluginResultSucess(po)
    }
}