package com.example.playwright.data

val deepD: Map<String, Extra> = emptyMap()

/** New lessons inserted after the lesson whose id is the key. */
val insertions: Map<String, List<Lesson>> by lazy {
    mapOf(
        "first-test" to listOf(npmNode),
        "ts-classes" to listOf(tsTypes),
        "reports-trace" to listOf(debugging),
        "loc-filter" to listOf(locAdvanced),
        "soft-steps" to listOf(events),
        "data" to listOf(globalSetup),
        "codegen-shots" to listOf(emulation),
    )
}

private val npmNode = Lesson(
    id = "npm-node", emoji = "📦", title = "Node and npm for Java people",
    story = "In Java you had the JDK, Maven and a `pom.xml`. Here, `Node` is the engine (like the JVM), `npm` is the package manager (like Maven), and `package.json` is the shopping list. `npx` is a handy tool that runs a program from your shopping list without installing it forever.",
    javaCode = """
        mvn clean test
        mvn dependency:tree
    """.trimIndent(),
    code = """
        npm install                 # install everything in package.json (mvn install)
        npm install -D dotenv       # add a dev-only dependency (<scope>test</scope>)
        npm run test:smoke          # run a script from package.json
        npx playwright test         # run a locally installed tool
        npm ls --depth=0            # list direct dependencies
    """.trimIndent(),
    codeLang = "Bash",
    codeCaption = "`-D` means dev dependency: needed for development and tests, not for shipping.",
    points = listOf(
        "`dependencies` are for shipping, `devDependencies` for testing tools like Playwright.",
        "`package-lock.json` pins exact versions, like a lock file. Commit it, and use `npm ci` in CI.",
        "`node_modules` can be deleted any time and recreated with `npm install`.",
    ),
    quiz = Quiz("In CI, which command installs exactly what's in the lock file?", listOf("npm ci", "npm run", "npx install"), 0, "`npm ci` is a clean, reproducible install."),
    deep = listOf(
        H("A package.json you'll see"),
        Code("package.json", "JSON", """
            {
              "name": "my-pw",
              "scripts": {
                "test": "playwright test",
                "test:smoke": "playwright test --grep @smoke",
                "report": "playwright show-report"
              },
              "devDependencies": {
                "@playwright/test": "^1.50.0",
                "dotenv": "^16.4.5"
              }
            }
        """.trimIndent()),
        P("Scripts are shortcuts: `npm test` runs `playwright test`; any other script uses `npm run name`. Teams use scripts to keep long commands in one place."),
        H("Version symbols"),
        Table(
            listOf("Written", "Means"),
            listOf(
                listOf("`1.50.0`", "exactly this"),
                listOf("`^1.50.0`", "any 1.x.x at or above 1.50.0"),
                listOf("`~1.50.0`", "any 1.50.x"),
            ),
        ),
        H("tsconfig.json"),
        P("Tells TypeScript how strict to be. Playwright runs TypeScript directly (no manual compile step), so you rarely edit it. You may enable `resolveJsonModule` for JSON imports."),
        Tip("`npx playwright --version` shows what's installed. Keep it updated every month or two: each release brings new browsers and features."),
        Warn("Don't install packages globally (`npm i -g`) for project work. Keep tools in the project so everyone on the team has the same version."),
    ),
    challenge = Challenge(
        "Add a script called `test:chrome` that runs only the chromium project headed, then show how to run it.",
        """
            // package.json -> "scripts"
            "test:chrome": "playwright test --project=chromium --headed"

            // terminal
            // npm run test:chrome
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("`^1.50.0` allows...", listOf("Any 1.x.x from 1.50.0 up", "Only 1.50.0", "Any version"), 0, "Caret allows minor and patch updates."),
    ),
)

private val tsTypes = Lesson(
    id = "ts-types", emoji = "🏷️", title = "Types, interfaces and generics",
    story = "Types are name-tags that stop you putting a square toy in a round hole. TypeScript checks the tags before your tests run, so many silly mistakes (like typing `.clik()`) are caught while you type, not after a 10 minute run.",
    javaCode = """
        enum Status { PASS, FAIL }
        List<String> names = new ArrayList<>();
        public <T> T first(List<T> list) { return list.get(0); }
    """.trimIndent(),
    code = """
        type Status = 'pass' | 'fail';              // type alias with fixed choices
        enum Env { Dev = 'dev', Prod = 'prod' }     // enum (strings are friendlier)

        const names: Array<string> = [];             // same as string[]

        function first<T>(list: T[]): T {            // generic, like Java's <T>
          return list[0];
        }
        const n = first(['a', 'b']);                 // n is a string
    """.trimIndent(),
    codeLang = "TypeScript",
    codeCaption = "Generics use `<T>` just like Java.",
    points = listOf(
        "A `type` alias gives a name to any type, even unions of exact words.",
        "Avoid `any`. It switches the safety net off. Use `unknown` and check before use.",
        "Red squiggles in VS Code are your friend. Read them.",
    ),
    quiz = Quiz("What does `type Status = 'pass' | 'fail'` allow?", listOf("Only those two words", "Any string", "Any number"), 0, "It's a union of literal values."),
    deep = listOf(
        H("interface vs type"),
        P("For object shapes they're almost the same. Use `interface` for things you'll extend or classes implement, `type` for unions and shortcuts. Don't agonize: either works for tests."),
        Code("Both", "TypeScript", """
            interface Product { id: number; title: string; price?: number }
            type ID = string | number;
            type Result = { ok: true; data: Product } | { ok: false; error: string };
        """.trimIndent()),
        H("Type assertions: telling the compiler"),
        Code("as", "TypeScript", """
            const body = (await res.json()) as Product;     // "I know it's a Product"
            const el = document.querySelector('h1') as HTMLElement;
        """.trimIndent()),
        Warn("`as` doesn't check anything at runtime. If the API returns something else, your code will crash later. Great for tests, dangerous in production."),
        H("Useful built-in helpers"),
        Code("Partial, Record, Promise", "TypeScript", """
            const update: Partial<Product> = { price: 10 };            // all fields optional
            const prices: Record<string, number> = { apple: 5, mango: 9 };  // like Map<String,Integer>
            async function load(): Promise<Product> { /* ... */ return {} as Product; }
        """.trimIndent()),
        H("Playwright types you'll meet"),
        Table(
            listOf("Type", "What it is"),
            listOf(
                listOf("`Page`", "a browser tab"),
                listOf("`Locator`", "pointer to elements"),
                listOf("`BrowserContext`", "clean profile"),
                listOf("`APIRequestContext`", "the `request` fixture"),
                listOf("`Response` / `Request`", "network objects"),
                listOf("`TestInfo`", "info about the running test"),
            ),
        ),
        Tip("Hover over any Playwright call in VS Code to see its signature and docs. It's like having the Javadoc built in."),
    ),
    challenge = Challenge(
        "Define an interface `Credentials` (user, pass, optional otp), a function `signIn(page, creds)` that fills the fields, and fills 'OTP' only when `otp` exists.",
        """
            import { Page } from '@playwright/test';

            interface Credentials { user: string; pass: string; otp?: string }

            async function signIn(page: Page, creds: Credentials) {
              await page.getByLabel('User').fill(creds.user);
              await page.getByLabel('Password').fill(creds.pass);
              if (creds.otp) {
                await page.getByLabel('OTP').fill(creds.otp);
              }
              await page.getByRole('button', { name: 'Sign in' }).click();
            }
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("`Partial<Product>` means...", listOf("All fields optional", "Half the fields deleted", "A class"), 0, "Handy for update payloads."),
        Quiz("Which is safer than `any`?", listOf("unknown", "object?", "dynamic"), 0, "`unknown` forces you to check the type before using it."),
    ),
)

