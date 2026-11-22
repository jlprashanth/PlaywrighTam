package com.example.playwright.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.playwright.data.Block
import com.example.playwright.data.Challenge
import com.example.playwright.data.Code
import com.example.playwright.data.H
import com.example.playwright.data.P
import com.example.playwright.data.Table
import com.example.playwright.data.Tip
import com.example.playwright.data.Warn
import com.example.playwright.ui.theme.StatusColors

@Composable
fun DeepDive(blocks: List<Block>, accent: Color) {
    SectionCard(t("🔬 Deep dive", "🔬 ஆழ்ந்த பார்வை", "🔬 Deep dive", "🔬 డీప్ డైవ్", "🔬 Deep dive")) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            blocks.forEach { b ->
                when (b) {
                    is H -> Text(b.text, style = MaterialTheme.typography.titleMedium, color = accent, modifier = Modifier.padding(top = 6.dp))
                    is P -> Text(richText(b.text), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    is Code -> CodeBlock(b.title, b.code, b.lang)
                    is Tip -> Callout("💡", b.text, StatusColors.green)
                    is Warn -> Callout("⚠️", b.text, StatusColors.orange)
                    is Table -> SimpleTable(b)
                }
            }
        }
    }
}

@Composable
private fun Callout(icon: String, text: String, color: Color) {
    Surface(shape = RoundedCornerShape(16.dp), color = color.copy(alpha = 0.12f), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(icon, style = MaterialTheme.typography.bodyLarge)
            Text(richText(text), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SimpleTable(t: Table) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                t.header.forEach { Text(it, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f)) }
            }
            t.rows.forEach { r ->
                Surface(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.fillMaxWidth().height(1.dp)) {}
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    r.forEach { Text(richText(it), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
fun ChallengeCard(c: Challenge, accent: Color) {
    var show by remember(c.prompt) { mutableStateOf(false) }
    SectionCard(t("🏋️ Practice challenge", "🏋️ பயிற்சி சவால்", "🏋️ Practice challenge", "🏋️ ప్రాక్టీస్ ఛాలెంజ్", "🏋️ Practice challenge"), container = accent.copy(alpha = 0.08f)) {
        Text(richText(c.prompt), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text(t("Try it yourself in VS Code first. Then peek.", "முதலில் VS Code-இல் நீங்களே முயலுங்கள். பிறகு பாருங்கள்.", "First VS Code-la neengale try pannunga. Appuram paarunga.", "ముందు VS Code లో మీరే try చేయండి. ఆ తర్వాత చూడండి.", "Mundu VS Code lo meere try cheyyandi. Tarvata chudandi."), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (!show) {
            Button(onClick = { show = true }) { Text(t("Show solution", "தீர்வைக் காட்டு", "Solution kaattu", "Solution చూపించు", "Solution chupinchu")) }
        } else {
            CodeBlock(t("✅ Solution", "✅ தீர்வு", "✅ Solution", "✅ పరిష్కారం", "✅ Solution"), c.solution, "TypeScript")
            TextButton(onClick = { show = false }) { Text(t("Hide", "மறை", "Hide", "దాచు", "Daachu")) }
        }
    }
}
