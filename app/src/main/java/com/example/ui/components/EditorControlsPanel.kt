package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.TemplatesRepository
import com.example.model.CanvasAspectRatio
import com.example.model.CoverDesignData
import com.example.model.FontCategory
import com.example.model.LayoutMode
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditorControlsPanel(
    data: CoverDesignData,
    onUpdateData: (CoverDesignData) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Content" to Icons.Default.TextFields,
        "Typography" to Icons.Default.FormatSize,
        "Photo Edit" to Icons.Default.Image,
        "Colors" to Icons.Default.ColorLens,
        "Layout" to Icons.Default.DashboardCustomize
    )

    Column(modifier = modifier.fillMaxWidth()) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, (label, icon) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(label, fontSize = 11.sp, maxLines = 1) },
                    icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("editor_tab_$index")
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> ContentTab(data, onUpdateData)
                1 -> TypographyTab(data, onUpdateData)
                2 -> PhotoEditorPanel(data, onUpdateData)
                3 -> ColorsTab(data, onUpdateData)
                4 -> LayoutTab(data, onUpdateData)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContentTab(data: CoverDesignData, onUpdateData: (CoverDesignData) -> Unit) {
    var newTagText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Main Title
        OutlinedTextField(
            value = data.title,
            onValueChange = { onUpdateData(data.copy(title = it)) },
            label = { Text("Main Title (العنوان الرئيسي)") },
            modifier = Modifier.fillMaxWidth().testTag("input_main_title")
        )

        // Subtitle
        OutlinedTextField(
            value = data.subtitle,
            onValueChange = { onUpdateData(data.copy(subtitle = it)) },
            label = { Text("Subtitle (العنوان الفرعي)") },
            modifier = Modifier.fillMaxWidth().testTag("input_subtitle")
        )

        // Bottom Badge (like "JIGSAW" from user's image)
        OutlinedTextField(
            value = data.badgeText,
            onValueChange = { onUpdateData(data.copy(badgeText = it)) },
            label = { Text("Badge Slug / Brand (الشارة السفلية مثلا JIGSAW)") },
            modifier = Modifier.fillMaxWidth().testTag("input_badge")
        )

        // Meta tags editor (TRAVEL, BOOK, MUSIC)
        Column {
            Text(
                text = "META TAGS (الوسوم والتصنيفات)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                data.metaTags.forEachIndexed { index, tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "• $tag", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove tag",
                                modifier = Modifier
                                    .size(12.dp)
                                    .clickable {
                                        val updated = data.metaTags.toMutableList().also { it.removeAt(index) }
                                        onUpdateData(data.copy(metaTags = updated))
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTagText,
                    onValueChange = { newTagText = it },
                    label = { Text("Add Tag (e.g. TRAVEL)") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newTagText.isNotBlank()) {
                            val updated = data.metaTags + newTagText.trim().uppercase()
                            onUpdateData(data.copy(metaTags = updated))
                            newTagText = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add tag")
                }
            }
        }

        // Author / Creator
        OutlinedTextField(
            value = data.authorName,
            onValueChange = { onUpdateData(data.copy(authorName = it)) },
            label = { Text("Author / Creator (المؤلف أو المصمم)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Secondary Info
        OutlinedTextField(
            value = data.secondaryInfo,
            onValueChange = { onUpdateData(data.copy(secondaryInfo = it)) },
            label = { Text("Secondary Info / Issue (معلومات الإصدار)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Business Card Details if in business card layout
        if (data.layoutMode == LayoutMode.BUSINESS_CLASSIC || data.layoutMode == LayoutMode.BUSINESS_VERTICAL || data.aspectRatio == CanvasAspectRatio.CARD_16_9) {
            Text(
                text = "BUSINESS CARD DETAILS (بيانات بطاقة العمل)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            OutlinedTextField(
                value = data.company,
                onValueChange = { onUpdateData(data.copy(company = it)) },
                label = { Text("Company / Studio Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = data.phone,
                onValueChange = { onUpdateData(data.copy(phone = it)) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = data.email,
                onValueChange = { onUpdateData(data.copy(email = it)) },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = data.website,
                onValueChange = { onUpdateData(data.copy(website = it)) },
                label = { Text("Website") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TypographyTab(data: CoverDesignData, onUpdateData: (CoverDesignData) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Font Family Selection
        Text(
            text = "FONT FAMILY (نمط الخط)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FontCategory.values().forEach { fontCat ->
                FilterChip(
                    selected = data.fontCategory == fontCat,
                    onClick = { onUpdateData(data.copy(fontCategory = fontCat)) },
                    label = { Text(fontCat.displayName) }
                )
            }
        }

        // Font Size Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Font Size", fontSize = 12.sp)
                Text("${data.titleFontSize.toInt()} sp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = data.titleFontSize,
                onValueChange = { onUpdateData(data.copy(titleFontSize = it)) },
                valueRange = 16f..46f
            )
        }

        // Letter Spacing (Tracking) Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Letter Spacing (تباعد الحروف)", fontSize = 12.sp)
                Text("${data.titleLetterSpacing.toInt()} sp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = data.titleLetterSpacing,
                onValueChange = { onUpdateData(data.copy(titleLetterSpacing = it)) },
                valueRange = 0f..14f
            )
        }

        // Text Alignment & Uppercase
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { onUpdateData(data.copy(textAlign = "LEFT")) }) {
                    Icon(
                        Icons.Default.FormatAlignLeft,
                        contentDescription = "Align Left",
                        tint = if (data.textAlign == "LEFT") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onUpdateData(data.copy(textAlign = "CENTER")) }) {
                    Icon(
                        Icons.Default.FormatAlignCenter,
                        contentDescription = "Align Center",
                        tint = if (data.textAlign == "CENTER") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onUpdateData(data.copy(textAlign = "RIGHT")) }) {
                    Icon(
                        Icons.Default.FormatAlignRight,
                        contentDescription = "Align Right",
                        tint = if (data.textAlign == "RIGHT") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ALL CAPS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Switch(
                    checked = data.isUppercase,
                    onCheckedChange = { onUpdateData(data.copy(isUppercase = it)) }
                )
            }
        }
    }
}

@Composable
private fun ColorsTab(data: CoverDesignData, onUpdateData: (CoverDesignData) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "CURATED EDITORIAL PALETTES (لوحات الألوان)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Palette presets list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TemplatesRepository.colorPalettes.forEach { palette ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable {
                            onUpdateData(
                                data.copy(
                                    backgroundColorHex = palette.bgHex,
                                    textColorHex = palette.textHex,
                                    subtitleColorHex = palette.subHex,
                                    badgeBgColorHex = palette.badgeBgHex,
                                    badgeTextColorHex = palette.badgeTextHex,
                                    accentColorHex = palette.accentHex
                                )
                            )
                        }
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(palette.nameEn, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(palette.nameAr, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    // Swatches
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ColorCircle(palette.bgHex)
                        ColorCircle(palette.textHex)
                        ColorCircle(palette.badgeBgHex)
                        ColorCircle(palette.accentHex)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Swatches for Canvas Background
        Text(
            text = "CANVAS BACKGROUND COLOR",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        val bgPresets = listOf("#FFFFFF", "#FAF8F5", "#F7F5F0", "#141518", "#1E2229", "#2D1E18", "#0E1A17")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            bgPresets.forEach { hex ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(parseColorSafe(hex))
                        .border(
                            width = if (data.backgroundColorHex.equals(hex, ignoreCase = true)) 2.5.dp else 1.dp,
                            color = if (data.backgroundColorHex.equals(hex, ignoreCase = true)) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .clickable { onUpdateData(data.copy(backgroundColorHex = hex)) }
                )
            }
        }
    }
}

@Composable
fun ColorCircle(hex: String) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(parseColorSafe(hex))
            .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
    )
}

@Composable
private fun LayoutTab(data: CoverDesignData, onUpdateData: (CoverDesignData) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Aspect Ratio Selector
        Text(
            text = "CANVAS RATIO (أبعاد وتنسيق الغلاف)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CanvasAspectRatio.values().forEach { ratio ->
                FilterChip(
                    selected = data.aspectRatio == ratio,
                    onClick = { onUpdateData(data.copy(aspectRatio = ratio)) },
                    label = { Text(ratio.label) }
                )
            }
        }

        // Layout Mode
        Text(
            text = "LAYOUT STRUCTURE (الهيكل والتصميم)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            LayoutMode.values().forEach { mode ->
                FilterChip(
                    selected = data.layoutMode == mode,
                    onClick = { onUpdateData(data.copy(layoutMode = mode)) },
                    label = { Text("${mode.titleEn} • ${mode.titleAr}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Elements Toggles
        Text(
            text = "DECORATIVE ELEMENTS (العناصر التحريرية)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Meta Tags Dots (• TRAVEL • BOOK)", fontSize = 12.sp)
            Switch(
                checked = data.showMetaTags,
                onCheckedChange = { onUpdateData(data.copy(showMetaTags = it)) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Badge Slug (e.g. JIGSAW)", fontSize = 12.sp)
            Switch(
                checked = data.showBadge,
                onCheckedChange = { onUpdateData(data.copy(showBadge = it)) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Border Frame (إطار خارجي)", fontSize = 12.sp)
            Switch(
                checked = data.showBorder,
                onCheckedChange = { onUpdateData(data.copy(showBorder = it)) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Barcode Stamp (باركود)", fontSize = 12.sp)
            Switch(
                checked = data.showBarcode,
                onCheckedChange = { onUpdateData(data.copy(showBarcode = it)) }
            )
        }
    }
}