private val debugging = Lesson(
    id = "debugging", emoji = "🐞", title = "Debugging like a detective",
    story = "When a test fails, don't guess. Be a detective: look at the clues. Playwright gives you a magnifying glass (the Inspector), a time machine (the trace), a video camera and a diary (the console). Use them in this order and most mysteries solve themselves.",
    javaCode = """
        // Selenium: sprinkle breakpoints and Thread.sleep(5000),
        // print page source, take screenshots by hand
    """.trimIndent(),
    code = """
        npx playwright test --debug                 # opens the Inspector
        npx playwright test --ui                    # time-travel UI
        PWDEBUG=1 npx playwright test               # same as --debug
        DEBUG=pw:api npx playwright test            # log every Playwright call
        npx playwright show-trace trace.zip
    """.trimIndent(),
    codeLang = "Bash",
    codeCaption = "On Windows PowerShell use: ${'$'}env:DEBUG='pw:api'; npx playwright test",
    points = listOf(
        "Start with the error message. Playwright's messages say what it waited for and what blocked it.",
        "Use the trace for CI failures, UI mode or `--debug` for local ones.",
        "`await page.pause()` is a breakpoint that opens the Inspector.",
    ),
    quiz = Quiz("Which gives a time-travel view of what happened in CI?", listOf("Trace Viewer", "console.log", "Thread.sleep"), 0, "Traces bundle snapshots, network and logs."),
    deep = listOf(
        H("Reading an error message"),
        Code("Typical failure", "Bash", """
            Error: locator.click: Timeout 30000ms exceeded.
            Call log:
              - waiting for getByRole('button', { name: 'Save' })
              -   locator resolved to <button disabled>Save</button>
              -   element is not enabled
              - retrying click action ...
        """.trimIndent()),
        P("This tells you: the button was FOUND, but it stayed disabled for 30 seconds. The bug is probably an earlier step (a required field wasn't filled), not the locator."),
        H("The debugging checklist"),
        Table(
            listOf("Symptom", "Likely cause"),
            listOf(
                listOf("Timeout waiting for locator", "Wrong locator, or page not where you think"),
                listOf("'strict mode violation'", "Locator matches several elements"),
                listOf("element not enabled / intercepts pointer events", "Missing earlier step / overlay covering it"),
                listOf("Passes alone, fails in suite", "Shared data or state between tests"),
                listOf("Passes locally, fails in CI", "Slower machine, different viewport/OS, data differences"),
            ),
        ),
        H("Console and page errors in your test"),
        Code("Log what the page says", "TypeScript", """
            page.on('console', (msg) => console.log('PAGE LOG:', msg.text()));
            page.on('pageerror', (err) => console.log('PAGE ERROR:', err.message));
            page.on('requestfailed', (req) => console.log('FAILED:', req.url()));
        """.trimIndent()),
        H("Step through in VS Code"),
        P("Install the extension, click the gutter to add a breakpoint in your spec, and use 'Debug Test'. The browser opens and you can step line by line, hover variables, and use 'Pick locator' to test selectors live."),
        Tip("Run the failing test 10 times: `--repeat-each=10`. If it fails sometimes, you've got a timing or data problem, not a logic bug."),
        Warn("Don't fix flakiness by adding `waitForTimeout`. Find what the page is actually waiting for and wait for that."),
    ),
    challenge = Challenge(
        "Add a listener that prints every failed network request URL and every page JavaScript error, then pause the test at the end of the flow.",
        """
            page.on('requestfailed', (req) => console.log('FAILED:', req.url()));
            page.on('pageerror', (err) => console.log('PAGE ERROR:', err.message));

            // ... test steps ...
            await page.pause();
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("'locator resolved to <button disabled>' means...", listOf("Found, but not clickable yet", "Not found", "Hidden"), 0, "Check what should enable it."),
    ),
)

private val locAdvanced = Lesson(
    id = "loc-advanced", emoji = "🧭", title = "Locator strategy: tough cases",
    story = "Some pages are tricky: IDs that change every refresh, items that appear late, pop-ups that sometimes come. A good detective has a plan for each. Today you learn the plan, so your locators stay strong for months.",
    javaCode = """
        // dynamic id: //input[starts-with(@id,'user_')]
        driver.findElement(By.xpath("//input[starts-with(@id,'user_')]"));
    """.trimIndent(),
    code = """
        // 1. Prefer meaning over structure
        page.getByRole('textbox', { name: 'Username' });

        // 2. Dynamic ids: use a stable part
        page.locator('input[id^="user_"]');

        // 3. Many identical buttons: scope first
        page.getByRole('listitem').filter({ hasText: 'Milk' }).getByRole('button', { name: 'Add' });

        // 4. Optional popup
        const banner = page.getByRole('dialog', { name: 'Cookies' });
        if (await banner.isVisible()) await banner.getByRole('button', { name: 'Accept' }).click();
    """.trimIndent(),
    codeLang = "TypeScript",
    codeCaption = "Four strategies that cover most real-life pages.",
    points = listOf(
        "Scope locators: find the container first, then the thing inside it.",
        "Never depend on position or auto-generated class names.",
        "For optional popups use `isVisible()` or `addLocatorHandler`.",
    ),
    quiz = Quiz("Best way to click 'Add' for the item 'Milk' in a list?", listOf("Filter the list item by 'Milk', then find its Add button", "nth(7)", "XPath with absolute path"), 0, "Scoping survives reordering."),
    deep = listOf(
        H("Decision guide"),
        Table(
            listOf("Situation", "Use"),
            listOf(
                listOf("Button / link / heading", "`getByRole` + name"),
                listOf("Form field", "`getByLabel` or placeholder"),
                listOf("Static text", "`getByText`"),
                listOf("Icon-only button", "`getByRole` with aria-label, or `getByTestId`"),
                listOf("Nothing sensible exists", "Ask devs for `data-testid`"),
                listOf("Repeating cards", "Scope with `filter`"),
            ),
        ),
        H("Handling popups that show up randomly"),
        P("Cookie banners and promo modals break clicks because they cover the page. `addLocatorHandler` registers a rule: 'whenever this appears, do this', and Playwright applies it automatically before actions."),
        Code("addLocatorHandler", "TypeScript", """
            await page.addLocatorHandler(
              page.getByRole('button', { name: 'Accept cookies' }),
              async (btn) => { await btn.click(); },
            );
        """.trimIndent()),
        H("Test ids: change the attribute name"),
        Code("Use your team's attribute", "TypeScript", """
            // playwright.config.ts
            use: { testIdAttribute: 'data-qa' },
            // now getByTestId('login') finds data-qa="login"
        """.trimIndent()),
        H("Check your locator"),
        Code("Count matches", "TypeScript", """
            console.log(await page.getByRole('button', { name: 'Save' }).count());   // want exactly 1
            // or use Inspector 'Pick locator' and codegen to confirm
        """.trimIndent()),
        Tip("If a locator needs more than two chained filters, ask yourself whether the page needs a better accessible name or a `data-testid`."),
        Warn("`isVisible()` doesn't wait. It answers right now. For 'maybe it appears', combine with `addLocatorHandler` or assert with a short timeout inside try/catch."),
    ),
    challenge = Challenge(
        "Click 'Delete' for the card titled 'Old plan' within a list of cards (each card has a heading and Delete button), without using nth().",
        """
            const card = page.locator('.card').filter({ has: page.getByRole('heading', { name: 'Old plan' }) });
            await card.getByRole('button', { name: 'Delete' }).click();
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("What does `testIdAttribute: 'data-qa'` do?", listOf("getByTestId reads data-qa instead", "Renames your tests", "Adds a QA team"), 0, "It adapts to your app's attribute."),
    ),
)

