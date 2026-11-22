package com.example.playwright.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playwright.data.Lesson
import com.example.playwright.data.Module
import com.example.playwright.data.allLessons
import com.example.playwright.data.courseModules
import com.example.playwright.data.hasTranslation
import com.example.playwright.data.loc
import com.example.playwright.ui.theme.StatusColors

class ProgressStore(context: Context) {
    private val prefs = context.getSharedPreferences("progress", Context.MODE_PRIVATE)
    var done by mutableStateOf(prefs.getStringSet("done", emptySet())!!.toSet())
        private set

    fun markDone(id: String) {
        if (id in done) return
        done = done + id
        prefs.edit().putStringSet("done", done).apply()
    }

    var language by mutableStateOf(prefs.getInt("lang", 0))
        private set

    fun chooseLanguage(l: Int) {
        language = l
        prefs.edit().putInt("lang", l).apply()
    }

    var themeMode by mutableStateOf(prefs.getInt("theme", 0))
        private set

    fun setTheme(mode: Int) {
        themeMode = mode
        prefs.edit().putInt("theme", mode).apply()
    }

    fun resetProgress() {
        done = emptySet()
        prefs.edit().remove("done").apply()
    }

    fun toggle(id: String) {
        done = if (id in done) done - id else done + id
        prefs.edit().putStringSet("done", done).apply()
    }
}

@Composable
fun PlaywrightApp(store: ProgressStore) {
    var openId by rememberSaveable { mutableStateOf<String?>(null) }
    var expandedId by rememberSaveable { mutableStateOf<String?>("m1") }
    val listState = rememberLazyListState()

    BackHandler(enabled = openId != null) { openId = null }

    AnimatedContent(
        targetState = openId,
        transitionSpec = {
            if (targetState != null && initialState == null) {
                (slideInHorizontally(tween(220)) { it / 8 } + fadeIn(tween(220))) togetherWith fadeOut(tween(120))
            } else if (targetState == null) {
                fadeIn(tween(200)) togetherWith (slideOutHorizontally(tween(200)) { it / 8 } + fadeOut(tween(150)))
            } else {
                fadeIn(tween(160)) togetherWith fadeOut(tween(120))
            }
        },
        label = "screen",
    ) { id ->
        if (id == null) {
            HomeScreen(store, listState, expandedId, { expandedId = it }, { openId = it }, onSettings = { openId = "__settings" })
        } else if (id == "__settings") {
            SettingsScreen(store, onBack = { openId = null })
        } else {
            LessonScreen(id, store, onBack = { openId = null }, onOpen = { openId = it })
        }
    }
}

// ───────────────────────── Home ─────────────────────────

