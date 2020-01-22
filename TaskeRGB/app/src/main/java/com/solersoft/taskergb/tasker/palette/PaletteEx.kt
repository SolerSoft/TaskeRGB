package com.solersoft.taskergb.tasker.palette

import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

/**
 * Represents a result for a single {@link ColorTargetType}.
 * @param type The {@link ColorTargetType} which the result was generated for.
 * @param primary The primary {@link Swatch} that was found for the target.
 * @param variants The {@link Swatch} variants that were found for the target.
 */
class ColorTargetResult(val type: ColorTargetType, var primary: Swatch? = null, var variants: ArrayList<Swatch> = ArrayList<Swatch>())

/**
 * An extension to the {@link Palette} class that supports additional color spaces and variants.
 */
class PaletteEx(val bitmap: Bitmap, private val palette: Palette, private val variantThreshold: Float, val types: Array<ColorTargetType>)
{
    // region Companion Static Functions
    companion object {

        /**
         * Gets the HSV value for the specified {@link Swatch}
         */
        val Swatch.hsv: FloatArray get() {
            val hsv = FloatArray(3)
            Color.colorToHSV(this.rgb, hsv)
            return hsv
        }

        /**
         * Generates a {@link PaletteEx} from the specified {@link Palette}.
         * @param palette An already generated {@link Palette} to start from.
         * @param types The list of {@link ColorTargetType} values to calculate.
         */
        suspend fun generate(bitmap: Bitmap, palette: Palette, variantThreshold: Float, types: Array<ColorTargetType> = ColorTargetType.values()) : PaletteEx =
            withContext(Dispatchers.Default) {
                // Create a new PaletteEx
                val ex = PaletteEx(bitmap, palette, variantThreshold, types)

                // Generate
                ex.generate()

                // Return variant
                return@withContext ex
            }

        /**
         * Generates a {@link PaletteEx} from the specified {@link Palette}.
         * @param bitmap The {@link Bitmap} to generate the palette from.
         * @param maximumColorCount The maximum number of colors to generate.
         * @param types The list of {@link ColorTargetType} values to calculate.
         */
        suspend fun generate(bitmap: Bitmap, maximumColorCount: Int, variantThreshold: Float, types: Array<ColorTargetType> = ColorTargetType.values()) : PaletteEx =
                withContext(Dispatchers.Default) {

                    // Create the AndroidX Palette Builder from the bitmap
                    val builder = Palette.from(bitmap)

                    // Set max color count on the Builder
                    builder.maximumColorCount(maximumColorCount)

                    // Add HSL targets to AndroidX Palette Builder
                    val hslTypes = types.filter { it.space == ColorSpace.HSL }
                    hslTypes.forEach { builder.addTarget(it.target) }

                    // Build the AndroidX palette in its own coroutine thread
                    val palette = withContext(Dispatchers.Default) {
                        builder.generate()
                    }

                    // Pass the generated AndroidX Palette to other override for ongoing generation
                    return@withContext generate(bitmap, palette, variantThreshold, types)
                }

    }
    // endregion

    // region Member Variables
    // private val hsvSwatches = HashMap<ColorTargetType, Swatch>()
    // private val variants = HashMap<ColorTargetType, ArrayList<Swatch>>()
    private val usedColors = ArrayList<Int>()
    // endregion

    // region Internal Functions
    private fun shouldBeScoredForType(swatch: Swatch, type: ColorTargetType): Boolean {
        // Check whether the color space values are within the correct ranges
        return when (type.space)
        {
            ColorSpace.HSL -> {
                val hsl = swatch.hsl
                hsl[1] >= type.target.minimumSaturation && hsl[1] <= type.target.maximumSaturation && hsl[2] >= type.target.minimumLightness && hsl[2] <= type.target.maximumLightness
            }

            ColorSpace.HSV -> {
                val hsv = swatch.hsv
                hsv[1] >= type.target.minimumSaturation && hsv[1] <= type.target.maximumSaturation && hsv[2] >= type.target.minimumLightness && hsv[2] <= type.target.maximumLightness
            }
        }
    }

    private fun generateScore(swatch: Swatch, type: ColorTargetType): Float {
        // Population is always the same regardless of color space
        var populationScore = 0f
        val maxPopulation = palette.dominantSwatch?.population ?: 1
        if (type.target.populationWeight > 0) {
            populationScore = (type.target.populationWeight
                    * (swatch.population / maxPopulation.toFloat()))
        }

         // Remainder is by color space
        return when (type.space) {

            ColorSpace.HSL -> {
                val hsl = swatch.hsl
                var saturationScore = 0f
                var luminanceScore = 0f
                if (type.target.saturationWeight > 0) {
                    saturationScore = (type.target.saturationWeight
                            * (1f - abs(hsl[1] - type.target.targetSaturation)))
                }
                if (type.target.lightnessWeight > 0) {
                    luminanceScore = (type.target.lightnessWeight
                            * (1f - abs(hsl[2] - type.target.targetLightness)))
                }

                // Calculate
                saturationScore + luminanceScore + populationScore
            }

            ColorSpace.HSV -> {
                val hsv = swatch.hsv
                var saturationScore = 0f
                var valueScore = 0f
                if (type.target.saturationWeight > 0) {
                    saturationScore = (type.target.saturationWeight
                            * (1f - abs(hsv[1] - type.target.targetSaturation)))
                }
                if (type.target.lightnessWeight > 0) {
                    valueScore = (type.target.lightnessWeight
                            * (1f - abs(hsv[2] - type.target.targetLightness)))
                }

                // Calculate
                saturationScore + valueScore + populationScore
            }
        }
    }

    /**
     * Generates a {@link PaletteVariant} from the {@link Palette}
     */
    private fun generate() {

        // Go through all target types
        types.forEach { type ->

            // For calculating max values
            var maxSwatch : Swatch? = null
            var maxScore : Float = 0F

            // For each target, go through all swatches
            palette.swatches.forEach { swatch ->

                // Is this swatch valid for the target?
                if (shouldBeScoredForType(swatch, type)) {

                    // Yes. Calculate a score.
                    val score = generateScore(swatch, type)

                    // If score meets variant threshold, save it.
                    if (score >= variantThreshold) {
                        getOrCreateResult(type).variants.add(swatch)
                    }

                    // If score beats max score and this swatch isn't already used, update max swatch
                    if ((score > maxScore) && (!type.target.isExclusive || !usedColors.contains(swatch.rgb)))
                    {
                        // Update max swatch
                        maxSwatch = swatch
                    }
                }
            }

            // Save the max swatch?
            if (maxSwatch != null) {

                // Get non-null swatch
                val ms = maxSwatch!!

                // Set it as the primary
                getOrCreateResult(type).primary = ms

                // If it's exclusive, mark it as used
                if (type.target.isExclusive) { usedColors.add(ms.rgb) }
            }
        }

        // Done generating. No longer need used colors
        usedColors.clear()
    }

    /**
     * Gets the result if already created or creates a new one.
     */
    private fun getOrCreateResult(target: ColorTargetType) : ColorTargetResult {
        return targetResults.getOrPut(target) { ColorTargetResult(target) }
    }

    // endregion

    // region Public Functions
    /**
     * Gets all of the target results as a list without keys.
     */
    val results :List<ColorTargetResult> get() = targetResults.values.toList()

    /**
     * Gets all of the swatches generated for the {@link PaletteEx}.
     */
    val swatches : List<Swatch> get() = palette.swatches

    /**
     * Gets all color target results generated for the {@link PaletteEx}.
     */
    val targetResults = HashMap<ColorTargetType, ColorTargetResult>()
    // endregion
}