private val events = Lesson(
    id = "events", emoji = "📣", title = "Listening: console, requests and responses",
    story = "Your robot has ears. It can hear everything the page says (console messages), every letter the page sends to the server (requests) and every reply (responses). You can use that to catch hidden bugs, like a page that works but secretly screams errors.",
    javaCode = """
        // Selenium needs DevTools protocol or BrowserMob proxy
        // to read console logs or network traffic
    """.trimIndent(),
    code = """
        const errors: string[] = [];
        page.on('console', (m) => { if (m.type() === 'error') errors.push(m.text()); });
        page.on('pageerror', (e) => errors.push(e.message));

        await page.goto('/');
        await page.getByRole('link', { name: 'Products' }).click();

        expect(errors).toEqual([]);   // no JavaScript errors on the way
    """.trimIndent(),
    codeLang = "TypeScript",
    codeCaption = "A cheap and powerful smoke check: 'no console errors'.",
    points = listOf(
        "`page.on(event, handler)` listens forever; `page.once` listens one time.",
        "Common events: `console`, `pageerror`, `request`, `response`, `requestfailed`, `dialog`, `popup`, `download`.",
        "Register listeners before the action that triggers them.",
    ),
    quiz = Quiz("Which event reports uncaught JavaScript exceptions on the page?", listOf("pageerror", "dialog", "download"), 0, "`pageerror` fires for uncaught exceptions."),
    deep = listOf(
        H("Read the body of a response"),
        Code("Check the API the UI called", "TypeScript", """
            const [res] = await Promise.all([
              page.waitForResponse((r) => r.url().includes('/api/cart') && r.request().method() === 'POST'),
              page.getByRole('button', { name: 'Add to cart' }).click(),
            ]);
            expect(res.status()).toBe(200);
            const body = await res.json();
            expect(body.items).toHaveLength(1);
        """.trimIndent()),
        H("Record all API calls"),
        Code("Collect requests", "TypeScript", """
            const calls: string[] = [];
            page.on('request', (r) => { if (r.url().includes('/api/')) calls.push(r.method() + ' ' + r.url()); });
            // ... do things ...
            console.log(calls);
        """.trimIndent()),
        H("Network status in the report"),
        P("Open the Network tab in the Trace Viewer to see every call, status and timing for a failed test, without adding any code."),
        H("Offline mode"),
        Code("Simulate no internet", "TypeScript", """
            await page.context().setOffline(true);
            await page.getByText('Refresh').click();
            await expect(page.getByText('You are offline')).toBeVisible();
            await page.context().setOffline(false);
        """.trimIndent()),
        Tip("Wrap the 'no console errors' trick in a fixture with `auto: true`, and every test gets it for free."),
        Warn("Some sites log harmless errors (blocked ads, analytics). Filter those out by URL or text, or you'll drown in false alarms."),
    ),
    challenge = Challenge(
        "Click 'Save' and verify the page sent a POST to /api/profile with body containing name 'Asha', and the server replied 200.",
        """
            const [req, res] = await Promise.all([
              page.waitForRequest((r) => r.url().includes('/api/profile') && r.method() === 'POST'),
              page.waitForResponse((r) => r.url().includes('/api/profile')),
              page.getByRole('button', { name: 'Save' }).click(),
            ]);
            expect(req.postDataJSON()).toMatchObject({ name: 'Asha' });
            expect(res.status()).toBe(200);
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("Why register `page.on(...)` before the click?", listOf("Or you may miss the event", "It's required by TypeScript", "To save memory"), 0, "Events fire once; you must already be listening."),
    ),
)

private val globalSetup = Lesson(
    id = "global-setup", emoji = "🌍", title = "Global setup, teardown and environments",
    story = "Before a big football match, the ground staff mark the field once. After the match they clean up once. Not before every single kick! Global setup and teardown work like that: they run once for the whole suite.",
    javaCode = """
        @BeforeSuite public void startAll() { ... }
        @AfterSuite  public void stopAll()  { ... }
    """.trimIndent(),
    code = """
        // tests/db.setup.ts
        import { test as setup } from '@playwright/test';
        setup('seed database', async ({ request }) => {
          await request.post('/api/test/seed');
        });

        // tests/db.teardown.ts
        import { test as teardown } from '@playwright/test';
        teardown('clean database', async ({ request }) => {
          await request.post('/api/test/clean');
        });

        // playwright.config.ts
        projects: [
          { name: 'db-setup', testMatch: /db\.setup\.ts/, teardown: 'db-cleanup' },
          { name: 'db-cleanup', testMatch: /db\.teardown\.ts/ },
          { name: 'app', dependencies: ['db-setup'] },
        ],
    """.trimIndent(),
    codeLang = "TypeScript",
    codeCaption = "Setup projects show up in the HTML report and traces too, which `globalSetup` files don't.",
    points = listOf(
        "Prefer setup/teardown PROJECTS over the older `globalSetup` file option.",
        "`@BeforeSuite` → setup project; `@AfterSuite` → its `teardown` project.",
        "Use `.env` files or `BASE_URL`-style variables to switch environments.",
    ),
    quiz = Quiz("Which is the modern replacement for @BeforeSuite?", listOf("A setup project", "beforeEach", "retries"), 0, "Projects with dependencies run first and are visible in reports."),
    deep = listOf(
        H("Multiple environments"),
        Code("env.ts pattern", "TypeScript", """
            // run: ENV=staging npx playwright test
            import dotenv from 'dotenv';
            dotenv.config({ path: '.env.' + (process.env.ENV ?? 'dev') });
            // .env.dev     BASE_URL=http://localhost:3000
            // .env.staging BASE_URL=https://staging.example.com
        """.trimIndent()),
        H("Cleaning up what a test created"),
        Code("afterEach cleanup", "TypeScript", """
            let createdId: string;
            test('create item', async ({ request }) => {
              const res = await request.post('/api/items', { data: { name: 'x' } });
              createdId = (await res.json()).id;
            });
            test.afterEach(async ({ request }) => {
              if (createdId) await request.delete('/api/items/' + createdId);
            });
        """.trimIndent()),
        Warn("Shared mutable state in module-level variables breaks under parallel runs. Prefer fixtures that create and delete their own data."),
        H("Serial flows"),
        P("Some flows are naturally sequential (create, edit, delete). Mark them with `test.describe.configure({ mode: 'serial' })`, but prefer one longer test with `test.step` blocks."),
        Tip("Keep setup short. A 10 minute global setup makes every run painful. Seed with APIs, not the UI."),
    ),
    challenge = Challenge(
        "Config two environments: running `ENV=qa npx playwright test` should load `.env.qa` and use its BASE_URL as baseURL.",
        """
            // playwright.config.ts
            import { defineConfig } from '@playwright/test';
            import dotenv from 'dotenv';
            dotenv.config({ path: '.env.' + (process.env.ENV ?? 'dev') });

            export default defineConfig({
              use: { baseURL: process.env.BASE_URL },
            });
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("Why seed data with APIs instead of the UI?", listOf("Faster and more reliable", "UI can't create data", "Required by Playwright"), 0, "Less to break, less to wait for."),
    ),
)

