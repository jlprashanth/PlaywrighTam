package com.example.playwright.data

// Tamil (1): deep dives + extra quizzes, part A (matches DeepA.kt). Code blocks are C("title"); code comes from English.

val deepTaA: Map<String, ExtraTr> = mapOf(
    "what-is" to ExtraTr(
        deep = listOf(
            H("இது உண்மையில் எப்படி வேலை செய்கிறது?"),
            P("Selenium ஒரு தனி driver program-உடன் (chromedriver) HTTP மூலம் பேசும்; அந்த driver browser-உடன் பேசும். Playwright இடையில் உள்ளவரைத் தவிர்த்து, browser-உடன் ஒரே வேகமான இணைப்பைத் திறந்து வைத்து, அதன் வழியாக commands அனுப்புகிறது. அதனால்தான் இது வேகமானது; network calls, page load போன்றவற்றையும் கவனிக்க முடிகிறது."),
            Table(
                listOf("கருத்து", "Selenium", "Playwright"),
                listOf(
                    listOf("Browser-உடன் பேசும் வழி", "Driver + HTTP", "நேரடி WebSocket இணைப்பு"),
                    listOf("Browsers", "Driver உள்ள எதுவும்", "Chromium, Firefox, WebKit (உடன் வருபவை)"),
                    listOf("காத்திருத்தல்", "நீங்களே waits எழுத வேண்டும்", "உள்ளமைந்த auto-wait"),
                    listOf("Test runner", "TestNG / JUnit (நீங்கள் சேர்க்க வேண்டும்)", "`@playwright/test` (உடன் வருகிறது)"),
                ),
            ),
            H("4 அடிப்படைக் கட்டுமானங்கள்"),
            P("`Browser` என்பது முழு browser program. `BrowserContext` என்பது அதற்குள் இருக்கும் சுத்தமான profile (incognito window போல: தனி cookies, தனி storage). `Page` என்பது ஒரு context-க்குள் இருக்கும் ஒரு tab. `Locator` என்பது page-இல் உள்ள ஒன்றைச் சுட்டிக்காட்டும் pointer."),
            C("Browser → Context → Page"),
            Tip("`@playwright/test` பயன்படுத்தும்போது, ஒவ்வொரு test-க்கும் browser, context, page-ஐ அதுவே உருவாக்கிக் கொடுக்கும். Scripts-இல் மட்டுமே நீங்கள் கையால் செய்வீர்கள்."),
            Warn("Playwright, Selenium-உடன் ஒத்துப்போகாது. உங்கள் Java `WebDriver` code-ஐ மீண்டும் பயன்படுத்த முடியாது; ஆனால் யோசனைகள் (locators, waits, page objects) எல்லாம் அப்படியே வரும்."),
        ),
        quiz = listOf(
            Quiz("BrowserContext எதைப் போன்றது?", listOf("ஒரு incognito profile", "ஒரு browser tab", "ஒரு Maven module"), 0, "ஒரு context-க்கு தனி cookies, storage உண்டு; மற்ற contexts-இலிருந்து பிரிக்கப்பட்டது."),
            Quiz("ஒரு BrowserContext-இல் எத்தனை Pages இருக்கலாம்?", listOf("சரியாக ஒன்று", "நீங்கள் திறக்கும் அளவு", "ஒன்றும் இல்லை"), 1, "ஒரு context பல tabs-ஐ (pages) வைத்திருக்கலாம்."),
        ),
    ),
    "why-better" to ExtraTr(
        deep = listOf(
            H("Flaky tests: ஏன் வருகின்றன?"),
            P("Code மாற்றம் இல்லாமல் ஒரு test சில நேரம் pass, சில நேரம் fail ஆனால் அது flaky. வழக்கமான காரணம்: test, page-ஐ விட வேகமாக ஓடுகிறது. Button தயாராகும் முன் click செய்கிறது, அல்லது text வருவதற்கு முன் படிக்கிறது. Playwright-இன் auto-wait மற்றும் retry செய்யும் assertions இதில் பெரும்பாலானவற்றை வடிவமைப்பிலேயே சரிசெய்கின்றன."),
            H("தனிமை = நம்பகமான tests"),
            P("ஒவ்வொரு Playwright test-உம் தனக்கே உரிய புதிய context-இல் ஓடும். Test A-இல் login செய்தது test B-ஐ பாதிக்காது. Selenium-இல் cookies அழிக்க அல்லது driver-ஐ restart செய்ய நீங்கள் நினைவில் வைக்க வேண்டியிருந்தது."),
            H("வேகம்"),
            C("4 workers-இல் 100 tests-ஐ parallel-ஆக ஓட்டுதல்"),
            P("Contexts மலிவானவை (milliseconds) மற்றும் workers தனித்தனி processes என்பதால், Selenium-இல் ஒரு மணி நேரம் எடுத்த suites பெரும்பாலும் சில நிமிடங்களில் முடிகின்றன."),
            Table(
                listOf("வசதி", "Selenium உலகம்", "Playwright"),
                listOf(
                    listOf("Reports", "Extent / Allure plugin", "HTML report உள்ளமைந்தது"),
                    listOf("Debugging", "Breakpoints + வேண்டுதல்கள்", "Inspector, UI mode, Trace Viewer"),
                    listOf("API calls", "RestAssured", "`request` fixture"),
                    listOf("Network mock", "Proxy tools", "`page.route()`"),
                    listOf("Tests பதிவு", "Selenium IDE", "`codegen`"),
                ),
            ),
            Warn("Playwright மாயம் அல்ல. மோசமான locators, பகிரப்பட்ட test data, விடுபட்ட `await` இன்னும் flaky tests-ஐ உருவாக்கும். ஒவ்வொன்றையும் எப்படித் தவிர்ப்பது என்று கற்போம்."),
        ),
        quiz = listOf(
            Quiz("Playwright tests ஏன் ஒன்றுக்கொன்று தனிமைப்படுத்தப்பட்டுள்ளன?", listOf("ஒவ்வொன்றுக்கும் புதிய browser context கிடைக்கும்", "எல்லாம் ஒரே tab-ஐப் பகிரும்", "ஒன்றன்பின் ஒன்றாக ஓடும்"), 0, "ஒவ்வொரு test-க்கும் புதிய context இருந்தால் cookies அல்லது state கசியாது."),
            Quiz("Flaky tests-இன் பொதுவான காரணம் எது?", listOf("Parallel-ஆக ஓட்டுவது மட்டும்", "Page தயாராகும் முன் செயல்படுவது", "TypeScript பயன்படுத்துவது"), 1, "Page-ஐ விட வேகமாக இருப்பதே பாரம்பரியமான காரணம்."),
        ),
    ),
    "setup" to ExtraTr(
        deep = listOf(
            H("நிறுவுதல், படிப்படியாக"),
            P("1) nodejs.org-இலிருந்து Node.js (LTS version) நிறுவுங்கள். 2) VS Code நிறுவுங்கள். 3) ஒரு folder உருவாக்கி init command ஓட்டுங்கள். 4) ஒரே click-இல் tests ஓட்ட 'Playwright Test for VS Code' extension-ஐ நிறுவுங்கள்."),
            C("எல்லாவற்றையும் சரிபாருங்கள்"),
            H("உருவாக்கப்பட்ட folder எப்படி இருக்கும்"),
            C("Project அமைப்பு"),
            Tip("`node_modules`, `playwright-report`, `test-results` ஆகியவற்றை `.gitignore`-இல் சேருங்கள். Init command ஏற்கனவே இதைச் செய்துவிடும்."),
            H("Playwright-ஐ update செய்தல்"),
            C("Upgrade"),
            Warn("'Executable doesn't exist' என்று வந்தால், `npx playwright install` ஓட்ட மறந்துவிட்டீர்கள். Browsers, npm package-இலிருந்து தனி."),
        ),
        quiz = listOf(
            Quiz("`npx playwright install` என்ன செய்கிறது?", listOf("Browsers-ஐ download செய்கிறது", "Node-ஐ நிறுவுகிறது", "Tests-ஐ உருவாக்குகிறது"), 0, "npm package-இல் browsers இல்லை; இந்த command அவற்றைக் கொண்டுவரும்."),
            Quiz("`node_modules`-ஐ git-இல் commit செய்ய வேண்டுமா?", listOf("ஆம்", "இல்லை", "வெள்ளிக்கிழமை மட்டும்"), 1, "package.json-இலிருந்து `npm install` அதை மீண்டும் உருவாக்கும்."),
        ),
    ),
    "first-test" to ExtraTr(
        deep = listOf(
            H("ஒரு test-இன் அமைப்பு, வரி வரியாக"),
            C("குறிப்புகளுடன்"),
            P("ஒவ்வொரு test-உம் ஒரே வடிவம்: Arrange (எங்காவது செல்), Act (click, type), Assert (சரிபார்). ஒரு test-க்கு ஒரு முக்கிய யோசனை மட்டும் வையுங்கள்."),
            H("நிஜமான ஒன்று: docs-இல் தேடுதல்"),
            C("Search test"),
            H("முடிவுகள் எங்கே போகும்?"),
            P("Terminal-இல் pass/fail தெரியும். HTML report `playwright-report/`-இல் எழுதப்படும். Configure செய்திருந்தால், failures-இன் screenshots, traces `test-results/`-இல் சேமிக்கப்படும்."),
            Tip("`npx playwright test --ui` மூலம் இந்த test-ஐ time-travel timeline-உடன் ஓட்டிப் பார்க்கலாம். கற்றுக்கொள்ள இதுவே சிறந்த வழி."),
            Warn("ஒரு file-இல் test பெயர்கள் தனித்துவமாக இருக்க வேண்டும். 'test1' அல்ல, 'user can sign in' போல நடத்தையை விவரியுங்கள்."),
        ),
        quiz = listOf(
            Quiz("நல்ல test-இன் மூன்று படிகள் என்ன?", listOf("Arrange, Act, Assert", "Read, Write, Run", "Click, Click, Click"), 0, "தயார் செய், ஏதாவது செய், முடிவைச் சரிபார்."),
            Quiz("`toHaveURL(/intro/)` எதைச் சரிபார்க்கிறது?", listOf("URL சரியாக intro என்று இருக்க வேண்டும்", "URL-இல் intro இருக்க வேண்டும்", "Title-இல் intro இருக்க வேண்டும்"), 1, "/intro/ regex அந்த சொல் உள்ள எந்த URL-உடனும் பொருந்தும்."),
        ),
    ),
    "ts-vars" to ExtraTr(
        deep = listOf(
            H("நீங்கள் உண்மையில் பயன்படுத்தப்போகும் types"),
            Table(
                listOf("Type", "எடுத்துக்காட்டு", "Java இணை"),
                listOf(
                    listOf("`string`", "'hello'", "String"),
                    listOf("`number`", "42, 3.14", "int / double"),
                    listOf("`boolean`", "true", "boolean"),
                    listOf("`string[]`", "['a','b']", "List<String>"),
                    listOf("`any`", "எதுவும்", "Object (தவிர்க்கவும்!)"),
                    listOf("`unknown`", "பாதுகாப்பான 'எதுவும்'", "Object + cast"),
                ),
            ),
            H("Union types மற்றும் null"),
            P("ஒரு variable பல types-இல் ஒன்றாக இருக்கலாம். `|` (pipe) பயன்படுத்துங்கள். 'மதிப்பு அல்லது ஒன்றும் இல்லை' என்பதற்கு இது மிகவும் பொதுவானது."),
            C("Unions"),
            H("சமத்துவம் மற்றும் truthiness"),
            C("கண்டிப்பாக இருங்கள்"),
            Tip("TypeScript-ஐ யூகிக்க விடுங்கள். `const url = 'https://x.com'` ஏற்கனவே `string`. Function parameters-க்கும், compiler யூகிக்க முடியாதவற்றுக்கும் மட்டும் types சேருங்கள்."),
            Warn("`const` objects-ஐ உறைய வைக்காது: `const user = {}; user.name = 'x'` வேலை செய்யும். Variable-ஐ வேறொன்றுக்குச் சுட்டுவதை மட்டுமே தடுக்கும்."),
        ),
        quiz = listOf(
            Quiz("எந்த ஒப்பீட்டை விரும்ப வேண்டும்?", listOf("==", "===", "="), 1, "`===` உங்களுக்குத் தெரியாமல் types-ஐ மாற்றாது."),
            Quiz("`string | undefined` என்றால் என்ன?", listOf("ஒரு string அல்லது ஒன்றும் இல்லை", "இரண்டு strings", "ஒரு பிழை"), 0, "மதிப்பு இல்லாமலும் இருக்கலாம்."),
        ),
    ),
    "ts-functions" to ExtraTr(
        deep = listOf(
            H("Optional மற்றும் default parameters"),
            C("நெகிழ்வான parameters"),
            H("Functions என்பவை மதிப்புகள்"),
            P("Java-வில் objects-ஐ அனுப்புவீர்கள். TypeScript-இல் ஒரு function-ஐ இன்னொரு function-க்கும் அனுப்பலாம். `test('name', async () => {...})` இப்படித்தான் வேலை செய்கிறது: இரண்டாவது argument ஒரு function; அதை Playwright பிறகு ஓட்டும்."),
            C("Functions-ஐ அனுப்புதல்"),
            H("Async functions, Promises-ஐத் தரும்"),
            C("Tests-இல் helper function"),
            Tip("Page Objects-க்குப் போவதற்கு முன், திரும்பத் திரும்ப எழுதுவதைத் தவிர்க்க helper functions எளிமையான வழி."),
            Warn("`function` vs arrow: arrow functions-க்குத் தனி `this` கிடையாது. Tests-இல் இது அரிதாகவே முக்கியம்; ஆனால் classes-க்குள் நினைவில் வையுங்கள்."),
        ),
        quiz = listOf(
            Quiz("`wait(ms: number = 1000)` என்றால் என்ன?", listOf("ms-இன் default 1000", "ms கண்டிப்பாக 1000", "ms ஒரு optional text"), 0, "அழைப்பவர் argument தராவிட்டால் default மதிப்பு பயன்படுத்தப்படும்."),
            Quiz("`test('x', async () => {...})` ஏன் வேலை செய்கிறது?", listOf("Functions-ஐ மதிப்புகளாக அனுப்பலாம்", "மாயம்", "அது Java வசதி"), 0, "உங்கள் function-ஐ Playwright பெற்று, தேவைப்படும்போது ஓட்டும்."),
        ),
    ),
    "ts-objects" to ExtraTr(
        deep = listOf(
            H("Interfaces: ஒரு object-இன் வடிவத்தை விவரி"),
            P("`interface` ஒரு ஒப்பந்தம்: 'இந்த type-இன் object-க்கு இந்த fields இருக்கும்'. Getters, setters இல்லாத Java POJO போல."),
            C("interface"),
            H("பயனுள்ள array methods"),
            C("map, filter, find"),
            H("Spread மற்றும் optional chaining"),
            C("... மற்றும் ?."),
            Tip("Config-இல் `...devices['Desktop Chrome']` பார்த்தீர்கள். அது spread operator: settings-ஐ உங்கள் object-க்குள் நகலெடுக்கிறது."),
            Warn("இல்லாத property-ஐ படித்தால் Java-வின் NullPointerException போல exception வராது; `undefined` கிடைக்கும். Bugs அமைதியாக மறைந்திருக்கும்; அதனால் உங்கள் objects-க்கு type கொடுங்கள்."),
        ),
        quiz = listOf(
            Quiz("`{ ...base, retries: 3 }` என்ன செய்கிறது?", listOf("base-ஐ நகலெடுத்து retries-ஐ மாற்றுகிறது", "base-ஐ அழிக்கிறது", "பிழையை வீசுகிறது"), 0, "Spread fields-ஐ நகலெடுக்கும்; பின்னால் வரும் keys முன்னவற்றை மாற்றும்."),
            Quiz("address undefined ஆக இருக்கும்போது `user.address?.city` தருவது...", listOf("ஒரு crash", "undefined", "ஒரு வெற்று string"), 1, "Optional chaining பாதுகாப்பாக நின்றுவிடும்."),
        ),
    ),
    "ts-async" to ExtraTr(
        deep = listOf(
            H("Promise என்றால் என்ன?"),
            P("Promise என்பது இன்னும் தயாராகாத ஒரு முடிவைக் குறிக்கும் object. அது pending, fulfilled (வெற்றி) அல்லது rejected (பிழை) ஆக இருக்கும். `await` அதைத் திறந்து தரும்."),
            C("Promise நிலைகள்"),
            H("ஒரே நேரத்தில் செய்தல்"),
            P("வரிசையான awaits ஒன்றன்பின் ஒன்றாக ஓடும். `Promise.all` அனைத்தையும் சேர்த்து ஓட்டி, எல்லாம் முடியும் வரை காத்திருக்கும். 'click செய்து புதிய tab-க்குக் காத்திரு' போன்ற முறைகளுக்கு இதைப் பயன்படுத்துவீர்கள்."),
            C("Promise.all"),
            H("பிழைகளைக் கையாளுதல்"),
            C("try / catch / finally"),
            H("await எங்கே தவறாகும்"),
            C("பொதுவான தவறுகள்"),
            Warn("`forEach`-க்குள் `await` இருந்தால் அது காத்திருக்காது. படிகள் வரிசையில் நடக்க வேண்டுமென்றால் எப்போதும் `for...of` loop பயன்படுத்துங்கள்."),
        ),
        quiz = listOf(
            Quiz("Promise.all என்ன செய்கிறது?", listOf("Promises-ஐ சேர்த்து ஓட்டி, எல்லாவற்றுக்கும் காத்திருக்கிறது", "Promises-ஐ ரத்து செய்கிறது", "அவற்றை ஒரு loop-இல் ஓட்டுகிறது"), 0, "ஒவ்வொரு promise-உம் முடியும் வரை அது காத்திருக்கும்."),
            Quiz("`forEach(async ...)` ஏன் ஆபத்தானது?", listOf("ஒவ்வொரு await-க்கும் காத்திருக்காது", "மிகவும் மெதுவானது", "அனுமதி இல்லை"), 0, "forEach திரும்பும் promises-ஐ புறக்கணிக்கும்; அதனால் படிகள் ஒன்றோடொன்று கலக்கும். for...of பயன்படுத்துங்கள்."),
        ),
    ),
    "ts-classes" to ExtraTr(
        deep = listOf(
            H("Access modifiers மற்றும் readonly"),
            C("public / private / readonly"),
            H("Inheritance (பரம்பரை)"),
            C("extends"),
            H("Default vs named exports"),
            C("இரண்டு வகைகள்"),
            Tip("ஒரு file-க்கு ஒரு class; file பெயர் class பெயராகவே. Java-வில் நீங்கள் வைத்திருக்கும் அதே ஒழுங்குப் பழக்கம்."),
            Warn("Import paths relative ஆனவை. `'./LoginPage'` என்றால் அதே folder; `'../pages/LoginPage'` என்றால் முதலில் மேலே செல். `.ts` extension சேர்க்காதீர்கள்."),
        ),
        quiz = listOf(
            Quiz("`protected` எதை அனுமதிக்கிறது?", listOf("Class-க்குள்ளும் அதன் child classes-இலும் access", "எங்கிருந்தும் access", "எதுவும் இல்லை"), 0, "Java-வின் protected போலவே (package access தவிர)."),
            Quiz("Import paths-இல் `.ts` எழுத வேண்டுமா?", listOf("ஆம்", "இல்லை", "classes-க்கு மட்டும்"), 1, "Extension-ஐ விட்டுவிடுங்கள்."),
        ),
    ),
    "test-structure" to ExtraTr(
        deep = listOf(
            H("எல்லா hooks"),
            Table(
                listOf("Playwright", "TestNG", "எப்போது ஓடும்"),
                listOf(
                    listOf("`beforeAll`", "@BeforeClass", "ஒரு worker-க்கு, ஒரு describe-க்கு ஒருமுறை"),
                    listOf("`beforeEach`", "@BeforeMethod", "ஒவ்வொரு test-க்கு முன்"),
                    listOf("`afterEach`", "@AfterMethod", "ஒவ்வொரு test-க்கு பின்"),
                    listOf("`afterAll`", "@AfterClass", "கடைசியில் ஒருமுறை"),
                ),
            ),
            Warn("`beforeAll`-க்கு `page` கிடைக்காது (பகிரப்பட்ட tab இல்லை). Tests தனிமைப்படுத்தப்பட்டவை என்பதால் `beforeEach`-ஐ விரும்புங்கள்."),
            H("உள்ளமைந்த groups"),
            C("describe-க்குள் describe"),
            H("Skip, focus, எதிர்பார்த்த தோல்வி"),
            C("Annotations"),
            Tip("Config-இல் `forbidOnly: !!process.env.CI` வையுங்கள்; அப்போது மறந்துபோன `test.only`, மற்ற எல்லா tests-ஐயும் அமைதியாகத் தவிர்ப்பதற்குப் பதில் build-ஐ fail செய்யும்."),
        ),
        quiz = listOf(
            Quiz("எந்த hook பாதுகாப்பாக `page`-ஐத் தரும்?", listOf("beforeAll", "beforeEach", "இரண்டும் இல்லை"), 1, "beforeEach ஒவ்வொரு test-க்கும் ஓடும்; அங்கே ஒவ்வொரு test-க்கும் தனி page உண்டு."),
            Quiz("`test.fixme` என்ன செய்கிறது?", listOf("தெரிந்த bug உள்ள test-ஐத் தவிர்க்கிறது", "Test-ஐச் சரிசெய்கிறது", "இரண்டு முறை ஓட்டுகிறது"), 0, "அந்த test-ஐ 'தெரிந்த பிரச்சினை' என்று குறித்து, தவிர்க்கிறது."),
        ),
    ),
    "run-tests" to ExtraTr(
        deep = listOf(
            H("பயனுள்ள flags"),
            Table(
                listOf("Flag", "என்ன செய்கிறது"),
                listOf(
                    listOf("`--headed`", "Browser-ஐக் காட்டு"),
                    listOf("`--workers=1`", "Tests-ஐ ஒவ்வொன்றாக ஓட்டு"),
                    listOf("`--retries=2`", "Failures-ஐ மீண்டும் முயற்சி செய்"),
                    listOf("`--repeat-each=10`", "ஒவ்வொரு test-ஐயும் 10 முறை ஓட்டு (flakiness-ஐத் தேட)"),
                    listOf("`--last-failed`", "Fail ஆனவற்றை மட்டும் மீண்டும் ஓட்டு"),
                    listOf("`--reporter=list`", "எளிய console output"),
                    listOf("`--grep-invert @slow`", "ஒரு tag உள்ள tests-ஐத் தவிர்"),
                    listOf("`--max-failures=1`", "முதல் failure-க்குப் பிறகு நிறுத்து"),
                ),
            ),
            H("வரி எண் மூலம் ஒரு test-ஐ ஓட்டுதல்"),
            C("file:line"),
            H("VS Code extension"),
            P("'Playwright Test for VS Code' extension ஒவ்வொரு test-க்கும் அருகில் பச்சை play button, Testing sidebar, 'Show browser' checkbox, 'Pick locator' கருவி ஆகியவற்றைச் சேர்க்கும். உங்கள் test-இல் breakpoints வைத்து debug icon-ஐ அழுத்துங்கள்."),
            H("Fail ஆகும் test-ஐ debug செய்தல்"),
            C("Inspector"),
            Tip("சந்தேகமான test-இல் `--repeat-each=20 --workers=4` பயன்படுத்தி, அது உண்மையில் எவ்வளவு flaky என்று கண்டறியுங்கள்."),
        ),
        quiz = listOf(
            Quiz("Fail ஆன tests-ஐ மட்டும் எப்படி மீண்டும் ஓட்டுவது?", listOf("--last-failed", "--again", "--retry-only"), 0, "`--last-failed` முந்தைய run-ஐ நினைவில் வைத்திருக்கும்."),
            Quiz("`page.pause()` எதைத் திறக்கிறது?", listOf("Playwright Inspector", "Report", "ஒன்றும் இல்லை"), 0, "Test உறைந்து நிற்கும்; Inspector மூலம் படிப்படியாகச் செல்லலாம், locators-ஐத் தேர்வு செய்யலாம்."),
        ),
    ),
    "reports-trace" to ExtraTr(
        deep = listOf(
            H("Reporters"),
            C("Config-இல் reporters-ஐத் தேர்ந்தெடுங்கள்"),
            H("Trace modes"),
            Table(
                listOf("மதிப்பு", "பொருள்"),
                listOf(
                    listOf("`on`", "எப்போதும் பதிவு (மெதுவானது, பெரியது)"),
                    listOf("`off`", "ஒருபோதும் இல்லை"),
                    listOf("`on-first-retry`", "Test retry ஆகும்போது பதிவு. CI-க்குச் சிறந்த default"),
                    listOf("`retain-on-failure`", "எப்போதும் பதிவு, failures-ஐ மட்டும் வைத்துக்கொள்"),
                ),
            ),
            H("Trace-இன் உள்ளே என்ன இருக்கிறது?"),
            P("ஒவ்வொரு action-இன் timeline, dev tools-ஆல் பார்க்கக்கூடிய முன்/பின் DOM snapshot, console logs, bodies உடன் network requests, source code வரி, screenshots. `trace.zip`-ஐ ஒரு teammate-க்கும் அனுப்பலாம்."),
            C("திறக்கும் வழி"),
            Tip("HTML report-இல் failed tests-உடன் trace இணைக்கப்பட்டிருக்கும். முழுக் கதையையும் காண அதை click செய்யுங்கள். 'என் machine-இல் வேலை செய்கிறது' வாதங்களில் 80%-ஐ இது மாற்றுகிறது."),
            Warn("Traces-இல் network calls-இலிருந்து passwords, tokens இருக்கலாம். CI artifacts-ஐ public-ஆக upload செய்யாதீர்கள்."),
        ),
        quiz = listOf(
            Quiz("CI-க்கு நல்ல default trace mode எது?", listOf("on-first-retry", "on", "off"), 0, "ஆராய வேண்டிய நிலை வரும்போது மட்டும் பதிவு செய்யும்."),
            Quiz("Jenkins-க்கு உதவும் reporter எது?", listOf("junit", "list", "dot"), 0, "Jenkins JUnit XML-ஐப் புரிந்துகொள்ளும்."),
        ),
    ),
)
