package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CoverDesignData
import com.example.model.FilterPreset
import com.example.model.ImageAdjustmentState

@Composable
fun PhotoEditorPanel(
    data: CoverDesignData,
    onUpdateData: (CoverDesignData) -> Unit,
    modifier: Modifier = Modifier
) {
    var editTargetTop by remember { mutableStateOf(true) }
    val currentAdjustments = if (editTargetTop) data.imageAdjustments else data.secondaryImageAdjustments

    fun updateCurrentAdjustments(newAdj: ImageAdjustmentState) {
        if (editTargetTop) {
            onUpdateData(data.copy(imageAdjustments = newAdj))
        } else {
            onUpdateData(data.copy(secondaryImageAdjustments = newAdj))
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onUpdateData(data.copy(customImageUri = uri.toString()))
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        // Target selector (if dual image split)
        if (data.bottomArtDrawable != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = editTargetTop,
                    onClick = { editTargetTop = true },
                    label = { Text("Top Artwork / Hero") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = !editTargetTop,
                    onClick = { editTargetTop = false },
                    label = { Text("Bottom Artwork") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Action Buttons: Pick Custom Image & Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.testTag("pick_photo_button")
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Pick Photo", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    updateCurrentAdjustments(ImageAdjustmentState())
                },
                modifier = Modifier.testTag("reset_photo_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FILTER PRESETS SECTION
        Text(
            text = "PHOTO FILTERS (الفلاتر والتأثيرات)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterPreset.values().forEach { filter ->
                val isSelected = currentAdjustments.activeFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        updateCurrentAdjustments(currentAdjustments.copy(activeFilter = filter))
                    },
                    label = {
                        Text(
                            text = filter.titleEn,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ADJUSTMENT SLIDERS SECTION
        Text(
            text = "FINE TUNING (السطوع والتباين والتشبع)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        // 1. Brightness
        AdjustmentSlider(
            title = "Brightness (السطوع)",
            value = currentAdjustments.brightness,
            valueRange = -0.5f..0.5f,
            displayValue = "${(currentAdjustments.brightness * 200).toInt()}%",
            onValueChange = { updateCurrentAdjustments(currentAdjustments.copy(brightness = it)) }
        )

        // 2. Contrast
        AdjustmentSlider(
            title = "Contrast (التباين)",
            value = currentAdjustments.contrast,
            valueRange = 0.5f..2.0f,
            displayValue = "%.2fx".format(currentAdjustments.contrast),
            onValueChange = { updateCurrentAdjustments(currentAdjustments.copy(contrast = it)) }
        )

        // 3. Saturation
        AdjustmentSlider(
            title = "Saturation (التشبع اللوني)",
            value = currentAdjustments.saturation,
            valueRange = 0.0f..2.0f,
            displayValue = "%.1fx".format(currentAdjustments.saturation),
            onValueChange = { updateCurrentAdjustments(currentAdjustments.copy(saturation = it)) }
        )

        // 4. Warmth / Temperature
        AdjustmentSlider(
            title = "Warmth (درجة الدفء)",
            value = currentAdjustments.warmth,
            valueRange = -0.3f..0.3f,
            displayValue = "${(currentAdjustments.warmth * 300).toInt()}%",
            onValueChange = { updateCurrentAdjustments(currentAdjustments.copy(warmth = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CROP, ZOOM & ROTATION SECTION
        Text(
            text = "CROP, SCALE & ROTATE (القص والتدوير والحجم)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Scale / Zoom
        AdjustmentSlider(
            title = "Zoom / Scale (تكبير الصورة)",
            value = currentAdjustments.scale,
            valueRange = 0.6f..3.0f,
            displayValue = "%.2fx".format(currentAdjustments.scale),
            onValueChange = { updateCurrentAdjustments(currentAdjustments.copy(scale = it)) }
        )

        // Rotate & Flip buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    val nextRotation = (currentAdjustments.rotation + 90f) % 360f
                    updateCurrentAdjustments(currentAdjustments.copy(rotation = nextRotation))
                }
            ) {
                Icon(Icons.Default.CropRotate, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Rotate 90° (${currentAdjustments.rotation.toInt()}°)", fontSize = 11.sp)
            }

            OutlinedButton(
                onClick = {
                    updateCurrentAdjustments(currentAdjustments.copy(flipHorizontal = !currentAdjustments.flipHorizontal))
                }
            ) {
                Icon(Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (currentAdjustments.flipHorizontal) "Flipped H" else "Flip H", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun AdjustmentSlider(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = displayValue, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
