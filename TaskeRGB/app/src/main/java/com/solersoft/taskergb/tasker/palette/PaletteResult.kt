package com.solersoft.taskergb.tasker.palette

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.R


/**
 * Kotlin output for the Palette action.
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