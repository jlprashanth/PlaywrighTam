package com.example.playwright.data

// Tenglish (4 = Telugu in English letters): deep dives + extra quizzes, part A (matches DeepA.kt). Code blocks are C("title").

val deepTnA: Map<String, ExtraTr> = mapOf(
    "what-is" to ExtraTr(
        deep = listOf(
            H("Idi nijanga ela pani chestundi?"),
            P("Selenium oka separate driver program tho (chromedriver) HTTP dwara matladutundi, aa driver browser tho matladutundi. Playwright madhyalo unnadanni skip chesi, browser tho oka fast connection open ga unchi, daani dwara commands pampistundi. Anduke idi fast; network calls, page loads lantivi kuda gamanincha galadu."),
            Table(
                listOf("Concept", "Selenium", "Playwright"),
                listOf(
                    listOf("Browser tho matlade vidhanam", "Driver + HTTP", "Direct WebSocket connection"),
                    listOf("Browsers", "Driver unna edaina", "Chromium, Firefox, WebKit (tho paatu vastayi)"),
                    listOf("Waiting", "Meere waits rayali", "Built-in auto-wait"),
                    listOf("Test runner", "TestNG / JUnit (meeru add cheyali)", "`@playwright/test` (tho paatu vastundi)"),
                ),
            ),
            H("4 building blocks"),
            P("`Browser` ante motham browser program. `BrowserContext` ante daani lopala oka clean profile (incognito window laaga: sontha cookies, sontha storage). `Page` ante oka context lopala unde oka tab. `Locator` ante page lo unna oka daanni choopinche pointer."),
            C("Browser → Context → Page"),
            Tip("`@playwright/test` vaadinappudu, prati test ki browser, context, page ni ade create chesi istundi. Scripts lo matrame meeru cheththo chestaaru."),
            Warn("Playwright, Selenium tho compatible kaadu. Mee Java `WebDriver` code ni malli vaadaleru, kani ideas (locators, waits, page objects) anni alage vastayi."),
        ),
        quiz = listOf(
            Quiz("BrowserContext denilaa untundi?", listOf("Oka incognito profile", "Oka browser tab", "Oka Maven module"), 0, "Oka context ki sontha cookies, storage untayi; migata contexts nundi veruga untundi."),
            Quiz("Oka BrowserContext lo enni Pages undochu?", listOf("Sarigga okati", "Meeru open chesinanni", "Okkati ledu"), 1, "Oka context chala tabs (pages) ni unchukogaladu."),
        ),
    ),
    "why-better" to ExtraTr(
        deep = listOf(
            H("Flaky tests: enduku vastayi?"),
            P("Code marchakundane oka test konnisarlu pass, konnisarlu fail aithe adi flaky. Saadharana karanam: test, page kante fast ga nadustundi. Button ready avvakamunde click chestundi, leda text rakamunde chaduvutundi. Playwright auto-wait, retry chese assertions veetilo chalavaatini design thone sari chestayi."),
            H("Isolation = reliable tests"),
            P("Prati Playwright test kuda daani sontha fresh context lo nadustundi. Test A lo login cheyadam test B ni prabhavitam cheyadu. Selenium lo cookies clear cheyadam leda driver ni restart cheyadam meeru gurtunchukovalsi vachchedi."),
            H("Speed"),
            C("4 workers lo 100 tests ni parallel ga nadapadam"),
            P("Contexts chala chavaka (milliseconds) mariyu workers separate processes kabatti, Selenium lo ganta pattina suites tarachuga konni nimishallo ayipotayi."),
            Table(
                listOf("Feature", "Selenium prapancham", "Playwright"),
                listOf(
                    listOf("Reports", "Extent / Allure plugin", "HTML report built-in"),
                    listOf("Debugging", "Breakpoints + prarthanalu", "Inspector, UI mode, Trace Viewer"),
                    listOf("API calls", "RestAssured", "`request` fixture"),
                    listOf("Network mock", "Proxy tools", "`page.route()`"),
                    listOf("Tests record", "Selenium IDE", "`codegen`"),
                ),
            ),
            Warn("Playwright magic kaadu. Chedda locators, shared test data, marchipoyina `await` inka flaky tests ki karanamavutayi. Prati daanni ela tappinchukovaalo nerchukundam."),
        ),
        quiz = listOf(
            Quiz("Playwright tests okadanitho okati enduku isolate ayi untayi?", listOf("Prati test ki fresh browser context vastundi", "Anni oke tab ni share chestayi", "Okadani tarvata okati nadustayi"), 0, "Prati test ki kotha context unte cookies leda state leak avvadu."),
            Quiz("Flaky tests ki saadharana karanam edi?", listOf("Parallel ga nadapadam matrame", "Page ready avvakamunde act cheyadam", "TypeScript vaadatam"), 1, "Page kante fast ga undatame classic karanam."),
        ),
    ),
    "setup" to ExtraTr(
        deep = listOf(
            H("Install, step by step"),
            P("1) nodejs.org nundi Node.js (LTS version) install cheyandi. 2) VS Code install cheyandi. 3) Oka folder create chesi init command run cheyandi. 4) Oka click tho tests nadapadaniki 'Playwright Test for VS Code' extension install cheyandi."),
            C("Anni check cheyandi"),
            H("Generate ayina folder ela untundi"),
            C("Project layout"),
            Tip("`node_modules`, `playwright-report`, `test-results` ni `.gitignore` lo add cheyandi. Init command ippatike mee kosam aa pani chestundi."),
            H("Playwright ni update cheyadam"),
            C("Upgrade"),
            Warn("'Executable doesn't exist' ani vaste, `npx playwright install` run cheyadam marchipoyaru. Browsers, npm package nundi veru."),
        ),
        quiz = listOf(
            Quiz("`npx playwright install` em chestundi?", listOf("Browsers ni download chestundi", "Node ni install chestundi", "Tests ni create chestundi"), 0, "npm package lo browsers undavu; ee command vaatini teestundi."),
            Quiz("`node_modules` ni git lo commit cheyala?", listOf("Avunu", "Vaddu", "Sukravaaralu matrame"), 1, "package.json nundi `npm install` daanni malli create chestundi."),
        ),
    ),
    "first-test" to ExtraTr(
        deep = listOf(
            H("Oka test yokka anatomy, line by line"),
            C("Annotated"),
            P("Prati test kuda ade shape: Arrange (ekkadikaina vellu), Act (click, type), Assert (check cheyyi). Oka test ki oka main idea matrame unchandi."),
            H("Realistic okati: docs lo search"),
            C("Search test"),
            H("Results ekkadiki veltayi?"),
            P("Terminal lo pass/fail kanipistundi. HTML report `playwright-report/` lo rayabadutundi. Configure chesi unte, failures yokka screenshots, traces `test-results/` lo save avutayi."),
            Tip("`npx playwright test --ui` tho ee test ni time-travel timeline tho nadipi chudandi. Nerchukovadaniki ide best margam."),
            Warn("Oka file lo test names unique ga undali. 'test1' kaadu, 'user can sign in' laaga behaviour ni describe cheyandi."),
        ),
        quiz = listOf(
            Quiz("Manchi test yokka moodu steps enti?", listOf("Arrange, Act, Assert", "Read, Write, Run", "Click, Click, Click"), 0, "Set up cheyyi, edaina cheyyi, result check cheyyi."),
            Quiz("`toHaveURL(/intro/)` denni check chestundi?", listOf("URL sarigga intro ayi undali", "URL lo intro undali", "Title lo intro undali"), 1, "/intro/ regex aa padam unna e URL tho aina match avutundi."),
        ),
    ),
    "ts-vars" to ExtraTr(
        deep = listOf(
            H("Meeru nijanga vaade types"),
            Table(
                listOf("Type", "Example", "Java janta"),
                listOf(
                    listOf("`string`", "'hello'", "String"),
                    listOf("`number`", "42, 3.14", "int / double"),
                    listOf("`boolean`", "true", "boolean"),
                    listOf("`string[]`", "['a','b']", "List<String>"),
                    listOf("`any`", "edaina", "Object (vaddu!)"),
                    listOf("`unknown`", "safe ayina 'edaina'", "Object + cast"),
                ),
            ),
            H("Union types mariyu null"),
            P("Oka variable chala types lo okati kaavachu. `|` (pipe) vaadandi. 'value leda emi ledu' ki idi chala common."),
            C("Unions"),
            H("Equality mariyu truthiness"),
            C("Strict ga undandi"),
            Tip("TypeScript ni infer cheyanivvandi. `const url = 'https://x.com'` ippatike `string`. Function parameters ki, compiler oohinchalenivaatiki matrame types add cheyandi."),
            Warn("`const` objects ni freeze cheyadu: `const user = {}; user.name = 'x'` pani chestundi. Variable ni verokadaniki point cheyakunda matrame aaputundi."),
        ),
        quiz = listOf(
            Quiz("E comparison ni prefer cheyali?", listOf("==", "===", "="), 1, "`===` meeku teliyakunda types ni marchadu."),
            Quiz("`string | undefined` ante enti?", listOf("Oka string leda emi ledu", "Rendu strings", "Oka error"), 0, "Value lekunda kuda undochu."),
        ),
    ),
    "ts-functions" to ExtraTr(
        deep = listOf(
            H("Optional mariyu default parameters"),
            C("Flexible parameters"),
            H("Functions kuda values"),
            P("Java lo objects ni pampistaru. TypeScript lo oka function ni inko function ki kuda pampochu. `test('name', async () => {...})` ilaage pani chestundi: rendo argument oka function; daanni Playwright tarvata nadputundi."),
            C("Functions ni pass cheyadam"),
            H("Async functions Promises ni return chestayi"),
            C("Tests lo helper function"),
            Tip("Page Objects daaka velle mundu, malli malli rayadam tappinchukovadaniki helper functions sulabhamaina margam."),
            Warn("`function` vs arrow: arrow functions ki sontha `this` undadu. Tests lo idi chala araduga mukhyam, kani classes lopala gurtunchukondi."),
        ),
        quiz = listOf(
            Quiz("`wait(ms: number = 1000)` ante enti?", listOf("ms yokka default 1000", "ms tappanisarigaa 1000", "ms oka optional text"), 0, "Call chese vallu argument ivvakapote default value vaadabadutundi."),
            Quiz("`test('x', async () => {...})` enduku pani chestundi?", listOf("Functions ni values ga pass cheyochu", "Magic", "Idi Java feature"), 0, "Mee function ni Playwright teesukuni, avasaramainappudu nadputundi."),
        ),
    ),
    "ts-objects" to ExtraTr(
        deep = listOf(
            H("Interfaces: oka object yokka shape ni describe cheyyi"),
            P("`interface` oka contract: 'ee type object ki ee fields untayi'. Getters, setters leni Java POJO laaga."),
            C("interface"),
            H("Handy array methods"),
            C("map, filter, find"),
            H("Spread mariyu optional chaining"),
            C("... mariyu ?."),
            Tip("Config lo `...devices['Desktop Chrome']` chusaru. Adi spread operator: settings ni mee object loki copy chestundi."),
            Warn("Leni property ni chaduvite Java NullPointerException laaga exception raadu; `undefined` vastundi. Bugs nishabdanga daakkuntayi, anduke mee objects ki type ivvandi."),
        ),
        quiz = listOf(
            Quiz("`{ ...base, retries: 3 }` em chestundi?", listOf("base ni copy chesi retries ni override chestundi", "base ni delete chestundi", "Error throw chestundi"), 0, "Spread fields ni copy chestundi; tarvata vache keys mundunnavatini override chestayi."),
            Quiz("address undefined ainappudu `user.address?.city` ichchedi...", listOf("Oka crash", "undefined", "Oka empty string"), 1, "Optional chaining safe ga aagipotundi."),
        ),
    ),
    "ts-async" to ExtraTr(
        deep = listOf(
            H("Promise ante enti?"),
            P("Promise ante inka ready kaani oka result ni suchinche object. Adi pending, fulfilled (success) leda rejected (error) ga untundi. `await` daanni teriche istundi."),
            C("Promise states"),
            H("Okesari panulu cheyadam"),
            P("Sequential awaits okadani tarvata okati nadustayi. `Promise.all` annintini kalipi nadipi, anni ayyedaaka wait chestundi. 'Click chesi kotha tab kosam wait cheyyi' lanti patterns ki deenni vaadataru."),
            C("Promise.all"),
            H("Errors ni handle cheyadam"),
            C("try / catch / finally"),
            H("await ekkada tappu avutundi"),
            C("Common mistakes"),
            Warn("`forEach` lopala `await` unte adi wait cheyadu. Steps order lo jaragalante eppudu `for...of` loop vaadandi."),
        ),
        quiz = listOf(
            Quiz("Promise.all em chestundi?", listOf("Promises ni kalipi nadipi annintiki wait chestundi", "Promises ni cancel chestundi", "Vaatini oka loop lo nadputundi"), 0, "Prati promise purtayyedaaka adi wait chestundi."),
            Quiz("`forEach(async ...)` enduku pramadakaram?", listOf("Prati await ki wait cheyadu", "Chala slow", "Anumati ledu"), 0, "forEach return ayye promises ni ignore chestundi, anduke steps overlap avutayi. for...of vaadandi."),
        ),
    ),
    "ts-classes" to ExtraTr(
        deep = listOf(
            H("Access modifiers mariyu readonly"),
            C("public / private / readonly"),
            H("Inheritance"),
            C("extends"),
            H("Default vs named exports"),
            C("Rendu rakaalu"),
            Tip("Oka file ki oka class; file peru class perey. Java lo meeru paatinche ade tidy alavaatlu."),
            Warn("Import paths relative. `'./LoginPage'` ante ade folder; `'../pages/LoginPage'` ante mundu paiki vellu. `.ts` extension add cheyakandi."),
        ),
        quiz = listOf(
            Quiz("`protected` denni allow chestundi?", listOf("Class lopala, daani child classes lo access", "Ekkadinundaina access", "Emi ledu"), 0, "Java protected laage (package access tappa)."),
            Quiz("Import paths lo `.ts` rayala?", listOf("Avunu", "Vaddu", "Classes ki matrame"), 1, "Extension ni vadileyandi."),
        ),
    ),
    "test-structure" to ExtraTr(
        deep = listOf(
            H("Anni hooks"),
            Table(
                listOf("Playwright", "TestNG", "Eppudu nadustundi"),
                listOf(
                    listOf("`beforeAll`", "@BeforeClass", "Oka worker ki, oka describe ki okkasari"),
                    listOf("`beforeEach`", "@BeforeMethod", "Prati test ki mundu"),
                    listOf("`afterEach`", "@AfterMethod", "Prati test tarvata"),
                    listOf("`afterAll`", "@AfterClass", "Chivarlo okkasari"),
                ),
            ),
            Warn("`beforeAll` ki `page` raadu (shared tab ledu). Tests isolate ayi untayi kabatti `beforeEach` ni prefer cheyandi."),
            H("Nested groups"),
            C("describe lopala describe"),
            H("Skip, focus, expected failure"),
            C("Annotations"),
            Tip("Config lo `forbidOnly: !!process.env.CI` pettandi; appudu marchipoyina `test.only`, migata anni tests ni nishabdanga skip cheyadaniki badulu build ni fail chestundi."),
        ),
        quiz = listOf(
            Quiz("E hook safe ga `page` istundi?", listOf("beforeAll", "beforeEach", "Rendu kaadu"), 1, "beforeEach prati test ki nadustundi; akkada prati test ki sontha page untundi."),
            Quiz("`test.fixme` em chestundi?", listOf("Telisina bug unna test ni skip chestundi", "Test ni fix chestundi", "Rendusarlu nadputundi"), 0, "Aa test ni 'telisina samasya' ga mark chesi skip chestundi."),
        ),
    ),
    "run-tests" to ExtraTr(
        deep = listOf(
            H("Useful flags"),
            Table(
                listOf("Flag", "Em chestundi"),
                listOf(
                    listOf("`--headed`", "Browser ni choopinchu"),
                    listOf("`--workers=1`", "Tests ni okkokkatiga nadapu"),
                    listOf("`--retries=2`", "Failures ni malli try cheyyi"),
                    listOf("`--repeat-each=10`", "Prati test ni 10 sarlu nadapu (flakiness vetakadaniki)"),
                    listOf("`--last-failed`", "Fail ayinavatini matrame malli nadapu"),
                    listOf("`--reporter=list`", "Simple console output"),
                    listOf("`--grep-invert @slow`", "Oka tag unna tests ni skip cheyyi"),
                    listOf("`--max-failures=1`", "Modati failure tarvata aapu"),
                ),
            ),
            H("Line number tho oka test ni nadapadam"),
            C("file:line"),
            H("VS Code extension"),
            P("'Playwright Test for VS Code' extension prati test pakkana green play button, Testing sidebar, 'Show browser' checkbox, 'Pick locator' tool ni add chestundi. Mee test lo breakpoints petti debug icon nokkandi."),
            H("Fail avutunna test ni debug cheyadam"),
            C("Inspector"),
            Tip("Anumanamga unna test meeda `--repeat-each=20 --workers=4` vaadi, adi nijanga entha flaky ani telusukondi."),
        ),
        quiz = listOf(
            Quiz("Fail ayina tests ni matrame malli ela nadapali?", listOf("--last-failed", "--again", "--retry-only"), 0, "`--last-failed` mundu run ni gurtunchukuntundi."),
            Quiz("`page.pause()` denni open chestundi?", listOf("Playwright Inspector", "Report", "Emi ledu"), 0, "Test freeze avutundi; Inspector tho step chestu locators ni pick cheyochu."),
        ),
    ),
    "reports-trace" to ExtraTr(
        deep = listOf(
            H("Reporters"),
            C("Config lo reporters ni enchukondi"),
            H("Trace modes"),
            Table(
                listOf("Value", "Artham"),
                listOf(
                    listOf("`on`", "Eppudu record (slow, peddadi)"),
                    listOf("`off`", "Eppudu kaadu"),
                    listOf("`on-first-retry`", "Test retry ainappudu record. CI ki best default"),
                    listOf("`retain-on-failure`", "Eppudu record, failures ni matrame unchu"),
                ),
            ),
            H("Trace lopala emundi?"),
            P("Prati action yokka timeline, dev tools tho chudagalige mundu/tarvata DOM snapshot, console logs, bodies tho network requests, source code line, screenshots. `trace.zip` ni teammate ki kuda pampochu."),
            C("Open chese vidhanam"),
            Tip("HTML report lo failed tests ki trace attach ayi untundi. Purti katha chudadaniki daanni click cheyandi. 'Naa machine lo pani chestundi' vaadanallo 80% deenito potayi."),
            Warn("Traces lo network calls nundi passwords, tokens undochu. CI artifacts ni public ga upload cheyakandi."),
        ),
        quiz = listOf(
            Quiz("CI ki manchi default trace mode edi?", listOf("on-first-retry", "on", "off"), 0, "Edaina investigate cheyalsinappudu matrame record chestundi."),
            Quiz("Jenkins ki upayogapade reporter edi?", listOf("junit", "list", "dot"), 0, "Jenkins ki JUnit XML artham avutundi."),
        ),
    ),
)
