package com.example.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CanvasAspectRatio
import com.example.model.CoverDesignData
import com.example.model.FontCategory
import com.example.model.ImageAdjustmentState
import com.example.model.LayoutMode
import com.example.util.ImageEditUtils
import kotlin.math.roundToInt

fun parseColorSafe(hex: String, defaultColor: Color = Color.Black): Color {
    return try {
        val clean = hex.removePrefix("#")
        val colorLong = when (clean.length) {
            6 -> "FF$clean".toLong(16)
            8 -> clean.toLong(16)
            else -> return defaultColor
        }
        Color(colorLong)
    } catch (e: Exception) {
        defaultColor
    }
}

fun resolveFontFamily(category: FontCategory): FontFamily {
    return when (category) {
        FontCategory.EDITORIAL_SERIF -> FontFamily.Serif
        FontCategory.MODERN_SANS -> FontFamily.SansSerif
        FontCategory.TECHNICAL_MONO -> FontFamily.Monospace
        FontCategory.BOLD_DISPLAY -> FontFamily.SansSerif
        FontCategory.CURSIVE_SCRIPT -> FontFamily.Cursive
    }
}

@Composable
fun CoverCanvas(
    data: CoverDesignData,
    modifier: Modifier = Modifier,
    isMockupMode: Boolean = false
) {
    val bgColor = remember(data.backgroundColorHex) { parseColorSafe(data.backgroundColorHex, Color.White) }
    val textColor = remember(data.textColorHex) { parseColorSafe(data.textColorHex, Color.Black) }
    val subColor = remember(data.subtitleColorHex) { parseColorSafe(data.subtitleColorHex, Color(0xFF555555)) }
    val badgeBg = remember(data.badgeBgColorHex) { parseColorSafe(data.badgeBgColorHex, Color.Black) }
    val badgeText = remember(data.badgeTextColorHex) { parseColorSafe(data.badgeTextColorHex, Color.White) }
    val accentColor = remember(data.accentColorHex) { parseColorSafe(data.accentColorHex, Color(0xFFC4773B)) }

    val fontFamily = remember(data.fontCategory) { resolveFontFamily(data.fontCategory) }
    val fontWeight = when (data.fontCategory) {
        FontCategory.BOLD_DISPLAY -> FontWeight.Black
        FontCategory.TECHNICAL_MONO -> FontWeight.Medium
        FontCategory.EDITORIAL_SERIF -> FontWeight.SemiBold
        else -> FontWeight.Bold
    }

    Box(
        modifier = modifier
            .aspectRatio(data.aspectRatio.ratio)
            .shadow(
                elevation = if (isMockupMode) 22.dp else 10.dp,
                shape = RoundedCornerShape(if (data.aspectRatio == CanvasAspectRatio.CARD_16_9) 6.dp else 4.dp),
                spotColor = Color(0x66000000)
            )
            .clip(RoundedCornerShape(if (data.aspectRatio == CanvasAspectRatio.CARD_16_9) 6.dp else 4.dp))
            .background(bgColor)
    ) {
        // Main canvas layout based on LayoutMode
        when (data.layoutMode) {
            LayoutMode.SPLIT_EDITORIAL -> {
                // The exact inspiration layout from user's sample image:
                // Headline + Subtitle -> Meta tags dots -> Top Image -> Bottom Image -> JIGSAW badge
                EditorialSplitLayout(
                    data = data,
                    bgColor = bgColor,
                    textColor = textColor,
                    subColor = subColor,
                    badgeBg = badgeBg,
                    badgeText = badgeText,
                    accentColor = accentColor,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight
                )
            }
            LayoutMode.HERO_BLEED -> {
                HeroBleedLayout(
                    data = data,
                    textColor = textColor,
                    subColor = subColor,
                    badgeBg = badgeBg,
                    badgeText = badgeText,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight
                )
            }
            LayoutMode.SWISS_GRID, LayoutMode.MINIMAL_FRAME -> {
                SwissGridLayout(
                    data = data,
                    textColor = textColor,
                    subColor = subColor,
                    badgeBg = badgeBg,
                    badgeText = badgeText,
                    accentColor = accentColor,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight
                )
            }
            LayoutMode.MAGAZINE_MASTHEAD -> {
                MagazineMastheadLayout(
                    data = data,
                    textColor = textColor,
                    subColor = subColor,
                    badgeBg = badgeBg,
                    badgeText = badgeText,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight
                )
            }
            LayoutMode.BUSINESS_CLASSIC -> {
                BusinessClassicLayout(
                    data = data,
                    bgColor = bgColor,
                    textColor = textColor,
                    subColor = subColor,
                    accentColor = accentColor,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight
                )
            }
            LayoutMode.BUSINESS_VERTICAL -> {
                BusinessVerticalLayout(
                    data = data,
                    bgColor = bgColor,
                    textColor = textColor,
                    subColor = subColor,
                    badgeBg = badgeBg,
                    badgeText = badgeText,
                    accentColor = accentColor,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight
                )
            }
        }

        // Optional Border Frame
        if (data.showBorder) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .border(1.5.dp, textColor.copy(alpha = 0.8f))
            )
        }

        // Mockup 3D Overlay lighting effects if active
        if (isMockupMode) {
            MockupLightingOverlay(data.aspectRatio)
        }
    }
}

