package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.model.CanvasAspectRatio
import com.example.model.CoverDesignData
import com.example.model.LayoutMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

enum class ExportFormat(val extension: String, val mimeType: String, val displayName: String) {
    PNG("png", "image/png", "PNG (Lossless & Sharp)"),
    JPG("jpg", "image/jpeg", "JPG (Compressed & Small)")
}

enum class ExportResolution(val scaleFactor: Float, val label: String, val subtitle: String) {
    STANDARD(1.0f, "Standard (1080p)", "Best for social media & messaging"),
    HIGH_DEFINITION(2.0f, "HD 2K (2160p)", "Crisp high-res screens & retina"),
    ULTRA_PRINT(3.0f, "Ultra HD (3240p)", "300 DPI print & publication ready")
}

data class ExportOptions(
    val format: ExportFormat = ExportFormat.PNG,
    val resolution: ExportResolution = ExportResolution.STANDARD,
    val quality: Int = 95 // 1 to 100 for JPG
)

object ExportUtils {

    /**
     * Renders a full bitmap of the cover design with specified scale factor.
     */
    suspend fun renderDesignBitmap(
        context: Context,
        data: CoverDesignData,
        scaleMultiplier: Float = 1.0f
    ): Bitmap = withContext(Dispatchers.Default) {
        val baseWidth = (1080 * scaleMultiplier).toInt()
        val baseHeight = when (data.aspectRatio) {
            CanvasAspectRatio.BOOK_3_4 -> (1440 * scaleMultiplier).toInt()
            CanvasAspectRatio.NOVEL_2_3 -> (1620 * scaleMultiplier).toInt()
            CanvasAspectRatio.SQUARE_1_1 -> (1080 * scaleMultiplier).toInt()
            CanvasAspectRatio.STORY_9_16 -> (1920 * scaleMultiplier).toInt()
            CanvasAspectRatio.CARD_16_9 -> (608 * scaleMultiplier).toInt()
            CanvasAspectRatio.BANNER_4_5 -> (1350 * scaleMultiplier).toInt()
        }

        val bitmap = Bitmap.createBitmap(baseWidth, baseHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Draw Background
        val bgPaint = Paint().apply {
            color = try {
                AndroidColor.parseColor(data.backgroundColorHex)
            } catch (e: Exception) {
                AndroidColor.WHITE
            }
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, baseWidth.toFloat(), baseHeight.toFloat(), bgPaint)

        // 2. Load and draw artwork if present
        val topArtResId = data.topArtDrawable?.let { name ->
            context.resources.getIdentifier(name, "drawable", context.packageName)
        } ?: 0

        val bottomArtResId = data.bottomArtDrawable?.let { name ->
            context.resources.getIdentifier(name, "drawable", context.packageName)
        } ?: 0

        // Draw based on layout mode
        when (data.layoutMode) {
            LayoutMode.SPLIT_EDITORIAL -> {
                val headerSpace = (baseHeight * 0.16f)
                val topArtTop = headerSpace
                val topArtHeight = baseHeight * 0.28f
                val bottomArtTop = topArtTop + topArtHeight + (16f * scaleMultiplier)
                val bottomArtHeight = baseHeight * 0.48f

                // Draw Top Art
                if (topArtResId != 0) {
                    val topBmp = BitmapFactory.decodeResource(context.resources, topArtResId)
                    if (topBmp != null) {
                        val src = Rect(0, 0, topBmp.width, topBmp.height)
                        val dst = RectF(baseWidth * 0.05f, topArtTop, baseWidth * 0.95f, topArtTop + topArtHeight)
                        canvas.drawBitmap(topBmp, src, dst, null)
                    }
                }

                // Draw Bottom Art
                if (bottomArtResId != 0) {
                    val btmBmp = BitmapFactory.decodeResource(context.resources, bottomArtResId)
                    if (btmBmp != null) {
                        val src = Rect(0, 0, btmBmp.width, btmBmp.height)
                        val dst = RectF(baseWidth * 0.05f, bottomArtTop, baseWidth * 0.95f, bottomArtTop + bottomArtHeight)
                        canvas.drawBitmap(btmBmp, src, dst, null)
                    }
                }
            }
            LayoutMode.HERO_BLEED -> {
                val artRes = if (topArtResId != 0) topArtResId else bottomArtResId
                if (artRes != 0) {
                    val artBmp = BitmapFactory.decodeResource(context.resources, artRes)
                    if (artBmp != null) {
                        val src = Rect(0, 0, artBmp.width, artBmp.height)
                        val dst = RectF(0f, 0f, baseWidth.toFloat(), baseHeight.toFloat())
                        canvas.drawBitmap(artBmp, src, dst, null)
                    }
                }
            }
            LayoutMode.SWISS_GRID, LayoutMode.MINIMAL_FRAME -> {
                val artRes = if (topArtResId != 0) topArtResId else bottomArtResId
                if (artRes != 0) {
                    val artBmp = BitmapFactory.decodeResource(context.resources, artRes)
                    if (artBmp != null) {
                        val src = Rect(0, 0, artBmp.width, artBmp.height)
                        val dst = RectF(baseWidth * 0.1f, baseHeight * 0.28f, baseWidth * 0.9f, baseHeight * 0.72f)
                        canvas.drawBitmap(artBmp, src, dst, null)
                    }
                }
            }
            LayoutMode.MAGAZINE_MASTHEAD -> {
                val artRes = if (topArtResId != 0) topArtResId else bottomArtResId
                if (artRes != 0) {
                    val artBmp = BitmapFactory.decodeResource(context.resources, artRes)
                    if (artBmp != null) {
                        val src = Rect(0, 0, artBmp.width, artBmp.height)
                        val dst = RectF(0f, baseHeight * 0.20f, baseWidth.toFloat(), baseHeight.toFloat())
                        canvas.drawBitmap(artBmp, src, dst, null)
                    }
                }
            }
            LayoutMode.BUSINESS_CLASSIC -> {
                if (topArtResId != 0) {
                    val artBmp = BitmapFactory.decodeResource(context.resources, topArtResId)
                    if (artBmp != null) {
                        val src = Rect(0, 0, artBmp.width, artBmp.height)
                        val dst = RectF(baseWidth * 0.55f, 0f, baseWidth.toFloat(), baseHeight.toFloat())
                        canvas.drawBitmap(artBmp, src, dst, null)
                    }
                }
            }
            LayoutMode.BUSINESS_VERTICAL -> {
                if (topArtResId != 0) {
                    val artBmp = BitmapFactory.decodeResource(context.resources, topArtResId)
                    if (artBmp != null) {
                        val src = Rect(0, 0, artBmp.width, artBmp.height)
                        val dst = RectF(0f, 0f, baseWidth.toFloat(), baseHeight * 0.45f)
                        canvas.drawBitmap(artBmp, src, dst, null)
                    }
                }
            }
        }

        // 3. Draw Typography
        val textColor = try { AndroidColor.parseColor(data.textColorHex) } catch (e: Exception) { AndroidColor.BLACK }
        val subColor = try { AndroidColor.parseColor(data.subtitleColorHex) } catch (e: Exception) { AndroidColor.GRAY }

        val titlePaint = Paint().apply {
            color = textColor
            isAntiAlias = true
            textSize = (data.titleFontSize * 2.8f * scaleMultiplier).coerceAtLeast(28f)
            letterSpacing = data.titleLetterSpacing / 10f
            textAlign = when (data.textAlign) {
                "LEFT" -> Paint.Align.LEFT
                "RIGHT" -> Paint.Align.RIGHT
                else -> Paint.Align.CENTER
            }
            isFakeBoldText = true
        }

        val subPaint = Paint().apply {
            color = subColor
            isAntiAlias = true
            textSize = (data.titleFontSize * 1.1f * scaleMultiplier).coerceAtLeast(18f)
            letterSpacing = 0.15f
            textAlign = Paint.Align.CENTER
        }

        val titleText = if (data.isUppercase) data.title.uppercase() else data.title
        val titleX = when (data.textAlign) {
            "LEFT" -> baseWidth * 0.08f
            "RIGHT" -> baseWidth * 0.92f
            else -> baseWidth / 2f
        }

        // Layout specific text drawing
        when (data.layoutMode) {
            LayoutMode.SPLIT_EDITORIAL -> {
                val titleY = baseHeight * 0.065f
                canvas.drawText(titleText, titleX, titleY, titlePaint)
                canvas.drawText(data.subtitle, baseWidth / 2f, titleY + (38f * scaleMultiplier), subPaint)

                // Meta tags
                if (data.showMetaTags && data.metaTags.isNotEmpty()) {
                    val tagText = data.metaTags.joinToString("    ") { "• $it" }
                    val tagPaint = Paint().apply {
                        color = subColor
                        isAntiAlias = true
                        textSize = 20f * scaleMultiplier
                        letterSpacing = 0.2f
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    canvas.drawText(tagText, baseWidth / 2f, baseHeight * 0.14f, tagPaint)
                }

                // Bottom badge (e.g. JIGSAW)
                if (data.showBadge && data.badgeText.isNotBlank()) {
                    val badgePaint = Paint().apply {
                        color = try { AndroidColor.parseColor(data.badgeBgColorHex) } catch (e: Exception) { AndroidColor.BLACK }
                        style = Paint.Style.FILL
                    }
                    val badgeTextPaint = Paint().apply {
                        color = try { AndroidColor.parseColor(data.badgeTextColorHex) } catch (e: Exception) { AndroidColor.WHITE }
                        isAntiAlias = true
                        textSize = 24f * scaleMultiplier
                        letterSpacing = 0.25f
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    val badgeW = 200f * scaleMultiplier
                    val badgeH = 50f * scaleMultiplier
                    val badgeLeft = (baseWidth - badgeW) / 2f
                    val badgeTop = baseHeight * 0.94f
                    val badgeRect = RectF(badgeLeft, badgeTop, badgeLeft + badgeW, badgeTop + badgeH)
                    canvas.drawRoundRect(badgeRect, 8f * scaleMultiplier, 8f * scaleMultiplier, badgePaint)
                    canvas.drawText(data.badgeText.uppercase(), baseWidth / 2f, badgeTop + (35f * scaleMultiplier), badgeTextPaint)
                }
            }
            LayoutMode.HERO_BLEED -> {
                val titleY = baseHeight * 0.15f
                canvas.drawText(titleText, titleX, titleY, titlePaint)
                canvas.drawText(data.subtitle, baseWidth / 2f, titleY + (44f * scaleMultiplier), subPaint)
                if (data.authorName.isNotBlank()) {
                    val authPaint = Paint().apply {
                        color = textColor
                        isAntiAlias = true
                        textSize = 28f * scaleMultiplier
                        letterSpacing = 0.2f
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    canvas.drawText(data.authorName.uppercase(), baseWidth / 2f, baseHeight * 0.88f, authPaint)
                }
            }
            LayoutMode.BUSINESS_CLASSIC -> {
                val nameY = baseHeight * 0.35f
                canvas.drawText(titleText, baseWidth * 0.08f, nameY, titlePaint)
                canvas.drawText(data.subtitle, baseWidth * 0.08f, nameY + (36f * scaleMultiplier), Paint(subPaint).apply { textAlign = Paint.Align.LEFT })

                val contactPaint = Paint().apply {
                    color = subColor
                    isAntiAlias = true
                    textSize = 18f * scaleMultiplier
                    letterSpacing = 0.08f
                    textAlign = Paint.Align.LEFT
                }
                val contactY = baseHeight * 0.60f
                canvas.drawText(data.phone, baseWidth * 0.08f, contactY, contactPaint)
                canvas.drawText(data.email, baseWidth * 0.08f, contactY + (30f * scaleMultiplier), contactPaint)
                canvas.drawText(data.website, baseWidth * 0.08f, contactY + (60f * scaleMultiplier), contactPaint)
            }
            else -> {
                val titleY = baseHeight * 0.14f
                canvas.drawText(titleText, titleX, titleY, titlePaint)
                canvas.drawText(data.subtitle, baseWidth / 2f, titleY + (40f * scaleMultiplier), subPaint)
            }
        }

        // Optional Border
        if (data.showBorder) {
            val strokePaint = Paint().apply {
                color = textColor
                style = Paint.Style.STROKE
                strokeWidth = 5f * scaleMultiplier
                isAntiAlias = true
            }
            canvas.drawRect(baseWidth * 0.04f, baseHeight * 0.03f, baseWidth * 0.96f, baseHeight * 0.97f, strokePaint)
        }

        bitmap
    }

    /**
     * Saves the design with custom format (PNG or JPG), resolution, and quality.
     */
    suspend fun saveWithCustomOptions(
        context: Context,
        data: CoverDesignData,
        options: ExportOptions
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val bitmap = renderDesignBitmap(context, data, options.resolution.scaleFactor)
            val ext = options.format.extension
            val mimeType = options.format.mimeType
            val filename = "CoverCraft_${System.currentTimeMillis()}.$ext"

            val compressFormat = if (options.format == ExportFormat.PNG) {
                Bitmap.CompressFormat.PNG
            } else {
                Bitmap.CompressFormat.JPEG
            }

            var outputStream: OutputStream? = null
            var imageUri: Uri? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/CoverCraft")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri != null) {
                    outputStream = resolver.openOutputStream(imageUri)
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/CoverCraft"
                val file = File(imagesDir)
                if (!file.exists()) file.mkdirs()
                val imageFile = File(imagesDir, filename)
                outputStream = FileOutputStream(imageFile)
                imageUri = Uri.fromFile(imageFile)
            }

            if (outputStream != null) {
                bitmap.compress(compressFormat, options.quality, outputStream)
                outputStream.flush()
                outputStream.close()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && imageUri != null) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }
                context.contentResolver.update(imageUri, contentValues, null, null)
            }

            Result.success("Saved as ${options.format.name} (${options.resolution.label})")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Prepares and shares design image.
     */
    suspend fun shareDesignImage(
        context: Context,
        data: CoverDesignData,
        options: ExportOptions
    ) = withContext(Dispatchers.IO) {
        try {
            val bitmap = renderDesignBitmap(context, data, options.resolution.scaleFactor)
            val cachePath = File(context.cacheDir, "images")
            if (!cachePath.exists()) cachePath.mkdirs()
            val ext = options.format.extension
            val file = File(cachePath, "shared_cover_${System.currentTimeMillis()}.$ext")
            val stream = FileOutputStream(file)

            val compressFormat = if (options.format == ExportFormat.PNG) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
            bitmap.compress(compressFormat, options.quality, stream)
            stream.flush()
            stream.close()

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = options.format.mimeType
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, data.title)
                putExtra(Intent.EXTRA_TEXT, "${data.title} - Created with CoverCraft Studio")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share Cover Design").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
