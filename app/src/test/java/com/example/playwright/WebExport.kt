package com.example.playwright

import com.example.playwright.data.Block
import com.example.playwright.data.Code
import com.example.playwright.data.H
import com.example.playwright.data.Lesson
import com.example.playwright.data.P
import com.example.playwright.data.Quiz
import com.example.playwright.data.Table
import com.example.playwright.data.Tip
import com.example.playwright.data.Warn
import com.example.playwright.data.courseModules
import com.example.playwright.data.hasTranslation
import com.example.playwright.data.loc
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.io.File

/**
 * Exports the course content to JSON for the web (PWA) version in `web/data/`.
 *
 * Skipped unless the WEB_EXPORT_DIR environment variable is set, so normal test runs are unaffected.
 * Run it with `web/tools/export-content.sh` (or set WEB_EXPORT_DIR and run this test yourself).
 */
class WebExport {
    @Test
    fun exportCourseJson() {
        val dir = System.getenv("WEB_EXPORT_DIR")
        assumeTrue("WEB_EXPORT_DIR not set; skipping web export", !dir.isNullOrBlank())
        val out = File(dir!!).apply { mkdirs() }
        for (lang in 0..4) {
            val modules = courseModules.map { m ->
                val lm = m.loc(lang)
                obj(
                    "id" to lm.id, "emoji" to lm.emoji, "title" to lm.title, "subtitle" to lm.subtitle,
                    "accentIndex" to lm.accentIndex,
                    "lessons" to m.lessons.map { lesson(it, lang) },
                )
            }
            File(out, "course-$lang.json").writeText(json(obj("lang" to lang, "modules" to modules)), Charsets.UTF_8)
        }
    }

    private fun lesson(orig: Lesson, lang: Int): Map<String, Any?> {
        val l = orig.loc(lang)
        return obj(
            "id" to l.id, "emoji" to l.emoji, "title" to l.title, "story" to l.story,
            "javaCode" to l.javaCode, "code" to l.code, "codeLang" to l.codeLang, "codeCaption" to l.codeCaption,
            "points" to l.points, "quiz" to quiz(l.quiz),
            "deep" to l.deep.map(::block),
            "challenge" to l.challenge?.let { obj("prompt" to it.prompt, "solution" to it.solution) },
            "moreQuiz" to l.moreQuiz.map(::quiz),
            "translated" to orig.hasTranslation(lang),
        )
    }

    private fun quiz(q: Quiz) = obj("question" to q.question, "options" to q.options, "answer" to q.answer, "why" to q.why)

    private fun block(b: Block): Map<String, Any?> = when (b) {
        is H -> obj("t" to "h", "text" to b.text)
        is P -> obj("t" to "p", "text" to b.text)
        is Code -> obj("t" to "code", "title" to b.title, "lang" to b.lang, "code" to b.code)
        is Tip -> obj("t" to "tip", "text" to b.text)
        is Warn -> obj("t" to "warn", "text" to b.text)
        is Table -> obj("t" to "table", "header" to b.header, "rows" to b.rows)
    }

    private fun obj(vararg pairs: Pair<String, Any?>): Map<String, Any?> = linkedMapOf(*pairs)

    private fun json(v: Any?): String = StringBuilder().also { write(it, v) }.toString()

    private fun write(sb: StringBuilder, v: Any?) {
        when (v) {
            null -> sb.append("null")
            is String -> str(sb, v)
            is Number, is Boolean -> sb.append(v.toString())
            is Map<*, *> -> {
                sb.append('{')
                v.entries.forEachIndexed { i, (k, value) ->
                    if (i > 0) sb.append(',')
                    str(sb, k.toString()); sb.append(':'); write(sb, value)
                }
                sb.append('}')
            }
            is List<*> -> {
                sb.append('[')
                v.forEachIndexed { i, item -> if (i > 0) sb.append(','); write(sb, item) }
                sb.append(']')
            }
            else -> error("Unsupported JSON value: ${v::class}")
        }
    }

    private fun str(sb: StringBuilder, s: String) {
        sb.append('"')
        for (c in s) when {
            c == '"' -> sb.append("\\\"")
            c == '\\' -> sb.append("\\\\")
            c == '\n' -> sb.append("\\n")
            c == '\r' -> sb.append("\\r")
            c == '\t' -> sb.append("\\t")
            c < ' ' || c == ' ' || c == ' ' -> sb.append("\\u%04x".format(c.code))
            else -> sb.append(c)
        }
        sb.append('"')
    }
}
