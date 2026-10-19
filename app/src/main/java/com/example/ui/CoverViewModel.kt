package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TemplatesRepository
import com.example.data.local.AppDatabase
import com.example.data.local.SavedDesignEntity
import com.example.model.CanvasAspectRatio
import com.example.model.CoverDesignData
import com.example.model.FontCategory
import com.example.model.LayoutMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    LIBRARY,
    EDITOR,
    SAVED
}

class CoverViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val dao = database.designDao()

    val savedDesigns: StateFlow<List<SavedDesignEntity>> = dao.getAllSavedDesigns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Initial design is the user's reference inspiration template!
    private val defaultDesign = TemplatesRepository.templates.first().designData
    private val _currentDesign = MutableStateFlow(defaultDesign)
    val currentDesign: StateFlow<CoverDesignData> = _currentDesign.asStateFlow()

    private val _activeScreen = MutableStateFlow(AppScreen.LIBRARY)
    val activeScreen: StateFlow<AppScreen> = _activeScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _activeScreen.value = screen
    }

    fun selectTemplate(templateData: CoverDesignData) {
        _currentDesign.value = templateData
        _activeScreen.value = AppScreen.EDITOR
    }

    fun updateCurrentDesign(newData: CoverDesignData) {
        _currentDesign.value = newData
    }

    fun createNewBlank() {
        val blank = CoverDesignData(
            title = "NEW DESIGN",
            subtitle = "Modern Minimalist Composition",
            metaTags = listOf("DESIGN", "2026", "STUDIO"),
            badgeText = "COVERCRAFT",
            authorName = "Your Name",
            secondaryInfo = "VOL. 01 • EDITION",
            backgroundColorHex = "#FFFFFF",
            textColorHex = "#111111",
            subtitleColorHex = "#555555",
            badgeBgColorHex = "#111111",
            badgeTextColorHex = "#FFFFFF",
            accentColorHex = "#C4773B",
            topArtDrawable = "art_abstract_top",
            bottomArtDrawable = null,
            aspectRatio = CanvasAspectRatio.BOOK_3_4,
            layoutMode = LayoutMode.SPLIT_EDITORIAL,
            fontCategory = FontCategory.MODERN_SANS,
            titleFontSize = 26f,
            titleLetterSpacing = 6f
        )
        _currentDesign.value = blank
        _activeScreen.value = AppScreen.EDITOR
    }

    fun saveCurrentDesign() {
        viewModelScope.launch {
            val design = _currentDesign.value
            val entity = SavedDesignEntity(
                title = design.title.ifBlank { "Untitled Design" },
                category = when (design.aspectRatio) {
                    CanvasAspectRatio.CARD_16_9 -> "Business Card"
                    CanvasAspectRatio.SQUARE_1_1, CanvasAspectRatio.STORY_9_16, CanvasAspectRatio.BANNER_4_5 -> "Social Media"
                    else -> "Book Cover"
                },
                aspectRatio = design.aspectRatio.label,
                designData = design,
                updatedAt = System.currentTimeMillis()
            )
            dao.insertDesign(entity)
        }
    }

    fun deleteDesign(entity: SavedDesignEntity) {
        viewModelScope.launch {
            dao.deleteDesign(entity)
        }
    }
}
