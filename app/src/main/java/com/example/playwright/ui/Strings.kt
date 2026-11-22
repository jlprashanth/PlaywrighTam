package com.example.playwright.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

// EXPERIMENTAL multi-language support: 0 = English, 1 = Tamil, 2 = Tanglish, 3 = Telugu, 4 = Tenglish.
val LocalLang = compositionLocalOf { 0 }

val languageNames = listOf("English", "தமிழ்", "Tanglish", "తెలుగు", "Tenglish")

/** Picks the string for the current language (3 = Telugu, 4 = Tenglish; both fall back to English). */
@Composable
fun t(en: String, ta: String, tg: String, te: String = en, tn: String = en): String = when (LocalLang.current) {
    1 -> ta
    2 -> tg
    3 -> te
    4 -> tn
    else -> en
}
