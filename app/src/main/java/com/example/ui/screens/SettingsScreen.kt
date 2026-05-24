package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmberGlowOverlay
import com.example.ui.theme.SanctuaryTheme
import com.example.ui.theme.ThemeMap
import com.example.ui.viewmodel.JournalViewModel

@Composable
fun SettingsScreen(viewModel: JournalViewModel, modifier: Modifier = Modifier) {
    val activeTheme by viewModel.activeTheme.collectAsState()
    val biometricsEnabled by viewModel.biometricsEnabled.collectAsState()
    val username by viewModel.username.collectAsState()
    val googleEmail by viewModel.googleEmail.collectAsState()

    val themesList = SanctuaryTheme.values()

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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Sanctuary Settings",
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF433E35)
            )

            Text(
                text = "Personalize your quiet writing sanctuary...",
                fontFamily = FontFamily.Cursive,
                fontSize = 18.sp,
                color = Color(0xFFD48256),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Dynamic User Profile Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .shadow(1.dp, shape = RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFFF5EFEB), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (googleEmail != null) "👤" else "📜", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (googleEmail != null) "Verified Google Owner" else "Journal Owner",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9E8E7D)
                        )
                        Text(
                            text = username.ifEmpty { "Companion" },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF433E35)
                        )
                        if (googleEmail != null) {
                            Text(
                                text = googleEmail!!,
                                fontSize = 11.sp,
                                color = Color(0xFF2E6FA4C2),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    if (googleEmail != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F0FE),
                            contentColor = Color(0xFF1976D2),
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                text = "G Account",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ambient Mood Theme",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF433E35)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable list of beautiful immersive color palette settings
            themesList.forEach { themeKey ->
                val props = ThemeMap[themeKey] ?: return@forEach
                val isSelected = activeTheme == themeKey

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFFFFFDF9) else Color.Transparent)
                        .border(
                            width = if (isSelected) 1.5.dp else 0.5.dp,
                            color = if (isSelected) Color(0xFFD48256) else Color(0x3BE2D6C5),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectTheme(themeKey) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circle Color Swatch
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(props.accent, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = props.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF433E35)
                        )
                        Text(
                            text = props.description,
                            fontSize = 11.sp,
                            color = Color(0xFF9E8E7D),
                            lineHeight = 15.sp
                        )
                    }
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Theme enabled marker",
                            tint = Color(0xFFD48256),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Privacy & Safety",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF433E35)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Security card options
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, shape = RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Biometrics toggle representation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = "Security lock shape", tint = Color(0xFFD48256))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Biometric Lock", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF433E35))
                                Text("Require fingerprint sensor to open Sanctuary", fontSize = 10.sp, color = Color(0xFF9E8E7D))
                            }
                        }
                        Switch(
                            checked = biometricsEnabled,
                            onCheckedChange = { /* handled locally in state VM triggers if desired */ },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD48256))
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0x1AD48256))

                    // Cloud sync state toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = "Cloud backup icon", tint = Color(0xFF2E6FA4C2))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Synchronize Book", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF433E35))
                                Text("Keep memories synced to private Cloud Storage", fontSize = 10.sp, color = Color(0xFF9E8E7D))
                            }
                        }
                        Text(
                            "Synced 1m ago",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8FBC8F)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // keep space for navigation padding
        }
    }
}