@Composable
fun HomeScreen(
    store: ProgressStore,
    listState: LazyListState,
    expandedId: String?,
    onExpand: (String?) -> Unit,
    onOpen: (String) -> Unit,
    onSettings: () -> Unit,
) {
    val total = allLessons.size
    val doneCount = allLessons.count { it.id in store.done }
    val next = allLessons.firstOrNull { it.id !in store.done }?.loc(store.language)
    val modules = remember(store.language) { courseModules.map { it.loc(store.language) } }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 16.dp, bottom = padding.calculateBottomPadding() + 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(Modifier.padding(horizontal = 4.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Playwright Academy 🎭", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                        IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, t("Settings", "அமைப்புகள்", "Settings", "సెట్టింగ్స్", "Settings"), tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                    Text(
                        t(
                            "TypeScript + Playwright for Selenium folks, explained like you're five.",
                            "Selenium தெரிந்தவர்களுக்கான TypeScript + Playwright, ஐந்து வயதுக் குழந்தைக்குச் சொல்வது போல.",
                            "Selenium theriyum-nu irukkura ungalukku TypeScript + Playwright, oru friend kitta pesura maadhiri.",
                            "Selenium తెలిసిన వాళ్ళకి TypeScript + Playwright, ఫ్రెండ్ తో మాట్లాడినట్టు సింపుల్ గా.",
                            "Selenium telisina vallaki TypeScript + Playwright, friend tho matladinattu simple ga.",
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item {
                Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("$doneCount", fontSize = 44.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text(
                                t(" / $total lessons done", " / $total பாடங்கள் முடிந்தன", " / $total lessons mudinjadhu", " / $total పాఠాలు అయ్యాయి", " / $total lessons ayyayi"),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                        }
                        LinearProgressIndicator(
                            progress = { if (total == 0) 0f else doneCount / total.toFloat() },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            strokeCap = StrokeCap.Round,
                        )
                        Button(onClick = { onOpen((next ?: allLessons.first()).id) }) {
                            Text(
                                when {
                                    next == null -> t("Revisit from the start", "மீண்டும் முதலில் இருந்து", "Mothalla irundhu thirumba", "మళ్ళీ మొదటి నుండి", "Malli modati nundi")
                                    doneCount == 0 -> t("Start learning", "கற்க ஆரம்பிக்கலாம்", "Kathukka aarambikkalaam", "నేర్చుకోవడం మొదలుపెడదాం", "Nerchukovadam modalupedadam")
                                    else -> t("Continue: ", "தொடர்க: ", "Continue: ", "కంటిన్యూ: ", "Continue: ") + next.title
                                },
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
            items(modules, key = { it.id }) { module ->
                ModuleCard(module, store, expanded = expandedId == module.id, onToggle = { onExpand(if (expandedId == module.id) null else module.id) }, onOpen = onOpen)
            }
        }
    }
}

@Composable
private fun ModuleCard(module: Module, store: ProgressStore, expanded: Boolean, onToggle: () -> Unit, onOpen: (String) -> Unit) {
    val accent = accents[module.accentIndex % accents.size]
    val doneInModule = module.lessons.count { it.id in store.done }
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                EmojiBadge(module.emoji, accent)
                Column(Modifier.weight(1f)) {
                    Text(module.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(module.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    "$doneInModule/${module.lessons.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (doneInModule == module.lessons.size) StatusColors.green else MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Icon(
                    Icons.Default.KeyboardArrowDown, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f),
                )
            }
            AnimatedVisibility(expanded, enter = expandVertically(tween(200)) + fadeIn(tween(200)), exit = shrinkVertically(tween(160)) + fadeOut(tween(120))) {
                Column(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                    module.lessons.forEachIndexed { i, lesson ->
                        val isDone = lesson.id in store.done
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable { onOpen(lesson.id) }.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            if (isDone) {
                                Icon(Icons.Default.CheckCircle, t("Done", "முடிந்தது", "Mudinjadhu", "అయిపోయింది", "Ayipoyindi"), tint = StatusColors.green, modifier = Modifier.size(28.dp))
                            } else {
                                Surface(shape = CircleShape, color = accent.copy(alpha = 0.16f), modifier = Modifier.size(28.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("${i + 1}", style = MaterialTheme.typography.labelMedium, color = accent)
                                    }
                                }
                            }
                            Text(lesson.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                            Text(lesson.emoji, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}

// ───────────────────────── Lesson ─────────────────────────

@Composable
fun LessonScreen(id: String, store: ProgressStore, onBack: () -> Unit, onOpen: (String) -> Unit) {
    val index = allLessons.indexOfFirst { it.id == id }.coerceAtLeast(0)
    val lesson = allLessons[index].loc(store.language)
    val translated = allLessons[index].hasTranslation(store.language)
    val module = courseModules.first { m -> m.lessons.any { it.id == lesson.id } }.loc(store.language)
    val accent = accents[module.accentIndex % accents.size]
    val isDone = lesson.id in store.done
    val scroll = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.statusBarsPadding().padding(horizontal = 4.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, t("Back", "பின்செல்", "Back", "వెనక్కి", "Venakki")) }
                Column(Modifier.weight(1f)) {
                    Text(module.title, style = MaterialTheme.typography.labelMedium, color = accent, maxLines = 1)
                    Text(t("Lesson ${index + 1} of ${allLessons.size}", "பாடம் ${index + 1} / ${allLessons.size}", "Lesson ${index + 1} / ${allLessons.size}", "పాఠం ${index + 1} / ${allLessons.size}", "Paatham ${index + 1} / ${allLessons.size}"), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = { store.toggle(lesson.id) }) {
                    if (isDone) Icon(Icons.Default.CheckCircle, null, tint = StatusColors.green, modifier = Modifier.size(18.dp))
                    else Spacer(Modifier.width(0.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(if (isDone) t("Done", "முடிந்தது", "Mudinjadhu", "అయిపోయింది", "Ayipoyindi") else t("Mark done", "முடிந்தது என குறி", "Mudinjadhu nu mark pannu", "అయిపోయింది అని మార్క్ చెయ్యి", "Ayipoyindi ani mark cheyyi"))
                }
            }
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
                Row(
                    Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = { onOpen(allLessons[index - 1].id) },
                        enabled = index > 0,
                        modifier = Modifier.weight(1f),
                    ) { Text(t("Previous", "முந்தைய", "Previous", "వెనక్కి", "Previous")) }
                    Button(
                        onClick = {
                            store.markDone(lesson.id)
                            if (index < allLessons.lastIndex) onOpen(allLessons[index + 1].id) else onBack()
                        },
                        modifier = Modifier.weight(1.4f),
                    ) { Text(if (index < allLessons.lastIndex) t("Done · Next", "முடிந்தது · அடுத்து", "Mudinjadhu · Next", "అయింది · తర్వాత", "Ayindi · Tarvata") else t("Finish 🎉", "முடித்தேன் 🎉", "Finish 🎉", "ముగించాను 🎉", "Muginchaanu 🎉")) }
                }
            }
        },
    ) { padding ->
        LessonBody(lesson, accent, translated, Modifier.padding(padding).verticalScroll(scroll))
    }
}

@Composable
private fun LessonBody(lesson: Lesson, accent: Color, translated: Boolean, modifier: Modifier) {
    Column(modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(horizontal = 4.dp)) {
            EmojiBadge(lesson.emoji, accent, size = 56)
            Text(lesson.title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
        }

        if (!translated) SectionCard(t("", "ℹ️ இந்தப் பாடம் இன்னும் தமிழில் இல்லை (ஆங்கிலத்தில் உள்ளது)", "ℹ️ Indha lesson innum Tamil-la illa (English-la irukku)", "ℹ️ ఈ పాఠం ఇంకా తెలుగులో లేదు (ఇంగ్లీష్‌లో ఉంది)", "ℹ️ Ee paatham inka Telugu lo ledu (English lo undi)"), bordered = true) {}

        SectionCard(t("🧒 Story time", "🧒 கதை நேரம்", "🧒 Story time", "🧒 కథల టైమ్", "🧒 Katha time"), container = accent.copy(alpha = 0.12f), bordered = false) {
            Text(richText(lesson.story), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }

        if (lesson.javaCode != null) {
            CodeBlock(t("☕ In Selenium you did", "☕ Selenium-இல் நீங்கள் செய்தது", "☕ Selenium-la neenga pannadhu", "☕ Selenium లో మీరు చేసింది", "☕ Selenium lo meeru chesindi"), lesson.javaCode, "Java", accent = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        CodeBlock(t("🎭 In Playwright", "🎭 Playwright-இல்", "🎭 Playwright-la", "🎭 Playwright లో", "🎭 Playwright lo"), lesson.code, lesson.codeLang)
        Text(
            richText(lesson.codeCaption),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp),
        )

        SectionCard(t("📌 Remember", "📌 நினைவில் கொள்க", "📌 Gnabagam vachukkonga", "📌 గుర్తుంచుకోండి", "📌 Gurtunchukondi")) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                lesson.points.forEach { p ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(shape = CircleShape, color = accent, modifier = Modifier.padding(top = 9.dp).size(6.dp)) {}
                        Text(richText(p), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        if (lesson.deep.isNotEmpty()) DeepDive(lesson.deep, accent)

        lesson.challenge?.let { ChallengeCard(it, accent) }

        QuizCard(lesson.quiz, t("🧠 Quick check", "🧠 விரைவுச் சோதனை", "🧠 Quick check", "🧠 క్విక్ చెక్", "🧠 Quick check"), lesson.id + "0")
        lesson.moreQuiz.forEachIndexed { i, q -> QuizCard(q, t("🧠 Quick check", "🧠 விரைவுச் சோதனை", "🧠 Quick check", "🧠 క్విక్ చెక్", "🧠 Quick check") + " ${i + 2}", lesson.id + (i + 1)) }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun QuizCard(quiz: com.example.playwright.data.Quiz, heading: String, key: String) {
    var picked by remember(key) { mutableStateOf<Int?>(null) }
    val green = StatusColors.green
    val red = StatusColors.red

    SectionCard(heading) {
        Text(richText(quiz.question), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        quiz.options.forEachIndexed { i, option ->
            val answered = picked != null
            val isRight = i == quiz.answer
            val isPicked = picked == i
            val border = when {
                answered && isRight -> green
                answered && isPicked -> red
                else -> MaterialTheme.colorScheme.outlineVariant
            }
            val fill = when {
                answered && isRight -> green.copy(alpha = 0.12f)
                answered && isPicked -> red.copy(alpha = 0.12f)
                else -> Color.Transparent
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = fill,
                border = BorderStroke(if (answered && (isRight || isPicked)) 2.dp else 1.dp, border),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable(enabled = !answered) { picked = i },
            ) {
                Text(
                    richText(option),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                )
            }
        }
        AnimatedVisibility(picked != null, enter = fadeIn(tween(150)) + expandVertically(tween(180))) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val right = picked == quiz.answer
                Text(
                    if (right) t("Correct! 🎉", "சரி! 🎉", "Correct! 🎉", "కరెక్ట్! 🎉", "Correct! 🎉") else t("Not quite. 🤔", "இல்லை. 🤔", "Illa, thirumba try pannunga. 🤔", "కాదు, మళ్ళీ try చేయండి. 🤔", "Kaadu, malli try cheyyandi. 🤔"),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (right) green else red,
                )
                Text(richText(quiz.why), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (!right) TextButton(onClick = { picked = null }) { Text(t("Try again", "மீண்டும் முயல்க", "Thirumba try pannu", "మళ్ళీ try చెయ్యి", "Malli try cheyyi")) }
            }
        }
    }
}
