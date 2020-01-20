package com.solersoft.taskergb.tasker.palette

import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import androidx.palette.graphics.Target
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlinx.coroutines.*


class PaletteVariant(val palette: Palette, private val targetThreshold: Float)
{
    companion object {
        suspend fun generate(palette: Palette, targetThreshold: Float) : PaletteVariant =
            withContext(Dispatchers.Default) {
                // Create new variant
                val variant = PaletteVariant(palette, targetThreshold)

                // Generate
                variant.generate()

                // Return variant
                return@withContext variant
            }
    }

    private val targetSwatches = HashMap<Target, ArrayList<Swatch>>()

    private fun shouldBeScoredForTarget(swatch: Swatch, target: Target): Boolean {
        // Check whether the HSL values are within the correct ranges
        val hsl = swatch.hsl
        return hsl[1] >= target.minimumSaturation && hsl[1] <= target.maximumSaturation && hsl[2] >= target.minimumLightness && hsl[2] <= target.maximumLightness
    }

    private fun generateScore(swatch: Swatch, target: Target): Float {
        val hsl = swatch.hsl
        var saturationScore = 0f
        var luminanceScore = 0f
        var populationScore = 0f
        val maxPopulation = palette.dominantSwatch?.population ?: 1
        if (target.saturationWeight > 0) {
            saturationScore = (target.saturationWeight
                    * (1f - abs(hsl[1] - target.targetSaturation)))
        }
        if (target.lightnessWeight > 0) {
            luminanceScore = (target.lightnessWeight
                    * (1f - abs(hsl[2] - target.targetLightness)))
        }
        if (target.populationWeight > 0) {
            populationScore = (target.populationWeight
                    * (swatch.population / maxPopulation.toFloat()))
        }
        return saturationScore + luminanceScore + populationScore
    }

    /**
     * Generates a {@link PaletteVariant} from the {@link Palette}
     */
    private fun generate() {

        // Go through all targets
        palette.targets.forEach { target ->

            // For each target, go through all swatches
            palette.swatches.forEach { swatch ->

                // Is this swatch valid for the target?
                if (shouldBeScoredForTarget(swatch, target)) {

                    // Yes. Calculate a score.
                    val score = generateScore(swatch, target)

                    // If score at least meets threshold, save it.
                    if (score >= targetThreshold) {
                        getSwatches(target).add(swatch)
                    }
                }
            }
        }
    }


    fun getSwatches(target: Target) : ArrayList<Swatch> {
        if (!targetSwatches.containsKey(target)) {
            targetSwatches[target] = ArrayList<Swatch>()
        }
        return targetSwatches[target]!!
    }
}