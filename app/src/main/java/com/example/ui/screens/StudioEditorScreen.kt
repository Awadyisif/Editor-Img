package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CoverDesignData
import com.example.ui.components.CoverCanvas
import com.example.ui.components.EditorControlsPanel
import com.example.ui.components.ExportDialog
import com.example.util.ExportUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioEditorScreen(
    designData: CoverDesignData,
    onUpdateDesignData: (CoverDesignData) -> Unit,
    onSaveProject: (CoverDesignData) -> Unit,
    onBackToLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isMockupMode by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    if (showExportDialog) {
        ExportDialog(
            designData = designData,
            onDismiss = { showExportDialog = false }
        )
    }

    BackHandler {
        onBackToLibrary()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = designData.title.ifBlank { "Untitled Cover" },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "${designData.aspectRatio.label} • ${designData.layoutMode.titleEn}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackToLibrary) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Mockup View Toggle
                    IconButton(
                        onClick = { isMockupMode = !isMockupMode },
                        modifier = Modifier.testTag("toggle_mockup_button")
                    ) {
                        Icon(
                            Icons.Default.ViewInAr,
                            contentDescription = "Toggle 3D Mockup",
                            tint = if (isMockupMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Save Project to Room
                    IconButton(
                        onClick = {
                            onSaveProject(designData)
                            Toast.makeText(context, "Project saved to My Designs", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("save_project_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save Project")
                    }

                    // Export / Download Options (PNG, JPG, Resolution, Quality)
                    IconButton(
                        onClick = { showExportDialog = true },
                        modifier = Modifier.testTag("export_gallery_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Export Image")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // TOP HALF: Canvas Preview Area (Centered, spacious, with mockup or clean view)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.48f)
                    .background(
                        if (isMockupMode) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                CoverCanvas(
                    data = designData,
                    modifier = Modifier.fillMaxSize(0.95f),
                    isMockupMode = isMockupMode
                )

                if (isMockupMode) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "3D MOCKUP PREVIEW",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // BOTTOM HALF: Comprehensive Controls Sheet (Content, Typography, Photo Edit, Colors, Layout)
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.52f)
            ) {
                EditorControlsPanel(
                    data = designData,
                    onUpdateData = onUpdateDesignData,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
