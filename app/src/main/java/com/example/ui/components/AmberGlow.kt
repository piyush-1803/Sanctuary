package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.IntSize

@Composable
fun AmberGlowOverlay(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0x3BFFDC8F)
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Overlay a radial warm glow from the top center resembling a warm reading lamp
        val centerWidth = size.width / 2f
        val topAnchor = size.height * 0.15f
        
        val radialBrush = Brush.radialGradient(
            colors = listOf(
                glowColor,
                glowColor.copy(alpha = glowColor.alpha * 0.5f),
                Color.Transparent
            ),
            center = androidx.compose.ui.geometry.Offset(centerWidth, topAnchor),
            radius = size.width * 1.1f
        )
        
        drawRect(
            brush = radialBrush,
            size = size,
            blendMode = BlendMode.SrcOver
        )
    }
}