private val emulation = Lesson(
    id = "emulation", emoji = "📱", title = "Mobile, locale, clock and storage tricks",
    story = "Your robot can wear costumes! It can pretend to be an iPhone, speak French, live in Tokyo's time zone or even believe it is New Year's Eve. Great for testing how the site behaves for different people without owning 20 phones.",
    javaCode = """
        ChromeOptions o = new ChromeOptions();
        o.setExperimentalOption("mobileEmulation", Map.of("deviceName", "Pixel 7"));
    """.trimIndent(),
    code = """
        import { test, expect, devices } from '@playwright/test';

        test.use({
          ...devices['iPhone 13'],
          locale: 'fr-FR',
          timezoneId: 'Asia/Tokyo',
          colorScheme: 'dark',
        });

        test('mobile french dark', async ({ page }) => {
          await page.goto('/');
          await expect(page.getByRole('button', { name: 'Menu' })).toBeVisible();
        });
    """.trimIndent(),
    codeLang = "TypeScript",
    codeCaption = "`devices[...]` sets viewport, user agent, touch and pixel ratio together.",
    points = listOf(
        "Use `projects` to run a whole suite on desktop and mobile configs.",
        "Playwright emulates devices in real browser engines; it is not a real phone.",
        "You can control time with `page.clock` to test countdowns and expiry.",
    ),
    quiz = Quiz("How do you run a test as an iPhone?", listOf("Use devices['iPhone 13']", "Install an emulator", "Change the OS"), 0, "Spread the device descriptor into `use`."),
    deep = listOf(
        H("Control the clock"),
        Code("Fake time", "TypeScript", """
            await page.clock.install({ time: new Date('2026-12-31T23:59:50') });
            await page.goto('/countdown');
            await page.clock.fastForward('00:00:15');   // jump 15 seconds
            await expect(page.getByText('Happy New Year')).toBeVisible();
        """.trimIndent()),
        H("Cookies and storage"),
        Code("Read and write", "TypeScript", """
            await context.addCookies([{ name: 'ab', value: 'B', url: 'https://example.com' }]);
            const cookies = await context.cookies();

            await page.evaluate(() => localStorage.setItem('theme', 'dark'));
            const theme = await page.evaluate(() => localStorage.getItem('theme'));
            await context.clearCookies();
        """.trimIndent()),
        H("Geolocation, permissions, headers"),
        Code("Context options", "TypeScript", """
            test.use({
              geolocation: { latitude: 51.5, longitude: -0.12 },
              permissions: ['geolocation'],
              extraHTTPHeaders: { 'X-Test': '1' },
              viewport: { width: 1920, height: 1080 },
            });
        """.trimIndent()),
        H("Responsive testing"),
        Code("Resize on the fly", "TypeScript", """
            await page.setViewportSize({ width: 375, height: 812 });
            await expect(page.getByRole('button', { name: 'Menu' })).toBeVisible();
        """.trimIndent()),
        Tip("Mobile project in config: `{ name: 'Mobile Safari', use: { ...devices['iPhone 13'] } }` uses the WebKit engine, a handy stand-in for Safari bugs."),
        Warn("Emulation can't reproduce real device quirks like hardware keyboards or native browsers. Keep a few real-device checks for critical flows."),
    ),
    challenge = Challenge(
        "Write a test with a 375px wide viewport that asserts the hamburger 'Menu' button is visible and the 'Desktop nav' region is hidden.",
        """
            test('mobile layout', async ({ page }) => {
              await page.setViewportSize({ width: 375, height: 812 });
              await page.goto('/');
              await expect(page.getByRole('button', { name: 'Menu' })).toBeVisible();
              await expect(page.getByRole('navigation', { name: 'Desktop nav' })).toBeHidden();
            });
        """.trimIndent(),
    ),
    moreQuiz = listOf(
        Quiz("What does `page.clock.fastForward` do?", listOf("Moves fake time forward", "Speeds up the test runner", "Skips a test"), 0, "Useful for timers and expiry."),
    ),
)