/**
 * Editorial Split Layout (The User's uploaded reference design model)
 */
@Composable
private fun EditorialSplitLayout(
    data: CoverDesignData,
    bgColor: Color,
    textColor: Color,
    subColor: Color,
    badgeBg: Color,
    badgeText: Color,
    accentColor: Color,
    fontFamily: FontFamily,
    fontWeight: FontWeight
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP SECTION: Title & Subtitle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val titleStr = if (data.isUppercase) data.title.uppercase() else data.title
            Text(
                text = titleStr,
                color = textColor,
                fontSize = (data.titleFontSize * 0.75f).sp,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = (data.titleLetterSpacing * 0.4f).sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = data.subtitle,
                color = subColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )
        }

        // META TAGS: • TRAVEL    • BOOK    • MUSIC
        if (data.showMetaTags && data.metaTags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                data.metaTags.forEach { tag ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(textColor)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tag.uppercase(),
                            color = textColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // TOP ARTWORK FRAME (Monochrome Abstract / Scratches)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.38f)
                .clip(RoundedCornerShape(1.dp))
                .background(Color(0xFFEEEEEE))
        ) {
            AdjustableArtwork(
                drawableName = data.topArtDrawable,
                customUri = data.customImageUri,
                adjustments = data.imageAdjustments
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // BOTTOM ARTWORK FRAME (3D Coiled Ribbon Sculpture)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.52f)
                .clip(RoundedCornerShape(1.dp))
                .background(Color(0xFFE4E4E4))
        ) {
            AdjustableArtwork(
                drawableName = data.bottomArtDrawable,
                customUri = null,
                adjustments = data.secondaryImageAdjustments
            )
        }

        // BOTTOM BADGE (e.g. JIGSAW)
        if (data.showBadge && data.badgeText.isNotBlank()) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 2.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(badgeBg)
                    .padding(horizontal = 14.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = data.badgeText.uppercase(),
                    color = badgeText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

/**
 * Hero Bleed Layout: Full-bleed artwork with cinematic typography overlay
 */
@Composable
private fun HeroBleedLayout(
    data: CoverDesignData,
    textColor: Color,
    subColor: Color,
    badgeBg: Color,
    badgeText: Color,
    fontFamily: FontFamily,
    fontWeight: FontWeight
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Full bleed background art
        AdjustableArtwork(
            drawableName = data.topArtDrawable ?: data.bottomArtDrawable,
            customUri = data.customImageUri,
            adjustments = data.imageAdjustments
        )

        // Gradient readability scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.65f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top badges & tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (data.showBadge && data.badgeText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(badgeBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = data.badgeText.uppercase(),
                            color = badgeText,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Text(
                    text = data.secondaryInfo.uppercase(),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Center / Bottom Typography
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = if (data.textAlign == "LEFT") Alignment.Start else Alignment.CenterHorizontally
            ) {
                val titleStr = if (data.isUppercase) data.title.uppercase() else data.title
                Text(
                    text = titleStr,
                    color = Color.White,
                    fontSize = (data.titleFontSize * 0.85f).sp,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    letterSpacing = (data.titleLetterSpacing * 0.4f).sp,
                    textAlign = if (data.textAlign == "LEFT") TextAlign.Start else TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = data.subtitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp,
                    fontFamily = fontFamily,
                    textAlign = if (data.textAlign == "LEFT") TextAlign.Start else TextAlign.Center
                )
                if (data.authorName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "BY " + data.authorName.uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}

/**
 * Swiss Grid Layout: Modernist structural grid with precise typographic placement
 */
@Composable
private fun SwissGridLayout(
    data: CoverDesignData,
    textColor: Color,
    subColor: Color,
    badgeBg: Color,
    badgeText: Color,
    accentColor: Color,
    fontFamily: FontFamily,
    fontWeight: FontWeight
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.secondaryInfo.uppercase(),
                color = subColor,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            if (data.showBadge && data.badgeText.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(badgeBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = data.badgeText.uppercase(),
                        color = badgeText,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Title
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            val titleStr = if (data.isUppercase) data.title.uppercase() else data.title
            Text(
                text = titleStr,
                color = textColor,
                fontSize = (data.titleFontSize * 0.8f).sp,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = (data.titleLetterSpacing * 0.35f).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = data.subtitle,
                color = subColor,
                fontSize = 9.sp,
                fontFamily = fontFamily
            )
        }

        // Inset Focal Artwork
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(2.dp))
                .border(0.5.dp, textColor.copy(alpha = 0.2f))
        ) {
            AdjustableArtwork(
                drawableName = data.topArtDrawable ?: data.bottomArtDrawable,
                customUri = data.customImageUri,
                adjustments = data.imageAdjustments
            )
        }

        // Footer Row with Author & Barcode Stamp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.authorName.uppercase(),
                color = textColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            if (data.showBarcode) {
                BarcodeVisual(color = textColor)
            }
        }
    }
}

/**
 * Magazine Masthead Layout: High-fashion magazine cover
 */
@Composable
private fun MagazineMastheadLayout(
    data: CoverDesignData,
    textColor: Color,
    subColor: Color,
    badgeBg: Color,
    badgeText: Color,
    fontFamily: FontFamily,
    fontWeight: FontWeight
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background photo / art
        AdjustableArtwork(
            drawableName = data.topArtDrawable ?: data.bottomArtDrawable,
            customUri = data.customImageUri,
            adjustments = data.imageAdjustments
        )

        // Overlay layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Big Masthead
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SPECIAL EDITION",
                        color = textColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "AUTUMN / WINTER",
                        color = textColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                val titleStr = if (data.isUppercase) data.title.uppercase() else data.title
                Text(
                    text = titleStr,
                    color = textColor,
                    fontSize = (data.titleFontSize * 1.1f).sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = fontFamily,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = data.subtitle.uppercase(),
                    color = subColor,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Bottom callouts & Barcode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    if (data.authorName.isNotBlank()) {
                        Text(
                            text = data.authorName.uppercase(),
                            color = textColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = data.secondaryInfo,
                        color = subColor,
                        fontSize = 8.sp
                    )
                }
                if (data.showBarcode) {
                    BarcodeVisual(color = textColor)
                }
            }
        }
    }
}

