package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.JournalBook
import com.example.ui.components.AmberGlowOverlay
import com.example.ui.theme.ThemeMap
import com.example.ui.viewmodel.JournalViewModel

@Composable
fun LibraryScreen(viewModel: JournalViewModel, modifier: Modifier = Modifier) {
    val journals by viewModel.journals.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF6EE))
            .padding(horizontal = 20.dp)
    ) {
        AmberGlowOverlay(glowColor = Color(0x23FFDC8F))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Beautiful header
            Text(
                text = "My Library",
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF433E35)
            )

            Text(
                text = "Which memory shall we revisit today?",
                fontFamily = FontFamily.Cursive,
                fontSize = 18.sp,
                color = Color(0xFFD48256),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            if (journals.isEmpty()) {
                // Emotional empty book representation on a table
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .drawBehind {
                                // Draw a cartoonish wood shelf block line
                                drawLine(
                                    color = Color(0xFFCDAF95),
                                    start = Offset(0f, size.height * 0.9f),
                                    end = Offset(size.width, size.height * 0.9f),
                                    strokeWidth = 6.dp.toPx()
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📔",
                            fontSize = 62.sp,
                            modifier = Modifier.offset(y = (-15).dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Your bookshelves are quiet...",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF433E35)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Create your companion notebook to write down thoughts.",
                        fontSize = 12.sp,
                        color = Color(0xFF9E8E7D),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 30.dp)
                    )
                }
            } else {
                // Bookshelf Grid representation
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 80.dp), // keep room for fab / nav
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(journals, key = { it.id }) { journal ->
                        JournalBookItem(
                            journal = journal,
                            onSelect = { viewModel.selectJournal(journal.id) },
                            onDelete = { viewModel.deleteJournal(journal) }
                        )
                    }
                }
            }
        }

        // Floating Action Button Styled organically
        Button(
            onClick = { showCreateDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD48256)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(bottom = 86.dp, end = 4.dp)
                .height(48.dp)
                .shadow(4.dp, shape = RoundedCornerShape(20.dp))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = "Add book icon", tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "New Journal", 
                    fontFamily = FontFamily.Serif, 
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateJournalDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { title, style ->
                viewModel.createJournal(title, style)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun JournalBookItem(
    journal: JournalBook,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookColor = Color(android.graphics.Color.parseColor(journal.coverColorHex))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        horizontalAlignment = Alignment.Start
    ) {
        // Book Outer Binding Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.72f)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(4.dp, 16.dp, 16.dp, 4.dp))
                .background(bookColor, shape = RoundedCornerShape(4.dp, 16.dp, 16.dp, 4.dp))
                // Draw a beautiful leather bounding line indentation on the left side (Spine)
                .drawBehind {
                    val marginX = 14.dp.toPx()
                    drawLine(
                        color = Color(0x4D000000),
                        start = Offset(marginX, 0f),
                        end = Offset(marginX, size.height),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    drawLine(
                        color = Color(0x2BFFFFFF),
                        start = Offset(marginX + 1.dp.toPx(), 0f),
                        end = Offset(marginX + 1.dp.toPx(), size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Embellished Label Card simulating realistic physical binding labels
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .background(Color(0xE6FFFDF5), RoundedCornerShape(3.dp))
                    .border(0.5.dp, Color(0x3B433E35), RoundedCornerShape(3.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = journal.title,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35),
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Small Aesthetic Details e.g. "Sanctuary" standard stamp in gold colors
            Text(
                text = "SANCTUARY",
                fontSize = 8.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xAAFFFFFF),
                letterSpacing = 1.5.sp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Shelf item footer labels with quick delete hooks
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = journal.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(110.dp)
                )
                Text(
                    text = journal.styleType.replaceFirstChar { it.uppercase() },
                    fontSize = 11.sp,
                    color = Color(0xFF9E8E7D)
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete book option",
                    tint = Color(0xFFB0BEC5),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CreateJournalDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("vintage") }

    val styles = listOf(
        Pair("vintage", "Vintage Cream"),
        Pair("rainy", "Rainy Slate"),
        Pair("cottage", "Cottage Sage"),
        Pair("sunset", "Soft Sunset"),
        Pair("midnight", "Midnight Diary"),
        Pair("rose", "Rose Petals")
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Bind a New Journal",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35)
                )

                Text(
                    "Select a matching physical theme atmosphere.",
                    fontSize = 12.sp,
                    color = Color(0xFF9E8E7D),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Journal Title", fontFamily = FontFamily.Serif) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD48256),
                        unfocusedBorderColor = Color(0xFFE2D6C5)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Aesthetic Style:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF433E35),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable custom list representation of available bindings
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    styles.chunked(2).forEach { pairList ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            pairList.forEach { (styleKey, labelText) ->
                                val colors = when (styleKey) {
                                    "rainy" -> Color(0xFF28303C)
                                    "cottage" -> Color(0xFF708238)
                                    "sunset" -> Color(0xFFD38B70)
                                    "midnight" -> Color(0xFF1A1C30)
                                    "rose" -> Color(0xFFC86F83)
                                    else -> Color(0xFFD48256)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (selectedStyle == styleKey) Color(0xFFFAF6EE)
                                            else Color.Transparent
                                        )
                                        .border(
                                            width = if (selectedStyle == styleKey) 1.5.dp else 0.5.dp,
                                            color = if (selectedStyle == styleKey) colors else Color(0xFFE2D6C5),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedStyle = styleKey }
                                        .padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(colors, RoundedCornerShape(3.dp))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = labelText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF433E35)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF9E8E7D), fontFamily = FontFamily.Serif)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onCreate(title, selectedStyle)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD48256))
                    ) {
                        Text("Bind Book", fontFamily = FontFamily.Serif, color = Color.White)
                    }
                }
            }
        }
    }
}
