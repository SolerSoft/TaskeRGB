package com.solersoft.taskergb.tasker.palette

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.palette.graphics.Palette
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.R
import com.solersoft.taskergb.binding.bindDelegate
import androidx.palette.graphics.Palette.Swatch
import androidx.palette.graphics.Target

/****************************************
 * Enums
 ****************************************/

/**
 * Defines the types of color targets that can be detected in an image.
 */
enum class ColorTargetType(@IdRes val labelId: Int, @IdRes val descId: Int,  val target: Target) {
    /**
     * A target which has the characteristics of a muted color which is dark in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#DARK_MUTED">DARK_MUTED</a>
     */
    DarkMuted(R.string.darkMutedLabel, R.string.darkMutedDescription, Target.DARK_MUTED),

    /**
     * A target which has the characteristics of a vibrant color which is dark in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#DARK_VIBRANT">DARK_VIBRANT</a>
     */
    DarkVibrant(R.string.darkVibrantLabel, R.string.darkVibrantDescription, Target.DARK_VIBRANT),

    /**
     * A target which has the characteristics of a color which shows up most frequently.
     * @see <a href="https://developer.android.com/reference/kotlin/androidx/palette/graphics/Palette#getDominantSwatch()">getDominantSwatch</a>
     */
    Dominant(R.string.dominantLabel, R.string.dominantDescription, CustomTargets.dominant),

    /**
     * A target which has the characteristics of a muted color which is light in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#LIGHT_MUTED">LIGHT_MUTED</a>
     */
    LightMuted(R.string.lightMutedLabel, R.string.lightMutedDescription, Target.LIGHT_MUTED),

    /**
     * A target which has the characteristics of a vibrant color which is light in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#LIGHT_VIBRANT">LIGHT_VIBRANT</a>
     */
    LightVibrant(R.string.lightVibrantLabel, R.string.lightVibrantDescription, Target.LIGHT_VIBRANT),

    /**
     * A target which has the characteristics of a muted color which is neither light or dark.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#MUTED">MUTED</a>
     */
    Muted(R.string.mutedLabel, R.string.mutedDescription, Target.MUTED),

    /**
     * A target which has the characteristics of a vibrant color which is neither light or dark.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#VIBRANT">VIBRANT</a>
     */
    Vibrant(R.string.vibrantLabel, R.string.vibrantDescription, Target.VIBRANT);

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
}


/****************************************
 * Classes
 ****************************************/

/**
 * Represents a result for a single {@link ColorTargetType}.
 * @param target The {@link ColorTargetType} which the result was generated for.
 * @param primary The primary {@link Swatch} that was found for the target.
 * @param variants The {@link Swatch} variants that were found for the target.
 */
class TargetResult(val target: ColorTargetType, var primary: Swatch? = null, var variants: ArrayList<Swatch> = ArrayList<Swatch>()) {
    /**
     * Gets a friendly name for the target.
     */
    val targetName : String get() {
        // TODO: Use resource strings?
        return target.toString()
    }
}


/**
 * Result class for a single execution of {@link PaletteAction}.
 * @param loadedImage The bitmap image that the palette results will be calculated from.
 * @param results The list of all results calculated for the image.
 */
class PaletteResult (val loadedImage: Bitmap, val results: HashMap<ColorTargetType, TargetResult> = HashMap<ColorTargetType, TargetResult>()) {

    /**
     * Adds the swatch as a variant for the specified target.
     * @param target The target to add the variant swatch for.
     * @param primary The variant swatch to add.
     */
    fun addVariant(target: ColorTargetType, variant: Swatch) {

        // Get existing target result or create new one
        var result = results.getOrPut(target) { TargetResult(target) }

        // Add the variant
        result.variants.add(variant)
    }

    /**
     * Sets the primary swatch for the specified target.
     * @param target The target to set the primary swatch for.
     * @param primary The swatch to set as primary.
     */
    fun setPrimary(target: ColorTargetType, primary: Swatch?) {

        // Get existing target result or create new one
        var result = results.getOrPut(target) { TargetResult(target) }

        // Set the primary
        result.primary = primary
    }

    /**
     * Sets the swatch variants for the specified target.
     * @param target The target to set the variants  for.
     * @param primary The list of swatch variants to set.
     */
    fun setVariants(target: ColorTargetType, variants: ArrayList<Swatch>) {

        // Get existing target result or create new one
        var result = results.getOrPut(target) { TargetResult(target) }

        // Set the variants
        result.variants = variants
    }
}