/**
 * Luxury Business Card Classic (16:9 Landscape)
 */
@Composable
private fun BusinessClassicLayout(
    data: CoverDesignData,
    bgColor: Color,
    textColor: Color,
    subColor: Color,
    accentColor: Color,
    fontFamily: FontFamily,
    fontWeight: FontWeight
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: Identity & Contact details
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Company Branding
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = data.company.uppercase(),
                    color = textColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }

            // Name & Title
            Column {
                val titleStr = if (data.isUppercase) data.title.uppercase() else data.title
                Text(
                    text = titleStr,
                    color = textColor,
                    fontSize = (data.titleFontSize * 0.65f).sp,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = data.subtitle,
                    color = accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Contact Info
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (data.phone.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = subColor,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = data.phone, color = subColor, fontSize = 8.sp)
                    }
                }
                if (data.email.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = subColor,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = data.email, color = subColor, fontSize = 8.sp)
                    }
                }
                if (data.website.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = subColor,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = data.website, color = subColor, fontSize = 8.sp)
                    }
                }
            }
        }

        // Right Art Accent
        if (data.topArtDrawable != null) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
            ) {
                AdjustableArtwork(
                    drawableName = data.topArtDrawable,
                    customUri = data.customImageUri,
                    adjustments = data.imageAdjustments
                )
            }
        }
    }
}

