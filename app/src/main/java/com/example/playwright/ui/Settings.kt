package com.example.playwright.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.example.playwright.data.allLessons

@Composable
fun SettingsScreen(store: ProgressStore, onBack: () -> Unit) {
    var confirm by remember { mutableStateOf(false) }
    val red = Color(0xFFD93025)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.statusBarsPadding().padding(horizontal = 4.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, t("Back", "பின்செல்", "Back", "వెనక్కి", "Venakki")) }
                Text(t("Settings", "அமைப்புகள்", "Settings", "సెట్టింగ్స్", "Settings"), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            }
        },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionCard(t("🎨 Appearance", "🎨 தோற்றம்", "🎨 Appearance", "🎨 రూపం", "🎨 Roopam")) {
                Text(t("Choose how the app looks.", "ஆப் எப்படி தெரிய வேண்டும் என்பதைத் தேர்வு செய்யுங்கள்.", "App eppadi theriyanum-nu choose pannunga.", "యాప్ ఎలా కనిపించాలో ఎంచుకోండి.", "App ela kanipinchaalo enchukondi."), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(t("System", "சிஸ்டம்", "System", "సిస్టమ్", "System"), t("Light", "வெளிச்சம்", "Light", "లైట్", "Light"), t("Dark", "இருட்டு", "Dark", "డార్క్", "Dark")).forEachIndexed { i, label ->
                        FilterChip(
                            selected = store.themeMode == i,
                            onClick = { store.setTheme(i) },
                            label = { Text(label) },
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
            }

            SectionCard(t("🌐 Language", "🌐 மொழி", "🌐 Language", "🌐 భాష", "🌐 Bhasha")) {
                Text(
                    t(
                        "Everything is translated, including deep dives and quizzes. Code stays in English.",
                        "எல்லாமே மொழிபெயர்க்கப்பட்டுள்ளது: ஆழ்ந்த பார்வை, வினாக்கள் உட்பட. Code ஆங்கிலத்திலேயே இருக்கும்.",
                        "Ellaamae translate aagirukku: deep dive, quiz ellaam serthu. Code English-la dhaan irukkum.",
                        "అంతా తెలుగులోకి మార్చాం: deep dives, quizzes తో సహా. Code ఇంగ్లీష్‌లోనే ఉంటుంది.",
                        "Anta Telugu loki marchaam: deep dives, quizzes tho saha. Code English lone untundi.",
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    languageNames.forEachIndexed { i, label ->
                        FilterChip(
                            selected = store.language == i,
                            onClick = { store.chooseLanguage(i) },
                            label = { Text(label) },
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
            }

            SectionCard(t("🧹 Progress", "🧹 முன்னேற்றம்", "🧹 Progress", "🧹 ప్రోగ్రెస్", "🧹 Progress")) {
                val done = allLessons.count { it.id in store.done }
                Text(
                    t("You have completed $done of ${allLessons.size} lessons.", "நீங்கள் ${allLessons.size} பாடங்களில் $done முடித்துள்ளீர்கள்.", "Neenga ${allLessons.size} lessons-la $done mudichirukkeenga.", "మీరు ${allLessons.size} పాఠాల్లో $done పూర్తి చేశారు.", "Meeru ${allLessons.size} lessons lo $done complete chesaaru."),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OutlinedButton(
                    onClick = { confirm = true },
                    enabled = done > 0,
                    border = BorderStroke(1.dp, if (done > 0) red else MaterialTheme.colorScheme.outlineVariant),
                ) { Text(t("Reset progress", "முன்னேற்றத்தை அழி", "Progress-a reset pannu", "ప్రోగ్రెస్ రీసెట్ చేయి", "Progress reset cheyyi"), color = if (done > 0) red else MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    if (confirm) {
        AlertDialog(
            onDismissRequest = { confirm = false },
            shape = RoundedCornerShape(28.dp),
            title = { Text(t("Reset all progress?", "எல்லா முன்னேற்றத்தையும் அழிக்கவா?", "Ellaa progress-um reset pannalaamaa?", "ప్రోగ్రెస్ అంతా రీసెట్ చేయాలా?", "Progress anta reset cheyyaalaa?")) },
            text = { Text(t("All lessons will be marked as not done. This can't be undone.", "எல்லா பாடங்களும் முடிக்கப்படாதவை என குறிக்கப்படும். இதைத் திரும்பப் பெற முடியாது.", "Ellaa lessons-um mudikkala-nu mark aagum. Ithai thirumba edukka mudiyaadhu.", "అన్ని పాఠాలు పూర్తి కానివిగా మార్క్ అవుతాయి. దీన్ని వెనక్కి తీసుకోలేరు.", "Anni paatalu complete kaanivigaa mark avutaayi. Deenni venakki teesukoleru.")) },
            confirmButton = {
                TextButton(onClick = { store.resetProgress(); confirm = false }) { Text(t("Reset", "அழி", "Reset", "రీసెట్", "Reset"), color = red) }
            },
            dismissButton = { TextButton(onClick = { confirm = false }) { Text(t("Cancel", "ரத்து", "Cancel", "రద్దు", "Raddu")) } },
        )
    }
}
