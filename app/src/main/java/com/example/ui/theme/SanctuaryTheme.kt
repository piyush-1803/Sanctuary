package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

enum class SanctuaryTheme {
    VINTAGE_AMBER,
    RAINY_EVENING,
    COTTAGECORE,
    SOFT_SUNSET,
    MIDNIGHT_DIARY,
    ROSE_PAPER
}

data class ThemeProperties(
    val bg: Color,
    val surface: Color,
    val onSurface: Color,
    val accent: Color,
    val marginColor: Color,
    val lineColor: Color,
    val glowColor: Color,
    val isDark: Boolean,
    val name: String,
    val description: String
)

val ThemeMap = mapOf(
    SanctuaryTheme.VINTAGE_AMBER to ThemeProperties(
        bg = Color(0xFFF7F2E8),
        surface = Color(0xFFFFFDF9),
        onSurface = Color(0xFF433E35),
        accent = Color(0xFFD48256),
        marginColor = Color(0xFFE5BFA3),
        lineColor = Color(0xFFEAE2CD),
        glowColor = Color(0x3BFFDC8F),
        isDark = false,
        name = "Vintage Amber",
        description = "A warm physical notebook resting beneath flickering candle light."
    ),
    SanctuaryTheme.RAINY_EVENING to ThemeProperties(
        bg = Color(0xFF1E242B),
        surface = Color(0xFF28303C),
        onSurface = Color(0xFFECEFF1),
        accent = Color(0xFF8FBC8F),
        marginColor = Color(0xFF3E4E5E),
        lineColor = Color(0xFF37474F),
        glowColor = Color(0x2E6FA4C2),
        isDark = true,
        name = "Rainy Evening",
        description = "Deep slate notes accompanied by a cool, blue window glow."
    ),
    SanctuaryTheme.COTTAGECORE to ThemeProperties(
        bg = Color(0xFFECECE0),
        surface = Color(0xFFF4F4EB),
        onSurface = Color(0xFF3E473E),
        accent = Color(0xFF708238),
        marginColor = Color(0xFFCDD6C5),
        lineColor = Color(0xFFE1E5DC),
        glowColor = Color(0x23829F5D),
        isDark = false,
        name = "Cottagecore",
        description = "Muted sage-green accents, wild flora, and a dusty morning haze."
    ),
    SanctuaryTheme.SOFT_SUNSET to ThemeProperties(
        bg = Color(0xFFF8EBE3),
        surface = Color(0xFFFFF9F5),
        onSurface = Color(0xFF52443C),
        accent = Color(0xFFD38B70),
        marginColor = Color(0xFFEAD2C6),
        lineColor = Color(0xFFF5E3D8),
        glowColor = Color(0x3BFFBB99),
        isDark = false,
        name = "Soft Sunset",
        description = "Warm golden pigments casting long shadows onto fading pink sheets."
    ),
    SanctuaryTheme.MIDNIGHT_DIARY to ThemeProperties(
        bg = Color(0xFF121424),
        surface = Color(0xFF1A1C30),
        onSurface = Color(0xFFDFE1F3),
        accent = Color(0xFF9FA8DA),
        marginColor = Color(0xFF2E3251),
        lineColor = Color(0xFF242741),
        glowColor = Color(0x334F5BBE),
        isDark = true,
        name = "Midnight Diary",
        description = "Cosmic blue margins resting under starlight and dreams."
    ),
    SanctuaryTheme.ROSE_PAPER to ThemeProperties(
        bg = Color(0xFFF9ECF0),
        surface = Color(0xFFFFF6F8),
        onSurface = Color(0xFF4C3B3F),
        accent = Color(0xFFC86F83),
        marginColor = Color(0xFFEDCAD2),
        lineColor = Color(0xFFF8E1E6),
        glowColor = Color(0x2BC86F83),
        isDark = false,
        name = "Rose Paper",
        description = "Delicate floral dyes, sweet lilac notes, and nostalgic pressed petals."
    )
)

val LocalSanctuaryTheme = staticCompositionLocalOf { SanctuaryTheme.VINTAGE_AMBER }

@Composable
fun SanctuaryThemeProvider(
    selectedTheme: SanctuaryTheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSanctuaryTheme provides selectedTheme) {
        val props = ThemeMap[selectedTheme] ?: ThemeMap[SanctuaryTheme.VINTAGE_AMBER]!!
        
        val animatedBg by animateColorAsState(props.bg, animationSpec = tween(600), label = "bg")
        val animatedSurface by animateColorAsState(props.surface, animationSpec = tween(600), label = "surface")
        val animatedAccent by animateColorAsState(props.accent, animationSpec = tween(600), label = "accent")
        val animatedOnSurface by animateColorAsState(props.onSurface, animationSpec = tween(600), label = "onSurface")

        val colorScheme = if (props.isDark) {
            darkColorScheme(
                primary = animatedAccent,
                background = animatedBg,
                surface = animatedSurface,
                onBackground = animatedOnSurface,
                onSurface = animatedOnSurface
            )
        } else {
            lightColorScheme(
                primary = animatedAccent,
                background = animatedBg,
                surface = animatedSurface,
                onBackground = animatedOnSurface,
                onSurface = animatedOnSurface
            )
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