/**
 * Vertical Business Identity Card (9:16)
 */
@Composable
private fun BusinessVerticalLayout(
    data: CoverDesignData,
    bgColor: Color,
    textColor: Color,
    subColor: Color,
    badgeBg: Color,
    badgeText: Color,
    accentColor: Color,
    fontFamily: FontFamily,
    fontWeight: FontWeight
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Art Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.42f)
                .clip(RoundedCornerShape(3.dp))
        ) {
            AdjustableArtwork(
                drawableName = data.topArtDrawable ?: data.bottomArtDrawable,
                customUri = data.customImageUri,
                adjustments = data.imageAdjustments
            )
        }

        // Middle: Name & Title
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = data.company.uppercase(),
                color = accentColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            val titleStr = if (data.isUppercase) data.title.uppercase() else data.title
            Text(
                text = titleStr,
                color = textColor,
                fontSize = (data.titleFontSize * 0.7f).sp,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = 2.sp
            )
            Text(
                text = data.subtitle,
                color = subColor,
                fontSize = 9.sp,
                fontFamily = fontFamily
            )
        }

        // Bottom: Contact Details & Barcode / Badge
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = data.phone, color = textColor, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            Text(text = data.email, color = subColor, fontSize = 8.sp)
            Text(text = data.website, color = subColor, fontSize = 8.sp)
            Spacer(modifier = Modifier.height(4.dp))
            if (data.showBarcode) {
                BarcodeVisual(color = textColor)
            }
        }
    }
}

/**
 * Core image component applying real-time GPU brightness, contrast,
 * saturation, warmth, zoom, rotation, flip, and pan offset!
 */
@Composable
fun AdjustableArtwork(
    drawableName: String?,
    customUri: String?,
    adjustments: ImageAdjustmentState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colorFilter = remember(adjustments) {
        ImageEditUtils.createColorFilter(adjustments)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(0.dp))
    ) {
        if (!customUri.isNullOrEmpty()) {
            AsyncImage(
                model = Uri.parse(customUri),
                contentDescription = "Custom Artwork",
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = adjustments.scale * (if (adjustments.flipHorizontal) -1f else 1f)
                        scaleY = adjustments.scale * (if (adjustments.flipVertical) -1f else 1f)
                        translationX = adjustments.offsetX
                        translationY = adjustments.offsetY
                        rotationZ = adjustments.rotation
                    }
            )
        } else if (!drawableName.isNullOrEmpty()) {
            val resId = remember(drawableName) {
                context.resources.getIdentifier(drawableName, "drawable", context.packageName)
            }
            if (resId != 0) {
                val bitmap = remember(resId) {
                    BitmapFactory.decodeResource(context.resources, resId)
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Template Artwork",
                        contentScale = ContentScale.Crop,
                        colorFilter = colorFilter,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = adjustments.scale * (if (adjustments.flipHorizontal) -1f else 1f)
                                scaleY = adjustments.scale * (if (adjustments.flipVertical) -1f else 1f)
                                translationX = adjustments.offsetX
                                translationY = adjustments.offsetY
                                rotationZ = adjustments.rotation
                            }
                    )
                }
            }
        }
    }
}

/**
 * Clean aesthetic barcode graphic
 */
@Composable
fun BarcodeVisual(color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(20.dp)
            .width(55.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val bars = listOf(2, 1, 3, 1, 1, 2, 1, 3, 2, 1, 2, 1, 3, 1)
        bars.forEach { widthWeight ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(widthWeight.dp)
                    .background(color)
            )
        }
    }
}

/**
 * 3D Book Mockup Lighting Overlay to give realistic cover dimension & spine fold
 */
@Composable
fun MockupLightingOverlay(aspectRatio: CanvasAspectRatio) {
    // Spine shadow gradient on left edge
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    0.0f to Color.Black.copy(alpha = 0.35f),
                    0.03f to Color.White.copy(alpha = 0.15f),
                    0.06f to Color.Black.copy(alpha = 0.10f),
                    0.12f to Color.Transparent,
                    0.96f to Color.Transparent,
                    1.0f to Color.Black.copy(alpha = 0.15f)
                )
            )
    )
}
