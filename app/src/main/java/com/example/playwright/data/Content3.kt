package com.example.playwright.data

val module7 = Module(
    id = "m7", emoji = "🏗️", title = "Organize like a pro", subtitle = "Config, Page Objects, fixtures, data", accentIndex = 6,
    lessons = listOf(
        Lesson(
            id = "config", emoji = "⚙️", title = "playwright.config.ts",
            story = "The config file is the control panel of your robot: which browsers, how many helpers at once, how long to wait, where the website lives. Change it once and every test follows.",
            javaCode = """
                <suite name="Regression" parallel="tests" thread-count="4">
                  <test name="Chrome"> ... </test>
                </suite>
            """.trimIndent(),
            code = """
                import { defineConfig, devices } from '@playwright/test';

                export default defineConfig({
                  testDir: './tests',
                  timeout: 30_000,
                  retries: 1,
                  workers: 4,
                  reporter: [['html']],
                  use: {
                    baseURL: 'https://example.com',
                    headless: true,
                    trace: 'on-first-retry',
                    screenshot: 'only-on-failure',
                  },
                  projects: [
                    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
                    { name: 'firefox',  use: { ...devices['Desktop Firefox'] } },
                    { name: 'mobile',   use: { ...devices['Pixel 7'] } },
                  ],
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`projects` = run the same tests on several browsers or devices.",
            points = listOf(
                "With `baseURL`, you can write `page.goto('/login')`.",
                "`workers` is how many tests run at once (parallel threads).",
                "`...devices['Pixel 7']` spreads ready-made phone settings into your config.",
                "`projects` replaces the browser parameters in testng.xml.",
            ),
            quiz = Quiz("What does `baseURL` give you?", listOf("Shorter goto paths like '/login'", "Faster browsers", "A free website"), 0, "Playwright glues baseURL in front of relative paths."),
        ),
        Lesson(
            id = "pom", emoji = "📄", title = "Page Object Model",
            story = "Instead of telling the robot every tiny step each time, you write a \"page guide\" once: where the email box is, where the button is. Then all tests simply say \"login please\". You already know this from Selenium, and here it's even shorter.",
            javaCode = """
                public class LoginPage {
                    @FindBy(id = "email") WebElement email;
                    public void login(String u, String p) { ... }
                }
            """.trimIndent(),
            code = """
                // pages/LoginPage.ts
                import { Page, Locator } from '@playwright/test';

                export class LoginPage {
                  readonly email: Locator;
                  readonly password: Locator;
                  readonly submit: Locator;

                  constructor(private page: Page) {
                    this.email = page.getByLabel('Email');
                    this.password = page.getByLabel('Password');
                    this.submit = page.getByRole('button', { name: 'Sign in' });
                  }

                  async login(user: string, pass: string) {
                    await this.email.fill(user);
                    await this.password.fill(pass);
                    await this.submit.click();
                  }
                }

                // tests/login.spec.ts
                test('login works', async ({ page }) => {
                  await new LoginPage(page).login('asha@x.com', 'pw');
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "No `PageFactory.initElements` and no `@FindBy`. Locators are lazy, so storing them is safe.",
            points = listOf(
                "Store `Locator`s in the constructor. They're maps, not elements, so they never go stale.",
                "Page methods are `async` and `await` each step.",
                "Keep assertions in tests, not in page classes (a common good practice).",
            ),
            quiz = Quiz("Why is it safe to store Locators in a page class?", listOf("They search again on each action", "They're cached forever", "They are static"), 0, "Locators are lazy and re-find the element each time, so there's no staleness."),
        ),
        Lesson(
            id = "fixtures", emoji = "🧸", title = "Fixtures: ready-made toys",
            story = "You already met a fixture: `page`. It's a toy that appears whenever you ask for it. You can invent your own, like `loginPage`, that arrives already set up and gets tidied away after the test. It's like your `BaseTest` class but way more flexible.",
            javaCode = """
                public class BaseTest {
                    @BeforeMethod void setUp() { driver = new ChromeDriver(); }
                    @AfterMethod void tearDown() { driver.quit(); }
                }
            """.trimIndent(),
            code = """
                // fixtures.ts
                import { test as base, expect } from '@playwright/test';
                import { LoginPage } from './pages/LoginPage';

                export const test = base.extend<{ loginPage: LoginPage }>({
                  loginPage: async ({ page }, use) => {
                    const lp = new LoginPage(page);
                    await page.goto('/login');   // setup
                    await use(lp);               // test runs here
                    // teardown code can go after use()
                  },
                });
                export { expect };

                // my.spec.ts
                import { test, expect } from './fixtures';
                test('login', async ({ loginPage }) => {
                  await loginPage.login('asha@x.com', 'pw');
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Only fixtures you ask for are created, so tests stay fast.",
            points = listOf(
                "Code before `use()` is setup; code after is teardown.",
                "Replace inheritance (`extends BaseTest`) with fixtures.",
                "Import `test` from your own fixtures file instead of from Playwright.",
            ),
            quiz = Quiz("In a fixture, what does `await use(x)` mean?", listOf("Hand x to the test and run it", "Delete x", "Log x"), 0, "The test body runs at that point; code after use() is cleanup."),
        ),
        Lesson(
            id = "data", emoji = "🗂️", title = "Test data and environments",
            story = "The same recipe can be cooked with different ingredients. Loop over a list of users and make one test per user, just like TestNG's `@DataProvider`. Secrets like passwords go in environment variables, not in code.",
            javaCode = """
                @DataProvider
                public Object[][] users() {
                    return new Object[][] {{"asha","pw1"},{"ravi","pw2"}};
                }
            """.trimIndent(),
            code = """
                const users = [
                  { name: 'asha', pass: 'pw1' },
                  { name: 'ravi', pass: 'pw2' },
                ];

                for (const u of users) {
                  test(`login as ${'$'}{u.name}`, async ({ page }) => {
                    await page.goto('/login');
                    await page.getByLabel('User').fill(u.name);
                    await page.getByLabel('Password').fill(u.pass);
                    await page.getByRole('button', { name: 'Sign in' }).click();
                  });
                }

                // secrets from the environment
                const password = process.env.APP_PASSWORD ?? '';
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Run with: APP_PASSWORD=xyz npx playwright test",
            points = listOf(
                "A plain `for` loop creates one test per item. No annotation magic.",
                "`process.env.NAME` reads environment variables. Use the `dotenv` package for `.env` files.",
                "JSON files can be imported and used as data too.",
                "`??` means \"use the right side if the left is empty\".",
            ),
            quiz = Quiz("How do you read an environment variable?", listOf("System.getenv()", "process.env.NAME", "env.get(NAME)"), 1, "`process.env` is Node's version of System.getenv."),
        ),
    ),
)

val module8 = Module(
    id = "m8", emoji = "🚀", title = "Power-ups", subtitle = "API, mocking, login reuse, CI", accentIndex = 7,
    lessons = listOf(
        Lesson(
            id = "api", emoji = "📡", title = "API testing built in",
            story = "Sometimes it's faster to skip the browser and talk to the server directly, like calling a shop on the phone instead of walking there. Playwright has a `request` helper so you don't need RestAssured.",
            javaCode = """
                given().body(json).post("/users")
                    .then().statusCode(201);
            """.trimIndent(),
            code = """
                test('create user via API', async ({ request }) => {
                  const res = await request.post('https://reqres.in/api/users', {
                    data: { name: 'Asha', job: 'QA' },
                  });
                  expect(res.status()).toBe(201);

                  const body = await res.json();
                  expect(body.name).toBe('Asha');
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`request` is another free fixture, like `page`.",
            points = listOf(
                "`request.get/post/put/delete` are all available.",
                "Great for setting up data fast, then testing only the UI part.",
                "API and UI tests can live in the same project and share the same config.",
            ),
            quiz = Quiz("Where does `request` come from?", listOf("A fixture", "A Maven plugin", "A separate install"), 0, "Playwright Test gives it to you for free like `page`."),
        ),
        Lesson(
            id = "mock", emoji = "🎭", title = "Network mocking",
            story = "Imagine you secretly swap the postman's letter with your own. The website thinks the server replied, but YOU wrote the reply. That lets you test weird cases (errors, empty lists) without touching the real server.",
            javaCode = """
                // Selenium: needs proxies like BrowserMob
                // or DevTools protocol code
            """.trimIndent(),
            code = """
                test('shows empty state', async ({ page }) => {
                  await page.route('**/api/products', (route) =>
                    route.fulfill({ status: 200, json: [] })
                  );

                  await page.goto('/products');
                  await expect(page.getByText('No products found')).toBeVisible();
                });

                // Make a call fail
                // await page.route('**/api/save', (route) => route.abort());
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Set the route BEFORE `goto`, so the page's first request is caught.",
            points = listOf(
                "`route.fulfill()` replies with your fake data.",
                "`route.abort()` simulates a network failure.",
                "`route.continue()` lets a request go through, optionally changed.",
            ),
            quiz = Quiz("When should you call `page.route()`?", listOf("After the page loads", "Before the request happens", "Only in config"), 1, "The route must be in place before the page makes the request."),
        ),
        Lesson(
            id = "auth", emoji = "🔑", title = "Log in once, reuse everywhere",
            story = "Why show your ticket at the gate before every single ride? Show it once, get a wristband, and walk straight to rides. Playwright can save your logged-in state (cookies and storage) to a file and give it to every test.",
            javaCode = """
                // Selenium: log in inside @BeforeMethod for every test,
                // or juggle cookies by hand
            """.trimIndent(),
            code = """
                // tests/auth.setup.ts
                import { test as setup } from '@playwright/test';

                setup('login once', async ({ page }) => {
                  await page.goto('/login');
                  await page.getByLabel('Email').fill('asha@x.com');
                  await page.getByLabel('Password').fill('pw');
                  await page.getByRole('button', { name: 'Sign in' }).click();
                  await page.waitForURL('**/dashboard');
                  await page.context().storageState({ path: '.auth/user.json' });
                });

                // playwright.config.ts
                // projects: [
                //   { name: 'setup', testMatch: /auth\.setup\.ts/ },
                //   { name: 'app', dependencies: ['setup'],
                //     use: { storageState: '.auth/user.json' } },
                // ]
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Your test suite now starts already logged in. Huge time saver.",
            points = listOf(
                "`storageState` saves cookies and local storage to a JSON file.",
                "A \"setup\" project runs first, using `dependencies`.",
                "Add the `.auth` folder to `.gitignore`.",
            ),
            quiz = Quiz("What does `storageState` save?", listOf("Test results", "Cookies and local storage", "Screenshots"), 1, "That's what keeps you logged in."),
        ),
        Lesson(
            id = "codegen-shots", emoji = "📸", title = "Codegen, screenshots and visuals",
            story = "Don't want to write by hand? Turn on `codegen` and click around a website. The robot watches you and writes the test for you! You can also take pictures and compare them to catch accidental design changes.",
            javaCode = """
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(src, new File("shot.png"));
            """.trimIndent(),
            code = """
                // Terminal: record a test by clicking!
                // npx playwright codegen https://example.com

                await page.screenshot({ path: 'home.png', fullPage: true });
                await page.getByRole('banner').screenshot({ path: 'header.png' });

                // visual comparison against a saved baseline
                await expect(page).toHaveScreenshot('home.png');
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "First run of `toHaveScreenshot` creates the baseline. Later runs compare.",
            points = listOf(
                "Codegen is a perfect way to discover good locators. Then tidy up the generated code.",
                "Update baselines with `npx playwright test --update-snapshots`.",
                "Screenshots may differ across operating systems, so generate baselines in the same place you run CI.",
            ),
            quiz = Quiz("What does `npx playwright codegen` do?", listOf("Writes test code as you click", "Generates a report", "Updates Playwright"), 0, "It records your actions and turns them into code."),
        ),
        Lesson(
            id = "parallel-ci", emoji = "🏁", title = "Parallel, retries, tags and CI",
            story = "Many hands make light work: Playwright runs test files in parallel automatically. If a test fails by bad luck, it can try again. You can stick name tags like `@smoke` to run just some tests. And finally, a robot can run the whole thing on every code push in CI.",
            javaCode = """
                mvn test -Dgroups=smoke
                // parallel via testng.xml thread-count
            """.trimIndent(),
            code = """
                // tag a test
                test('checkout works @smoke', async ({ page }) => { /* ... */ });

                // run only tags
                // npx playwright test --grep @smoke

                // run tests inside one file in order
                test.describe.configure({ mode: 'serial' });

                // .github/workflows/pw.yml (key steps)
                // - run: npm ci
                // - run: npx playwright install --with-deps
                // - run: npx playwright test
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`retries: 2` in config lets flaky tests try again before failing.",
            points = listOf(
                "Files run in parallel by default; tests inside one file run in order unless `fullyParallel: true`.",
                "Keep tests independent so they can run in any order.",
                "Retried tests are marked \"flaky\" in the report. Fix them, don't hide them.",
                "Congratulations! You now know enough to build a real framework. 🎉",
            ),
            quiz = Quiz("How do you run only tests tagged @smoke?", listOf("--grep @smoke", "--only smoke", "-Dgroups=smoke"), 0, "Use --grep to filter by text in the test title."),
        ),
    ),
)
