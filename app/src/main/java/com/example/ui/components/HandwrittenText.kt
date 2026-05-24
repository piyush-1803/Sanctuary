package com.example.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimulatedHandwritingText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    color: Color = Color.Unspecified,
    fontStyle: String = "organic" // organic, slow_cursive, classic
) {
    if (text.isEmpty()) return

    // Choose font family
    val baseFontFamily = com.example.ui.theme.getFontFamilyByStyle(fontStyle)

    // Split text into lines, then words, then characters
    val lines = text.split("\n")

    androidx.compose.foundation.layout.Column(modifier = modifier) {
        lines.forEachIndexed { lineIdx, lineText ->
            if (lineText.isEmpty()) {
                // Return an empty height to simulate a line break
                Text("", fontSize = fontSize, lineHeight = fontSize * 1.5)
            } else {
                FlowRow(
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    val words = lineText.split(" ")
                    words.forEachIndexed { wordIdx, word ->
                        FlowRow(
                            modifier = Modifier.padding(end = (fontSize.value * 0.4f).dp)
                        ) {
                            word.forEachIndexed { charIdx, char ->
                                // Generate pseudo-random baseline imperfections based on index & char
                                val seed = (lineIdx * 77 + wordIdx * 17 + charIdx * 31 + char.code).toLong()
                                val rand = Random(seed)

                                // Slight Y-baseline shift: -1.5dp to +1.5dp
                                val yShift = (rand.nextFloat() * 2.8f - 1.4f)
                                // Slight rotation slant: -4 deg to +4 deg
                                val rotation = (rand.nextFloat() * 7.0f - 3.5f)
                                // Slight scale variation: 0.95 to 1.05
                                val scale = 0.96f + (rand.nextFloat() * 0.08f)

                                Text(
                                    text = char.toString(),
                                    fontFamily = baseFontFamily,
                                    fontSize = fontSize * scale,
                                    fontStyle = if (fontStyle == "slow_cursive") FontStyle.Italic else FontStyle.Normal,
                                    fontWeight = if (fontStyle == "classic") FontWeight.Medium else FontWeight.Normal,
                                    color = color,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            translationY = yShift.dp.toPx()
                                        }
                                        .rotate(rotation)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
