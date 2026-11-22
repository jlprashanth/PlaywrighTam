package com.example.playwright.data

// Telugu (3): deep dives + extra quizzes, part A (matches DeepA.kt). Code blocks are C("title"); code comes from English.

val deepTeA: Map<String, ExtraTr> = mapOf(
    "what-is" to ExtraTr(
        deep = listOf(
            H("ఇది నిజంగా ఎలా పని చేస్తుంది?"),
            P("Selenium ఒక separate driver program తో (chromedriver) HTTP ద్వారా మాట్లాడుతుంది, ఆ driver browser తో మాట్లాడుతుంది. Playwright మధ్యలో ఉన్నదాన్ని skip చేసి, browser తో ఒక fast connection ఓపెన్ గా ఉంచి, దాని ద్వారా commands పంపిస్తుంది. అందుకే ఇది fast; network calls, page loads లాంటివి కూడా గమనించగలదు."),
            Table(
                listOf("Concept", "Selenium", "Playwright"),
                listOf(
                    listOf("Browser తో మాట్లాడే విధానం", "Driver + HTTP", "Direct WebSocket connection"),
                    listOf("Browsers", "Driver ఉన్న ఏదైనా", "Chromium, Firefox, WebKit (తో పాటే వస్తాయి)"),
                    listOf("Waiting", "మీరే waits రాయాలి", "Built-in auto-wait"),
                    listOf("Test runner", "TestNG / JUnit (మీరు add చేయాలి)", "`@playwright/test` (తో పాటే వస్తుంది)"),
                ),
            ),
            H("4 building blocks"),
            P("`Browser` అంటే మొత్తం browser program. `BrowserContext` అంటే దాని లోపల ఒక clean profile (incognito window లాగా: సొంత cookies, సొంత storage). `Page` అంటే ఒక context లోపల ఉండే ఒక tab. `Locator` అంటే page లో ఉన్న ఒక దాన్ని చూపించే pointer."),
            C("Browser → Context → Page"),
            Tip("`@playwright/test` వాడినప్పుడు, ప్రతి test కీ browser, context, page ని అదే create చేసి ఇస్తుంది. Scripts లో మాత్రమే మీరు చేత్తో చేస్తారు."),
            Warn("Playwright, Selenium తో compatible కాదు. మీ Java `WebDriver` code ని మళ్ళీ వాడలేరు, కానీ ideas (locators, waits, page objects) అన్నీ అలాగే వస్తాయి."),
        ),
        quiz = listOf(
            Quiz("BrowserContext దేనిలా ఉంటుంది?", listOf("ఒక incognito profile", "ఒక browser tab", "ఒక Maven module"), 0, "ఒక context కి సొంత cookies, storage ఉంటాయి; మిగతా contexts నుండి వేరుగా ఉంటుంది."),
            Quiz("ఒక BrowserContext లో ఎన్ని Pages ఉండొచ్చు?", listOf("సరిగ్గా ఒకటి", "మీరు ఓపెన్ చేసినన్ని", "ఒక్కటీ లేదు"), 1, "ఒక context చాలా tabs (pages) ని ఉంచుకోగలదు."),
        ),
    ),
    "why-better" to ExtraTr(
        deep = listOf(
            H("Flaky tests: ఎందుకు వస్తాయి?"),
            P("Code మార్చకుండానే ఒక test కొన్నిసార్లు pass, కొన్నిసార్లు fail అయితే అది flaky. సాధారణ కారణం: test, page కంటే fast గా నడుస్తుంది. Button రెడీ అవ్వకముందే click చేస్తుంది, లేదా text రాకముందే చదువుతుంది. Playwright auto-wait, retry చేసే assertions వీటిలో చాలావాటిని design తోనే సరిచేస్తాయి."),
            H("Isolation = reliable tests"),
            P("ప్రతి Playwright test కూడా దాని సొంత fresh context లో నడుస్తుంది. Test A లో login చేయడం test B ని ప్రభావితం చేయదు. Selenium లో cookies clear చేయడం లేదా driver ని restart చేయడం మీరు గుర్తుంచుకోవాల్సి వచ్చేది."),
            H("Speed"),
            C("4 workers లో 100 tests ని parallel గా నడపడం"),
            P("Contexts చాలా చవక (milliseconds) మరియు workers separate processes కాబట్టి, Selenium లో గంట పట్టిన suites తరచుగా కొన్ని నిమిషాల్లో అయిపోతాయి."),
            Table(
                listOf("Feature", "Selenium ప్రపంచం", "Playwright"),
                listOf(
                    listOf("Reports", "Extent / Allure plugin", "HTML report built-in"),
                    listOf("Debugging", "Breakpoints + ప్రార్థనలు", "Inspector, UI mode, Trace Viewer"),
                    listOf("API calls", "RestAssured", "`request` fixture"),
                    listOf("Network mock", "Proxy tools", "`page.route()`"),
                    listOf("Tests record", "Selenium IDE", "`codegen`"),
                ),
            ),
            Warn("Playwright మ్యాజిక్ కాదు. చెడ్డ locators, shared test data, మర్చిపోయిన `await` ఇంకా flaky tests కి కారణమవుతాయి. ప్రతి దాన్ని ఎలా తప్పించుకోవాలో నేర్చుకుందాం."),
        ),
        quiz = listOf(
            Quiz("Playwright tests ఒకదానితో ఒకటి ఎందుకు isolate అయ్యి ఉంటాయి?", listOf("ప్రతి test కీ fresh browser context వస్తుంది", "అన్నీ ఒకే tab ని share చేస్తాయి", "ఒకదాని తర్వాత ఒకటి నడుస్తాయి"), 0, "ప్రతి test కీ కొత్త context ఉంటే cookies లేదా state leak అవ్వదు."),
            Quiz("Flaky tests కి సాధారణ కారణం ఏది?", listOf("Parallel గా నడపడం మాత్రమే", "Page రెడీ అవ్వకముందే act చేయడం", "TypeScript వాడటం"), 1, "Page కంటే fast గా ఉండటమే classic కారణం."),
        ),
    ),
    "setup" to ExtraTr(
        deep = listOf(
            H("Install, step by step"),
            P("1) nodejs.org నుండి Node.js (LTS version) install చేయండి. 2) VS Code install చేయండి. 3) ఒక folder క్రియేట్ చేసి init command run చేయండి. 4) ఒక click తో tests నడపడానికి 'Playwright Test for VS Code' extension install చేయండి."),
            C("అన్నీ చెక్ చేయండి"),
            H("Generate అయిన folder ఎలా ఉంటుంది"),
            C("Project layout"),
            Tip("`node_modules`, `playwright-report`, `test-results` ని `.gitignore` లో add చేయండి. Init command ఇప్పటికే మీ కోసం ఆ పని చేస్తుంది."),
            H("Playwright ని update చేయడం"),
            C("Upgrade"),
            Warn("'Executable doesn't exist' అని వస్తే, `npx playwright install` run చేయడం మర్చిపోయారు. Browsers, npm package నుండి వేరు."),
        ),
        quiz = listOf(
            Quiz("`npx playwright install` ఏం చేస్తుంది?", listOf("Browsers ని download చేస్తుంది", "Node ని install చేస్తుంది", "Tests ని create చేస్తుంది"), 0, "npm package లో browsers ఉండవు; ఈ command వాటిని తెస్తుంది."),
            Quiz("`node_modules` ని git లో commit చేయాలా?", listOf("అవును", "వద్దు", "శుక్రవారాలు మాత్రమే"), 1, "package.json నుండి `npm install` దాన్ని మళ్ళీ create చేస్తుంది."),
        ),
    ),
    "first-test" to ExtraTr(
        deep = listOf(
            H("ఒక test యొక్క anatomy, line by line"),
            C("Annotated"),
            P("ప్రతి test కూడా అదే shape: Arrange (ఎక్కడికైనా వెళ్ళు), Act (click, type), Assert (చెక్ చేయి). ఒక test కి ఒక main idea మాత్రమే ఉంచండి."),
            H("Realistic ఒకటి: docs లో search"),
            C("Search test"),
            H("Results ఎక్కడికి వెళ్తాయి?"),
            P("Terminal లో pass/fail కనిపిస్తుంది. HTML report `playwright-report/` లో రాయబడుతుంది. Configure చేసి ఉంటే, failures యొక్క screenshots, traces `test-results/` లో save అవుతాయి."),
            Tip("`npx playwright test --ui` తో ఈ test ని time-travel timeline తో నడిపి చూడండి. నేర్చుకోవడానికి ఇదే best మార్గం."),
            Warn("ఒక file లో test names unique గా ఉండాలి. 'test1' కాదు, 'user can sign in' లాగా behaviour ని describe చేయండి."),
        ),
        quiz = listOf(
            Quiz("మంచి test యొక్క మూడు steps ఏంటి?", listOf("Arrange, Act, Assert", "Read, Write, Run", "Click, Click, Click"), 0, "Set up చేయి, ఏదైనా చేయి, result చెక్ చేయి."),
            Quiz("`toHaveURL(/intro/)` దేన్ని చెక్ చేస్తుంది?", listOf("URL సరిగ్గా intro అయి ఉండాలి", "URL లో intro ఉండాలి", "Title లో intro ఉండాలి"), 1, "/intro/ regex ఆ పదం ఉన్న ఏ URL తోనైనా match అవుతుంది."),
        ),
    ),
    "ts-vars" to ExtraTr(
        deep = listOf(
            H("మీరు నిజంగా వాడే types"),
            Table(
                listOf("Type", "Example", "Java జంట"),
                listOf(
                    listOf("`string`", "'hello'", "String"),
                    listOf("`number`", "42, 3.14", "int / double"),
                    listOf("`boolean`", "true", "boolean"),
                    listOf("`string[]`", "['a','b']", "List<String>"),
                    listOf("`any`", "ఏదైనా", "Object (వద్దు!)"),
                    listOf("`unknown`", "safe అయిన 'ఏదైనా'", "Object + cast"),
                ),
            ),
            H("Union types మరియు null"),
            P("ఒక variable చాలా types లో ఒకటి కావచ్చు. `|` (pipe) వాడండి. 'value లేదా ఏమీ లేదు' కి ఇది చాలా common."),
            C("Unions"),
            H("Equality మరియు truthiness"),
            C("Strict గా ఉండండి"),
            Tip("TypeScript ని infer చేయనివ్వండి. `const url = 'https://x.com'` ఇప్పటికే `string`. Function parameters కీ, compiler ఊహించలేని వాటికీ మాత్రమే types add చేయండి."),
            Warn("`const` objects ని freeze చేయదు: `const user = {}; user.name = 'x'` పని చేస్తుంది. Variable ని వేరొకదానికి point చేయకుండా మాత్రమే ఆపుతుంది."),
        ),
        quiz = listOf(
            Quiz("ఏ comparison ని prefer చేయాలి?", listOf("==", "===", "="), 1, "`===` మీకు తెలియకుండా types ని మార్చదు."),
            Quiz("`string | undefined` అంటే ఏంటి?", listOf("ఒక string లేదా ఏమీ లేదు", "రెండు strings", "ఒక error"), 0, "Value లేకుండా కూడా ఉండొచ్చు."),
        ),
    ),
    "ts-functions" to ExtraTr(
        deep = listOf(
            H("Optional మరియు default parameters"),
            C("Flexible parameters"),
            H("Functions కూడా values"),
            P("Java లో objects ని పంపిస్తారు. TypeScript లో ఒక function ని ఇంకో function కి కూడా పంపొచ్చు. `test('name', async () => {...})` ఇలాగే పని చేస్తుంది: రెండో argument ఒక function; దాన్ని Playwright తర్వాత నడుపుతుంది."),
            C("Functions ని pass చేయడం"),
            H("Async functions Promises ని return చేస్తాయి"),
            C("Tests లో helper function"),
            Tip("Page Objects దాకా వెళ్ళే ముందు, మళ్ళీ మళ్ళీ రాయడం తప్పించుకోవడానికి helper functions సులభమైన మార్గం."),
            Warn("`function` vs arrow: arrow functions కి సొంత `this` ఉండదు. Tests లో ఇది చాలా అరుదుగా ముఖ్యం, కానీ classes లోపల గుర్తుంచుకోండి."),
        ),
        quiz = listOf(
            Quiz("`wait(ms: number = 1000)` అంటే ఏంటి?", listOf("ms యొక్క default 1000", "ms తప్పనిసరిగా 1000", "ms ఒక optional text"), 0, "Call చేసేవాళ్ళు argument ఇవ్వకపోతే default value వాడబడుతుంది."),
            Quiz("`test('x', async () => {...})` ఎందుకు పని చేస్తుంది?", listOf("Functions ని values గా pass చేయొచ్చు", "మ్యాజిక్", "ఇది Java feature"), 0, "మీ function ని Playwright తీసుకుని, అవసరమైనప్పుడు నడుపుతుంది."),
        ),
    ),
    "ts-objects" to ExtraTr(
        deep = listOf(
            H("Interfaces: ఒక object యొక్క shape ని describe చేయి"),
            P("`interface` ఒక contract: 'ఈ type object కి ఈ fields ఉంటాయి'. Getters, setters లేని Java POJO లాగా."),
            C("interface"),
            H("Handy array methods"),
            C("map, filter, find"),
            H("Spread మరియు optional chaining"),
            C("... మరియు ?."),
            Tip("Config లో `...devices['Desktop Chrome']` చూశారు. అది spread operator: settings ని మీ object లోకి copy చేస్తుంది."),
            Warn("లేని property ని చదివితే Java NullPointerException లాగా exception రాదు; `undefined` వస్తుంది. Bugs నిశ్శబ్దంగా దాక్కుంటాయి, అందుకే మీ objects కి type ఇవ్వండి."),
        ),
        quiz = listOf(
            Quiz("`{ ...base, retries: 3 }` ఏం చేస్తుంది?", listOf("base ని copy చేసి retries ని override చేస్తుంది", "base ని delete చేస్తుంది", "Error throw చేస్తుంది"), 0, "Spread fields ని copy చేస్తుంది; తర్వాత వచ్చే keys ముందున్నవాటిని override చేస్తాయి."),
            Quiz("address undefined అయినప్పుడు `user.address?.city` ఇచ్చేది...", listOf("ఒక crash", "undefined", "ఒక empty string"), 1, "Optional chaining safe గా ఆగిపోతుంది."),
        ),
    ),
    "ts-async" to ExtraTr(
        deep = listOf(
            H("Promise అంటే ఏంటి?"),
            P("Promise అంటే ఇంకా రెడీ కాని ఒక result ని సూచించే object. అది pending, fulfilled (success) లేదా rejected (error) గా ఉంటుంది. `await` దాన్ని తెరిచి ఇస్తుంది."),
            C("Promise states"),
            H("ఒకేసారి పనులు చేయడం"),
            P("Sequential awaits ఒకదాని తర్వాత ఒకటి నడుస్తాయి. `Promise.all` అన్నింటినీ కలిపి నడిపి, అన్నీ అయ్యేదాకా wait చేస్తుంది. 'Click చేసి కొత్త tab కోసం wait చేయి' లాంటి patterns కి దీన్ని వాడతారు."),
            C("Promise.all"),
            H("Errors ని handle చేయడం"),
            C("try / catch / finally"),
            H("await ఎక్కడ తప్పు అవుతుంది"),
            C("Common mistakes"),
            Warn("`forEach` లోపల `await` ఉంటే అది wait చేయదు. Steps order లో జరగాలంటే ఎప్పుడూ `for...of` loop వాడండి."),
        ),
        quiz = listOf(
            Quiz("Promise.all ఏం చేస్తుంది?", listOf("Promises ని కలిపి నడిపి అన్నింటికీ wait చేస్తుంది", "Promises ని cancel చేస్తుంది", "వాటిని ఒక loop లో నడుపుతుంది"), 0, "ప్రతి promise పూర్తయ్యేదాకా అది wait చేస్తుంది."),
            Quiz("`forEach(async ...)` ఎందుకు ప్రమాదకరం?", listOf("ప్రతి await కీ wait చేయదు", "చాలా slow", "అనుమతి లేదు"), 0, "forEach return అయ్యే promises ని ignore చేస్తుంది, అందుకే steps overlap అవుతాయి. for...of వాడండి."),
        ),
    ),
    "ts-classes" to ExtraTr(
        deep = listOf(
            H("Access modifiers మరియు readonly"),
            C("public / private / readonly"),
            H("Inheritance"),
            C("extends"),
            H("Default vs named exports"),
            C("రెండు రకాలు"),
            Tip("ఒక file కి ఒక class; file పేరు class పేరే. Java లో మీరు పాటించే అదే tidy అలవాట్లు."),
            Warn("Import paths relative. `'./LoginPage'` అంటే అదే folder; `'../pages/LoginPage'` అంటే ముందు పైకి వెళ్ళు. `.ts` extension add చేయకండి."),
        ),
        quiz = listOf(
            Quiz("`protected` దేన్ని allow చేస్తుంది?", listOf("Class లోపల, దాని child classes లో access", "ఎక్కడినుండైనా access", "ఏమీ లేదు"), 0, "Java protected లాగే (package access తప్ప)."),
            Quiz("Import paths లో `.ts` రాయాలా?", listOf("అవును", "వద్దు", "classes కి మాత్రమే"), 1, "Extension ని వదిలేయండి."),
        ),
    ),
    "test-structure" to ExtraTr(
        deep = listOf(
            H("అన్ని hooks"),
            Table(
                listOf("Playwright", "TestNG", "ఎప్పుడు నడుస్తుంది"),
                listOf(
                    listOf("`beforeAll`", "@BeforeClass", "ఒక worker కి, ఒక describe కి ఒక్కసారి"),
                    listOf("`beforeEach`", "@BeforeMethod", "ప్రతి test కి ముందు"),
                    listOf("`afterEach`", "@AfterMethod", "ప్రతి test తర్వాత"),
                    listOf("`afterAll`", "@AfterClass", "చివర్లో ఒక్కసారి"),
                ),
            ),
            Warn("`beforeAll` కి `page` రాదు (shared tab లేదు). Tests isolate అయి ఉంటాయి కాబట్టి `beforeEach` ని prefer చేయండి."),
            H("Nested groups"),
            C("describe లోపల describe"),
            H("Skip, focus, expected failure"),
            C("Annotations"),
            Tip("Config లో `forbidOnly: !!process.env.CI` పెట్టండి; అప్పుడు మర్చిపోయిన `test.only`, మిగతా అన్ని tests ని నిశ్శబ్దంగా skip చేయడానికి బదులు build ని fail చేస్తుంది."),
        ),
        quiz = listOf(
            Quiz("ఏ hook safe గా `page` ఇస్తుంది?", listOf("beforeAll", "beforeEach", "రెండూ కాదు"), 1, "beforeEach ప్రతి test కీ నడుస్తుంది; అక్కడ ప్రతి test కీ సొంత page ఉంటుంది."),
            Quiz("`test.fixme` ఏం చేస్తుంది?", listOf("తెలిసిన bug ఉన్న test ని skip చేస్తుంది", "Test ని fix చేస్తుంది", "రెండుసార్లు నడుపుతుంది"), 0, "ఆ test ని 'తెలిసిన సమస్య' గా mark చేసి skip చేస్తుంది."),
        ),
    ),
    "run-tests" to ExtraTr(
        deep = listOf(
            H("Useful flags"),
            Table(
                listOf("Flag", "ఏం చేస్తుంది"),
                listOf(
                    listOf("`--headed`", "Browser ని చూపించు"),
                    listOf("`--workers=1`", "Tests ని ఒక్కొక్కటిగా నడుపు"),
                    listOf("`--retries=2`", "Failures ని మళ్ళీ try చేయి"),
                    listOf("`--repeat-each=10`", "ప్రతి test ని 10 సార్లు నడుపు (flakiness వెతకడానికి)"),
                    listOf("`--last-failed`", "Fail అయినవాటిని మాత్రమే మళ్ళీ నడుపు"),
                    listOf("`--reporter=list`", "Simple console output"),
                    listOf("`--grep-invert @slow`", "ఒక tag ఉన్న tests ని skip చేయి"),
                    listOf("`--max-failures=1`", "మొదటి failure తర్వాత ఆపు"),
                ),
            ),
            H("Line number తో ఒక test ని నడపడం"),
            C("file:line"),
            H("VS Code extension"),
            P("'Playwright Test for VS Code' extension ప్రతి test పక్కన green play button, Testing sidebar, 'Show browser' checkbox, 'Pick locator' tool ని add చేస్తుంది. మీ test లో breakpoints పెట్టి debug icon నొక్కండి."),
            H("Fail అవుతున్న test ని debug చేయడం"),
            C("Inspector"),
            Tip("అనుమానంగా ఉన్న test మీద `--repeat-each=20 --workers=4` వాడి, అది నిజంగా ఎంత flaky అని తెలుసుకోండి."),
        ),
        quiz = listOf(
            Quiz("Fail అయిన tests ని మాత్రమే మళ్ళీ ఎలా నడపాలి?", listOf("--last-failed", "--again", "--retry-only"), 0, "`--last-failed` ముందు run ని గుర్తుంచుకుంటుంది."),
            Quiz("`page.pause()` దేన్ని ఓపెన్ చేస్తుంది?", listOf("Playwright Inspector", "Report", "ఏమీ లేదు"), 0, "Test freeze అవుతుంది; Inspector తో step చేస్తూ locators ని pick చేయొచ్చు."),
        ),
    ),
    "reports-trace" to ExtraTr(
        deep = listOf(
            H("Reporters"),
            C("Config లో reporters ని ఎంచుకోండి"),
            H("Trace modes"),
            Table(
                listOf("Value", "అర్థం"),
                listOf(
                    listOf("`on`", "ఎప్పుడూ record (slow, పెద్దది)"),
                    listOf("`off`", "ఎప్పుడూ కాదు"),
                    listOf("`on-first-retry`", "Test retry అయినప్పుడు record. CI కి best default"),
                    listOf("`retain-on-failure`", "ఎప్పుడూ record, failures ని మాత్రమే ఉంచు"),
                ),
            ),
            H("Trace లోపల ఏముంది?"),
            P("ప్రతి action యొక్క timeline, dev tools తో చూడగలిగే ముందు/తర్వాత DOM snapshot, console logs, bodies తో network requests, source code line, screenshots. `trace.zip` ని teammate కి కూడా పంపొచ్చు."),
            C("ఓపెన్ చేసే విధానం"),
            Tip("HTML report లో failed tests కి trace attach అయి ఉంటుంది. పూర్తి కథ చూడటానికి దాన్ని click చేయండి. 'నా machine లో పని చేస్తుంది' వాదనల్లో 80% దీంతో పోతాయి."),
            Warn("Traces లో network calls నుండి passwords, tokens ఉండొచ్చు. CI artifacts ని public గా upload చేయకండి."),
        ),
        quiz = listOf(
            Quiz("CI కి మంచి default trace mode ఏది?", listOf("on-first-retry", "on", "off"), 0, "ఏదైనా investigate చేయాల్సినప్పుడు మాత్రమే record చేస్తుంది."),
            Quiz("Jenkins కి ఉపయోగపడే reporter ఏది?", listOf("junit", "list", "dot"), 0, "Jenkins కి JUnit XML అర్థమవుతుంది."),
        ),
    ),
)
