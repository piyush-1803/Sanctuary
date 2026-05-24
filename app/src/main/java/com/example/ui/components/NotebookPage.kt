package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ThemeMap

@Composable
fun NotebookPage(
    modifier: Modifier = Modifier,
    paperColor: Color = Color(0xFFFFFDF9),
    lineColor: Color = Color(0xFFEAE2CD),
    marginColor: Color = Color(0xFFE5BFA3),
    paperStyle: String = "ruled", // ruled, blank, grid, dots
    content: @Composable BoxScope.() -> Unit
) {
    // We stack several boxes offset by 1.dp and 2.dp to simulate paper depth. This feels like a physical page stack!
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Deepest leaf shadow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 3.dp, y = 3.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp, 24.dp, 24.dp, 10.dp))
                .background(paperColor.copy(alpha = 0.7f))
        )

        // Middle leaf shadow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 1.5.dp, y = 1.5.dp)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(8.dp, 22.dp, 22.dp, 8.dp))
                .background(paperColor.copy(alpha = 0.9f))
        )

        val backgroundBrush = if (paperColor == Color(0xFFFFFDF9)) {
            androidx.compose.ui.graphics.Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFFDF5), // Light warm parchment center
                    Color(0xFFFCF5E2), // Gentle warm gold mid-section
                    Color(0xFFF3E5C5), // Aged yellow-brown
                    Color(0xFFE0CDA5)  // Antique vignette corners
                ),
                radius = 1100f
            )
        } else {
            androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(paperColor, paperColor)
            )
        }

        // Top active page
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(6.dp, 20.dp, 20.dp, 6.dp))
                .background(brush = backgroundBrush, shape = RoundedCornerShape(6.dp, 20.dp, 20.dp, 6.dp))
                // Draw ruled lines or grid pattern behind contents
                .drawBehind {
                    val width = size.width
                    val height = size.height

                    // Extra real antique vignette shadow and blemishes for Vintage Amber
                    if (paperColor == Color(0xFFFFFDF9)) {
                        // Drawing subtle aged edges manually on top of background
                        drawRect(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(Color.Transparent, Color(0x0C8D6724), Color(0x3A4A3511)),
                                center = Offset(width / 2, height / 2),
                                radius = size.minDimension * 0.95f
                            ),
                            size = size
                        )

                        // Draw organic paper specks/stains
                        val rand = java.util.Random(42) // Stable seed to avoid recomposition flicker
                        for (i in 0..15) {
                            val dotX = rand.nextFloat() * width
                            val dotY = rand.nextFloat() * height
                            val dotRadius = 1.dp.toPx() + rand.nextFloat() * 1.5.dp.toPx()
                            val dotAlpha = 0.04f + rand.nextFloat() * 0.08f
                            drawCircle(
                                color = Color(0xFF4E3711),
                                radius = dotRadius,
                                center = Offset(dotX, dotY),
                                alpha = dotAlpha
                            )
                        }
                    }

                    when (paperStyle) {
                        "ruled" -> {
                            // Draw red vertical notebook margin on left
                            val marginX = 54.dp.toPx()
                            drawLine(
                                color = marginColor,
                                start = Offset(marginX, 0f),
                                end = Offset(marginX, height),
                                strokeWidth = 1.5.dp.toPx()
                            )

                            // Let's draw another subtle line on the right column edge
                            val spineX = 10.dp.toPx()
                            drawLine(
                                color = marginColor.copy(alpha = 0.4f),
                                start = Offset(spineX, 0f),
                                end = Offset(spineX, height),
                                strokeWidth = 1.dp.toPx()
                            )

                            // Draw soft blue horizontal lines
                            val spacing = 26.dp.toPx()
                            var y = 60.dp.toPx()
                            while (y < height) {
                                drawLine(
                                    color = lineColor,
                                    start = Offset(0f, y),
                                    end = Offset(width, y),
                                    strokeWidth = 1.dp.toPx()
                                )
                                y += spacing
                            }
                        }
                        "grid" -> {
                            val spacing = 24.dp.toPx()
                            // Horizontal lines
                            var y = 0f
                            while (y < height) {
                                drawLine(
                                    color = lineColor.copy(alpha = 0.8f),
                                    start = Offset(0f, y),
                                    end = Offset(width, y),
                                    strokeWidth = 0.8.dp.toPx()
                                )
                                y += spacing
                            }
                            // Vertical lines
                            var x = 0f
                            while (x < width) {
                                drawLine(
                                    color = lineColor.copy(alpha = 0.8f),
                                    start = Offset(x, 0f),
                                    end = Offset(x, height),
                                    strokeWidth = 0.8.dp.toPx()
                                )
                                x += spacing
                            }
                        }
                        "dots" -> {
                            val spacing = 28.dp.toPx()
                            var x = 20.dp.toPx()
                            while (x < width - 10.dp.toPx()) {
                                var y = 20.dp.toPx()
                                while (y < height - 10.dp.toPx()) {
                                    drawCircle(
                                        color = lineColor.copy(alpha = 0.9f),
                                        radius = 1.5.dp.toPx(),
                                        center = Offset(x, y)
                                    )
                                    y += spacing
                                }
                                x += spacing
                            }
                        }
                        // blank
                        else -> {
                            // Just a clean beautiful sheet
                        }
                    }
                }
                .clip(RoundedCornerShape(6.dp, 20.dp, 20.dp, 6.dp))
                .padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                content()
            }
        }
    }
}
