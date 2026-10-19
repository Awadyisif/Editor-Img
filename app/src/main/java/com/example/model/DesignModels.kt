package com.example.model

import java.util.UUID

enum class CanvasAspectRatio(val label: String, val labelAr: String, val ratio: Float) {
    BOOK_3_4("3:4 Book Cover", "غلاف كتاب (3:4)", 3f / 4f),
    NOVEL_2_3("2:3 Editorial / Novel", "رواية / بوستر (2:3)", 2f / 3f),
    SQUARE_1_1("1:1 Social / Album", "منشور مربع / ألبوم (1:1)", 1f),
    STORY_9_16("9:16 Story / Reel", "قصة ستوري (9:16)", 9f / 16f),
    CARD_16_9("16:9 Business Card", "بطاقة عمل أفقية (16:9)", 16f / 9f),
    BANNER_4_5("4:5 Feed Portrait", "تغذية بورتريه (4:5)", 4f / 5f)
}

enum class TemplateCategory(val titleEn: String, val titleAr: String) {
    ALL("All", "الكل"),
    BOOK_COVER("Book & Editorial", "أغلفة الكتب"),
    SOCIAL_MEDIA("Social Media", "منشورات التواصل"),
    BUSINESS_CARD("Business Cards", "بطاقات العمل")
}

enum class LayoutMode(val titleEn: String, val titleAr: String) {
    SPLIT_EDITORIAL("Editorial Split", "تقسيم تحريري فني"),
    HERO_BLEED("Hero Bleed", "صورة كاملة"),
    SWISS_GRID("Swiss Grid", "شبكة سويسرية"),
    MINIMAL_FRAME("Minimal Passe-Partout", "إطار مينيمل"),
    MAGAZINE_MASTHEAD("Magazine Masthead", "ترويسة مجلة"),
    BUSINESS_CLASSIC("Luxury Business", "بطاقة عمل فاخرة"),
    BUSINESS_VERTICAL("Vertical Identity", "هوية رأسية")
}

enum class FontCategory(val displayName: String) {
    MODERN_SANS("Modern Sans"),
    EDITORIAL_SERIF("Editorial Serif"),
    TECHNICAL_MONO("Technical Mono"),
    BOLD_DISPLAY("Bold Display"),
    CURSIVE_SCRIPT("Artistic Script")
}

enum class FilterPreset(val titleEn: String, val titleAr: String) {
    NONE("Normal", "عادي"),
    NOIR("Noir Film", "سينما كلاسيك"),
    EDITORIAL_SEPIA("Sepia Print", "طباعة سيبيا"),
    WARM_OCHRE("Warm Ochre", "دفء ترابي"),
    DUOTONE_CYBER("Duotone", "ثنائي اللون"),
    CRISP_MONO("Crisp B&W", "أبيض وأسود نقي"),
    FADED_MATTE("Faded Matte", "مات باهت"),
    VINTAGE_ART("Vintage Art", "فن قديم")
}

data class ImageAdjustmentState(
    val brightness: Float = 0f,       // -0.5f to 0.5f
    val contrast: Float = 1f,         // 0.5f to 2.0f
    val saturation: Float = 1f,       // 0f (grayscale) to 2.0f (vivid)
    val warmth: Float = 0f,           // -0.3f (cool) to 0.3f (warm)
    val scale: Float = 1f,            // 0.6f to 3.0f
    val offsetX: Float = 0f,          // -300 to 300
    val offsetY: Float = 0f,          // -300 to 300
    val rotation: Float = 0f,         // 0, 90, 180, 270
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val activeFilter: FilterPreset = FilterPreset.NONE,
    val vignette: Float = 0f          // 0f to 1f
)

data class ColorPalette(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val bgHex: String,
    val textHex: String,
    val subHex: String,
    val badgeBgHex: String,
    val badgeTextHex: String,
    val accentHex: String
)

data class CoverDesignData(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "BE YOUR SELF",
    val subtitle: String = "people will like you anyway",
    val metaTags: List<String> = listOf("TRAVEL", "BOOK", "MUSIC"),
    val badgeText: String = "JIGSAW",
    val authorName: String = "Editorial Monograph",
    val secondaryInfo: String = "VOL. 01 • 2026 EDITION",
    // Business card specifics
    val phone: String = "+1 (555) 382-9102",
    val email: String = "contact@studioatelier.design",
    val website: String = "www.studioatelier.design",
    val company: String = "STUDIO ATELIER",
    val roleTitle: String = "Principal Creative Director",
    val address: String = "Zurich • Paris • Tokyo",
    // Colors
    val backgroundColorHex: String = "#FFFFFF",
    val textColorHex: String = "#111111",
    val subtitleColorHex: String = "#4D4D4D",
    val badgeBgColorHex: String = "#111111",
    val badgeTextColorHex: String = "#FFFFFF",
    val accentColorHex: String = "#C4773B",
    // Visuals & Art
    val topArtDrawable: String? = "art_abstract_top",
    val bottomArtDrawable: String? = "art_abstract_bottom",
    val customImageUri: String? = null,
    // Adjustments for the primary/hero artwork
    val imageAdjustments: ImageAdjustmentState = ImageAdjustmentState(),
    // Secondary adjustments (for bottom frame in split layouts)
    val secondaryImageAdjustments: ImageAdjustmentState = ImageAdjustmentState(),
    // Configuration
    val aspectRatio: CanvasAspectRatio = CanvasAspectRatio.BOOK_3_4,
    val layoutMode: LayoutMode = LayoutMode.SPLIT_EDITORIAL,
    val fontCategory: FontCategory = FontCategory.MODERN_SANS,
    val titleFontSize: Float = 26f,
    val titleLetterSpacing: Float = 6f,
    val isUppercase: Boolean = true,
    val textAlign: String = "CENTER", // LEFT, CENTER, RIGHT
    val showMetaTags: Boolean = true,
    val showBadge: Boolean = true,
    val showBorder: Boolean = false,
    val showBarcode: Boolean = false,
    val showDivider: Boolean = true
)

data class TemplateItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: TemplateCategory,
    val designData: CoverDesignData,
    val thumbnailTag: String = "preview"
)
