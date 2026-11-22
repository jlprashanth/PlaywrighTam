package com.example.playwright.data

// Translations: 1 = Tamil, 2 = Tanglish, 3 = Telugu, 4 = Tenglish. Language 0 = English (the source).

data class Tr(
    val title: String,
    val story: String,
    val caption: String,
    val points: List<String>,
    val q: String,
    val opts: List<String>,
    val why: String,
)

fun Lesson.hasTranslation(lang: Int): Boolean = when (lang) {
    1 -> id in lessonTa
    2 -> id in lessonTg
    3 -> id in lessonTe
    4 -> id in lessonTn
    else -> true
}

/** Translated deep dive + extra quizzes for one lesson. In [deep], write code blocks as `C("title")`: code and language come from the English original. */
data class ExtraTr(val deep: List<Block>, val quiz: List<Quiz> = emptyList())

/** Placeholder code block for translators: only the title is translated; code/lang are copied from the English block at the same position. */
fun C(title: String): Block = Code(title, "", "")

internal fun mergeBlocks(orig: List<Block>, tr: List<Block>?): List<Block> {
    if (tr == null || tr.size != orig.size) return orig
    return orig.zip(tr).map { (o, t) ->
        when {
            o is Code && t is Code -> o.copy(title = t.title)
            o is H && t is H -> t
            o is P && t is P -> t
            o is Tip && t is Tip -> t
            o is Warn && t is Warn -> t
            o is Table && t is Table && o.header.size == t.header.size && o.rows.size == t.rows.size &&
                o.rows.indices.all { o.rows[it].size == t.rows[it].size } -> t
            else -> o
        }
    }
}

internal fun mergeQuiz(orig: List<Quiz>, tr: List<Quiz>?): List<Quiz> {
    if (tr == null || tr.size != orig.size) return orig
    return orig.zip(tr).map { (o, t) ->
        if (t.options.size == o.options.size) o.copy(question = t.question, options = t.options, why = t.why) else o
    }
}

private fun extraFor(lang: Int, id: String): ExtraTr? = when (lang) {
    1 -> deepTa[id]
    2 -> deepTg[id]
    3 -> deepTe[id]
    4 -> deepTn[id]
    else -> null
}

fun Lesson.loc(lang: Int): Lesson {
    val tr = when (lang) {
        1 -> lessonTa[id]
        2 -> lessonTg[id]
        3 -> lessonTe[id]
        4 -> lessonTn[id]
        else -> null
    } ?: return this
    val ch = when (lang) {
        1 -> challengeTa
        2 -> challengeTg
        3 -> challengeTe
        else -> challengeTn
    }[id]
    val ex = extraFor(lang, id)
    return copy(
        challenge = if (ch != null) challenge?.copy(prompt = ch) else challenge,
        title = tr.title, story = tr.story, codeCaption = tr.caption, points = tr.points,
        quiz = quiz.copy(question = tr.q, options = tr.opts, why = tr.why),
        deep = mergeBlocks(deep, ex?.deep),
        moreQuiz = mergeQuiz(moreQuiz, ex?.quiz),
    )
}

fun Module.loc(lang: Int): Module {
    val t = when (lang) {
        1 -> moduleTa[id]
        2 -> moduleTg[id]
        3 -> moduleTe[id]
        4 -> moduleTn[id]
        else -> null
    }
    return copy(title = t?.first ?: title, subtitle = t?.second ?: subtitle, lessons = lessons.map { it.loc(lang) })
}

