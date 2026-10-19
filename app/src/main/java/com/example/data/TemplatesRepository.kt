package com.example.data

import com.example.model.CanvasAspectRatio
import com.example.model.ColorPalette
import com.example.model.CoverDesignData
import com.example.model.FontCategory
import com.example.model.LayoutMode
import com.example.model.TemplateCategory
import com.example.model.TemplateItem

object TemplatesRepository {

    val colorPalettes = listOf(
        ColorPalette(
            id = "sepia_editorial",
            nameEn = "Editorial Noir & Sepia",
            nameAr = "تحريري سيبيا وأسود",
            bgHex = "#FBFBF9",
            textHex = "#18181A",
            subHex = "#555555",
            badgeBgHex = "#18181A",
            badgeTextHex = "#FFFFFF",
            accentHex = "#C4773B"
        ),
        ColorPalette(
            id = "obsidian_copper",
            nameEn = "Obsidian & Copper",
            nameAr = "أوبسيديان ونحاسي",
            bgHex = "#141518",
            textHex = "#F4F4F6",
            subHex = "#A2A4AC",
            badgeBgHex = "#D37D42",
            badgeTextHex = "#121316",
            accentHex = "#E4965C"
        ),
        ColorPalette(
            id = "bauhaus_contrast",
            nameEn = "Bauhaus Classic",
            nameAr = "باوهاوس كلاسيك",
            bgHex = "#F6F3EB",
            textHex = "#101010",
            subHex = "#2E2E2E",
            badgeBgHex = "#D93829",
            badgeTextHex = "#FFFFFF",
            accentHex = "#2255A4"
        ),
        ColorPalette(
            id = "nordic_slate",
            nameEn = "Nordic Slate",
            nameAr = "سليت نورديك هادئ",
            bgHex = "#22252A",
            textHex = "#E8ECEF",
            subHex = "#929AA3",
            badgeBgHex = "#39414B",
            badgeTextHex = "#E8ECEF",
            accentHex = "#5E93AA"
        ),
        ColorPalette(
            id = "terracotta_sand",
            nameEn = "Terracotta & Sand",
            nameAr = "تيراكوتا ورملي",
            bgHex = "#F7EFE7",
            textHex = "#2D1E18",
            subHex = "#6E5044",
            badgeBgHex = "#C25E3B",
            badgeTextHex = "#FFFFFF",
            accentHex = "#8E3D23"
        ),
        ColorPalette(
            id = "pure_monochrome",
            nameEn = "Minimalist Monochrome",
            nameAr = "أبيض وأسود أدبي",
            bgHex = "#FFFFFF",
            textHex = "#050505",
            subHex = "#404040",
            badgeBgHex = "#050505",
            badgeTextHex = "#FFFFFF",
            accentHex = "#666666"
        ),
        ColorPalette(
            id = "emerald_gold",
            nameEn = "Emerald & Champagne",
            nameAr = "زمردي وذهبي فاخر",
            bgHex = "#0E1A17",
            textHex = "#F2EADB",
            subHex = "#A6B4AE",
            badgeBgHex = "#C8A96E",
            badgeTextHex = "#0E1A17",
            accentHex = "#DFC28E"
        )
    )

