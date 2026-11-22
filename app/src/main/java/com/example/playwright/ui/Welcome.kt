package com.example.playwright.ui

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.playwright.R
import kotlinx.coroutines.delay

private val Navy = Color(0xFF0B1437)
private val Indigo = Color(0xFF1B2A78)
private val Violet = Color(0xFF4A2A9A)
private val Blue = Color(0xFF1A73E8)
private val Gold = Color(0xFFFBBC04)

private data class LangOption(val glyph: String, val name: String, val sub: String)

private val langOptions = listOf(
    LangOption("Aa", "English", "Learn in English"),
    LangOption("அ", "தமிழ்", "Tamil"),
    LangOption("Ta", "Tanglish", "Tamil in English letters"),
    LangOption("అ", "తెలుగు", "Telugu"),
    LangOption("Te", "Tenglish", "Telugu in English letters"),
)

/**
 * Start page: logo, app name and a language picker. Shown on first launch and
 * on demand from Settings. Always uses its own dark gradient, whatever the theme.
 */
@Composable
fun WelcomeScreen(store: ProgressStore, onStart: () -> Unit) {
    // Light status/navigation bar icons on the dark gradient while this page is shown.
    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as? Activity)?.window
        val controller = window?.let { WindowCompat.getInsetsController(it, view) }
        val prevStatus = controller?.isAppearanceLightStatusBars
        val prevNav = controller?.isAppearanceLightNavigationBars
        controller?.isAppearanceLightStatusBars = false
        controller?.isAppearanceLightNavigationBars = false
        onDispose {
            prevStatus?.let { controller.isAppearanceLightStatusBars = it }
            prevNav?.let { controller.isAppearanceLightNavigationBars = it }
        }
    }

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(60); appeared = true }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, Indigo, Violet))),
    ) {
        AuroraBackground()

        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(4.dp))
            Reveal(appeared, 0) { GlowingLogo() }
            Spacer(Modifier.height(8.dp))
            Reveal(appeared, 1) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Playwright",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                    )
                    Text(
                        "ACADEMY",
                        color = Gold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 8.sp,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Reveal(appeared, 2) {
                AnimatedLangText(store.language) {
                    Text(
                        t(
                            "TypeScript + Playwright for Selenium folks, explained like you're five.",
                            "Selenium தெரிந்தவர்களுக்கான TypeScript + Playwright, எளிமையாக.",
                            "Selenium theriyum-nu irukkura ungalukku TypeScript + Playwright, simple-aa.",
                            "Selenium తెలిసిన వాళ్ళకి TypeScript + Playwright, సింపుల్ గా.",
                            "Selenium telisina vallaki TypeScript + Playwright, simple ga.",
                        ),
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 340.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Reveal(appeared, 3) {
                Column(Modifier.widthIn(max = 480.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AnimatedLangText(store.language) {
                        Text(
                            t("Choose your language", "உங்கள் மொழியைத் தேர்ந்தெடுங்கள்", "Unga language-a choose pannunga", "మీ భాషను ఎంచుకోండి", "Mee bhaashanu enchukondi"),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                        )
                    }
                    langOptions.forEachIndexed { i, opt ->
                        Reveal(appeared, 4 + i) {
                            LanguageCard(opt, selected = store.language == i) { store.chooseLanguage(i) }
                        }
                    }
                    AnimatedLangText(store.language) {
                        Text(
                            t(
                                "You can change this any time in Settings.",
                                "இதை எப்போது வேண்டுமானாலும் அமைப்புகளில் மாற்றலாம்.",
                                "Ithai eppo venaalum Settings-la maathalaam.",
                                "దీన్ని ఎప్పుడైనా సెట్టింగ్స్‌లో మార్చుకోవచ్చు.",
                                "Deenni eppudainaa Settings lo maarchukovachu.",
                            ),
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        )
                    }
                }
            }

        }
        // Start button stays pinned at the bottom so it's always reachable.
        Box(Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp), contentAlignment = Alignment.Center) {
            Reveal(appeared, 9) { StartButton(store.language, onStart) }
        }
        }
    }
}

/** Staggered fade + rise-in for each block. */
@Composable
private fun Reveal(visible: Boolean, index: Int, content: @Composable () -> Unit) {
    val progress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 520, delayMillis = index * 70, easing = FastOutSlowInEasing),
        label = "reveal",
    )
    Box(
        Modifier.graphicsLayer {
            alpha = progress
            translationY = (1f - progress) * 40.dp.toPx()
        },
    ) { content() }
}