val moduleTa: Map<String, Pair<String, String>> = mapOf(
    "m1" to ("Playwright-க்கு வணக்கம்" to "அது என்ன, ஏன் நல்லது, முதல் test"),
    "m2" to ("தேவையான அளவு TypeScript" to "தேவையானது மட்டும், பயமே இல்லை"),
    "m3" to ("Test அடிப்படைகள்" to "அமைப்பு, இயக்குதல், reports"),
    "m4" to ("Elements-ஐ கண்டுபிடித்தல் (Locators)" to "Pro போல சுட்டிக்காட்டுங்கள்"),
    "m5" to ("செயல்கள் (Actions)" to "Click, type, select, drag, switch"),
    "m6" to ("காத்திருத்தலும் சரிபார்த்தலும்" to "Auto-wait மற்றும் assertions"),
    "m7" to ("Pro போல ஒழுங்கமைத்தல்" to "Config, Page Objects, fixtures, data"),
    "m8" to ("Power-ups" to "API, mocking, login மறுபயன்பாடு, CI"),
    "m9" to ("Hero zone" to "Capstone, best practices, interview தயாரிப்பு"),
)

val moduleTg: Map<String, Pair<String, String>> = mapOf(
    "m1" to ("Playwright-a meet pannunga" to "Adhu enna, yen nalladhu, first test"),
    "m2" to ("Podhumaana TypeScript" to "Venumnu mattum, bayame illa"),
    "m3" to ("Test basics" to "Structure, run pannardhu, reports"),
    "m4" to ("Elements-a kandupidikkardhu (Locators)" to "Pro maadhiri point pannunga"),
    "m5" to ("Actions pannardhu" to "Click, type, select, drag, switch"),
    "m6" to ("Wait pannardhum check pannardhum" to "Auto-wait & assertions"),
    "m7" to ("Pro maadhiri organize pannunga" to "Config, Page Objects, fixtures, data"),
    "m8" to ("Power-ups" to "API, mocking, login reuse, CI"),
    "m9" to ("Hero zone" to "Capstone, best practices, interview prep"),
)

