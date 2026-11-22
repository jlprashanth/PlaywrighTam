package com.example.playwright.data

data class Quiz(val question: String, val options: List<String>, val answer: Int, val why: String)

data class Lesson(
    val id: String,
    val emoji: String,
    val title: String,
    val story: String,
    val javaCode: String?,
    val code: String,
    val codeLang: String,
    val codeCaption: String,
    val points: List<String>,
    val quiz: Quiz,
    val deep: List<Block> = emptyList(),
    val challenge: Challenge? = null,
    val moreQuiz: List<Quiz> = emptyList(),
)

data class Module(
    val id: String,
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentIndex: Int,
    val lessons: List<Lesson>,
)

sealed interface Block
data class H(val text: String) : Block
data class P(val text: String) : Block
data class Code(val title: String, val lang: String, val code: String) : Block
data class Tip(val text: String) : Block
data class Warn(val text: String) : Block
data class Table(val header: List<String>, val rows: List<List<String>>) : Block

data class Challenge(val prompt: String, val solution: String)
data class Extra(val deep: List<Block>, val challenge: Challenge? = null, val quiz: List<Quiz> = emptyList())

private fun enrich(l: Lesson): Lesson = deepExtras[l.id]?.let { l.copy(deep = it.deep, challenge = it.challenge, moreQuiz = it.quiz) } ?: l

val courseModules: List<Module> by lazy {
    val base = listOf(module1, module2, module3, module4, module5, module6, module7, module8, module9)
    base.map { m ->
        m.copy(lessons = m.lessons.flatMap { l -> listOf(enrich(l)) + insertions[l.id].orEmpty().map(::enrich) })
    }
}
val allLessons: List<Lesson> by lazy { courseModules.flatMap { it.lessons } }