    val templates: List<TemplateItem> = listOf(
        // 1. EXACT INSPIRATION MODEL (The user's uploaded reference image!)
        TemplateItem(
            id = "model_be_your_self",
            title = "BE YOUR SELF (Editorial Split)",
            subtitle = "Avant-garde editorial layout with dual art frames & badge",
            category = TemplateCategory.BOOK_COVER,
            designData = CoverDesignData(
                title = "BE YOUR SELF",
                subtitle = "people will like you anyway",
                metaTags = listOf("TRAVEL", "BOOK", "MUSIC"),
                badgeText = "JIGSAW",
                authorName = "Contemporary Art Series",
                secondaryInfo = "ISSUE 01 • MONOGRAPH",
                backgroundColorHex = "#FFFFFF",
                textColorHex = "#111111",
                subtitleColorHex = "#4D4D4D",
                badgeBgColorHex = "#111111",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#C4773B",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = "art_abstract_bottom",
                aspectRatio = CanvasAspectRatio.BOOK_3_4,
                layoutMode = LayoutMode.SPLIT_EDITORIAL,
                fontCategory = FontCategory.MODERN_SANS,
                titleFontSize = 26f,
                titleLetterSpacing = 7f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = true,
                showBadge = true,
                showDivider = true,
                showBarcode = false
            )
        ),

        // 2. Minimalist Architecture Monograph
        TemplateItem(
            id = "book_swiss_architecture",
            title = "THE ARCHITECT'S VISION",
            subtitle = "Modernist structure, space and geometry",
            category = TemplateCategory.BOOK_COVER,
            designData = CoverDesignData(
                title = "GEOMETRY OF LIGHT",
                subtitle = "Monograph on modernist concrete and space",
                metaTags = listOf("DESIGN", "SPACE", "FORM"),
                badgeText = "EDITION ARCHIV",
                authorName = "Alexander Van Der Rohe",
                secondaryInfo = "ZURICH • 2026",
                backgroundColorHex = "#F7F5F0",
                textColorHex = "#0D0E10",
                subtitleColorHex = "#555A60",
                badgeBgColorHex = "#0D0E10",
                badgeTextColorHex = "#F7F5F0",
                accentColorHex = "#D35400",
                topArtDrawable = "art_abstract_bottom",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.NOVEL_2_3,
                layoutMode = LayoutMode.SWISS_GRID,
                fontCategory = FontCategory.TECHNICAL_MONO,
                titleFontSize = 28f,
                titleLetterSpacing = 4f,
                isUppercase = true,
                textAlign = "LEFT",
                showMetaTags = true,
                showBadge = true,
                showBorder = true,
                showBarcode = true
            )
        ),

        // 3. Literary Noir Novel
        TemplateItem(
            id = "book_literary_noir",
            title = "SHADOWS IN ZURICH",
            subtitle = "A compelling mystery of quiet streets",
            category = TemplateCategory.BOOK_COVER,
            designData = CoverDesignData(
                title = "SHADOWS IN ZURICH",
                subtitle = "A psychological mystery of forgotten rooms",
                metaTags = listOf("FICTION", "NOIR", "MYSTERY"),
                badgeText = "BESTSELLER",
                authorName = "Elena H. Rossi",
                secondaryInfo = "CRITICS CIRCLE AWARD WINNER",
                backgroundColorHex = "#121417",
                textColorHex = "#FFFFFF",
                subtitleColorHex = "#B3B9C2",
                badgeBgColorHex = "#D97736",
                badgeTextColorHex = "#121417",
                accentColorHex = "#D97736",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.BOOK_3_4,
                layoutMode = LayoutMode.HERO_BLEED,
                fontCategory = FontCategory.EDITORIAL_SERIF,
                titleFontSize = 30f,
                titleLetterSpacing = 5f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = true,
                showBadge = true,
                showBorder = false,
                showBarcode = true
            )
        ),

        // 4. Poetry & White Canvas
        TemplateItem(
            id = "book_poetry_minimal",
            title = "SILENCE & SOLITUDE",
            subtitle = "Collected poems and handwritten reflections",
            category = TemplateCategory.BOOK_COVER,
            designData = CoverDesignData(
                title = "SILENCE & SOLITUDE",
                subtitle = "Collected reflections on silence, time and light",
                metaTags = listOf("POETRY", "VERSE", "2026"),
                badgeText = "FIRST PRINTING",
                authorName = "Miriam K. Al-Sabah",
                secondaryInfo = "PARIS • VALEUR PRESS",
                backgroundColorHex = "#FAF8F5",
                textColorHex = "#262626",
                subtitleColorHex = "#666666",
                badgeBgColorHex = "#262626",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#9C6644",
                topArtDrawable = "art_abstract_bottom",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.BOOK_3_4,
                layoutMode = LayoutMode.MINIMAL_FRAME,
                fontCategory = FontCategory.EDITORIAL_SERIF,
                titleFontSize = 24f,
                titleLetterSpacing = 6f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = false,
                showBadge = true,
                showBorder = true,
                showBarcode = false
            )
        ),

        // 5. Magazine Masthead
        TemplateItem(
            id = "book_magazine_masthead",
            title = "ATELIER MAGAZINE",
            subtitle = "Quarterly review of art, design and typography",
            category = TemplateCategory.BOOK_COVER,
            designData = CoverDesignData(
                title = "ATELIER",
                subtitle = "THE NEW MINIMALISM • GLOBAL DESIGN PERSPECTIVES",
                metaTags = listOf("CULTURE", "STYLE", "ART"),
                badgeText = "VOL. 08 • $15",
                authorName = "Editorial Board",
                secondaryInfo = "FEATURING INTERVIEWS WITH 12 MODERN ARCHITECTS",
                backgroundColorHex = "#FFFFFF",
                textColorHex = "#0A0A0A",
                subtitleColorHex = "#3A3A3A",
                badgeBgColorHex = "#0A0A0A",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#E05A47",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.BOOK_3_4,
                layoutMode = LayoutMode.MAGAZINE_MASTHEAD,
                fontCategory = FontCategory.BOLD_DISPLAY,
                titleFontSize = 42f,
                titleLetterSpacing = 3f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = true,
                showBadge = true,
                showBorder = false,
                showBarcode = true
            )
        ),

        // 6. Social Media: Editorial Quote Post (1:1 Square)
        TemplateItem(
            id = "social_quote_square",
            title = "Aesthetic Quote Post (1:1)",
            subtitle = "Thought-provoking social media graphic with split artwork",
            category = TemplateCategory.SOCIAL_MEDIA,
            designData = CoverDesignData(
                title = "\"SIMPLICITY IS THE ULTIMATE SOPHISTICATION\"",
                subtitle = "Focus on what matters, eliminate the rest.",
                metaTags = listOf("DAILY INSPIRATION", "DESIGN MIND", "STUDIO"),
                badgeText = "@STUDIOCRAFT",
                authorName = "LEONARDO DA VINCI",
                secondaryInfo = "SAVE THIS FOR INSPIRATION",
                backgroundColorHex = "#181A1E",
                textColorHex = "#FFFFFF",
                subtitleColorHex = "#B5BAC4",
                badgeBgColorHex = "#E2A03F",
                badgeTextColorHex = "#121316",
                accentColorHex = "#E2A03F",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.SQUARE_1_1,
                layoutMode = LayoutMode.SPLIT_EDITORIAL,
                fontCategory = FontCategory.MODERN_SANS,
                titleFontSize = 20f,
                titleLetterSpacing = 3f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = true,
                showBadge = true,
                showDivider = true
            )
        ),

        // 7. Social Media: Vertical Story Event Teaser (9:16)
        TemplateItem(
            id = "social_story_event",
            title = "Exhibition Story / Reel (9:16)",
            subtitle = "High-impact vertical story for Instagram and TikTok",
            category = TemplateCategory.SOCIAL_MEDIA,
            designData = CoverDesignData(
                title = "RETROSPECTIVE 2026",
                subtitle = "Contemporary sculpture & generative print installation",
                metaTags = listOf("OCTOBER 24-30", "KUNSTHALLE", "RSVP NOW"),
                badgeText = "LINK IN BIO",
                authorName = "Curated by Studio Atelier",
                secondaryInfo = "DOORS OPEN 18:00 • ADMISSION FREE",
                backgroundColorHex = "#FFFFFF",
                textColorHex = "#111111",
                subtitleColorHex = "#555555",
                badgeBgColorHex = "#111111",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#C4773B",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = "art_abstract_bottom",
                aspectRatio = CanvasAspectRatio.STORY_9_16,
                layoutMode = LayoutMode.SPLIT_EDITORIAL,
                fontCategory = FontCategory.MODERN_SANS,
                titleFontSize = 26f,
                titleLetterSpacing = 5f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = true,
                showBadge = true,
                showDivider = true
            )
        ),

        // 8. Social Media: Album Art / Spotify Cover (1:1)
        TemplateItem(
            id = "social_album_art",
            title = "Vinyl & EP Cover (1:1)",
            subtitle = "Avant-garde album cover art with catalog badge",
            category = TemplateCategory.SOCIAL_MEDIA,
            designData = CoverDesignData(
                title = "ANALOG DRIFT",
                subtitle = "Distant echoes from modular synthesizers",
                metaTags = listOf("ELECTRONIC", "AMBIENT", "VINYL"),
                badgeText = "CAT: ATR-092",
                authorName = "KAIRO STATIC",
                secondaryInfo = "MASTERED AT 96KHZ / 24-BIT",
                backgroundColorHex = "#0F1012",
                textColorHex = "#F5F5F7",
                subtitleColorHex = "#8F939D",
                badgeBgColorHex = "#C4773B",
                badgeTextColorHex = "#0F1012",
                accentColorHex = "#C4773B",
                topArtDrawable = "art_abstract_bottom",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.SQUARE_1_1,
                layoutMode = LayoutMode.HERO_BLEED,
                fontCategory = FontCategory.TECHNICAL_MONO,
                titleFontSize = 28f,
                titleLetterSpacing = 5f,
                isUppercase = true,
                textAlign = "LEFT",
                showMetaTags = true,
                showBadge = true,
                showBarcode = true
            )
        ),

        // 9. Social Media: Portrait Banner Announcement (4:5)
        TemplateItem(
            id = "social_banner_announcement",
            title = "Editorial Feed Portrait (4:5)",
            subtitle = "Optimized 4:5 ratio for modern social media posts",
            category = TemplateCategory.SOCIAL_MEDIA,
            designData = CoverDesignData(
                title = "THE AUTUMN EDIT",
                subtitle = "A sensory study in texture, craft and warm tones",
                metaTags = listOf("NEW RELEASE", "LIMITED", "STUDIO"),
                badgeText = "DISCOVER MORE",
                authorName = "Editorial Team",
                secondaryInfo = "EXPLORE THE ARCHIVE",
                backgroundColorHex = "#F4EFEB",
                textColorHex = "#231F1C",
                subtitleColorHex = "#615750",
                badgeBgColorHex = "#C4773B",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#C4773B",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.BANNER_4_5,
                layoutMode = LayoutMode.SWISS_GRID,
                fontCategory = FontCategory.EDITORIAL_SERIF,
                titleFontSize = 26f,
                titleLetterSpacing = 4f,
                isUppercase = true,
                textAlign = "CENTER",
                showMetaTags = true,
                showBadge = true,
                showBorder = true
            )
        ),

        // 10. Business Card: Obsidian & Copper Executive (16:9 Landscape)
        TemplateItem(
            id = "card_obsidian_copper",
            title = "Obsidian & Copper Executive (16:9)",
            subtitle = "Luxury dark business card with copper geometric accents",
            category = TemplateCategory.BUSINESS_CARD,
            designData = CoverDesignData(
                title = "ALEXANDRE VANCE",
                subtitle = "Principal Architect & Managing Partner",
                metaTags = listOf("ARCHITECTURE", "INTERIORS", "ZURICH"),
                badgeText = "VANCE & CO.",
                authorName = "Alexandre Vance",
                secondaryInfo = "STUDIO VANCE INTERIORS",
                phone = "+41 44 289 1040",
                email = "a.vance@studio-vance.ch",
                website = "www.studio-vance.ch",
                company = "VANCE ARCHITECTURE GROUP",
                roleTitle = "Managing Partner",
                address = "Bahnhofstrasse 42, 8001 Zürich",
                backgroundColorHex = "#121417",
                textColorHex = "#FFFFFF",
                subtitleColorHex = "#D37D42",
                badgeBgColorHex = "#D37D42",
                badgeTextColorHex = "#121417",
                accentColorHex = "#E4965C",
                topArtDrawable = "art_card_dark",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.CARD_16_9,
                layoutMode = LayoutMode.BUSINESS_CLASSIC,
                fontCategory = FontCategory.MODERN_SANS,
                titleFontSize = 20f,
                titleLetterSpacing = 4f,
                isUppercase = true,
                textAlign = "LEFT",
                showMetaTags = false,
                showBadge = true,
                showBorder = true
            )
        ),

        // 11. Business Card: Studio Minimalist White (16:9 Landscape)
        TemplateItem(
            id = "card_minimal_studio",
            title = "Studio Atelier Minimal (16:9)",
            subtitle = "Clean Swiss typographic business card on textured stone",
            category = TemplateCategory.BUSINESS_CARD,
            designData = CoverDesignData(
                title = "ELENA ROSTOVA",
                subtitle = "Creative Direction & Brand Identity",
                metaTags = listOf("BRANDING", "DIGITAL", "STRATEGY"),
                badgeText = "STUDIO 01",
                authorName = "Elena Rostova",
                secondaryInfo = "INDEPENDENT CONSULTANCY",
                phone = "+1 (212) 849-0210",
                email = "elena@rostova.studio",
                website = "www.rostova.studio",
                company = "ROSTOVA STUDIO",
                roleTitle = "Creative Director",
                address = "New York • Berlin • Tokyo",
                backgroundColorHex = "#FAF8F5",
                textColorHex = "#111111",
                subtitleColorHex = "#555555",
                badgeBgColorHex = "#111111",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#9C6644",
                topArtDrawable = null,
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.CARD_16_9,
                layoutMode = LayoutMode.BUSINESS_CLASSIC,
                fontCategory = FontCategory.TECHNICAL_MONO,
                titleFontSize = 18f,
                titleLetterSpacing = 3f,
                isUppercase = true,
                textAlign = "LEFT",
                showMetaTags = true,
                showBadge = true,
                showBorder = true,
                showDivider = true
            )
        ),

        // 12. Business Card: Avant-Garde Vertical Identity (9:16)
        TemplateItem(
            id = "card_vertical_avantgarde",
            title = "Vertical Creative Identity (9:16)",
            subtitle = "Brutalist vertical business card with barcode stamp",
            category = TemplateCategory.BUSINESS_CARD,
            designData = CoverDesignData(
                title = "LEO MARCUS",
                subtitle = "Sound Designer & Visual Artist",
                metaTags = listOf("AUDIOVISUAL", "MODULAR", "INSTALLATION"),
                badgeText = "MEMBER ID: 8840",
                authorName = "Leo Marcus",
                secondaryInfo = "AVAILABLE WORLDWIDE",
                phone = "+33 6 40 21 89 05",
                email = "leo@leomarcus.art",
                website = "www.leomarcus.art",
                company = "MARCUS LABS",
                roleTitle = "Lead Sound Engineer",
                address = "Studio 4, Rue Oberkampf, Paris",
                backgroundColorHex = "#F4EFEA",
                textColorHex = "#0C0C0E",
                subtitleColorHex = "#4A4D53",
                badgeBgColorHex = "#0C0C0E",
                badgeTextColorHex = "#FFFFFF",
                accentColorHex = "#C4773B",
                topArtDrawable = "art_abstract_top",
                bottomArtDrawable = null,
                aspectRatio = CanvasAspectRatio.STORY_9_16,
                layoutMode = LayoutMode.BUSINESS_VERTICAL,
                fontCategory = FontCategory.BOLD_DISPLAY,
                titleFontSize = 24f,
                titleLetterSpacing = 4f,
                isUppercase = true,
                textAlign = "LEFT",
                showMetaTags = true,
                showBadge = true,
                showBarcode = true,
                showBorder = true
            )
        )
    )
}
