package com.example.playwright.data

// Tanglish (2): deep dives + extra quizzes, part A (matches DeepA.kt). Code blocks are C("title"); code comes from English.

val deepTgA: Map<String, ExtraTr> = mapOf(
    "what-is" to ExtraTr(
        deep = listOf(
            H("Idhu unmaiyaave eppadi work aagudhu?"),
            P("Selenium oru thani driver program-oda (chromedriver) HTTP vazhiya pesum; andha driver browser-oda pesum. Playwright naduvula irukkuravara skip pannitu, browser-oda oru fast connection-a open-a vechchu, adhu vazhiya commands anuppudhu. Adhunaala dhaan idhu fast; network calls, page load maadhiri vishayangalaiyum gavanikka mudiyudhu."),
            Table(
                listOf("Concept", "Selenium", "Playwright"),
                listOf(
                    listOf("Browser-oda pesura vazhi", "Driver + HTTP", "Direct WebSocket connection"),
                    listOf("Browsers", "Driver irukkura edhuvum", "Chromium, Firefox, WebKit (kooda varum)"),
                    listOf("Wait pannuradhu", "Neengale waits eludhanum", "Built-in auto-wait"),
                    listOf("Test runner", "TestNG / JUnit (neenga add pannanum)", "`@playwright/test` (kooda varum)"),
                ),
            ),
            H("4 building blocks"),
            P("`Browser` nu sonna muzhu browser program. `BrowserContext` adhukulla irukkura clean profile (incognito window maadhiri: thani cookies, thani storage). `Page` nu sonna oru context-kulla irukkura oru tab. `Locator` nu sonna page-la irukkura oru vishayatha point pannura pointer."),
            C("Browser → Context → Page"),
            Tip("`@playwright/test` use pannumbodhu, ovvoru test-ukkum browser, context, page-a adhuve create pannidum. Scripts-la mattum dhaan neenga kaiyaala pannuveenga."),
            Warn("Playwright, Selenium-oda compatible illa. Ungal Java `WebDriver` code-a thirumba use panna mudiyaadhu; aana ideas (locators, waits, page objects) ellaam appadiye varum."),
        ),
        quiz = listOf(
            Quiz("BrowserContext edhu maadhiri?", listOf("Oru incognito profile", "Oru browser tab", "Oru Maven module"), 0, "Oru context-ukku thani cookies, storage irukku; mathha contexts-la irundhu pirichchu irukkum."),
            Quiz("Oru BrowserContext-la ethanai Pages irukkalaam?", listOf("Sariyaa onnu", "Neenga open pannura alavukku", "Onnum illa"), 1, "Oru context pala tabs-a (pages) vechchirukkalaam."),
        ),
    ),
    "why-better" to ExtraTr(
        deep = listOf(
            H("Flaky tests: yen varudhu?"),
            P("Code maatram illama oru test sila neram pass, sila neram fail aanaa adhu flaky. Pothuvaana kaaranam: test, page-a vida fast-a odudhu. Button ready aagum munnaadiye click pannudhu, illana text varuvadharku munnaadi padikkudhu. Playwright-oda auto-wait, retry panra assertions idhula perumbaalaanadhai design-laye sari pannidum."),
            H("Isolation = reliable tests"),
            P("Ovvoru Playwright test-um adhuvukkunnu thani fresh context-la odum. Test A-la login pannadhu test B-a paadhikkaadhu. Selenium-la cookies clear panna illana driver-a restart panna neenga gnabagam vechchukkanum."),
            H("Speed"),
            C("4 workers-la 100 tests-a parallel-a odudhal"),
            P("Contexts romba cheap (milliseconds) matrum workers thani processes-nu irukkradhunaala, Selenium-la oru manineram eduththa suites perumbaalum sila nimishathula mudiyum."),
            Table(
                listOf("Feature", "Selenium ulagam", "Playwright"),
                listOf(
                    listOf("Reports", "Extent / Allure plugin", "HTML report built-in"),
                    listOf("Debugging", "Breakpoints + vendudhal", "Inspector, UI mode, Trace Viewer"),
                    listOf("API calls", "RestAssured", "`request` fixture"),
                    listOf("Network mock", "Proxy tools", "`page.route()`"),
                    listOf("Tests record", "Selenium IDE", "`codegen`"),
                ),
            ),
            Warn("Playwright magic illa. Mosamaana locators, share panna test data, vittu pona `await` ippovum flaky tests-a create pannum. Ovvoru onnaiyum eppadi thavirkkardhunu kathukkalaam."),
        ),
        quiz = listOf(
            Quiz("Playwright tests yen onnukkonnu isolate aagi irukku?", listOf("Ovvoru test-ukkum fresh browser context kidaikkum", "Ellaam oru tab-a share pannum", "Onnu onnaa dhaan odum"), 0, "Ovvoru test-ukkum puthu context irundha cookies illa state leak aagaadhu."),
            Quiz("Flaky tests-oda pothuvaana kaaranam edhu?", listOf("Parallel-a odudhuradhu mattum", "Page ready aagum munnaadi act pannuradhu", "TypeScript use pannuradhu"), 1, "Page-a vida fast-a irukkuradhu dhaan classic kaaranam."),
        ),
    ),
    "setup" to ExtraTr(
        deep = listOf(
            H("Install, step by step"),
            P("1) nodejs.org-la irundhu Node.js (LTS version) install pannunga. 2) VS Code install pannunga. 3) Oru folder create pannitu init command run pannunga. 4) Oru click-la tests odanum-na 'Playwright Test for VS Code' extension install pannunga."),
            C("Ellaathaiyum check pannunga"),
            H("Generate aana folder eppadi irukkum"),
            C("Project layout"),
            Tip("`node_modules`, `playwright-report`, `test-results`-a `.gitignore`-la add pannunga. Init command already idhai ungalukkaaga pannidum."),
            H("Playwright-a update pannuradhu"),
            C("Upgrade"),
            Warn("'Executable doesn't exist'nu vandha, `npx playwright install` run panna marandhuteenga. Browsers, npm package-la irundhu thani."),
        ),
        quiz = listOf(
            Quiz("`npx playwright install` enna pannum?", listOf("Browsers-a download pannum", "Node-a install pannum", "Tests-a create pannum"), 0, "npm package-la browsers illa; indha command adhai kondu varum."),
            Quiz("`node_modules`-a git-la commit pannanuma?", listOf("Aamaa", "Illa", "Velli kizhamai mattum"), 1, "package.json-la irundhu `npm install` adhai thirumba create pannidum."),
        ),
    ),
    "first-test" to ExtraTr(
        deep = listOf(
            H("Oru test-oda anatomy, line by line"),
            C("Annotated"),
            P("Ovvoru test-um same shape dhaan: Arrange (engayaavadhu po), Act (click, type), Assert (check pannu). Oru test-ukku oru main idea mattum vachchukkonga."),
            H("Realistic ondru: docs-la search"),
            C("Search test"),
            H("Results enga pogum?"),
            P("Terminal-la pass/fail theriyum. HTML report `playwright-report/`-la ezhudhappadum. Configure pannirundha, failures-oda screenshots, traces `test-results/`-la save aagum."),
            Tip("`npx playwright test --ui` use panni indha test-a time-travel timeline-oda odi paarunga. Kathukka idhu dhaan best vazhi."),
            Warn("Oru file-la test names unique-a irukkanum. 'test1' illa, 'user can sign in' maadhiri behaviour-a describe pannunga."),
        ),
        quiz = listOf(
            Quiz("Nalla test-oda moonu steps enna?", listOf("Arrange, Act, Assert", "Read, Write, Run", "Click, Click, Click"), 0, "Set up pannu, edhaavadhu pannu, result check pannu."),
            Quiz("`toHaveURL(/intro/)` edhai check pannudhu?", listOf("URL sariyaa intro-nu irukkanum", "URL-la intro irukkanum", "Title-la intro irukkanum"), 1, "/intro/ regex andha word irukkura endha URL-oda-vum match aagum."),
        ),
    ),
    "ts-vars" to ExtraTr(
        deep = listOf(
            H("Neenga unmaiyaave use pannum types"),
            Table(
                listOf("Type", "Example", "Java inai"),
                listOf(
                    listOf("`string`", "'hello'", "String"),
                    listOf("`number`", "42, 3.14", "int / double"),
                    listOf("`boolean`", "true", "boolean"),
                    listOf("`string[]`", "['a','b']", "List<String>"),
                    listOf("`any`", "edhuvum", "Object (thavirunga!)"),
                    listOf("`unknown`", "safe-aana 'edhuvum'", "Object + cast"),
                ),
            ),
            H("Union types matrum null"),
            P("Oru variable pala types-la onnaa irukkalaam. `|` (pipe) use pannunga. 'Value illa onnum illa'nu solradhukku idhu romba common."),
            C("Unions"),
            H("Equality matrum truthiness"),
            C("Strict-a irunga"),
            Tip("TypeScript-a infer panna vidunga. `const url = 'https://x.com'` already `string`. Function parameters-ukkum, compiler guess panna mudiyaadha edathukkum mattum types add pannunga."),
            Warn("`const` objects-a freeze pannaadhu: `const user = {}; user.name = 'x'` work aagum. Variable-a vera onnukku point pannuradhai mattum thadukkum."),
        ),
        quiz = listOf(
            Quiz("Edhu comparison-a prefer pannanum?", listOf("==", "===", "="), 1, "`===` ungalukku theriyaama types-a maathaadhu."),
            Quiz("`string | undefined` na enna?", listOf("Oru string illa onnum illa", "Rendu strings", "Oru error"), 0, "Value illaamayum irukkalaam."),
        ),
    ),
    "ts-functions" to ExtraTr(
        deep = listOf(
            H("Optional matrum default parameters"),
            C("Flexible parameters"),
            H("Functions-um values dhaan"),
            P("Java-la objects-a anuppuveenga. TypeScript-la oru function-a innoru function-ukkum anuppalaam. `test('name', async () => {...})` appadi dhaan work aagudhu: rendavadhu argument oru function; adhai Playwright apparam odum."),
            C("Functions-a pass pannuradhu"),
            H("Async functions Promises-a tharum"),
            C("Tests-la helper function"),
            Tip("Page Objects-ukku pogaradhukku munnaadi, thirumba thirumba ezhudhuradhai thavirkka helper functions simple-aana vazhi."),
            Warn("`function` vs arrow: arrow functions-ukku thani `this` kidaiyaadhu. Tests-la idhu rombavum mukkiyam illa; aana classes-kulla gnabagam vachchukkonga."),
        ),
        quiz = listOf(
            Quiz("`wait(ms: number = 1000)` na enna?", listOf("ms-oda default 1000", "ms kandippa 1000", "ms oru optional text"), 0, "Call pannuravar argument tharalana default value use aagum."),
            Quiz("`test('x', async () => {...})` yen work aagudhu?", listOf("Functions-a values-a pass pannalaam", "Magic", "Idhu Java feature"), 0, "Ungal function-a Playwright vaangi, thevai padumbodhu odum."),
        ),
    ),
    "ts-objects" to ExtraTr(
        deep = listOf(
            H("Interfaces: oru object-oda shape-a describe pannu"),
            P("`interface` oru contract: 'indha type object-ukku indha fields irukkum'. Getters, setters illaadha Java POJO maadhiri."),
            C("interface"),
            H("Useful array methods"),
            C("map, filter, find"),
            H("Spread matrum optional chaining"),
            C("... matrum ?."),
            Tip("Config-la `...devices['Desktop Chrome']` paathirukkeenga. Adhu spread operator: settings-a ungal object-kulla copy pannudhu."),
            Warn("Illaadha property-a padicha Java-oda NullPointerException maadhiri exception varaadhu; `undefined` kedaikkum. Bugs amaidhiya olinjirukkum; adhunaala ungal objects-ukku type kudunga."),
        ),
        quiz = listOf(
            Quiz("`{ ...base, retries: 3 }` enna pannudhu?", listOf("base-a copy panni retries-a override pannudhu", "base-a delete pannudhu", "Error throw pannudhu"), 0, "Spread fields-a copy pannum; pinnaadi varra keys munnadi irukkuradhai override pannum."),
            Quiz("address undefined-aa irukkumbodhu `user.address?.city` tharuvadhu...", listOf("Oru crash", "undefined", "Oru empty string"), 1, "Optional chaining safe-a nindrudum."),
        ),
    ),
    "ts-async" to ExtraTr(
        deep = listOf(
            H("Promise na enna?"),
            P("Promise nu sonna innum ready aagaadha oru result-a represent panra object. Adhu pending, fulfilled (success) illa rejected (error)-nu irukkum. `await` adhai thirandhu tharum."),
            C("Promise states"),
            H("Onnaa sernthu seiyyardhu"),
            P("Sequential awaits onnu pinnaadi onnaa odum. `Promise.all` ellaathaiyum serthu odi, ellaam mudiyura varaikkum wait pannum. 'Click pannitu puthu tab-ukku wait pannu' maadhiri patterns-ukku idhai use pannuveenga."),
            C("Promise.all"),
            H("Errors-a handle pannuradhu"),
            C("try / catch / finally"),
            H("await enga thappa pogum"),
            C("Common mistakes"),
            Warn("`forEach`-kulla `await` irundha adhu wait pannaadhu. Steps order-la nadakkanum-na eppavum `for...of` loop use pannunga."),
        ),
        quiz = listOf(
            Quiz("Promise.all enna pannum?", listOf("Promises-a serthu odi ellaathukkum wait pannum", "Promises-a cancel pannum", "Avatrai oru loop-la odum"), 0, "Ovvoru promise-um mudiyura varaikkum adhu wait pannum."),
            Quiz("`forEach(async ...)` yen aabathu?", listOf("Ovvoru await-ukkum wait pannaadhu", "Romba slow", "Allow illa"), 0, "forEach return aagura promises-a ignore pannum, adhunaala steps overlap aagum. for...of use pannunga."),
        ),
    ),
    "ts-classes" to ExtraTr(
        deep = listOf(
            H("Access modifiers matrum readonly"),
            C("public / private / readonly"),
            H("Inheritance"),
            C("extends"),
            H("Default vs named exports"),
            C("Rendu vagai"),
            Tip("Oru file-ukku oru class; file name class name-a irukkanum. Java-la neenga follow pannura adhey tidy habits."),
            Warn("Import paths relative. `'./LoginPage'` na adhey folder; `'../pages/LoginPage'` na mudhalla mela po. `.ts` extension add pannaadheenga."),
        ),
        quiz = listOf(
            Quiz("`protected` edhai allow pannudhu?", listOf("Class-kullayum adhoda child classes-layum access", "Enga irundhum access", "Onnum illa"), 0, "Java-oda protected maadhiri dhaan (package access thavira)."),
            Quiz("Import paths-la `.ts` ezhudhanuma?", listOf("Aamaa", "Illa", "Classes-ukku mattum"), 1, "Extension-a vittudunga."),
        ),
    ),
    "test-structure" to ExtraTr(
        deep = listOf(
            H("Ellaa hooks"),
            Table(
                listOf("Playwright", "TestNG", "Eppo odum"),
                listOf(
                    listOf("`beforeAll`", "@BeforeClass", "Oru worker-ukku, oru describe-ukku onnu dhaan"),
                    listOf("`beforeEach`", "@BeforeMethod", "Ovvoru test-ukkum munnaadi"),
                    listOf("`afterEach`", "@AfterMethod", "Ovvoru test-ukkum pinnaadi"),
                    listOf("`afterAll`", "@AfterClass", "Kadaisiyil onnu dhaan"),
                ),
            ),
            Warn("`beforeAll`-ukku `page` kidaikkaadhu (shared tab illa). Tests isolate aagi irukkradhunaala `beforeEach`-a prefer pannunga."),
            H("Nested groups"),
            C("describe-kulla describe"),
            H("Skip, focus, expected failure"),
            C("Annotations"),
            Tip("Config-la `forbidOnly: !!process.env.CI` vainga; appo marandhu pona `test.only` mathha ellaa tests-aiyum amaidhiya skip pannaama build-a fail pannum."),
        ),
        quiz = listOf(
            Quiz("Edhu hook safe-a `page` tharum?", listOf("beforeAll", "beforeEach", "Rendum illa"), 1, "beforeEach ovvoru test-ukkum odum; angae ovvoru test-ukkum thani page irukku."),
            Quiz("`test.fixme` enna pannum?", listOf("Theriyaadha bug irukkura test-a skip pannum", "Test-a fix pannum", "Rendu thadava odum"), 0, "Andha test-a 'theriyaadha problem'nu mark panni skip pannum."),
        ),
    ),
    "run-tests" to ExtraTr(
        deep = listOf(
            H("Useful flags"),
            Table(
                listOf("Flag", "Enna pannum"),
                listOf(
                    listOf("`--headed`", "Browser-a kaattu"),
                    listOf("`--workers=1`", "Tests-a onnu onnaa odu"),
                    listOf("`--retries=2`", "Failures-a thirumba try pannu"),
                    listOf("`--repeat-each=10`", "Ovvoru test-aiyum 10 thadava odu (flakiness-a vettai aada)"),
                    listOf("`--last-failed`", "Fail aanadhai mattum thirumba odu"),
                    listOf("`--reporter=list`", "Simple console output"),
                    listOf("`--grep-invert @slow`", "Oru tag irukkura tests-a skip pannu"),
                    listOf("`--max-failures=1`", "Mudhal failure-ukku apparam nillu"),
                ),
            ),
            H("Line number vachchu oru test-a odudhal"),
            C("file:line"),
            H("VS Code extension"),
            P("'Playwright Test for VS Code' extension ovvoru test pakkathulayum pachchai play button, Testing sidebar, 'Show browser' checkbox, 'Pick locator' tool-a add pannum. Ungal test-la breakpoints vachchu debug icon-a press pannunga."),
            H("Fail aagura test-a debug pannuradhu"),
            C("Inspector"),
            Tip("Sandhegamaana test-la `--repeat-each=20 --workers=4` use panni, adhu unmaiyaave evlo flaky-nu kandupidinga."),
        ),
        quiz = listOf(
            Quiz("Fail aana tests-a mattum eppadi thirumba odurathu?", listOf("--last-failed", "--again", "--retry-only"), 0, "`--last-failed` munnaadi run-a gnabagam vachchirukkum."),
            Quiz("`page.pause()` edhai open pannudhu?", listOf("Playwright Inspector", "Report", "Onnum illa"), 0, "Test freeze aagum; Inspector vazhiya step panni locators-a pick pannalaam."),
        ),
    ),
    "reports-trace" to ExtraTr(
        deep = listOf(
            H("Reporters"),
            C("Config-la reporters-a choose pannunga"),
            H("Trace modes"),
            Table(
                listOf("Value", "Artham"),
                listOf(
                    listOf("`on`", "Eppovum record (slow, periya size)"),
                    listOf("`off`", "Ippodhum illa"),
                    listOf("`on-first-retry`", "Test retry aagumbodhu record. CI-ku best default"),
                    listOf("`retain-on-failure`", "Eppovum record, failures-a mattum vachchukko"),
                ),
            ),
            H("Trace-kulla enna irukku?"),
            P("Ovvoru action-oda timeline, dev tools-la paarkkalaam-aana munnaadi/pinnaadi DOM snapshot, console logs, bodies-oda network requests, source code line, screenshots. `trace.zip`-a oru teammate-kum anuppalaam."),
            C("Open pannum vazhi"),
            Tip("HTML report-la failed tests-oda trace attach aagi irukkum. Muzhu kadhaiyum paaka adhai click pannunga. 'En machine-la work aagudhu' vaadhangalil 80%-a idhu maathidum."),
            Warn("Traces-la network calls-la irundhu passwords, tokens irukkalaam. CI artifacts-a public-a upload pannaadheenga."),
        ),
        quiz = listOf(
            Quiz("CI-ku nalla default trace mode edhu?", listOf("on-first-retry", "on", "off"), 0, "Investigate panna vendiya nilai varumbodhu mattum record pannum."),
            Quiz("Jenkins-ukku help panra reporter edhu?", listOf("junit", "list", "dot"), 0, "Jenkins JUnit XML-a purinjukkum."),
        ),
    ),
)