val lessonTaA: Map<String, Tr> = mapOf(
    "what-is" to Tr(
        title = "Playwright என்றால் என்ன?",
        story = "ஒரு குட்டி ரோபோ நண்பனை கற்பனை செய்யுங்கள். அவன் ஒரு web browser-ஐ திறந்து, நீங்கள் செய்வது போலவே type செய்வான், click செய்வான், scroll செய்வான். நீங்கள் ஒரு வேலைப் பட்டியல் எழுதுங்கள்; ரோபோ அதைப் பின்பற்றி, ஏதாவது தவறாக இருந்தால் சொல்லும். அந்த ரோபோதான் `Playwright`. இதை Microsoft உருவாக்கியது; Chrome, Firefox, Safari (WebKit) மூன்றிலும் வேலை செய்யும்.",
        caption = "அதே கதைதான், வார்த்தைகள் மட்டும் புதியவை: browser-ஐ launch செய், ஒரு tab (`page`) திற, ஒரு site-க்குப் போ, மூடு.",
        points = listOf(
            "`chromedriver.exe` download செய்யவோ, உங்கள் browser version-உடன் பொருத்தவோ தேவையில்லை. Playwright தன் browser-களை தானே கொண்டு வரும்.",
            "இது Node.js-இல் ஓடும். நீங்கள் TypeScript-இல் எழுதுவீர்கள்; அது types உள்ள JavaScript (Java போலவே!).",
            "Chromium, Firefox, WebKit எல்லாவற்றுக்கும் ஒரே API.",
        ),
        q = "Playwright-இல் நீங்களே நிர்வகிக்க வேண்டியதில்லாதது எது?",
        opts = listOf("Node.js", "chromedriver.exe கோப்பு", "உங்கள் test code"),
        why = "Playwright தன் browser-களை தானே download செய்து நிர்வகிக்கும்; அதனால் driver தொல்லை இல்லை.",
    ),
    "why-better" to Tr(
        title = "Selenium-ஐ விட ஏன் எல்லோருக்கும் பிடிக்கிறது?",
        story = "Selenium-இல், ஒரு கதவின் முன் நின்று \"திறந்துவிட்டதா? திறந்துவிட்டதா?\" என்று திரும்பத் திரும்பக் கேட்பீர்கள் (அதுதான் `WebDriverWait`). Playwright-இல் ரோபோ பொறுமையாக இருக்கும். கதவு திறக்கும் வரை தானே காத்திருந்து, பிறகு உள்ளே போகும். கேள்வி குறைவு; முட்டாள்தனமான காரணங்களுக்காக fail ஆகும் tests-ம் குறைவு.",
        caption = "Selenium-இல் இரண்டு வரி wait code, இங்கே பூஜ்யம்.",
        points = listOf(
            "Auto-waiting: click செய்வதற்கு முன் element தெரிகிறதா, enabled-ஆ, அசையாமல் இருக்கிறதா என்று தானே சரிபார்க்கும்.",
            "Tests இயல்பாகவே parallel-ஆக ஓடும்; வேகமானவை.",
            "உள்ளேயே உள்ளவை: reports, screenshots, videos, trace viewer, API testing, network mocking.",
            "ஒவ்வொரு test-க்கும் தனி, சுத்தமான browser context (புதிய incognito window போல).",
        ),
        q = "Button 2 வினாடி தாமதமாகத் தெரிகிறது. `click()` என்ன செய்யும்?",
        opts = listOf("உடனே fail ஆகும்", "காத்திருந்து, பிறகு click செய்யும்", "எதுவும் செய்யாமல் அடுத்ததுக்குப் போகும்"),
        why = "Auto-wait தான் சூப்பர் சக்தி: element தயாராகும் வரை (timeout வரை) click() காத்திருக்கும்.",
    ),
    "setup" to Tr(
        title = "உங்கள் விளையாட்டுப் பெட்டியை தயார் செய்வோம்",
        story = "விளையாட, ஒரு விளையாட்டுப் பெட்டி வேண்டும். `Node.js` (இயந்திரம்) install செய்யுங்கள்; பிறகு Playwright-ஐ ஒரு தயார் project உருவாக்கச் சொல்லுங்கள். ஒரே command, சில கேள்விகள், முடிந்தது. இது `Maven archetype` போலத்தான், ஆனால் இன்னும் நட்பானது.",
        caption = "இவற்றை terminal-இல் இயக்குங்கள் (VS Code-இன் terminal நன்றாக வேலை செய்யும்).",
        points = listOf(
            "`package.json` தான் உங்கள் `pom.xml`. dependencies, scripts இதில் இருக்கும்.",
            "`node_modules/` உங்கள் `.m2` folder போல. இதை edit செய்யாதீர்கள்.",
            "`playwright.config.ts` உங்கள் `testng.xml` + settings.",
            "`tests/` folder-இல் spec files இருக்கும். பெயர்கள் `.spec.ts`-இல் முடியும்.",
        ),
        q = "`package.json`-க்கு நிகரான Selenium/Maven file எது?",
        opts = listOf("pom.xml", "testng.xml", "chromedriver.exe"),
        why = "இரண்டிலும் dependencies மற்றும் project தகவல்கள் இருக்கும்.",
    ),
    "first-test" to Tr(
        title = "உங்கள் முதல் test",
        story = "ரோபோவிடம் சொல்வோம்: \"Playwright website-க்குப் போய், page title-இல் Playwright என்று இருக்கிறதா பார்.\" அதுதான் ஒரு test! Test-க்கு ஒரு பெயர் கொடுக்கிறீர்கள், இலவசமாக ஒரு `page` (புதிய browser tab) கிடைக்கிறது, பிறகு steps எழுதுகிறீர்கள்.",
        caption = "இயக்க: npx playwright test",
        points = listOf(
            "`test('name', ...)` தான் உங்கள் `@Test`.",
            "`{ page }` என்றால்: \"Playwright, ஒரு புதிய tab கொடு.\" Driver setup code தேவையில்லை.",
            "`/Playwright/` ஒரு regular expression; \"Playwright உள்ளது\" என்று பொருள்.",
            "`await` எங்கும் வரும். ஏன் என்று TypeScript பகுதியில் கற்போம்.",
        ),
        q = "Test-இல் `page` என்றால் என்ன?",
        opts = listOf("உங்களுக்குக் கொடுக்கப்படும் புதிய browser tab", "Test-இன் பெயர்", "Disk-இல் உள்ள file"),
        why = "ஒவ்வொரு test-க்கும் Playwright சுத்தமான tab உருவாக்கி உள்ளே கொடுக்கும்.",
    ),
    "ts-vars" to Tr(
        title = "Variables மற்றும் types",
        story = "Variables என்பவை பெயரிட்ட பெட்டிகள். `const` பெட்டி ஒட்டப்பட்டது: உள்ளே வைத்தது மாறாது. `let` பெட்டியை திறந்து பின்னர் மாற்றலாம். Type-ஐ colon-க்குப் பின் எழுதுவீர்கள், name tag போல: `age: number`.",
        caption = "TypeScript பெரும்பாலும் type-ஐ ஊகிக்கும்; அதனால் `const x = 5` என்றும் எழுதலாம்.",
        points = listOf(
            "இயல்பாக `const` பயன்படுத்துங்கள்; மதிப்பு மாறினால் மட்டும் `let`. `var` வேண்டாம்.",
            "எண்களுக்கு ஒரே type: `number` (int, double, long இல்லை).",
            "ஒப்பிட `===` பயன்படுத்துங்கள், `==` வேண்டாம்.",
            "Semicolon விருப்பமானது; ஆனால் பலர் சேர்ப்பார்கள்.",
        ),
        q = "பின்னர் மதிப்பை மாற்ற அனுமதிக்கும் keyword எது?",
        opts = listOf("const", "let", "string"),
        why = "`let` பெட்டிகளை மாற்றலாம்; `const` முடியாது.",
    ),
    "ts-functions" to Tr(
        title = "Functions மற்றும் arrow functions",
        story = "Function என்பது சமையல் குறிப்பு அட்டை: பொருட்களைக் கொடுத்தால் உணவு தரும். Arrow function அதே குறிப்பை `=>` கொண்டு சுருக்கமாக எழுதுவது. Playwright முழுவதும் arrow-கள் வரும்; ஏனெனில் tests-ம் arrow functions தான்.",
        caption = "`(x) => ...` என்பது Java-வின் lambda `x -> ...` போலவே.",
        points = listOf(
            "Java lambda `x -> x + 1` தான் TypeScript-இல் `(x) => x + 1`.",
            "Return type அடைப்புக்குறிக்குப் பின்: `): number`.",
            "`console.log()` தான் உங்கள் `System.out.println()`.",
        ),
        q = "Java-வில் `() => {}`-ஐ என்ன என்று அழைப்பீர்கள்?",
        opts = listOf("Lambda", "Interface", "Package"),
        why = "Arrow functions தான் TypeScript-இன் lambdas.",
    ),
    "ts-objects" to Tr(
        title = "Arrays மற்றும் objects",
        story = "Array என்பது பொம்மைகள் வரிசையாக இருக்கும் அலமாரி. Object என்பது `name`, `age`, `role` என்று லேபிள் ஒட்டிய பைகள் உள்ள முதுகுப்பை. Playwright ஒரு பையைக் கொடுத்து \"உனக்கு வேண்டிய பாக்கெட்டை மட்டும் எடு\" என்கிறது. அதற்குப் பெயர் destructuring.",
        caption = "தகவலை வைக்க class தேவையில்லை. சாதாரண object போதும்.",
        points = listOf(
            "Objects: `{ key: value }`. Dot கொண்டு அணுகலாம்: `user.name`.",
            "Arrays: `[a, b, c]`. Loop: `for (const x of list)`.",
            "`async ({ page }) => ...` என்பது destructuring: Playwright தரும் பெரிய object-இல் இருந்து `page`-ஐ மட்டும் எடுப்பது.",
        ),
        q = "`async ({ page }) => {}`-இல் curly braces என்ன செய்கின்றன?",
        opts = listOf("வெற்று code block", "பெரிய object-இல் இருந்து `page`-ஐ எடுக்கிறது", "புதிய class உருவாக்குகிறது"),
        why = "அதுதான் destructuring: Playwright helpers நிறைந்த object தரும்; நீங்கள் வேண்டியதை எடுக்கிறீர்கள்.",
    ),
    "ts-async" to Tr(
        title = "async மற்றும் await (மிக முக்கியம்)",
        story = "நீங்கள் ஒரு பீட்சா ஆர்டர் செய்கிறீர்கள். கடை \"வந்துகொண்டிருக்கிறது\" என்கிறது (அதுதான் `Promise`). வருவதற்கு முன் சாப்பிட ஆரம்பித்தால் காற்றைத்தான் சாப்பிடுவீர்கள். `await` என்றால்: பீட்சா வரும் வரை இங்கே காத்திரு, பிறகு தொடரு. Playwright-இன் ஒவ்வொரு செயலும் ஒரு பீட்சா ஆர்டர்; அதனால் ஒவ்வொன்றுக்கும் `await` வேண்டும்.",
        caption = "`await` `async` என்று குறிக்கப்பட்ட function-க்குள் மட்டுமே வேலை செய்யும்.",
        points = listOf(
            "Browser செயல்களுக்கு நேரம் ஆகும்; அதனால் அவை `Promise` (\"பிறகு முடியும்\" டிக்கெட்) தருகின்றன.",
            "`await` அந்த டிக்கெட் முடியும் வரை test-ஐ நிறுத்தி வைக்கும்.",
            "`await` மறப்பதுதான் புதியவர்களின் முதல் bug. அதைப் பிடிக்க ESLint rule `no-floating-promises` இயக்குங்கள்.",
            "`async` function-க்கு முன்; `await` செயலுக்கு முன்.",
        ),
        q = "`click()`-க்கு முன் `await` மறந்தால் என்ன ஆகலாம்?",
        opts = listOf("Click முடிவதற்குள் அடுத்த step ஓடிவிடும்", "Java error தரும்", "ஒன்றும் ஆகாது, அது விருப்பம்"),
        why = "await இல்லாவிட்டால் code காத்திருக்காது; steps ஒன்றோடொன்று கலந்து test flaky ஆகும்.",
    ),
    "ts-classes" to Tr(
        title = "Classes, import மற்றும் export",
        story = "நண்பனின் அறையில் இருந்து ஒரு பொம்மையை இரவல் வாங்குவது போல: நீங்கள் `import` செய்கிறீர்கள். உங்கள் பொம்மையை மற்றவர்கள் எடுக்க வேண்டுமானால் `export` செய்கிறீர்கள். Classes Java போலவே, ஆனால் சடங்குகள் குறைவு. பின்னர் Page Objects-க்கு இவற்றைப் பயன்படுத்துவீர்கள்.",
        caption = "`constructor(private page: Page) {}` ஒரே வரியில் field-ஐ அறிவித்து மதிப்பும் அளிக்கிறது.",
        points = listOf(
            "`export` என்பது ஒரு file-இன் பொருளுக்கான `public` போல.",
            "`import { X } from './file'` Java-வின் `import package.X;`-க்குப் பதில். Relative paths பயன்படுத்துங்கள்.",
            "Class-க்குள் fields-ஐ அணுக `this.` பயன்படுத்துங்கள்.",
            "Browser வேலை செய்யும் methods `async` ஆக இருக்கும்.",
        ),
        q = "உங்கள் class-ஐ வேறு file பயன்படுத்த எது அனுமதிக்கும்?",
        opts = listOf("export", "static", "final"),
        why = "அதை `export` என்று குறியுங்கள்; பிறகு வேறிடத்தில் `import` செய்யுங்கள்.",
    ),
)

