package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.CoverViewModel
import com.example.ui.screens.SavedDesignsScreen
import com.example.ui.screens.StudioEditorScreen
import com.example.ui.screens.TemplateLibraryScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: CoverViewModel) {
    val activeScreen by viewModel.activeScreen.collectAsState()
    val currentDesign by viewModel.currentDesign.collectAsState()
    val savedDesigns by viewModel.savedDesigns.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = activeScreen == AppScreen.LIBRARY,
                    onClick = { viewModel.navigateTo(AppScreen.LIBRARY) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Templates") },
                    label = { Text("Templates", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_library")
                )

                NavigationBarItem(
                    selected = activeScreen == AppScreen.EDITOR,
                    onClick = { viewModel.navigateTo(AppScreen.EDITOR) },
                    icon = { Icon(Icons.Default.Palette, contentDescription = "Studio Editor") },
                    label = { Text("Studio Editor", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_editor")
                )

                NavigationBarItem(
                    selected = activeScreen == AppScreen.SAVED,
                    onClick = { viewModel.navigateTo(AppScreen.SAVED) },
                    icon = { Icon(Icons.Default.FolderSpecial, contentDescription = "My Designs") },
                    label = { Text("My Designs", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_saved")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeScreen) {
                AppScreen.LIBRARY -> {
                    TemplateLibraryScreen(
                        onSelectTemplate = { template -> viewModel.selectTemplate(template) },
                        onCreateBlank = { viewModel.createNewBlank() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                AppScreen.EDITOR -> {
                    StudioEditorScreen(
                        designData = currentDesign,
                        onUpdateDesignData = { updated -> viewModel.updateCurrentDesign(updated) },
                        onSaveProject = { viewModel.saveCurrentDesign() },
                        onBackToLibrary = { viewModel.navigateTo(AppScreen.LIBRARY) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                AppScreen.SAVED -> {
                    SavedDesignsScreen(
                        savedDesigns = savedDesigns,
                        onOpenDesign = { design -> viewModel.selectTemplate(design) },
                        onDeleteDesign = { entity -> viewModel.deleteDesign(entity) },
                        onBrowseTemplates = { viewModel.navigateTo(AppScreen.LIBRARY) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
