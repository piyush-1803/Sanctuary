package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmberGlowOverlay
import com.example.ui.viewmodel.JournalViewModel

@Composable
fun OnboardingScreen(viewModel: JournalViewModel, modifier: Modifier = Modifier) {
    var nameInput by remember { mutableStateOf("") }
    var biometricEnabled by remember { mutableStateOf(true) }
    var showGoogleChooser by remember { mutableStateOf(false) }
    var isGoogleConnecting by remember { mutableStateOf(false) }

    // Floating animation for particles
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val floatOne by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "f1"
    )
    val floatTwo by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -90f,
        animationSpec = infiniteRepeatable(
            animation = tween(5500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "f2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7F2E8), Color(0xFFFFFDF9))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Overlay slow warm ambient light glow
        AmberGlowOverlay(glowColor = Color(0x28FFDC8F))

        // Rising soft particle decoration elements
        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.BottomStart)
                .offset(x = 60.dp, y = floatOne.dp)
                .alpha(0.35f)
                .background(Color(0xFFFFCC80), RoundedCornerShape(10.dp))
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-80).dp, y = floatTwo.dp)
                .alpha(0.25f)
                .background(Color(0xFFFFE0B2), RoundedCornerShape(10.dp))
        )

        // Opening Notebook Simulation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .statusBarsPadding()
                .wrapContentHeight()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp, 6.dp, 6.dp, 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Book Spine Visual Line Indicator on the left
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.12f)
                        .height(6.dp)
                        .background(Color(0xFFD48256), RoundedCornerShape(6.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "📔",
                    fontSize = 58.sp,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sanctuary",
                    fontFamily = FontFamily.Serif,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF433E35),
                    fontStyle = FontStyle.Italic
                )

                Text(
                    text = "Open your private notebook",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 18.sp,
                    color = Color(0xFFD48256),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                Text(
                    text = "A quiet, slow space for memories, thoughts, and the small details you want to keep forever.",
                    fontSize = 14.sp,
                    color = Color(0xFF7D7260),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notebook Signature style input field
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Sign your name to this book", fontFamily = FontFamily.Cursive, fontSize = 16.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD48256),
                        unfocusedBorderColor = Color(0xFFE2D6C5),
                        unfocusedLabelColor = Color(0xFF9E8E7D),
                        focusedLabelColor = Color(0xFFD48256)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Biometrics options
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFAF6EE))
                        .padding(12.dp)
                        .clickable { biometricEnabled = !biometricEnabled },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Biometrics icon",
                        tint = Color(0xFFD48256),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Secure page with Biometrics",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF433E35)
                        )
                        Text(
                            "Locks the sanctuary with your fingerprint",
                            fontSize = 11.sp,
                            color = Color(0xFF9E8E7D)
                        )
                    }
                    Switch(
                        checked = biometricEnabled,
                        onCheckedChange = { biometricEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFD48256),
                            checkedTrackColor = Color(0xFFF0DCD3)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val finalName = if (nameInput.isBlank()) "Companion" else nameInput
                        viewModel.setOnboardingCompleted(finalName, biometricEnabled)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD48256)),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        "Open My Journal",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        showGoogleChooser = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Sign in with Google",
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            color = Color(0xFF7D7260)
                        )
                    }
                }
            }
        }

        // Account Chooser Dialog
        if (showGoogleChooser) {
            AlertDialog(
                onDismissRequest = { showGoogleChooser = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
                containerColor = Color.White,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(1.dp, Color(0xFFE5DDD3), RoundedCornerShape(28.dp)),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text("G", fontWeight = FontWeight.Bold, color = Color(0xFF4285F4), fontSize = 24.sp)
                            Text("o", fontWeight = FontWeight.Bold, color = Color(0xFFEA4335), fontSize = 24.sp)
                            Text("o", fontWeight = FontWeight.Bold, color = Color(0xFFFBBC05), fontSize = 24.sp)
                            Text("g", fontWeight = FontWeight.Bold, color = Color(0xFF4285F4), fontSize = 24.sp)
                            Text("l", fontWeight = FontWeight.Bold, color = Color(0xFF34A853), fontSize = 24.sp)
                            Text("e", fontWeight = FontWeight.Bold, color = Color(0xFFEA4335), fontSize = 24.sp)
                        }
                        Text(
                            text = "Sign in to Sanctuary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF202124),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "To continue, Google will share your name, email address, and profile picture with Sanctuary.",
                            fontSize = 11.sp,
                            color = Color(0xFF5F6368),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp, start = 4.dp, end = 4.dp),
                            lineHeight = 15.sp
                        )
                    }
                },
                text = {
                    Column {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F3F4))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8F9FA))
                                .clickable {
                                    showGoogleChooser = false
                                    isGoogleConnecting = true
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFFF5722), RoundedCornerShape(18.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Y", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "You Know What",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF3C4043)
                                )
                                Text(
                                    text = "youknowwhat1803@gmail.com",
                                    fontSize = 11.sp,
                                    color = Color(0xFF5F6368)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { /* noop */ }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFE8F0FE), RoundedCornerShape(18.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Use another account",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF3C4043)
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F3F4))
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showGoogleChooser = false }) {
                        Text("Cancel", color = Color(0xFF121212))
                    }
                }
            )
        }

        if (isGoogleConnecting) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(1800)
                isGoogleConnecting = false
                viewModel.setOnboardingCompleted("You Know What", biometricEnabled, "youknowwhat1803@gmail.com")
            }

            AlertDialog(
                onDismissRequest = {},
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                confirmButton = {},
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF4285F4),
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Signing in to Google...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF3C4043)
                        )
                    }
                }
            )
        }
    }
}
