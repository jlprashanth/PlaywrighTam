package com.example.playwright

import com.example.playwright.data.Code
import com.example.playwright.data.ExtraTr
import com.example.playwright.data.H
import com.example.playwright.data.Lesson
import com.example.playwright.data.P
import com.example.playwright.data.Table
import com.example.playwright.data.Tip
import com.example.playwright.data.Warn
import com.example.playwright.data.allLessons
import com.example.playwright.data.challengeTa
import com.example.playwright.data.challengeTe
import com.example.playwright.data.challengeTg
import com.example.playwright.data.challengeTn
import com.example.playwright.data.courseModules
import com.example.playwright.data.deepTa
import com.example.playwright.data.deepTe
import com.example.playwright.data.deepTg
import com.example.playwright.data.deepTn
import com.example.playwright.data.hasTranslation
import com.example.playwright.data.loc
import com.example.playwright.data.moduleTa
import com.example.playwright.data.moduleTe
import com.example.playwright.data.moduleTg
import com.example.playwright.data.moduleTn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TranslationCoverageTest {
    private val deepByLang: Map<Int, Map<String, ExtraTr>> = mapOf(1 to deepTa, 2 to deepTg, 3 to deepTe, 4 to deepTn)
    private val challengeByLang: Map<Int, Map<String, String>> = mapOf(1 to challengeTa, 2 to challengeTg, 3 to challengeTe, 4 to challengeTn)
    private val moduleByLang: Map<Int, Map<String, Pair<String, String>>> = mapOf(1 to moduleTa, 2 to moduleTg, 3 to moduleTe, 4 to moduleTn)
    private val names = mapOf(1 to "Tamil", 2 to "Tanglish", 3 to "Telugu", 4 to "Tenglish")
    /** Questions made only of technical terms, which legitimately read the same in every language. */
    private val technicalQuestions = setOf("`page.request` vs `request` fixture?", "Locator vs ElementHandle?")

    private fun Lesson.hasExtras() = deep.isNotEmpty() || moreQuiz.isNotEmpty()

    @Test
    fun everyLessonHasCoreTranslationInEveryLanguage() {
        for (lang in 1..4) for (l in allLessons) {
            assertTrue("${l.id} has no core translation for ${names[lang]}", l.hasTranslation(lang))
        }
    }

    @Test
    fun everyModuleHasATitleInEveryLanguage() {
        for (lang in 1..4) for (m in courseModules) {
            assertTrue("module ${m.id} missing in ${names[lang]}", moduleByLang[lang]!!.containsKey(m.id))
        }
    }

    @Test
    fun everyChallengeIsTranslatedInEveryLanguage() {
        for (lang in 1..4) for (l in allLessons) {
            if (l.challenge != null) {
                assertTrue("${l.id} challenge missing in ${names[lang]}", challengeByLang[lang]!!.containsKey(l.id))
            }
        }
    }

    @Test
    fun everyDeepDiveAndExtraQuizIsTranslatedInEveryLanguage() {
        for (lang in 1..4) {
            val map = deepByLang[lang]!!
            for (l in allLessons.filter { it.hasExtras() }) {
                assertTrue("${l.id} deep dive missing in ${names[lang]}", map.containsKey(l.id))
            }
            val known = allLessons.map { it.id }.toSet()
            for (id in map.keys) assertTrue("${names[lang]} has an unknown lesson id '$id'", id in known)
        }
    }

    @Test
    fun translatedDeepDivesMatchTheEnglishStructure() {
        for (lang in 1..4) {
            val map = deepByLang[lang]!!
            for (l in allLessons.filter { it.hasExtras() }) {
                val tr = map[l.id] ?: continue
                val where = "${l.id} / ${names[lang]}"

                assertEquals("$where: number of deep blocks", l.deep.size, tr.deep.size)
                l.deep.zip(tr.deep).forEachIndexed { i, (o, t) ->
                    assertEquals("$where: block #$i type", o::class, t::class)
                    when (o) {
                        is Table -> {
                            val tt = t as Table
                            assertEquals("$where: block #$i table columns", o.header.size, tt.header.size)
                            assertEquals("$where: block #$i table rows", o.rows.size, tt.rows.size)
                            o.rows.indices.forEach { r -> assertEquals("$where: block #$i row $r width", o.rows[r].size, tt.rows[r].size) }
                        }
                        is P -> assertNotEquals("$where: block #$i paragraph left in English", o.text, (t as P).text)
                        is Tip -> assertNotEquals("$where: block #$i tip left in English", o.text, (t as Tip).text)
                        is Warn -> assertNotEquals("$where: block #$i warning left in English", o.text, (t as Warn).text)
                        is H, is Code -> Unit
                    }
                }

                assertEquals("$where: number of extra quizzes", l.moreQuiz.size, tr.quiz.size)
                l.moreQuiz.zip(tr.quiz).forEachIndexed { i, (o, t) ->
                    assertEquals("$where: quiz #$i option count", o.options.size, t.options.size)
                    assertEquals("$where: quiz #$i answer index", o.answer, t.answer)
                    if (o.question !in technicalQuestions) assertNotEquals("$where: quiz #$i question left in English", o.question, t.question)
                    assertNotEquals("$where: quiz #$i explanation left in English", o.why, t.why)
                }
            }
        }
    }

    @Test
    fun localizedLessonsKeepEnglishCodeAndCorrectAnswers() {
        for (lang in 1..4) for (l in allLessons) {
            val loc = l.loc(lang)
            assertEquals("${l.id}: deep size after merge", l.deep.size, loc.deep.size)
            l.deep.zip(loc.deep).forEach { (o, t) ->
                if (o is Code) {
                    val tc = t as Code
                    assertEquals("${l.id}: code must stay English", o.code, tc.code)
                    assertEquals("${l.id}: code language", o.lang, tc.lang)
                }
            }
            assertEquals(l.quiz.answer, loc.quiz.answer)
            l.moreQuiz.zip(loc.moreQuiz).forEach { (o, t) -> assertEquals(o.answer, t.answer) }
            // The deep dive must really be translated (not silently fallen back to English).
            if (l.deep.isNotEmpty()) assertTrue("${l.id} deep dive fell back to English in ${names[lang]}", loc.deep != l.deep)
        }
    }
}
