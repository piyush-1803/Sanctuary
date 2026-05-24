package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PlacedSticker

@Composable
fun DraggableSticker(
    sticker: PlacedSticker,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onUpdate: (Float, Float, Float, Float) -> Unit, // xOffset, yOffset, scale, rotation
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    containerWidthPx: Int = 1000,
    containerHeightPx: Int = 1500
) {
    var localX by remember { mutableStateOf(sticker.xOffset) }
    var localY by remember { mutableStateOf(sticker.yOffset) }
    var scale by remember { mutableStateOf(sticker.scale) }
    var rotation by remember { mutableStateOf(sticker.rotation) }

    // Sync with database updates
    LaunchedEffect(sticker) {
        localX = sticker.xOffset
        localY = sticker.yOffset
        scale = sticker.scale
        rotation = sticker.rotation
    }

    Box(
        modifier = modifier
            .offset(
                x = (localX * containerWidthPx / 3f).dp, // approximate scale for conversion
                y = (localY * containerHeightPx / 3f).dp
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
            }
            .pointerInput(sticker.id) {
                detectTransformGestures { _, pan, zoom, rotate ->
                    // Correct offsets based on scale limits
                    scale = (scale * zoom).coerceIn(0.5f, 4.0f)
                    rotation += rotate

                    // Handle panning by converting coordinate space delta (pixels)
                    val deltaX = pan.x / containerWidthPx
                    val deltaY = pan.y / containerHeightPx
                    localX = (localX + deltaX).coerceIn(-0.2f, 1.2f)
                    localY = (localY + deltaY).coerceIn(-0.2f, 1.2f)

                    onUpdate(localX, localY, scale, rotation)
                }
            }
            .pointerInput(sticker.id) {
                detectTapGestures(
                    onTap = { onSelect() },
                    onDoubleTap = { onDelete() }
                )
            }
    ) {
        // Render Sticker Object
        Box(
            modifier = Modifier
                .padding(12.dp)
                .then(
                    if (isSelected) {
                        Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0x9E9FA8DA),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .background(Color(0x0E000000))
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            when (sticker.stickerType) {
                "tape" -> {
                    // Vintage translucent sticky tape decoration
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp)
                            .graphicsLayer { alpha = 0.5f }
                            .background(
                                color = Color(0xFFD2B48C), // amber/tan tape color
                                shape = RoundedCornerShape(2.dp)
                            )
                            .border(0.5.dp, Color(0x33000000))
                    )
                }
                "📎" -> {
                    Text("📎", fontSize = 32.sp)
                }
                "🌸" -> {
                    Text("🌸", fontSize = 34.sp)
                }
                "🌿" -> {
                    Text("🌿", fontSize = 34.sp)
                }
                "butterfly" -> {
                    Text("🦋", fontSize = 34.sp)
                }
                "polaroid" -> {
                    // Retro picture frame sticker
                    Surface(
                        tonalElevation = 6.dp,
                        shadowElevation = 4.dp,
                        color = Color(0xFFFFFDF5),
                        modifier = Modifier
                            .width(90.dp)
                            .height(110.dp)
                            .padding(4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(Color(0xFFE3DAC9))
                            ) {
                                Text(
                                    "✨", 
                                    modifier = Modifier.align(Alignment.Center), 
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Sanctuary", 
                                fontSize = 8.sp, 
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }
                else -> {
                    Text(sticker.stickerType, fontSize = 28.sp)
                }
            }

            // Quick Delete Button visible when sticker is actively highlighted
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-10).dp)
                        .size(18.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.Red, CircleShape)
                        .pointerInput(sticker.id) {
                            detectTapGestures(onTap = { onDelete() })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("×", color = Color.Red, fontSize = 12.sp, modifier = Modifier.offset(y = (-1).dp))
                }
            }
        }
    }
}
