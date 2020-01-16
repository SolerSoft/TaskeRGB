package com.solersoft.taskergb.tasker.palette

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.R

/****************************************
 * Enums
 ****************************************/

/**
 * Defines the types of color targets that can be detected in an image.
 */
enum class ColorTargetType {
    /**
     * A target which has the characteristics of a muted color which is dark in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#DARK_MUTED">DARK_MUTED</a>
     */
    DarkMuted,

    /**
     * A target which has the characteristics of a vibrant color which is dark in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#DARK_VIBRANT">DARK_VIBRANT</a>
     */
    DarkVibrant,

    /**
     * A target which has the characteristics of a color which shows up most frequently.
     * @see <a href="https://developer.android.com/reference/kotlin/androidx/palette/graphics/Palette#getDominantSwatch()">getDominantSwatch</a>
     */
    Dominant,

    /**
     * A target which has the characteristics of a muted color which is light in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#LIGHT_MUTED">LIGHT_MUTED</a>
     */
    LightMuted,

    /**
     * A target which has the characteristics of a vibrant color which is light in luminance.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#LIGHT_VIBRANT">LIGHT_VIBRANT</a>
     */
    LightVibrant,

    /**
     * A target which has the characteristics of a muted color which is neither light or dark.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#MUTED">MUTED</a>
     */
    Muted,

    /**
     * A target which has the characteristics of a vibrant color which is neither light or dark.
     * @see <a href="https://developer.android.com/reference/androidx/palette/graphics/Target.html#VIBRANT">VIBRANT</a>
     */
    Vibrant
}


/****************************************
 * Classes
 ****************************************/

/**
 * Result class for a single execution of {@link PaletteAction}.
 */
class PaletteResult (val loadedImage: Bitmap) {

    // Named Colors
    @ColorInt
    var darkMuted = Color.BLACK
    @ColorInt
    var darkVibrant = Color.BLACK
    @ColorInt
    var dominant = Color.BLACK
    @ColorInt
    var lightMuted = Color.BLACK
    @ColorInt
    var lightVibrant = Color.BLACK
    @ColorInt
    var muted = Color.BLACK
    @ColorInt
    var vibrant = Color.BLACK

    // Color Collections
    @ColorInt
    var allColors = ArrayList<Int>()
    var darkColors = ArrayList<Int>()
    var lightColors = ArrayList<Int>()
    var mutedColors = ArrayList<Int>()
    var targetColors = HashMap<ColorTargetType, Int>()
    var vibrantColors = ArrayList<Int>()
}