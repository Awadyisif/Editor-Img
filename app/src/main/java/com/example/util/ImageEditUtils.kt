package com.example.util

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import com.example.model.FilterPreset
import com.example.model.ImageAdjustmentState

object ImageEditUtils {

    /**
     * Builds a Compose ColorFilter using GPU-accelerated ColorMatrix combining
     * Brightness, Contrast, Saturation, Warmth and Filter Presets.
     */
    fun createColorFilter(state: ImageAdjustmentState): ColorFilter {
        val matrix = ColorMatrix()

        // 1. Base filter presets if chosen
        when (state.activeFilter) {
            FilterPreset.NONE -> {
                matrix.reset()
            }
            FilterPreset.NOIR -> {
                // High contrast cinematic black and white
                val grayMatrix = ColorMatrix().apply { setToSaturation(0f) }
                matrix.set(grayMatrix)
                // Boost contrast and darks
                val contrastMatrix = ColorMatrix(
                    floatArrayOf(
                        1.4f, 0f, 0f, 0f, -40f,
                        0f, 1.4f, 0f, 0f, -40f,
                        0f, 0f, 1.4f, 0f, -40f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                matrix.timesAssign(contrastMatrix)
            }
            FilterPreset.EDITORIAL_SEPIA -> {
                // Classic warm sepia
                val sepiaMatrix = ColorMatrix(
                    floatArrayOf(
                        0.393f, 0.769f, 0.189f, 0f, 0f,
                        0.349f, 0.686f, 0.168f, 0f, 0f,
                        0.272f, 0.534f, 0.131f, 0f, 0f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                matrix.set(sepiaMatrix)
            }
            FilterPreset.WARM_OCHRE -> {
                // Warm terracotta and ochre earth wash
                val ochreMatrix = ColorMatrix(
                    floatArrayOf(
                        1.2f, 0.1f, 0f, 0f, 15f,
                        0.1f, 1.05f, 0f, 0f, 5f,
                        0f, 0f, 0.8f, 0f, -10f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                matrix.set(ochreMatrix)
            }
            FilterPreset.DUOTONE_CYBER -> {
                // Indigo / Amber duotone
                val duotoneMatrix = ColorMatrix(
                    floatArrayOf(
                        0.9f, 0.3f, 0.1f, 0f, 25f,
                        0.1f, 0.8f, 0.3f, 0f, 10f,
                        0.2f, 0.1f, 1.3f, 0f, 30f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                matrix.set(duotoneMatrix)
            }
            FilterPreset.CRISP_MONO -> {
                // Clean neutral grayscale
                val monoMatrix = ColorMatrix().apply { setToSaturation(0f) }
                matrix.set(monoMatrix)
            }
            FilterPreset.FADED_MATTE -> {
                // Matte lifted blacks with desaturated editorial tone
                val matteMatrix = ColorMatrix(
                    floatArrayOf(
                        0.9f, 0f, 0f, 0f, 25f,
                        0f, 0.9f, 0f, 0f, 25f,
                        0f, 0f, 0.9f, 0f, 25f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                val desatMatrix = ColorMatrix().apply { setToSaturation(0.6f) }
                matrix.set(desatMatrix)
                matrix.timesAssign(matteMatrix)
            }
            FilterPreset.VINTAGE_ART -> {
                // Rich golden age vintage print
                val vintageMatrix = ColorMatrix(
                    floatArrayOf(
                        1.15f, 0.05f, 0f, 0f, 10f,
                        0.05f, 1.0f, 0f, 0f, 8f,
                        0f, 0.05f, 0.85f, 0f, -5f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                matrix.set(vintageMatrix)
            }
        }

        // 2. Apply Custom Saturation
        if (state.saturation != 1.0f) {
            val satMatrix = ColorMatrix().apply { setToSaturation(state.saturation) }
            matrix.timesAssign(satMatrix)
        }

        // 3. Apply Custom Contrast
        if (state.contrast != 1.0f) {
            val c = state.contrast
            val translate = (1f - c) * 128f
            val contrastMatrix = ColorMatrix(
                floatArrayOf(
                    c, 0f, 0f, 0f, translate,
                    0f, c, 0f, 0f, translate,
                    0f, 0f, c, 0f, translate,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            matrix.timesAssign(contrastMatrix)
        }

        // 4. Apply Custom Brightness
        if (state.brightness != 0f) {
            val b = state.brightness * 255f
            val brightnessMatrix = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, b,
                    0f, 1f, 0f, 0f, b,
                    0f, 0f, 1f, 0f, b,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            matrix.timesAssign(brightnessMatrix)
        }

        // 5. Apply Warmth (Red boost, Blue reduction)
        if (state.warmth != 0f) {
            val w = state.warmth * 40f
            val warmthMatrix = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, w,
                    0f, 1f, 0f, 0f, w * 0.4f,
                    0f, 0f, 1f, 0f, -w,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            matrix.timesAssign(warmthMatrix)
        }

        return ColorFilter.colorMatrix(matrix)
    }
}
