package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JournalEntry
import com.example.ui.components.AmberGlowOverlay
import com.example.ui.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimelineScreen(viewModel: JournalViewModel, modifier: Modifier = Modifier) {
    val entries by viewModel.allEntries.collectAsState()
    val journals by viewModel.journals.collectAsState()

    val dateFormatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())

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

            Text(
                text = "My Memories",
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF433E35)
            )

            Text(
                text = "A chronicle of your emotional steps...",
                fontFamily = FontFamily.Cursive,
                fontSize = 18.sp,
                color = Color(0xFFD48256),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            if (entries.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("⏳", fontSize = 54.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Your timeline is waiting...",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF433E35)
                    )
                    Text(
                        "Once you write diary entries, they blossom here into a beautiful, chronological memory cascade.",
                        fontSize = 11.sp,
                        color = Color(0xFF9E8E7D),
                        modifier = Modifier.padding(horizontal = 40.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(entries, key = { it.id }) { entry ->
                        val matchingBook = journals.find { it.id == entry.journalId }
                        val bookColorHex = matchingBook?.coverColorHex ?: "#D48256"
                        val bookColor = Color(android.graphics.Color.parseColor(bookColorHex))

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, shape = RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.selectEntry(entry.id)
                                    viewModel.navigateTo(com.example.ui.viewmodel.Screen.Writing)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Simulated Date marker
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Info,
                                            contentDescription = "Date calendar",
                                            tint = Color(0xFF9E8E7D),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = dateFormatter.format(Date(entry.date)),
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF9E8E7D)
                                        )
                                    }

                                    // Emotional Badge
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = when (entry.mood) {
                                            "Peaceful" -> Color(0xFFE8F5E9)
                                            "Dreamy" -> Color(0xFFE8EAF6)
                                            "Nostalgic" -> Color(0xFFFFF3E0)
                                            "Cozy" -> Color(0xFFF1F1F1)
                                            "Melancholy" -> Color(0xFFECEFF1)
                                            else -> Color(0xFFFFF8E1)
                                        }
                                    ) {
                                        Text(
                                            text = when (entry.mood) {
                                                "Peaceful" -> "🌱 Peaceful"
                                                "Dreamy" -> "🌌 Dreamy"
                                                "Nostalgic" -> "⏳ Nostalgic"
                                                "Cozy" -> "☕ Cozy"
                                                "Melancholy" -> "🌧️ Melancholy"
                                                else -> "🌙 Calm"
                                            },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF433E35),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = entry.title,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF433E35)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Cursive handwriting preview
                                Text(
                                    text = if (entry.content.isBlank()) "An unwritten memory page resting in peace..." else entry.content,
                                    fontFamily = FontFamily.Cursive,
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF7D7260),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Book association tag
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(bookColor.copy(alpha = 0.08f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(bookColor, RoundedCornerShape(2.dp))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = matchingBook?.title ?: "Sanctuary",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF433E35)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
