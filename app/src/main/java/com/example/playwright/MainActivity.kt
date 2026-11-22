package com.example.playwright

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import com.example.playwright.ui.PlaywrightApp
import com.example.playwright.ui.ProgressStore
import com.example.playwright.ui.theme.PlaywrightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = ProgressStore(applicationContext)
        setContent {
            val dark = when (store.themeMode) {
                1 -> false
                2 -> true
                else -> isSystemInDarkTheme()
            }
            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { dark },
                )
                onDispose {}
            }
            PlaywrightTheme(darkTheme = dark) {
                androidx.compose.runtime.CompositionLocalProvider(com.example.playwright.ui.LocalLang provides store.language) {
                    PlaywrightApp(store)
                }
            }
        }
    }
}
