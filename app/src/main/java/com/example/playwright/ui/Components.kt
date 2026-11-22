package com.example.playwright.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import com.example.playwright.ui.theme.LocalIsDark
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playwright.ui.theme.GBlue
import com.example.playwright.ui.theme.GGreen
import com.example.playwright.ui.theme.GOrange
import com.example.playwright.ui.theme.GPink
import com.example.playwright.ui.theme.GPurple
import com.example.playwright.ui.theme.GRed
import com.example.playwright.ui.theme.GTeal
import com.example.playwright.ui.theme.GYellow

val accents = listOf(GBlue, GRed, GGreen, GYellow, GPurple, GTeal, GOrange, GPink)

/** Turns `backtick` segments into inline code spans. */
@Composable
fun richText(text: String): AnnotatedString {
    val bg = MaterialTheme.colorScheme.surfaceVariant
    val fg = if (LocalIsDark.current) Color(0xFFF28B82) else Color(0xFFC5221F)
    return buildAnnotatedString {
        text.split("`").forEachIndexed { i, part ->
            if (i % 2 == 1) {
                withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = bg, color = fg, fontSize = 14.sp)) { append(part) }
            } else append(part)
        }
    }
}


private val keywords = setOf(
    "import", "from", "export", "const", "let", "var", "async", "await", "function", "return", "if", "else", "for", "of",
    "new", "class", "extends", "public", "private", "void", "static", "final", "readonly", "constructor", "this", "true",
    "false", "null", "string", "number", "boolean", "String", "int", "List", "Map", "WebDriver", "WebElement", "default",
    "try", "catch", "throw", "interface", "type",
)

private val tokenRegex = Regex("""(//[^\n]*|#[^\n]*)|("(?:\\.|[^"\\\n])*"|'(?:\\.|[^'\\\n])*'|`[^`]*`)|\b([A-Za-z_]+)\b|\b(\d[\d_]*)\b""")

@Composable
fun highlight(code: String, lang: String): AnnotatedString {
    val dark = LocalIsDark.current
    val cComment = if (dark) Color(0xFF9AA0A6) else Color(0xFF80868B)
    val cString = if (dark) Color(0xFF81C995) else Color(0xFF188038)
    val cKeyword = if (dark) Color(0xFFD7AEFB) else Color(0xFF9334E6)
    val cNumber = if (dark) Color(0xFFFCAD70) else Color(0xFFE8710A)
    val useKw = lang != "Bash"
    return buildAnnotatedString {
        var last = 0
        for (m in tokenRegex.findAll(code)) {
            if (m.range.first > last) append(code.substring(last, m.range.first))
            val text = m.value
            val color = when {
                m.groups[1] != null -> {
                    // '#' only counts as a comment in Bash
                    if (text.startsWith("#") && lang != "Bash") null else cComment
                }
                m.groups[2] != null -> cString
                m.groups[3] != null -> if (useKw && text in keywords) cKeyword else null
                else -> cNumber
            }
            if (color != null) withStyle(SpanStyle(color = color)) { append(text) } else append(text)
            last = m.range.last + 1
        }
        if (last < code.length) append(code.substring(last))
    }
}

@Composable
fun CodeBlock(title: String, code: String, lang: String, modifier: Modifier = Modifier, accent: Color = MaterialTheme.colorScheme.primary) {
    val context = LocalContext.current
    var copied by remember(code) { mutableStateOf(false) }
    val dark = LocalIsDark.current
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (dark) Color(0xFF1B1C1E) else Color(0xFFF1F3F4),
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("$title · $lang", style = MaterialTheme.typography.labelMedium, color = accent)
                TextButton(onClick = {
                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("code", code))
                    copied = true
                }) { Text(if (copied) t("Copied ✓", "நகலெடுக்கப்பட்டது ✓", "Copy aachu ✓", "కాపీ అయింది ✓", "Copy ayindi ✓") else t("Copy", "நகலெடு", "Copy", "కాపీ", "Copy"), style = MaterialTheme.typography.labelMedium) }
            }
            Text(
                text = highlight(code, lang),
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                softWrap = false,
            )
        }
    }
}

@Composable
fun SectionCard(
    heading: String,
    modifier: Modifier = Modifier,
    container: Color = MaterialTheme.colorScheme.surface,
    bordered: Boolean = true,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = container,
        border = if (bordered) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(heading, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            content()
        }
    }
}

@Composable
fun EmojiBadge(emoji: String, tint: Color, size: Int = 48, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(50), color = tint.copy(alpha = 0.16f)) {
        Row(Modifier.padding((size / 4).dp).background(Color.Transparent), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = (size / 2).sp)
        }
    }
}
