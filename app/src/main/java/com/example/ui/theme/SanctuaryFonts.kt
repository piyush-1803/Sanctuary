package com.example.ui.theme

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

// Google Font Provider Configuration
val FontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// 1. Caveat Font Family
val CaveatFont = GoogleFont("Caveat")
val CaveatFontFamily = FontFamily(
    Font(googleFont = CaveatFont, fontProvider = FontProvider, weight = FontWeight.Normal),
    Font(googleFont = CaveatFont, fontProvider = FontProvider, weight = FontWeight.Medium),
    Font(googleFont = CaveatFont, fontProvider = FontProvider, weight = FontWeight.Bold)
)

// 2. Indie Flower Font Family
val IndieFlowerFont = GoogleFont("Indie Flower")
val IndieFlowerFontFamily = FontFamily(
    Font(googleFont = IndieFlowerFont, fontProvider = FontProvider, weight = FontWeight.Normal)
)

// 3. Satisfy Font Family
val SatisfyFont = GoogleFont("Satisfy")
val SatisfyFontFamily = FontFamily(
    Font(googleFont = SatisfyFont, fontProvider = FontProvider, weight = FontWeight.Normal)
)

/**
 * Maps a style key to its corresponding [FontFamily], supporting offline-first cursive rendering.
 */
fun getFontFamilyByStyle(styleKey: String): FontFamily {
    return when (styleKey) {
        "caveat" -> FontFamily.Cursive
        "indie_flower" -> FontFamily.Cursive
        "satisfy" -> FontFamily.Cursive
        "organic" -> FontFamily.Cursive
        "slow_cursive" -> FontFamily.Cursive
        "classic" -> FontFamily.Serif
        else -> FontFamily.SansSerif
    }
}
