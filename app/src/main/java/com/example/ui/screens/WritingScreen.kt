package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JournalEntry
import com.example.data.PlacedSticker
import com.example.ui.components.AmberGlowOverlay
import com.example.ui.components.DraggableSticker
import com.example.ui.components.NotebookPage
import com.example.ui.components.SimulatedHandwritingText
import com.example.ui.viewmodel.JournalViewModel

@Composable
fun WritingScreen(viewModel: JournalViewModel, modifier: Modifier = Modifier) {
    val activeEntry by viewModel.activeEntry.collectAsState()
    val stickers by viewModel.activeEntryStickers.collectAsState()
    val selectedStickerId by viewModel.selectedStickerId.collectAsState()

    var showStickerPanel by remember { mutableStateOf(false) }
    var isNostalgiaPreviewMode by remember { mutableStateOf(false) } // Toggles the custom simulated handwriting rendering view

    if (activeEntry == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFD48256))
        }
        return
    }

    val entry = activeEntry!!

    val currentTheme = com.example.ui.theme.LocalSanctuaryTheme.current
    val props = com.example.ui.theme.ThemeMap[currentTheme] ?: com.example.ui.theme.ThemeMap[com.example.ui.theme.SanctuaryTheme.VINTAGE_AMBER]!!
    var showSavedFeedback by remember { mutableStateOf(false) }

    // Keep track of edit text state locally to prevent keyboard lag
    var editContent by remember(entry.id) { mutableStateOf(entry.content) }
    var editTitle by remember(entry.id) { mutableStateOf(entry.title) }
    var selectedMood by remember(entry.id) { mutableStateOf(entry.mood) }
    var selectedPaperStyle by remember(entry.id) { mutableStateOf(entry.paperStyle) }
    var selectedHandwriting by remember(entry.id) { mutableStateOf(entry.handwritingStyle) }

    // Sync with database via debounce or save callback on change
    LaunchedEffect(editContent, editTitle, selectedMood, selectedPaperStyle, selectedHandwriting) {
        // Prevent blocking/hammering the database on every single keystroke by adding an 800ms debounce delay
        kotlinx.coroutines.delay(800)
        viewModel.updateActiveEntry(
            title = editTitle,
            content = editContent,
            mood = selectedMood,
            handwritingStyle = selectedHandwriting,
            paperStyle = selectedPaperStyle
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(props.bg)
            .imePadding()
    ) {
        // Soft amber lighting glow covering the canvas
        AmberGlowOverlay(glowColor = props.glowColor)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Screen Header Navigation Panel representing the binder headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        // Immediately save the latest content on back pressed
                        viewModel.updateActiveEntry(
                            title = editTitle,
                            content = editContent,
                            mood = selectedMood,
                            handwritingStyle = selectedHandwriting,
                            paperStyle = selectedPaperStyle
                        )
                        viewModel.navigateTo(com.example.ui.viewmodel.Screen.Library)
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBack, 
                        contentDescription = "Return to library",
                        tint = Color(0xFF433E35)
                    )
                }

                Text(
                    text = if (isNostalgiaPreviewMode) "Nostalgic Preview" else "Writing Area",
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35)
                )

                Row {
                    // Preview Switcher (Toggles standard typing view vs gorgeous simulated letters rendering layout)
                    IconButton(
                        onClick = { isNostalgiaPreviewMode = !isNostalgiaPreviewMode },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isNostalgiaPreviewMode) Color(0x23D48256) else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = if (isNostalgiaPreviewMode) Icons.Default.Create else Icons.Default.Star,
                            contentDescription = "Toggle handwriting view style",
                            tint = Color(0xFFD48256)
                        )
                    }

                    // Sticker Overlay expand trigger
                    IconButton(
                        onClick = { showStickerPanel = !showStickerPanel },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (showStickerPanel) Color(0x23D48256) else Color.Transparent)
                    ) {
                        Icon(
                            Icons.Default.Edit, 
                            contentDescription = "Expand sticker pack",
                            tint = Color(0xFFD48256)
                        )
                    }
                }
            }

            // Quick customization tray
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mood Selection badge
                MoodSelector(selectedMood) {
                    selectedMood = it
                    viewModel.updateActiveEntry(
                        title = editTitle,
                        content = editContent,
                        mood = it,
                        handwritingStyle = selectedHandwriting,
                        paperStyle = selectedPaperStyle
                    )
                }

                // Paper Style Selector (ruled, blank, grid, dot)
                PaperStyleSelector(selectedPaperStyle) {
                    selectedPaperStyle = it
                    viewModel.updateActiveEntry(
                        title = editTitle,
                        content = editContent,
                        mood = selectedMood,
                        handwritingStyle = selectedHandwriting,
                        paperStyle = it
                    )
                }

                // Voice Handwriting Sim Style Selector
                HandwritingStyleSelector(selectedHandwriting) {
                    selectedHandwriting = it
                    viewModel.updateActiveEntry(
                        title = editTitle,
                        content = editContent,
                        mood = selectedMood,
                        handwritingStyle = it,
                        paperStyle = selectedPaperStyle
                    )
                }
                
                // Trash option
                TextButton(
                    onClick = { viewModel.deleteActiveEntry() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFC62828))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Trash", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Discard", fontSize = 11.sp, fontFamily = FontFamily.Serif)
                }
            }

            // The main interactive paper page!
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                NotebookPage(
                    paperColor = props.surface,
                    lineColor = props.lineColor,
                    marginColor = props.marginColor,
                    paperStyle = selectedPaperStyle
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .clickable { viewModel.selectSticker(null) } // dismiss active highlight on white taps
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 58.dp, end = 12.dp, top = 16.dp, bottom = 120.dp) // shift text past margin
                        ) {
                            // Title row, styled warmly
                            TextField(
                                value = editTitle,
                                onValueChange = { editTitle = it },
                                textStyle = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = props.onSurface
                                ),
                                placeholder = {
                                    Text(
                                        "My thoughts...", 
                                        fontFamily = FontFamily.Serif, 
                                        fontSize = 24.sp, 
                                        color = props.onSurface.copy(alpha = 0.35f)
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            if (isNostalgiaPreviewMode) {
                                // GORGEOUS RENDER MODE: uses unique simulated baseline offsets & custom slants
                                SimulatedHandwritingText(
                                    text = editContent,
                                    fontSize = 18.sp,
                                    color = props.onSurface,
                                    fontStyle = selectedHandwriting,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                // EDIT WRITING INPUT: fluid, responsive Typing
                                TextField(
                                    value = editContent,
                                    onValueChange = { editContent = it },
                                    textStyle = TextStyle(
                                        fontFamily = com.example.ui.theme.getFontFamilyByStyle(selectedHandwriting),
                                        fontSize = if (selectedHandwriting == "satisfy" || selectedHandwriting == "slow_cursive") 20.sp else 18.sp,
                                        fontStyle = if (selectedHandwriting == "slow_cursive" || selectedHandwriting == "satisfy") FontStyle.Italic else FontStyle.Normal,
                                        fontWeight = if (selectedHandwriting == "classic") FontWeight.Medium else FontWeight.Normal,
                                        letterSpacing = when (selectedHandwriting) {
                                            "caveat" -> (-0.5).sp
                                            "indie_flower" -> 0.4.sp
                                            "organic" -> (-0.2).sp
                                            else -> 0.sp
                                        },
                                        lineHeight = 26.sp,
                                        color = props.onSurface
                                    ),
                                    placeholder = {
                                        Text(
                                            "Tap here to start writing inside your quiet companion. Double tap decorative sticker elements to erase them.",
                                            fontFamily = FontFamily.Cursive,
                                            fontSize = 17.sp,
                                            color = props.onSurface.copy(alpha = 0.4f)
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 350.dp)
                                )
                            }
                        }

                        // Sticker layering canvas container on top of text area
                        stickers.forEach { sticker ->
                            DraggableSticker(
                                sticker = sticker,
                                isSelected = selectedStickerId == sticker.id,
                                onSelect = { viewModel.selectSticker(sticker.id) },
                                onUpdate = { x, y, scale, rotation ->
                                    viewModel.updateStickerPosition(sticker.id, x, y, scale, rotation)
                                },
                                onDelete = { viewModel.deleteSticker(sticker.id) },
                                modifier = Modifier.wrapContentSize(),
                                containerWidthPx = 1100,
                                containerHeightPx = 1600
                            )
                        }
                    }
                }

                // Dedicated Option to explicitly Save Entry Page
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.updateActiveEntry(
                            title = editTitle,
                            content = editContent,
                            mood = selectedMood,
                            handwritingStyle = selectedHandwriting,
                            paperStyle = selectedPaperStyle
                        )
                        showSavedFeedback = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 24.dp),
                    containerColor = props.accent,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check, 
                        contentDescription = "Save Page Button", 
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Save Entry",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Dynamic temporary saved toast/banner
                androidx.compose.animation.AnimatedVisibility(
                    visible = showSavedFeedback,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(animationSpec = tween(300)),
                    exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(animationSpec = tween(300)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xE12E2A24),
                        contentColor = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✨ Saved beautifully to shelves.", fontSize = 12.sp, fontFamily = FontFamily.Serif)
                        }
                    }
                    LaunchedEffect(showSavedFeedback) {
                        if (showSavedFeedback) {
                            kotlinx.coroutines.delay(1800)
                            showSavedFeedback = false
                        }
                    }
                }
            }
        }

        // Expanded Sticker Panel Drawer sliding out from the bottom
        AnimatedVisibility(
            visible = showStickerPanel,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(400)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(400)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            Card(
                shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(4.dp)
                            .background(Color(0xFFE2D6C5), CircleShape)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Scrapbook Accessories Shelf",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF433E35)
                    )
                    Text(
                        "Tap a memory sticker to drop it on the active page. Pinch/drag to compose.",
                        fontSize = 11.sp,
                        color = Color(0xFF9E8E7D)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val stickersPreset = listOf(
                            Pair("🌸", "Cherry"),
                            Pair("🌿", "Leaf"),
                            Pair("butterfly", "Wings"),
                            Pair("tape", "Sticky Tape"),
                            Pair("📎", "Paperclip"),
                            Pair("polaroid", "Snapshot")
                        )

                        stickersPreset.forEach { (typeKey, textLabel) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.addSticker(typeKey)
                                        showStickerPanel = false
                                    }
                                    .padding(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(Color(0xFFF7F2E8), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (typeKey) {
                                        "tape" -> {
                                            Box(
                                                modifier = Modifier
                                                    .width(30.dp)
                                                    .height(10.dp)
                                                    .background(Color(0x99D2B48C), RoundedCornerShape(2.dp))
                                            )
                                        }
                                        "butterfly" -> Text("🦋", fontSize = 20.sp)
                                        "polaroid" -> Text("🖼️", fontSize = 20.sp)
                                        else -> Text(typeKey, fontSize = 22.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(textLabel, fontSize = 10.sp, color = Color(0xFF433E35))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun MoodSelector(
    selectedMood: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val moods = listOf("Calm", "Peaceful", "Dreamy", "Nostalgic", "Cozy", "Melancholy")

    Box {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = when (selectedMood) {
                "Peaceful" -> Color(0xFFE8F5E9)
                "Dreamy" -> Color(0xFFE8EAF6)
                "Nostalgic" -> Color(0xFFFFF3E0)
                "Cozy" -> Color(0xFFF1F1F1)
                "Melancholy" -> Color(0xFFECEFF1)
                else -> Color(0xFFFFF8E1) // Calm
            },
            modifier = Modifier.clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (selectedMood) {
                        "Peaceful" -> "🌱 Peaceful"
                        "Dreamy" -> "🌌 Dreamy"
                        "Nostalgic" -> "⏳ Nostalgic"
                        "Cozy" -> "☕ Cozy"
                        "Melancholy" -> "🌧️ Melancholy"
                        else -> "🌙 Calm"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35)
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown", modifier = Modifier.size(16.dp))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFFFFDF9))
        ) {
            moods.forEach { mood ->
                DropdownMenuItem(
                    text = { Text(mood, fontSize = 12.sp, fontFamily = FontFamily.Serif) },
                    onClick = {
                        onSelect(mood)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PaperStyleSelector(
    selectedStyle: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val styles = listOf("ruled", "blank", "grid", "dots")

    Box {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5EFEB),
            modifier = Modifier.clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📄 ${selectedStyle.replaceFirstChar { it.uppercase() }}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35)
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown", modifier = Modifier.size(16.dp))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFFFFDF9))
        ) {
            styles.forEach { style ->
                DropdownMenuItem(
                    text = { Text(style.replaceFirstChar { it.uppercase() }, fontSize = 12.sp, fontFamily = FontFamily.Serif) },
                    onClick = {
                        onSelect(style)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun HandwritingStyleSelector(
    selectedStyle: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val fonts = listOf(
        Pair("caveat", "Caveat Handwriting"),
        Pair("indie_flower", "Indie Flower Marker"),
        Pair("satisfy", "Satisfy Cursive"),
        Pair("organic", "Organic Flow"),
        Pair("slow_cursive", "Cursive Loop"),
        Pair("classic", "Book Serif")
    )

    Box {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFAF2E4),
            modifier = Modifier.clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✍️ ${fonts.find { it.first == selectedStyle }?.second ?: "Organic"}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35)
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown", modifier = Modifier.size(16.dp))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFFFFDF9))
        ) {
            fonts.forEach { (key, name) ->
                DropdownMenuItem(
                    text = { Text(name, fontSize = 12.sp, fontFamily = FontFamily.Serif) },
                    onClick = {
                        onSelect(key)
                        expanded = false
                    }
                )
            }
        }
    }
}