/** Cross-fades text when the language changes. */
@Composable
private fun AnimatedLangText(lang: Int, content: @Composable () -> Unit) {
    AnimatedContent(
        targetState = lang,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "langText",
    ) { l -> CompositionLocalProvider(LocalLang provides l) { content() } }
}

/** Soft, slowly drifting colour blobs behind everything. */
@Composable
private fun AuroraBackground() {
    val inf = rememberInfiniteTransition(label = "aurora")
    val drift by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(9000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "drift")
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        fun blob(color: Color, cx: Float, cy: Float, r: Float) =
            drawCircle(Brush.radialGradient(listOf(color, Color.Transparent), center = Offset(cx, cy), radius = r), radius = r, center = Offset(cx, cy))
        blob(Blue.copy(alpha = 0.45f), w * (0.15f + 0.2f * drift), h * 0.12f, w * 0.75f)
        blob(Color(0xFFD01884).copy(alpha = 0.28f), w * (0.95f - 0.25f * drift), h * (0.45f + 0.08f * drift), w * 0.7f)
        blob(Gold.copy(alpha = 0.16f), w * (0.3f + 0.3f * drift), h * 0.92f, w * 0.65f)
    }
}

/** App icon in a glowing, gently floating circle. */
@Composable
private fun GlowingLogo() {
    val inf = rememberInfiniteTransition(label = "logo")
    val float by inf.animateFloat(-6f, 6f, infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "float")
    val pulse by inf.animateFloat(0.92f, 1.08f, infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pulse")

    Box(Modifier.size(160.dp).graphicsLayer { translationY = float.dp.toPx() }, contentAlignment = Alignment.Center) {
        // Halo
        Box(
            Modifier
                .size(160.dp)
                .scale(pulse)
                .background(Brush.radialGradient(listOf(Gold.copy(alpha = 0.35f), Blue.copy(alpha = 0.15f), Color.Transparent)), CircleShape),
        )
        // Ring
        Box(
            Modifier
                .size(124.dp)
                .border(1.5.dp, Brush.sweepGradient(listOf(Gold, Color.White.copy(alpha = 0.2f), Blue, Gold)), CircleShape),
        )
        // Icon
        Box(
            Modifier
                .size(108.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFF3B8BFF), Blue, Color(0xFF0F4FB8)))),
            contentAlignment = Alignment.Center,
        ) {
            Image(painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Playwright Academy logo", modifier = Modifier.requiredSize(170.dp))
        }
    }
}

@Composable
private fun LanguageCard(opt: LangOption, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(if (selected) Color.White.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.07f), tween(220), label = "bg")
    val borderColor by animateColorAsState(if (selected) Gold else Color.White.copy(alpha = 0.12f), tween(220), label = "border")
    val scale by animateFloatAsState(if (selected) 1.02f else 1f, tween(220), label = "scale")
    val shape = RoundedCornerShape(20.dp)

    Row(
        Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(shape)
            .background(bg)
            .border(if (selected) 1.5.dp else 1.dp, borderColor, shape)
            .clickable(remember { MutableInteractionSource() }, indication = androidx.compose.material3.ripple(color = Color.White), onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (selected) Brush.linearGradient(listOf(Gold, Color(0xFFF9AB00)))
                    else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.16f), Color.White.copy(alpha = 0.08f))),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(opt.glyph, color = if (selected) Navy else Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
            Text(opt.name, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(opt.sub, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
        }
        val checkAlpha by animateFloatAsState(if (selected) 1f else 0f, tween(200), label = "check")
        Box(
            Modifier
                .size(26.dp)
                .graphicsLayer { alpha = checkAlpha; scaleX = 0.6f + 0.4f * checkAlpha; scaleY = 0.6f + 0.4f * checkAlpha }
                .background(Gold, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Navy, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun StartButton(lang: Int, onClick: () -> Unit) {
    val shape = RoundedCornerShape(50)
    Row(
        Modifier
            .widthIn(min = 240.dp)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(Gold, Color(0xFFFFD45C))))
            .clickable(onClick = onClick)
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedLangText(lang) {
            Text(
                t("Let's start", "தொடங்குவோம்", "Start pannalaam", "మొదలుపెడదాం", "Modalupedadaam"),
                color = Navy,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.size(10.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Navy)
    }
}
