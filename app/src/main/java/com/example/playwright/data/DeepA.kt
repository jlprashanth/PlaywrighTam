package com.example.playwright.data

val deepExtras: Map<String, Extra> by lazy { deepA + deepB + deepC + deepD }

val deepA: Map<String, Extra> = mapOf(
    "what-is" to Extra(
        deep = listOf(
            H("How does it actually work?"),
            P("Selenium talks to a separate driver program (chromedriver) over HTTP, and the driver talks to the browser. Playwright skips the middle-man: it keeps one fast connection open to the browser and sends commands over it. That's why it is quicker and why it can notice things like network calls and page loads."),
            Table(
                listOf("Concept", "Selenium", "Playwright"),
                listOf(
                    listOf("Talks to browser via", "Driver + HTTP", "Direct WebSocket connection"),
                    listOf("Browsers", "Any with a driver", "Chromium, Firefox, WebKit (bundled)"),
                    listOf("Waiting", "You write waits", "Built in auto-wait"),
                    listOf("Test runner", "TestNG / JUnit (you add)", "`@playwright/test` (included)"),
                ),
            ),
            H("The 4 building blocks"),
            P("`Browser` is the whole browser program. A `BrowserContext` is a clean profile inside it (like an incognito window: its own cookies and storage). A `Page` is one tab inside a context. A `Locator` is a pointer to something on a page."),
            Code("Browser → Context → Page", "TypeScript", """
                const browser = await chromium.launch();          // the browser program
                const context = await browser.newContext();       // a clean profile
                const page = await context.newPage();             // a tab
                const other = await browser.newContext();         // totally separate user
            """.trimIndent()),
            Tip("When you use `@playwright/test`, it creates the browser, context and page for you for every test. You only do it by hand in scripts."),
            Warn("Playwright is not Selenium-compatible. Your Java `WebDriver` code can't be reused, but the ideas (locators, waits, page objects) all carry over."),
        ),
        challenge = Challenge(
            "Write a small script (not a test) that opens playwright.dev, prints the page title in the console, and closes the browser.",
            """
                import { chromium } from '@playwright/test';

                const browser = await chromium.launch();
                const page = await browser.newPage();
                await page.goto('https://playwright.dev');
                console.log(await page.title());
                await browser.close();
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What is a BrowserContext most like?", listOf("An incognito profile", "A browser tab", "A Maven module"), 0, "A context has its own cookies and storage, isolated from other contexts."),
            Quiz("How many Pages can one BrowserContext have?", listOf("Exactly one", "As many as you open", "None"), 1, "A context can hold many tabs (pages)."),
        ),
    ),
    "why-better" to Extra(
        deep = listOf(
            H("Flaky tests: why they happen"),
            P("A test is flaky when it sometimes passes and sometimes fails with no code change. The usual cause: the test is faster than the page. It clicks before the button is ready, or reads text before it appears. Playwright's auto-wait and retrying assertions fix most of this by design."),
            H("Isolation = reliable tests"),
            P("Each Playwright test runs in its own fresh context. Test A logging in cannot affect test B. In Selenium you often had to remember to clear cookies or restart the driver."),
            H("Speed"),
            Code("Run 100 tests in parallel on 4 workers", "Bash", """
                npx playwright test --workers=4
            """.trimIndent()),
            P("Because contexts are cheap (milliseconds) and workers are separate processes, suites that took an hour in Selenium often take minutes."),
            Table(
                listOf("Feature", "Selenium world", "Playwright"),
                listOf(
                    listOf("Reports", "Extent / Allure plugin", "HTML report built in"),
                    listOf("Debugging", "Breakpoints + prayers", "Inspector, UI mode, Trace Viewer"),
                    listOf("API calls", "RestAssured", "`request` fixture"),
                    listOf("Mock network", "Proxy tools", "`page.route()`"),
                    listOf("Record tests", "Selenium IDE", "`codegen`"),
                ),
            ),
            Warn("Playwright is not magic. Bad locators, shared test data and missing `await` still cause flaky tests. We'll learn how to avoid each one."),
        ),
        challenge = Challenge(
            "Look at your own Selenium suite. Write down 3 places where you used an explicit wait or Thread.sleep. Which Playwright feature would remove each one? (Think auto-wait, web-first assertion, waitForURL.)",
            """
                // Example answers
                // 1. wait for button clickable   -> just click()
                // 2. wait for text to appear     -> await expect(locator).toHaveText('...')
                // 3. wait for page change        -> await page.waitForURL('**/dashboard')
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Why are Playwright tests isolated from each other?", listOf("Each gets a fresh browser context", "They share one tab", "They run one at a time"), 0, "A new context per test means no leaked cookies or state."),
            Quiz("Which is a common cause of flaky tests?", listOf("Running in parallel only", "Acting before the page is ready", "Using TypeScript"), 1, "Being faster than the page is the classic cause."),
        ),
    ),
    "setup" to Extra(
        deep = listOf(
            H("Install, step by step"),
            P("1) Install Node.js (LTS version) from nodejs.org. 2) Install VS Code. 3) Create a folder and run the init command. 4) Install the \"Playwright Test for VS Code\" extension to run tests with one click."),
            Code("Check everything", "Bash", """
                node -v
                npm -v
                npx playwright --version
                npx playwright install            # download browsers
                npx playwright install --with-deps # also Linux system libraries (CI)
            """.trimIndent()),
            H("What the generated folder looks like"),
            Code("Project layout", "Bash", """
                my-pw/
                  tests/
                    example.spec.ts
                  tests-examples/       <- demo tests, safe to delete
                  playwright.config.ts
                  package.json
                  package-lock.json     <- like a locked dependency tree
                  node_modules/         <- never commit this
                  playwright-report/    <- generated after a run
                  test-results/         <- traces, screenshots
            """.trimIndent()),
            Tip("Add `node_modules`, `playwright-report` and `test-results` to `.gitignore`. The init command already does it for you."),
            H("Updating Playwright"),
            Code("Upgrade", "Bash", """
                npm install -D @playwright/test@latest
                npx playwright install
            """.trimIndent()),
            Warn("If you see 'Executable doesn't exist', you forgot `npx playwright install`. The browsers are separate from the npm package."),
        ),
        challenge = Challenge(
            "Create a new project, delete the demo tests, and write `tests/hello.spec.ts` with one test that opens https://example.com and checks the heading says 'Example Domain'. Run it in headed mode.",
            """
                import { test, expect } from '@playwright/test';

                test('example heading', async ({ page }) => {
                  await page.goto('https://example.com');
                  await expect(page.getByRole('heading', { name: 'Example Domain' })).toBeVisible();
                });

                // npx playwright test hello.spec.ts --headed
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `npx playwright install` do?", listOf("Downloads the browsers", "Installs Node", "Creates tests"), 0, "The npm package doesn't include browsers; this command fetches them."),
            Quiz("Should `node_modules` be committed to git?", listOf("Yes", "No", "Only on Fridays"), 1, "It's regenerated by `npm install` from package.json."),
        ),
    ),
    "first-test" to Extra(
        deep = listOf(
            H("Anatomy of a test, line by line"),
            Code("Annotated", "TypeScript", """
                import { test, expect } from '@playwright/test';   // 1. borrow tools

                test('name shown in report', async ({ page }) => { // 2. declare a test
                  await page.goto('https://playwright.dev');       // 3. an ACTION
                  await expect(page).toHaveTitle(/Playwright/);    // 4. an ASSERTION
                });
            """.trimIndent()),
            P("Every test follows the same shape: Arrange (go somewhere), Act (click and type), Assert (check). Keep one main idea per test."),
            H("A realistic one: search the docs"),
            Code("Search test", "TypeScript", """
                test('search finds locators page', async ({ page }) => {
                  await page.goto('https://playwright.dev');
                  await page.getByRole('link', { name: 'Docs' }).click();
                  await expect(page).toHaveURL(/intro/);
                  await expect(page.getByRole('heading', { name: 'Installation' })).toBeVisible();
                });
            """.trimIndent()),
            H("Where do results go?"),
            P("The terminal shows pass/fail. An HTML report is written to `playwright-report/`. Failures also save screenshots and traces in `test-results/` if configured."),
            Tip("Use `npx playwright test --ui` to run and watch this test with a time-travel timeline. It's the best way to learn."),
            Warn("Test names should be unique inside a file. Describe behaviour, like 'user can sign in', not 'test1'."),
        ),
        challenge = Challenge(
            "Write a test for https://playwright.dev: click the 'Docs' link, then assert the URL contains 'intro' and that a heading containing 'Installation' is visible.",
            """
                import { test, expect } from '@playwright/test';

                test('docs page opens', async ({ page }) => {
                  await page.goto('https://playwright.dev');
                  await page.getByRole('link', { name: 'Docs' }).click();
                  await expect(page).toHaveURL(/intro/);
                  await expect(page.getByRole('heading', { name: 'Installation' })).toBeVisible();
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What are the three steps of a good test?", listOf("Arrange, Act, Assert", "Read, Write, Run", "Click, Click, Click"), 0, "Set up, do something, check the result."),
            Quiz("What does `toHaveURL(/intro/)` check?", listOf("URL equals exactly intro", "URL contains intro", "Title contains intro"), 1, "The regex /intro/ matches any URL containing that word."),
        ),
    ),
    "ts-vars" to Extra(
        deep = listOf(
            H("The types you'll actually use"),
            Table(
                listOf("Type", "Example", "Java twin"),
                listOf(
                    listOf("`string`", "'hello'", "String"),
                    listOf("`number`", "42, 3.14", "int / double"),
                    listOf("`boolean`", "true", "boolean"),
                    listOf("`string[]`", "['a','b']", "List<String>"),
                    listOf("`any`", "anything", "Object (avoid!)"),
                    listOf("`unknown`", "safer 'anything'", "Object + cast"),
                ),
            ),
            H("Union types and null"),
            P("A variable may be one of several types. Use `|` (pipe). It's super common for 'value or nothing'."),
            Code("Unions", "TypeScript", """
                let id: string | number = 'abc';
                id = 123;                      // also fine

                let nickname: string | undefined;   // may be missing
                let status: 'pass' | 'fail' = 'pass'; // only these two words
            """.trimIndent()),
            H("Equality and truthiness"),
            Code("Be strict", "TypeScript", """
                5 === 5        // true
                '5' === 5      // false (different types)
                '5' == 5       // true  (sloppy! avoid ==)

                // falsy values: false, 0, '', null, undefined, NaN
                if (!name) { /* name is empty or missing */ }
            """.trimIndent()),
            Tip("Let TypeScript infer. `const url = 'https://x.com'` is already a `string`. Add types for function parameters and for things the compiler can't guess."),
            Warn("`const` doesn't freeze objects: `const user = {}; user.name = 'x'` works. It only stops you re-pointing the variable."),
        ),
        challenge = Challenge(
            "Declare: a const `baseUrl` (string), a let `retries` (number) starting at 2, and a const `browsers` array holding 'chromium' and 'firefox'. Then print a sentence using a template string.",
            """
                const baseUrl: string = 'https://example.com';
                let retries: number = 2;
                const browsers: string[] = ['chromium', 'firefox'];
                console.log(`Running on ${'$'}{browsers.length} browsers with ${'$'}{retries} retries at ${'$'}{baseUrl}`);
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Which comparison should you prefer?", listOf("==", "===", "="), 1, "`===` doesn't convert types behind your back."),
            Quiz("What does `string | undefined` mean?", listOf("A string or nothing", "Two strings", "An error"), 0, "The value may be missing."),
        ),
    ),
    "ts-functions" to Extra(
        deep = listOf(
            H("Optional and default parameters"),
            Code("Flexible parameters", "TypeScript", """
                function login(user: string, remember?: boolean) { /* remember may be missing */ }
                function wait(ms: number = 1000) { /* default 1000 */ }

                login('asha');          // ok
                login('asha', true);    // ok
                wait();                 // uses 1000
            """.trimIndent()),
            H("Functions are values"),
            P("In Java you pass objects. In TypeScript you can also pass a function to another function. That's how `test('name', async () => {...})` works: the second argument is a function, run later by Playwright."),
            Code("Passing functions", "TypeScript", """
                function repeat(times: number, action: () => void) {
                  for (let i = 0; i < times; i++) action();
                }
                repeat(3, () => console.log('hi'));
            """.trimIndent()),
            H("Async functions return Promises"),
            Code("Helper function in tests", "TypeScript", """
                async function login(page: Page, user: string) {
                  await page.getByLabel('User').fill(user);
                  await page.getByRole('button', { name: 'Sign in' }).click();
                }

                test('uses helper', async ({ page }) => {
                  await page.goto('/login');
                  await login(page, 'asha');     // don't forget await!
                });
            """.trimIndent()),
            Tip("Helper functions are the simplest way to avoid repeating yourself, before you reach for Page Objects."),
            Warn("`function` vs arrow: arrow functions don't have their own `this`. In tests this rarely matters, but keep it in mind inside classes."),
        ),
        challenge = Challenge(
            "Write an async helper `fillSearch(page, text, press = true)` that fills the 'Search' box and presses Enter only when `press` is true.",
            """
                import { Page } from '@playwright/test';

                async function fillSearch(page: Page, text: string, press: boolean = true) {
                  const box = page.getByLabel('Search');
                  await box.fill(text);
                  if (press) await box.press('Enter');
                }
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `wait(ms: number = 1000)` mean?", listOf("ms defaults to 1000", "ms must be 1000", "ms is optional text"), 0, "A default value is used when the caller omits the argument."),
            Quiz("Why does `test('x', async () => {...})` work?", listOf("Functions can be passed as values", "Magic", "It's a Java feature"), 0, "Playwright receives your function and runs it when needed."),
        ),
    ),
    "ts-objects" to Extra(
        deep = listOf(
            H("Interfaces: describe an object's shape"),
            P("An `interface` is a contract: 'an object of this type has these fields'. Like a Java POJO without the getters and setters."),
            Code("interface", "TypeScript", """
                interface User {
                  name: string;
                  password: string;
                  role?: string;     // optional
                }

                const admin: User = { name: 'asha', password: 'pw' };
                const users: User[] = [admin];
            """.trimIndent()),
            H("Handy array methods"),
            Code("map, filter, find", "TypeScript", """
                const nums = [1, 2, 3, 4];
                nums.map((n) => n * 2);            // [2,4,6,8]
                nums.filter((n) => n % 2 === 0);   // [2,4]
                nums.find((n) => n > 2);           // 3
                nums.some((n) => n > 3);           // true
                nums.includes(3);                  // true
                nums.length;                       // 4
            """.trimIndent()),
            H("Spread and optional chaining"),
            Code("... and ?.", "TypeScript", """
                const base = { headless: true, retries: 1 };
                const custom = { ...base, retries: 3 };   // copy + override

                const city = user.address?.city;          // undefined instead of crash
                const name = user.nickname ?? 'friend';   // default if missing
            """.trimIndent()),
            Tip("You saw `...devices['Desktop Chrome']` in the config. That's the spread operator copying settings into your object."),
            Warn("Reading a missing property gives `undefined`, not an exception like Java's NullPointerException. Bugs hide quietly, so type your objects."),
        ),
        challenge = Challenge(
            "Given `const links = ['Home','Docs','API','Blog']`, create a new array with only the names longer than 3 letters, in UPPERCASE.",
            """
                const links = ['Home', 'Docs', 'API', 'Blog'];
                const result = links.filter((l) => l.length > 3).map((l) => l.toUpperCase());
                console.log(result);   // ['HOME','DOCS','BLOG']
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `{ ...base, retries: 3 }` do?", listOf("Copies base and overrides retries", "Deletes base", "Throws an error"), 0, "Spread copies fields; later keys override earlier ones."),
            Quiz("`user.address?.city` when address is undefined gives...", listOf("A crash", "undefined", "An empty string"), 1, "Optional chaining stops safely."),
        ),
    ),
    "ts-async" to Extra(
        deep = listOf(
            H("What is a Promise?"),
            P("A Promise is an object representing a result that isn't ready yet. It's either pending, fulfilled (success) or rejected (error). `await` unwraps it."),
            Code("Promise states", "TypeScript", """
                const p = page.goto('https://example.com');  // Promise (pending)
                await p;                                      // now it's done
                // same as: await page.goto('https://example.com');
            """.trimIndent()),
            H("Doing things at the same time"),
            P("Sequential awaits run one after another. `Promise.all` runs things together and waits for all. You will use this for 'click and wait for the new tab' patterns."),
            Code("Promise.all", "TypeScript", """
                const [response] = await Promise.all([
                  page.waitForResponse('**/api/save'),   // start listening
                  page.getByText('Save').click(),         // then trigger it
                ]);
                console.log(response.status());
            """.trimIndent()),
            H("Handling errors"),
            Code("try / catch / finally", "TypeScript", """
                try {
                  await page.getByText('Optional banner').click({ timeout: 2000 });
                } catch (error) {
                  console.log('No banner, carry on');
                }
            """.trimIndent()),
            H("Where await goes wrong"),
            Code("Common mistakes", "TypeScript", """
                // 1. forgot await
                page.getByText('Go').click();             // BUG

                // 2. await inside forEach does NOT wait
                items.forEach(async (i) => { await i.click(); });   // BUG
                for (const i of items) { await i.click(); }         // correct

                // 3. await on expect is required
                expect(locator).toBeVisible();            // BUG: not awaited
                await expect(locator).toBeVisible();      // correct
            """.trimIndent()),
            Warn("`forEach` with `await` inside does not wait. Always use a `for...of` loop when you need steps in order."),
        ),
        challenge = Challenge(
            "Write a loop that clicks every button in `page.getByRole('button')` one by one, in order. (Hint: you need `.all()` and a `for...of`.)",
            """
                const buttons = await page.getByRole('button').all();
                for (const button of buttons) {
                  await button.click();
                }
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does Promise.all do?", listOf("Runs promises together and waits for all", "Cancels promises", "Runs them in a loop"), 0, "It waits until every promise has settled."),
            Quiz("Why is `forEach(async ...)` dangerous?", listOf("It doesn't wait for each await", "It's too slow", "It's not allowed"), 0, "forEach ignores the returned promises, so steps overlap. Use for...of."),
        ),
    ),
    "ts-classes" to Extra(
        deep = listOf(
            H("Access modifiers and readonly"),
            Code("public / private / readonly", "TypeScript", """
                export class Cart {
                  private items: string[] = [];       // only inside the class
                  readonly owner: string;             // set once, never changed
                  count = 0;                          // public by default

                  constructor(owner: string) { this.owner = owner; }

                  add(item: string) { this.items.push(item); this.count++; }
                }
            """.trimIndent()),
            H("Inheritance"),
            Code("extends", "TypeScript", """
                export class BasePage {
                  constructor(protected page: Page) {}
                  async open(path: string) { await this.page.goto(path); }
                }

                export class HomePage extends BasePage {
                  async search(text: string) {
                    await this.page.getByLabel('Search').fill(text);
                  }
                }
            """.trimIndent()),
            H("Default vs named exports"),
            Code("Two flavours", "TypeScript", """
                // named (preferred): import { LoginPage } from './LoginPage';
                export class LoginPage {}

                // default: import LoginPage from './LoginPage';
                export default class HomePage {}
            """.trimIndent()),
            Tip("One class per file, file named after the class. Same tidy habits you use in Java."),
            Warn("Import paths are relative. `'./LoginPage'` means same folder, `'../pages/LoginPage'` means go up first. Don't add the `.ts` extension."),
        ),
        challenge = Challenge(
            "Create `BasePage` with a `protected page` and a `goto(path)` method. Then `DashboardPage extends BasePage` with a `heading()` method returning the page's level-1 heading locator.",
            """
                import { Page } from '@playwright/test';

                export class BasePage {
                  constructor(protected page: Page) {}
                  async goto(path: string) { await this.page.goto(path); }
                }

                export class DashboardPage extends BasePage {
                  heading() { return this.page.getByRole('heading', { level: 1 }); }
                }
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `protected` allow?", listOf("Access in the class and its children", "Access from anywhere", "Nothing"), 0, "Same as Java's protected (minus package access)."),
            Quiz("Should you write `.ts` in import paths?", listOf("Yes", "No", "Only for classes"), 1, "Leave the extension off."),
        ),
    ),
    "test-structure" to Extra(
        deep = listOf(
            H("All the hooks"),
            Table(
                listOf("Playwright", "TestNG", "Runs"),
                listOf(
                    listOf("`beforeAll`", "@BeforeClass", "Once per worker, per describe"),
                    listOf("`beforeEach`", "@BeforeMethod", "Before every test"),
                    listOf("`afterEach`", "@AfterMethod", "After every test"),
                    listOf("`afterAll`", "@AfterClass", "Once at the end"),
                ),
            ),
            Warn("`beforeAll` does NOT receive `page` (no shared tab). Tests are isolated, so prefer `beforeEach`."),
            H("Nested groups"),
            Code("describe inside describe", "TypeScript", """
                test.describe('Shop', () => {
                  test.describe('Cart', () => {
                    test('adds item', async ({ page }) => { /* ... */ });
                  });
                  test.describe('Checkout', () => {
                    test.beforeEach(async ({ page }) => { /* only for checkout */ });
                    test('pays', async ({ page }) => { /* ... */ });
                  });
                });
            """.trimIndent()),
            H("Skip, focus, expect failure"),
            Code("Annotations", "TypeScript", """
                test.skip('not ready', async ({ page }) => {});
                test.only('debug this one', async ({ page }) => {});
                test.fixme('known bug', async ({ page }) => {});

                test('browser specific', async ({ page, browserName }) => {
                  test.skip(browserName === 'webkit', 'Not supported in Safari yet');
                });
            """.trimIndent()),
            Tip("Put `forbidOnly: !!process.env.CI` in config so a forgotten `test.only` fails the build instead of silently skipping all other tests."),
        ),
        challenge = Challenge(
            "Create a describe 'Search' with a beforeEach that opens '/', and two tests: one for the empty-search message and one skipped on firefox.",
            """
                test.describe('Search', () => {
                  test.beforeEach(async ({ page }) => {
                    await page.goto('/');
                  });

                  test('empty search shows hint', async ({ page }) => {
                    await page.getByRole('button', { name: 'Search' }).click();
                    await expect(page.getByText('Type something')).toBeVisible();
                  });

                  test('autocomplete', async ({ page, browserName }) => {
                    test.skip(browserName === 'firefox', 'flaky in firefox');
                    await page.getByLabel('Search').fill('pla');
                    await expect(page.getByRole('option')).not.toHaveCount(0);
                  });
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Which hook gives you `page` safely?", listOf("beforeAll", "beforeEach", "Neither"), 1, "beforeEach runs per test, where each test has its own page."),
            Quiz("What does `test.fixme` do?", listOf("Skips a test with a known bug", "Fixes the test", "Runs it twice"), 0, "It marks the test as a known problem and skips it."),
        ),
    ),
    "run-tests" to Extra(
        deep = listOf(
            H("Useful flags"),
            Table(
                listOf("Flag", "What it does"),
                listOf(
                    listOf("`--headed`", "Show the browser"),
                    listOf("`--workers=1`", "Run tests one at a time"),
                    listOf("`--retries=2`", "Retry failures"),
                    listOf("`--repeat-each=10`", "Run each test 10 times (hunt flakiness)"),
                    listOf("`--last-failed`", "Re-run only what failed"),
                    listOf("`--reporter=list`", "Plain console output"),
                    listOf("`--grep-invert @slow`", "Skip tests with a tag"),
                    listOf("`--max-failures=1`", "Stop after first failure"),
                ),
            ),
            H("Run one test by line number"),
            Code("file:line", "Bash", """
                npx playwright test tests/login.spec.ts:12
            """.trimIndent()),
            H("VS Code extension"),
            P("The 'Playwright Test for VS Code' extension adds a green play button beside every test, a Testing sidebar, 'Show browser' checkbox, and a 'Pick locator' tool. Set breakpoints in your test and press the debug icon."),
            H("Debug a failing test"),
            Code("Inspector", "TypeScript", """
                // add this line, run with --headed, and the test pauses here
                await page.pause();
            """.trimIndent()),
            Tip("Use `--repeat-each=20 --workers=4` on a suspicious test to find out how flaky it really is."),
        ),
        challenge = Challenge(
            "Write the command to run only tests whose title includes 'checkout', only in Firefox, headed, with one worker.",
            """
                npx playwright test -g "checkout" --project=firefox --headed --workers=1
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("How do you re-run only failed tests?", listOf("--last-failed", "--again", "--retry-only"), 0, "`--last-failed` remembers the previous run."),
            Quiz("What does `page.pause()` open?", listOf("Playwright Inspector", "The report", "Nothing"), 0, "The test freezes and the Inspector lets you step and pick locators."),
        ),
    ),
    "reports-trace" to Extra(
        deep = listOf(
            H("Reporters"),
            Code("Choose reporters in config", "TypeScript", """
                reporter: [
                  ['list'],                                   // console
                  ['html', { open: 'never' }],                // playwright-report/
                  ['junit', { outputFile: 'results.xml' }],   // for Jenkins
                  ['json', { outputFile: 'results.json' }],
                ],
            """.trimIndent()),
            H("Trace modes"),
            Table(
                listOf("Value", "Meaning"),
                listOf(
                    listOf("`on`", "Always record (slow, big)"),
                    listOf("`off`", "Never"),
                    listOf("`on-first-retry`", "Record when a test is retried. Best default for CI"),
                    listOf("`retain-on-failure`", "Record always, keep only failures"),
                ),
            ),
            H("What's inside a trace?"),
            P("A timeline of every action, a before and after DOM snapshot you can inspect with dev tools, console logs, network requests with bodies, the source code line and any screenshots. You can even send the `trace.zip` to a teammate."),
            Code("Open it", "Bash", """
                npx playwright show-trace test-results/login/trace.zip
                # or drag the zip onto https://trace.playwright.dev
            """.trimIndent()),
            Tip("In the HTML report, failed tests have the trace attached. Click it for the full story. This replaces 80% of 'it works on my machine' arguments."),
            Warn("Traces can contain passwords and tokens from network calls. Don't upload CI artifacts publicly."),
        ),
        challenge = Challenge(
            "Configure the project to: use both 'list' and 'html' reporters (html should not auto-open), keep traces only for failures, and screenshots only for failures.",
            """
                import { defineConfig } from '@playwright/test';

                export default defineConfig({
                  reporter: [['list'], ['html', { open: 'never' }]],
                  use: {
                    trace: 'retain-on-failure',
                    screenshot: 'only-on-failure',
                  },
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Which trace mode is a good default for CI?", listOf("on-first-retry", "on", "off"), 0, "It records only when something needs investigation."),
            Quiz("Which reporter helps Jenkins?", listOf("junit", "list", "dot"), 0, "Jenkins understands JUnit XML."),
        ),
    ),
)
