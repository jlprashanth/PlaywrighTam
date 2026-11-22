package com.example.playwright.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Google-style palette
val GBlue = Color(0xFF1A73E8)
val GRed = Color(0xFFEA4335)
val GYellow = Color(0xFFF9AB00)
val GGreen = Color(0xFF1E8E3E)
val GPurple = Color(0xFF9334E6)
val GTeal = Color(0xFF129EAF)
val GOrange = Color(0xFFE8710A)
val GPink = Color(0xFFD01884)

/** Status colours that stay readable in both themes (lighter shades on dark backgrounds). */
object StatusColors {
    val green @Composable get() = if (LocalIsDark.current) Color(0xFF81C995) else Color(0xFF1E8E3E)
    val red @Composable get() = if (LocalIsDark.current) Color(0xFFF28B82) else Color(0xFFD93025)
    val orange @Composable get() = if (LocalIsDark.current) Color(0xFFFCAD70) else Color(0xFFE8710A)
}