val lessonTgA: Map<String, Tr> = mapOf(
    "what-is" to Tr(
        title = "Playwright na enna?",
        story = "Oru chinna robot friend-a imagine pannunga. Avan oru web browser-a open pannitu, neenga pannura maadhiri type pannuvaan, click pannuvaan, scroll pannuvaan. Neenga oru to-do list ezhudhunga, robot adhai follow pannitu, edhavadhu thappa irundha solliduvaan. Andha robot dhaan `Playwright`. Idhu Microsoft senjadhu; Chrome, Firefox, Safari (WebKit) moonulayum work aagum.",
        caption = "Same story dhaan, words mattum puthusu: browser-a launch pannu, oru tab (`page`) open pannu, site-ku po, close pannu.",
        points = listOf(
            "`chromedriver.exe` download panna venaam, browser version-oda match panna venaam. Playwright thanoda browsers-a thaane kondu varum.",
            "Idhu Node.js-la odum. Neenga TypeScript-la ezhudhuveenga; adhu types irukura JavaScript (Java maadhiri dhaan!).",
            "Chromium, Firefox, WebKit ellathukum ore API.",
        ),
        q = "Playwright-la neengale manage panna vendiyadhillaadhadhu edhu?",
        opts = listOf("Node.js", "chromedriver.exe file", "Unga test code"),
        why = "Playwright thanoda browsers-a thaane download panni manage pannum, so driver thollai illa.",
    ),
    "why-better" to Tr(
        title = "Selenium-a vida yen ellarukkum pidikkudhu?",
        story = "Selenium-la, oru door munnadi nindhu \"Open aayiducha? Open aayiducha?\" nu thirumba thirumba kekkuveenga (adhu dhaan `WebDriverWait`). Playwright-la robot porumaiya irukkum. Door open aagura varaikkum thaane wait pannitu, appuram ulla pogum. Kelvi kammi; silly reasons-ku fail aagura tests-um kammi.",
        caption = "Selenium-la rendu line wait code, inga zero.",
        points = listOf(
            "Auto-waiting: click panradhukku munnadi element therigiradha, enabled-a, asaiyama irukkaa nu thaane check pannum.",
            "Tests default-ave parallel-a odum; romba fast.",
            "Ulle ye irukku: reports, screenshots, videos, trace viewer, API testing, network mocking.",
            "Ovvoru test-kum thani, clean browser context (puthu incognito window maadhiri).",
        ),
        q = "Button 2 second late-a varudhu. `click()` enna pannum?",
        opts = listOf("Udane fail aagum", "Wait pannitu, appuram click pannum", "Onnum pannama next-ku poidum"),
        why = "Auto-wait dhaan super power: element ready aagura varaikkum (timeout varai) click() wait pannum.",
    ),
    "setup" to Tr(
        title = "Unga toy box-a ready pannalaam",
        story = "Vilayaada oru toy box venum. `Node.js` (engine) install pannunga, appuram Playwright-a oru ready-made project create panna sollunga. Oru command, konjam questions, mudinjadhu. Idhu `Maven archetype` maadhiri dhaan, aana innum friendly.",
        caption = "Idhai terminal-la run pannunga (VS Code-oda terminal nalla work aagum).",
        points = listOf(
            "`package.json` dhaan unga `pom.xml`. Dependencies, scripts ellam adhula irukkum.",
            "`node_modules/` unga `.m2` folder maadhiri. Adhai edit panna koodaadhu.",
            "`playwright.config.ts` unga `testng.xml` + settings.",
            "`tests/` folder-la spec files irukkum. Peyar `.spec.ts`-la mudiyum.",
        ),
        q = "`package.json`-ku equal-aana Selenium/Maven file edhu?",
        opts = listOf("pom.xml", "testng.xml", "chromedriver.exe"),
        why = "Rendulayum dependencies and project info irukkum.",
    ),
    "first-test" to Tr(
        title = "Unga first test",
        story = "Robot-kitta solvom: \"Playwright website-ku poi, page title-la Playwright nu irukkaa paaru.\" Adhu dhaan oru test! Test-ku oru peyar kudukkureenga, free-a oru `page` (puthu browser tab) kedaikkum, appuram steps ezhudhureenga.",
        caption = "Run panna: npx playwright test",
        points = listOf(
            "`test('name', ...)` dhaan unga `@Test`.",
            "`{ page }` na: \"Playwright, oru puthu tab kudu.\" Driver setup code venaam.",
            "`/Playwright/` oru regular expression; \"Playwright irukku\" nu artham.",
            "`await` engayum varum. Yen nu TypeScript section-la paapom.",
        ),
        q = "Test-la `page` na enna?",
        opts = listOf("Unga-kku kudukkura puthu browser tab", "Test-oda peyar", "Disk-la irukkura file"),
        why = "Ovvoru test-kum Playwright clean tab create panni ulla kudukkum.",
    ),
    "ts-vars" to Tr(
        title = "Variables and types",
        story = "Variables na peyar ottina boxes. `const` box glue pottu ottiyaachu: ulla vachadhu maaraadhu. `let` box-a open panni appuram maathalaam. Type-a colon-ku appuram ezhudhuveenga, name tag maadhiri: `age: number`.",
        caption = "TypeScript pala time type-a guess pannidum, so `const x = 5` nu kooda ezhudhalaam.",
        points = listOf(
            "Default-a `const` use pannunga; value maarina mattum `let`. `var` venaam.",
            "Numbers-ku ore type: `number` (int, double, long illa).",
            "Compare panna `===` use pannunga, `==` venaam.",
            "Semicolon optional; aana neraiya perr podhuvaanga.",
        ),
        q = "Aprom value-a maatha allow panra keyword edhu?",
        opts = listOf("const", "let", "string"),
        why = "`let` boxes-a maathalaam; `const` mudiyaadhu.",
    ),
    "ts-functions" to Tr(
        title = "Functions and arrow functions",
        story = "Function na recipe card: ingredients kudutha, dish tharum. Arrow function adhe recipe-a `=>` vachu short-a ezhudhuradhu. Playwright full-a arrows varum, because tests-um arrow functions dhaan.",
        caption = "`(x) => ...` na Java-oda lambda `x -> ...` maadhiri dhaan.",
        points = listOf(
            "Java lambda `x -> x + 1` dhaan TypeScript-la `(x) => x + 1`.",
            "Return type bracket-ku appuram: `): number`.",
            "`console.log()` dhaan unga `System.out.println()`.",
        ),
        q = "Java-la `() => {}`-a enna nu solluveenga?",
        opts = listOf("Lambda", "Interface", "Package"),
        why = "Arrow functions dhaan TypeScript-oda lambdas.",
    ),
    "ts-objects" to Tr(
        title = "Arrays and objects",
        story = "Array na toys oru line-la irukkura shelf. Object na `name`, `age`, `role` nu label ottina pockets irukkura backpack. Playwright oru backpack-a kuduthutu \"unakku venum-nu pocket mattum edu\" nu solludhu. Adhukku peyar destructuring.",
        caption = "Data vaikka class venaam. Simple object podhum.",
        points = listOf(
            "Objects: `{ key: value }`. Dot vachu access pannalaam: `user.name`.",
            "Arrays: `[a, b, c]`. Loop: `for (const x of list)`.",
            "`async ({ page }) => ...` destructuring: Playwright tharra periya object-la irundhu `page`-a mattum edukkuradhu.",
        ),
        q = "`async ({ page }) => {}`-la curly braces enna pannudhu?",
        opts = listOf("Empty code block", "Periya object-la irundhu `page`-a edukkudhu", "Puthu class create pannudhu"),
        why = "Adhu dhaan destructuring: Playwright helpers niraindha object tharum; neenga venumnadhai edukkureenga.",
    ),
    "ts-async" to Tr(
        title = "async and await (romba mukkiyam)",
        story = "Neenga oru pizza order panreenga. Shop \"vandhutte irukku\" nu solludhu (adhu dhaan `Promise`). Varradhukku munnadi saapida aarambichaa, kaatha dhaan saapiduveenga. `await` na: pizza varra varaikkum inga wait pannu, appuram continue pannu. Playwright-oda ovvoru action-um oru pizza order, so ovvonnukkum `await` venum.",
        caption = "`await` `async` nu mark panna function-kulla mattum work aagum.",
        points = listOf(
            "Browser actions-ku time aagum, so avai `Promise` (\"appuram mudiyum\" ticket) tharum.",
            "`await` andha ticket mudiyara varaikkum test-a nirutthi vaikkum.",
            "`await` marakkardhu dhaan beginners-oda number one bug. Adhai pidikka ESLint rule `no-floating-promises` on pannunga.",
            "`async` function-ku munnadi; `await` action-ku munnadi.",
        ),
        q = "`click()`-ku munnadi `await` marandha enna aagalaam?",
        opts = listOf("Click mudiyarathukku munnadi next step odidum", "Java error tharum", "Onnum aagaadhu, adhu optional"),
        why = "await illaama code wait pannaadhu; steps onnoda onnu kalandhu test flaky aagum.",
    ),
    "ts-classes" to Tr(
        title = "Classes, import and export",
        story = "Friend-oda room-la irundhu oru toy-a borrow panra maadhiri: neenga `import` pannureenga. Unga toy-a mathavanga edukkanum-na `export` pannureenga. Classes Java maadhiri dhaan, aana ceremony kammi. Aprom Page Objects-ku idhai use pannuveenga.",
        caption = "`constructor(private page: Page) {}` oru line-la field-a declare panni value-um kudukkudhu.",
        points = listOf(
            "`export` na oru file-oda vishayathukku `public` maadhiri.",
            "`import { X } from './file'` Java-oda `import package.X;`-ku badhil. Relative paths use pannunga.",
            "Class-kulla fields-a access panna `this.` use pannunga.",
            "Browser velai pannura methods `async`-a irukkum.",
        ),
        q = "Unga class-a vera file use panna edhu allow pannum?",
        opts = listOf("export", "static", "final"),
        why = "Adhai `export` nu mark pannunga; appuram vera idathula `import` pannunga.",
    ),
)

val lessonTa: Map<String, Tr> by lazy { lessonTaA + lessonTaB + lessonTaC + lessonTaD }
val lessonTg: Map<String, Tr> by lazy { lessonTgA + lessonTgB + lessonTgC + lessonTgD }

val challengeTa: Map<String, String> by lazy { challengeTaAll }
val challengeTg: Map<String, String> by lazy { challengeTgAll }
val lessonTe: Map<String, Tr> by lazy { lessonTeA + lessonTeB + lessonTeC + lessonTeD }
val lessonTn: Map<String, Tr> by lazy { lessonTnA + lessonTnB + lessonTnC + lessonTnD }
val challengeTe: Map<String, String> by lazy { challengeTeAll }
val challengeTn: Map<String, String> by lazy { challengeTnAll }

val deepTa: Map<String, ExtraTr> by lazy { deepTaA + deepTaB + deepTaC + deepTaD }
val deepTg: Map<String, ExtraTr> by lazy { deepTgA + deepTgB + deepTgC + deepTgD }
val deepTe: Map<String, ExtraTr> by lazy { deepTeA + deepTeB + deepTeC + deepTeD }
val deepTn: Map<String, ExtraTr> by lazy { deepTnA + deepTnB + deepTnC + deepTnD }