val module9 = Module(
    id = "m9", emoji = "🏆", title = "Hero zone", subtitle = "Capstone, best practices, interview prep", accentIndex = 3,
    lessons = listOf(
        Lesson(
            id = "capstone", emoji = "🏗️", title = "Capstone: build a real framework",
            story = "Time to build the whole castle using every brick you've learned. A professional Playwright framework isn't complicated: it's a handful of folders with clear jobs. Build it once, and you can carry it to any company.",
            javaCode = """
                src/main/java/pages/...
                src/test/java/tests/...
                testng.xml, pom.xml, config.properties
            """.trimIndent(),
            code = """
                my-framework/
                  playwright.config.ts
                  package.json
                  .env.example
                  tests/
                    auth.setup.ts
                    login.spec.ts
                    cart.spec.ts
                  pages/
                    LoginPage.ts
                    CartPage.ts
                  components/
                    HeaderComponent.ts
                  fixtures/
                    index.ts           <- custom test + page fixtures
                  data/
                    users.json
                  utils/
                    api.ts             <- API helpers for setup
            """.trimIndent(),
            codeLang = "Bash",
            codeCaption = "A clean, scalable layout used by many real teams.",
            points = listOf(
                "Tests say WHAT happens; pages say HOW; fixtures hand things over; data feeds inputs.",
                "Auth via setup project, data via API, assertions in tests.",
                "CI runs smoke tests on every push, full suite nightly.",
            ),
            quiz = Quiz("Where do locators belong?", listOf("Page objects / components", "Config file", "package.json"), 0, "Keep them in one place so UI changes need one edit."),
            deep = listOf(
                H("Step-by-step build plan"),
                P("1) Init the project. 2) Write one test with raw steps. 3) Extract a Page Object. 4) Add a fixture that hands it to tests. 5) Add auth setup. 6) Add API helpers for test data. 7) Tag smoke tests. 8) Add CI. 9) Add traces and reports. Do each step only when you feel the need for it."),
                H("Fixtures file wires everything"),
                Code("fixtures/index.ts", "TypeScript", """
                    import { test as base, expect } from '@playwright/test';
                    import { LoginPage } from '../pages/LoginPage';
                    import { CartPage } from '../pages/CartPage';

                    type Pages = { loginPage: LoginPage; cartPage: CartPage };

                    export const test = base.extend<Pages>({
                      loginPage: async ({ page }, use) => { await use(new LoginPage(page)); },
                      cartPage: async ({ page }, use) => { await use(new CartPage(page)); },
                    });
                    export { expect };
                """.trimIndent()),
                H("A test that reads like English"),
                Code("tests/cart.spec.ts", "TypeScript", """
                    import { test, expect } from '../fixtures';

                    test.describe('Cart', () => {
                      test('user can remove an item', { tag: '@smoke' }, async ({ cartPage }) => {
                        await cartPage.open();
                        await cartPage.remove('Milk');
                        await expect(cartPage.itemRow('Milk')).toHaveCount(0);
                      });
                    });
                """.trimIndent()),
                H("Checklist before calling it done"),
                Table(
                    listOf("Item", "Done when"),
                    listOf(
                        listOf("Locators", "Role/label/testid first, no absolute XPath"),
                        listOf("Waits", "No `waitForTimeout` anywhere"),
                        listOf("Data", "Unique per run, cleaned up"),
                        listOf("Secrets", "In env / CI secrets only"),
                        listOf("CI", "Runs on every PR, uploads report"),
                        listOf("Flakes", "`--repeat-each=10` passes"),
                    ),
                ),
                Tip("Practice site to try your framework on: https://playwright.dev, https://demo.playwright.dev/todomvc, https://www.saucedemo.com."),
            ),
            challenge = Challenge(
                "Mini capstone: on https://www.saucedemo.com build `LoginPage` + `InventoryPage` page objects and a fixture, then write 3 tests: valid login, locked-out user error, add item to cart badge shows 1.",
                """
                    // LoginPage: open(), login(user, pass), error locator
                    // InventoryPage: addToCart(name), cartBadge locator
                    // users: standard_user / locked_out_user, password: secret_sauce

                    test('valid login', async ({ loginPage, page }) => {
                      await loginPage.open();
                      await loginPage.login('standard_user', 'secret_sauce');
                      await expect(page).toHaveURL(/inventory/);
                    });
                    test('locked out user', async ({ loginPage }) => {
                      await loginPage.open();
                      await loginPage.login('locked_out_user', 'secret_sauce');
                      await expect(loginPage.error).toContainText('locked out');
                    });
                    test('cart badge', async ({ loginPage, inventoryPage }) => {
                      await loginPage.open();
                      await loginPage.login('standard_user', 'secret_sauce');
                      await inventoryPage.addToCart('Sauce Labs Backpack');
                      await expect(inventoryPage.cartBadge).toHaveText('1');
                    });
                """.trimIndent(),
            ),
            moreQuiz = listOf(
                Quiz("Order of building a framework?", listOf("Start simple, extract as you feel pain", "Design everything upfront", "Copy another team's blindly"), 0, "Extract Page Objects and fixtures when duplication hurts."),
            ),
        ),
        Lesson(
            id = "best-practices", emoji = "🌟", title = "Best practices and anti-patterns",
            story = "Good habits make a hero. Here's the cheat list of do's and don'ts that separate flaky, painful suites from fast, calm ones. Read it again after your first month of real work.",
            javaCode = null,
            code = """
                // DO
                await expect(page.getByRole('alert')).toHaveText('Saved');   // web-first assertion
                page.getByRole('button', { name: 'Save' });                  // user-facing locator

                // DON'T
                await page.waitForTimeout(3000);                             // sleep
                page.locator('div > div:nth-child(3) > span');               // brittle path
                expect(await el.isVisible()).toBe(true);                     // no retry
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "If you remember nothing else, remember these three do/don't pairs.",
            points = listOf(
                "Test what users see, not implementation details.",
                "Keep tests independent: any test can run alone and in any order.",
                "Fail fast with clear messages; add custom messages to important assertions.",
            ),
            quiz = Quiz("Best replacement for `waitForTimeout(3000)`?", listOf("Wait for a specific condition", "waitForTimeout(5000)", "Retry the test"), 0, "Wait for the thing you actually need."),
            deep = listOf(
                H("The golden rules"),
                Table(
                    listOf("Rule", "Why"),
                    listOf(
                        listOf("One test, one behaviour", "Failures point at one problem"),
                        listOf("No test depends on another", "Parallel and reorder safe"),
                        listOf("Create data in the test (via API)", "No shared fragile state"),
                        listOf("Use `getByRole` / `getByLabel`", "Survives redesigns and checks accessibility"),
                        listOf("Web-first assertions", "Auto-retry removes flakiness"),
                        listOf("Mock third parties", "Payments, maps and emails shouldn't break your suite"),
                        listOf("Trace on retry in CI", "Debug without reproducing"),
                    ),
                ),
                H("Anti-patterns to hunt in code review"),
                Code("Smell list", "TypeScript", """
                    await page.waitForTimeout(2000);                 // 1. sleeps
                    if (await el.isVisible()) { ... }                // 2. conditional logic hiding failures
                    test.describe.serial(...)  // 3. long dependent chains
                    await page.locator('xpath=(//div)[7]').click();  // 4. index XPath
                    expect(await a.textContent()).toBe('x');         // 5. no retry
                    // 6. missing await   7. login through UI in every test   8. giant 100-line tests
                """.trimIndent()),
                H("Speed tips"),
                P("Log in once with storageState. Create data with APIs. Block images/analytics with `route`. Run in parallel. Shard in CI. Don't use `networkidle`. Reuse `webServer` locally."),
                H("Code style"),
                P("Use ESLint with `@typescript-eslint/no-floating-promises` to catch missing awaits and Prettier for formatting. Name tests like sentences: 'user can reset password'."),
                Warn("Tests with `if` statements usually hide a problem: the test doesn't know what should happen. Make the state deterministic instead."),
                Tip("Review each failure's trace before adding a retry. Retries should be a safety net, not a crutch."),
            ),
            challenge = Challenge(
                "Fix this flaky test: it sleeps 3s, uses an index XPath, and reads text without retry.",
                """
                    // BAD
                    await page.getByText('Save').click();
                    await page.waitForTimeout(3000);
                    const t = await page.locator('xpath=(//div)[7]').textContent();
                    expect(t).toBe('Saved');

                    // GOOD
                    await page.getByRole('button', { name: 'Save' }).click();
                    await expect(page.getByRole('status')).toHaveText('Saved');
                """.trimIndent(),
            ),
            moreQuiz = listOf(
                Quiz("Why mock third-party services?", listOf("They can be slow or down, making tests flaky", "To save memory", "It's mandatory"), 0, "Your suite shouldn't fail because someone else's service is down."),
            ),
        ),
        Lesson(
            id = "selenium-map", emoji = "🗺️", title = "Selenium to Playwright cheat map",
            story = "You're not starting from zero, you already know Selenium! This map translates the words you already know into Playwright words. Keep it handy when moving a real project.",
            javaCode = """
                driver.findElement(By.id("x")).sendKeys("a");
                driver.switchTo().frame("f");
                new Select(el).selectByValue("1");
            """.trimIndent(),
            code = """
                await page.locator('#x').fill('a');
                await page.frameLocator('#f');
                await el.selectOption('1');
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Almost everything has a shorter, safer equivalent.",
            points = listOf(
                "Selenium is 'find then act'. Playwright is 'describe, then act (with waits)'.",
                "There's no driver, no switchTo, no explicit wait for normal actions.",
                "Keep your Page Object idea, locators just become `Locator` fields.",
            ),
            quiz = Quiz("Selenium `findElements().size()` is Playwright...", listOf("locator.count()", "locator.size()", "locator.length"), 0, "`await locator.count()`."),
            deep = listOf(
                H("The translation table"),
                Table(
                    listOf("Selenium (Java)", "Playwright (TS)"),
                    listOf(
                        listOf("`driver.get(url)`", "`await page.goto(url)`"),
                        listOf("`findElement(By.id())`", "`page.locator('#id')`"),
                        listOf("`element.click()`", "`await locator.click()`"),
                        listOf("`sendKeys(text)`", "`await locator.fill(text)`"),
                        listOf("`element.getText()`", "`await locator.innerText()` or `toHaveText`"),
                        listOf("`getAttribute(x)`", "`await locator.getAttribute(x)`"),
                        listOf("`isDisplayed()`", "`await locator.isVisible()` or `toBeVisible`"),
                        listOf("`isSelected()`", "`await locator.isChecked()`"),
                        listOf("`findElements().size()`", "`await locator.count()`"),
                        listOf("`new Select(el)`", "`locator.selectOption()`"),
                        listOf("`Actions.moveToElement`", "`locator.hover()`"),
                        listOf("`dragAndDrop`", "`locator.dragTo()`"),
                        listOf("`switchTo().frame`", "`page.frameLocator()`"),
                        listOf("`switchTo().alert()`", "`page.on('dialog')`"),
                        listOf("`getWindowHandles`", "`context.pages()` / popup event"),
                        listOf("`JavascriptExecutor`", "`page.evaluate()`"),
                        listOf("`TakesScreenshot`", "`page.screenshot()`"),
                        listOf("`driver.getTitle()`", "`await page.title()`"),
                        listOf("`driver.getCurrentUrl()`", "`page.url()`"),
                        listOf("`driver.navigate().back()`", "`await page.goBack()`"),
                        listOf("`driver.navigate().refresh()`", "`await page.reload()`"),
                        listOf("`driver.quit()`", "automatic per test"),
                    ),
                ),
                H("Test framework map"),
                Table(
                    listOf("TestNG / JUnit", "Playwright Test"),
                    listOf(
                        listOf("`@Test`", "`test()`"),
                        listOf("`@BeforeMethod`", "`test.beforeEach`"),
                        listOf("`@DataProvider`", "`for` loop over data"),
                        listOf("`groups`", "tags + `--grep`"),
                        listOf("`SoftAssert`", "`expect.soft`"),
                        listOf("`testng.xml`", "`playwright.config.ts`"),
                        listOf("`retryAnalyzer`", "`retries` setting"),
                        listOf("`ExtentReports`", "built-in HTML report"),
                        listOf("Page Factory", "Locator fields in constructor"),
                    ),
                ),
                Tip("Migrate gradually: new tests in Playwright, old ones stay in Selenium until you have time. Both can run in the same CI pipeline."),
            ),
            challenge = Challenge(
                "Translate to Playwright: `driver.findElement(By.cssSelector(\".item\")).getText()` compared with 'Milk' using Assert.assertEquals.",
                """
                    await expect(page.locator('.item')).toHaveText('Milk');
                """.trimIndent(),
            ),
            moreQuiz = listOf(
                Quiz("TestNG `groups` become...", listOf("Tags with --grep", "Packages", "Fixtures"), 0, "Tag tests and filter with grep."),
            ),
        ),
        Lesson(
            id = "interview", emoji = "🎤", title = "Interview questions and answers",
            story = "Hero lesson: how to explain what you know when someone asks. Answer each question in your own words, using the kid-simple pictures from this app. If you can teach it to a 5-year-old, you understand it!",
            javaCode = null,
            code = """
                Q: Playwright vs Selenium?
                A: Direct browser connection, built-in auto-wait,
                   isolated contexts, parallel runner, tracing, API
                   testing and mocking out of the box.
            """.trimIndent(),
            codeLang = "Bash",
            codeCaption = "Short, confident answers win interviews.",
            points = listOf(
                "Always give one concrete example from a test you wrote.",
                "Mention trade-offs: Playwright doesn't use Selenium Grid, and uses Node, not Java.",
                "Know your debugging story: trace viewer, `--ui`, `--repeat-each`.",
            ),
            quiz = Quiz("What is the best way to answer 'how do you handle flaky tests'?", listOf("Find the root cause using traces, fix waits and data, then verify with repeat-each", "Add sleeps", "Re-run until green"), 0, "Root-cause thinking shows seniority."),
            deep = listOf(
                H("Core questions"),
                Table(
                    listOf("Question", "Short answer"),
                    listOf(
                        listOf("What is auto-waiting?", "Actionability checks (visible, stable, enabled, receives events) before each action"),
                        listOf("Locator vs ElementHandle?", "Locator is lazy and re-resolves each time; handles can go stale. Use locators"),
                        listOf("What is a BrowserContext?", "An isolated, incognito-like profile; one per test by default"),
                        listOf("How do you run in parallel?", "Workers run files in parallel; `fullyParallel` for tests in a file"),
                        listOf("Handle login?", "Setup project saves storageState, reused by all tests"),
                        listOf("Handle iframes?", "`frameLocator()`, no switching"),
                        listOf("How to mock APIs?", "`page.route()` with fulfill/abort/continue"),
                        listOf("How to debug a CI failure?", "Trace on first retry, open it in the Trace Viewer"),
                        listOf("How to handle new tabs?", "`waitForEvent('popup')` or context 'page' event"),
                        listOf("Soft assertions?", "`expect.soft`, collected and reported at test end"),
                        listOf("Data driven testing?", "Loop over data to generate tests, or parametrized projects"),
                        listOf("Does Playwright support Safari?", "WebKit engine, not the real Safari app"),
                    ),
                ),
                H("Coding round: practise these"),
                Code("Typical tasks", "TypeScript", """
                    // 1. Login and assert dashboard
                    // 2. Read a table, assert sorted order
                    const names = await page.locator('td.name').allInnerTexts();
                    expect(names).toEqual([...names].sort());

                    // 3. Wait for a response and check body
                    // 4. Mock an API error and check the UI message
                    // 5. Upload a file and assert success text
                    // 6. Write a Page Object and a fixture
                """.trimIndent()),
                H("Tell a story"),
                P("Prepare one real story: a flaky test you fixed. Structure: Symptom, Investigation (trace, repeat-each), Root cause (e.g. missing await or shared data), Fix, Result (e.g. 'flaky rate went from 15% to 0')."),
                Tip("Quiz yourself: cover the answer column above and answer aloud in simple words. Then redo each lesson's practice challenge."),
            ),
            challenge = Challenge(
                "Explain in 3 sentences, to a 5-year-old, why `await` matters in Playwright tests. Then write the code that shows the bug when it is missing.",
                """
                    // "The robot does things slowly. await tells our test to wait
                    //  until the robot finishes before asking for the next thing."

                    page.getByRole('button', { name: 'Save' }).click();    // missing await
                    await expect(page.getByText('Saved')).toBeVisible();   // may check too early
                """.trimIndent(),
            ),
            moreQuiz = listOf(
                Quiz("Locator vs ElementHandle?", listOf("Locators re-find the element each time", "Handles are lazy", "They're identical"), 0, "That's why locators never go stale."),
            ),
        ),
        Lesson(
            id = "cheatsheet", emoji = "🧾", title = "One-page cheat sheet",
            story = "Every hero keeps a pocket map. This page packs the most-used commands and snippets together. Bookmark it, and use the copy button to paste what you need.",
            javaCode = null,
            code = """
                // NAVIGATION
                await page.goto('/path');  await page.goBack();  await page.reload();

                // LOCATORS
                page.getByRole('button', { name: 'Save' })   page.getByLabel('Email')
                page.getByText('Hi')   page.getByPlaceholder('Search')   page.getByTestId('id')
                page.locator('css or xpath')   loc.filter({ hasText: 'x' })   loc.nth(0)

                // ACTIONS
                await loc.click();  await loc.fill('x');  await loc.check();
                await loc.selectOption('v');  await loc.hover();  await loc.press('Enter');
                await loc.setInputFiles('a.png');  await loc.dragTo(other);

                // ASSERTIONS
                await expect(loc).toBeVisible();  toHaveText('x');  toHaveCount(3);
                await expect(page).toHaveURL(/x/);  toHaveTitle('x');

                // WAITING
                await page.waitForURL('**/x');  await page.waitForResponse('**/api/x');
                await loc.waitFor({ state: 'hidden' });

                // NETWORK
                await page.route('**/api/x', (r) => r.fulfill({ json: {} }));

                // API
                const res = await request.post('/api', { data: {} });  expect(res.ok()).toBeTruthy();
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Commands for the terminal are in the next block.",
            points = listOf(
                "Most-used trio: `getByRole`, `click/fill`, `expect(...).toBeVisible()`.",
                "Debugging trio: `--ui`, `--debug`, Trace Viewer.",
                "Speed trio: storageState, API setup, parallel workers.",
            ),
            quiz = Quiz("Which three tools debug a failing test?", listOf("--ui, --debug, Trace Viewer", "sleep, retry, restart", "print, print, print"), 0, "The built-in tools beat guessing."),
            deep = listOf(
                H("Terminal commands"),
                Code("CLI", "Bash", """
                    npm init playwright@latest         # new project
                    npx playwright test                # run all
                    npx playwright test -g "login"     # by name
                    npx playwright test --ui           # UI mode
                    npx playwright test --headed       # watch
                    npx playwright test --debug        # inspector
                    npx playwright test --project=chromium
                    npx playwright test --grep @smoke
                    npx playwright test --repeat-each=10
                    npx playwright test --last-failed
                    npx playwright test --shard=1/4
                    npx playwright test --update-snapshots
                    npx playwright show-report
                    npx playwright show-trace trace.zip
                    npx playwright codegen https://site.com
                    npx playwright install
                """.trimIndent()),
                H("Test file skeleton"),
                Code("Copy me", "TypeScript", """
                    import { test, expect } from '@playwright/test';

                    test.describe('Feature', () => {
                      test.beforeEach(async ({ page }) => {
                        await page.goto('/');
                      });

                      test('does something', { tag: '@smoke' }, async ({ page }) => {
                        await page.getByRole('button', { name: 'Go' }).click();
                        await expect(page.getByText('Done')).toBeVisible();
                      });
                    });
                """.trimIndent()),
                H("Config skeleton"),
                Code("Copy me too", "TypeScript", """
                    import { defineConfig, devices } from '@playwright/test';
                    export default defineConfig({
                      testDir: './tests',
                      fullyParallel: true,
                      forbidOnly: !!process.env.CI,
                      retries: process.env.CI ? 2 : 0,
                      reporter: [['html', { open: 'never' }]],
                      use: { baseURL: process.env.BASE_URL, trace: 'on-first-retry' },
                      projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
                    });
                """.trimIndent()),
                Tip("You made it! Next steps: build the capstone, contribute tests to a real project, learn Playwright's component testing, and explore accessibility testing with `@axe-core/playwright`. 🚀"),
            ),
            challenge = null,
            moreQuiz = emptyList(),
        ),
    ),
)
