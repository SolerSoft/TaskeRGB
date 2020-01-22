package com.solersoft.taskergb.tasker.palette

import android.graphics.Bitmap
import androidx.annotation.IdRes
import com.solersoft.taskergb.R
import androidx.palette.graphics.Palette.Swatch
import androidx.palette.graphics.Target
import com.solersoft.taskergb.BR
import me.tatarka.bindingcollectionadapter2.ItemBinding

/****************************************
 * Enums
 ****************************************/
enum class ColorSpace {
    /**
     * Regular Hue, Saturation, Lightness space
     */
    HSL,

    /**
     * Modified Hue, Saturation, Value space
     */
    HSV
}


/****************************************
 * Classes
 ****************************************/
/**
 * Defines the types of color targets that can be detected in an image.
 */
enum class ColorTargetType(@IdRes val labelId: Int, @IdRes val descriptionId: Int, val target: Target, val space: ColorSpace = ColorSpace.HSL) {
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
    Vibrant(R.string.vibrantLabel, R.string.vibrantDescription, Target.VIBRANT),

    /**
     * A bright, mostly saturated color.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#VIBRANT">VIBRANT</a>
     */
    Bright(R.string.brightLabel, R.string.brightDescription, CustomTargets.bright, ColorSpace.HSV);

    /**
     * Contains all custom targets for the {@link PaletteRunner} action.
     */
    object CustomTargets {

        // The most dominant colors (RGB regardless of color space)
        val dominant: Target = Target.Builder()
                .setPopulationWeight(1f)
                .setSaturationWeight(0f)
                .setLightnessWeight(0f)
                .setExclusive(false)
                .build()

        // The most perceptively bright colors (HSV space)
        val bright: Target = Target.Builder()
                .setPopulationWeight(0f)
                .setMinimumSaturation(0.50f)
                .setMaximumSaturation(1.0f)
                .setSaturationWeight(0.20f)
                .setMinimumLightness(0.5f) // HSV Value
                .setMaximumLightness(1.0f) // HSV Value
                .setLightnessWeight(0.80f) // HSV Value
                .setExclusive(false)
                .build()
    }
}

/****************************************
 * Bindings
 ****************************************/
/**
 * Bindings used for palette objects.
 */
object PaletteBindings {
    @JvmStatic
    val colorTargetResult get() = ItemBinding.of<ColorTargetResult>(BR.colorTargetResult, R.layout.fragment_color_target_result)

    @JvmStatic
    val swatch get() = ItemBinding.of<Swatch>(BR.swatch, R.layout.fragment_palettesimple)
}