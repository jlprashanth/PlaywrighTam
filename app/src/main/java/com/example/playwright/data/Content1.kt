package com.example.playwright.data

val module1 = Module(
    id = "m1", emoji = "🎭", title = "Say hello to Playwright", subtitle = "What it is, why it's nice, first test", accentIndex = 0,
    lessons = listOf(
        Lesson(
            id = "what-is", emoji = "🤖", title = "What is Playwright?",
            story = "Imagine a tiny robot friend who can open a web browser and use it exactly like you do: type, click, scroll. You write a to-do list, and the robot follows it and tells you if something looks wrong. That robot is `Playwright`. It was made by Microsoft and works with Chrome, Firefox and Safari (WebKit).",
            javaCode = """
                WebDriver driver = new ChromeDriver();
                driver.get("https://playwright.dev");
                driver.quit();
            """.trimIndent(),
            code = """
                import { chromium } from '@playwright/test';

                const browser = await chromium.launch();
                const page = await browser.newPage();
                await page.goto('https://playwright.dev');
                await browser.close();
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Same story, new words: launch a browser, open a tab (`page`), visit a site, close.",
            points = listOf(
                "No `chromedriver.exe` to download or match with your browser version. Playwright brings its own browsers.",
                "It runs on Node.js. You write in TypeScript, which is JavaScript with types (like Java!).",
                "One API for Chromium, Firefox and WebKit.",
            ),
            quiz = Quiz("Which one do you NOT need to manage yourself in Playwright?", listOf("Node.js", "A chromedriver.exe file", "Your test code"), 1, "Playwright downloads and manages its own browsers, so no driver juggling."),
        ),
        Lesson(
            id = "why-better", emoji = "⚡", title = "Why people love it over Selenium",
            story = "In Selenium, you stand at a door and keep asking, \"Is it open yet? Is it open yet?\" (that's `WebDriverWait`). In Playwright, the robot is patient. It waits by itself until the door opens, then walks in. Less asking, fewer tests that fail for silly reasons.",
            javaCode = """
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
            """.trimIndent(),
            code = """
                // Playwright waits for the button automatically
                await page.locator('#login').click();
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Two lines of waiting code in Selenium became zero.",
            points = listOf(
                "Auto-waiting: it checks the element is visible, enabled and still before clicking.",
                "Tests run in parallel by default and are fast.",
                "Built in: reports, screenshots, videos, trace viewer, API testing, network mocking.",
                "Each test gets a fresh, isolated browser context (like a brand-new incognito window).",
            ),
            quiz = Quiz("The button appears 2 seconds late. What does `click()` do?", listOf("Fails right away", "Waits for it, then clicks", "Clicks nothing and moves on"), 1, "Auto-wait is the superpower: click() waits until the element is ready (up to the timeout)."),
        ),
        Lesson(
            id = "setup", emoji = "🧰", title = "Setting up your toy box",
            story = "To play, you need a toy box. Install `Node.js` (the engine), then ask Playwright to build a ready-made project for you. One command, a few questions, done. Think of it as a `Maven archetype`, but friendlier.",
            javaCode = """
                <dependency>
                  <groupId>org.seleniumhq.selenium</groupId>
                  <artifactId>selenium-java</artifactId>
                </dependency>
            """.trimIndent(),
            code = """
                node -v        # check Node is installed
                mkdir my-pw && cd my-pw
                npm init playwright@latest
                # choose: TypeScript, tests folder, install browsers = yes
            """.trimIndent(),
            codeLang = "Bash",
            codeCaption = "Run these in a terminal (VS Code's terminal works great).",
            points = listOf(
                "`package.json` is your `pom.xml`. It lists dependencies and scripts.",
                "`node_modules/` is your `.m2` folder. Never edit it.",
                "`playwright.config.ts` is your `testng.xml` plus settings.",
                "`tests/` is where your spec files live. Files end with `.spec.ts`.",
            ),
            quiz = Quiz("Which Selenium/Maven file is like `package.json`?", listOf("pom.xml", "testng.xml", "chromedriver.exe"), 0, "Both list dependencies and project info."),
        ),
        Lesson(
            id = "first-test", emoji = "🚀", title = "Your very first test",
            story = "Let's tell the robot: \"Go to the Playwright website and check that the page title says Playwright.\" That's a test! You give the test a name, you get a free `page` (a fresh browser tab), and then you write steps.",
            javaCode = """
                @Test
                public void titleTest() {
                    driver.get("https://playwright.dev");
                    Assert.assertTrue(driver.getTitle().contains("Playwright"));
                }
            """.trimIndent(),
            code = """
                import { test, expect } from '@playwright/test';

                test('has Playwright in title', async ({ page }) => {
                  await page.goto('https://playwright.dev');
                  await expect(page).toHaveTitle(/Playwright/);
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Run it with: npx playwright test",
            points = listOf(
                "`test('name', ...)` is your `@Test`.",
                "`{ page }` means: \"Playwright, please hand me a fresh tab.\" No driver setup code.",
                "`/Playwright/` is a regular expression meaning \"contains Playwright\".",
                "`await` appears everywhere. We'll learn why in the TypeScript module.",
            ),
            quiz = Quiz("What is `page` in the test?", listOf("A fresh browser tab given to you", "The name of the test", "A file on disk"), 0, "Playwright creates a clean tab for every test and passes it in."),
        ),
    ),
)

val module2 = Module(
    id = "m2", emoji = "📝", title = "Just enough TypeScript", subtitle = "Only what you need, nothing scary", accentIndex = 1,
    lessons = listOf(
        Lesson(
            id = "ts-vars", emoji = "📦", title = "Variables and types",
            story = "Variables are labelled boxes. A `const` box is glued shut: whatever you put in stays. A `let` box can be opened and changed later. You write the type after a colon, like a name tag: `age: number`.",
            javaCode = """
                String name = "Raj";
                int age = 5;
                boolean loggedIn = false;
                List<String> fruits = List.of("apple", "mango");
            """.trimIndent(),
            code = """
                const name: string = 'Raj';
                let age: number = 5;
                let loggedIn: boolean = false;
                const fruits: string[] = ['apple', 'mango'];

                age = 6;                         // fine, it's let
                const greeting = `Hi ${'$'}{name}!`;  // backticks embed values
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "TypeScript can often guess the type, so `const x = 5` is fine too.",
            points = listOf(
                "Use `const` by default, `let` only when the value changes. Avoid `var`.",
                "There is just one number type: `number` (no int, double, long).",
                "Use `===` to compare (strict equals), not `==`.",
                "Semicolons are optional but most people add them.",
            ),
            quiz = Quiz("Which keyword lets you change the value later?", listOf("const", "let", "string"), 1, "`let` boxes can be reassigned; `const` ones can't."),
        ),
        Lesson(
            id = "ts-functions", emoji = "🍳", title = "Functions and arrow functions",
            story = "A function is a recipe card: give it ingredients, it gives you a dish. An arrow function is the same recipe written in a short, quick way using `=>`. You'll see arrows all over Playwright because tests themselves are written as arrow functions.",
            javaCode = """
                int add(int a, int b) {
                    return a + b;
                }
                names.forEach(n -> System.out.println(n));
            """.trimIndent(),
            code = """
                function add(a: number, b: number): number {
                  return a + b;
                }

                const addArrow = (a: number, b: number): number => a + b;

                const names = ['Asha', 'Ravi'];
                names.forEach((n) => console.log(n));
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`(x) => ...` is exactly like Java's lambda `x -> ...`.",
            points = listOf(
                "Java lambda `x -> x + 1` is TypeScript `(x) => x + 1`.",
                "The return type goes after the parentheses: `): number`.",
                "`console.log()` is your `System.out.println()`.",
            ),
            quiz = Quiz("In Java you'd call `() => {}` a...", listOf("Lambda", "Interface", "Package"), 0, "Arrow functions are TypeScript's lambdas."),
        ),
        Lesson(
            id = "ts-objects", emoji = "🎒", title = "Arrays and objects",
            story = "An array is a toy shelf where toys sit in a row. An object is a backpack with labelled pockets: `name`, `age`, `role`. Playwright hands you a backpack and says, \"just take the pocket you need.\" That's called destructuring.",
            javaCode = """
                Map<String, String> user = new HashMap<>();
                user.put("name", "Asha");
                for (User u : users) { System.out.println(u.getName()); }
            """.trimIndent(),
            code = """
                const user = { name: 'Asha', password: 'secret123' };
                console.log(user.name);

                const users = [
                  { name: 'Asha', role: 'admin' },
                  { name: 'Ravi', role: 'guest' },
                ];
                for (const u of users) {
                  console.log(u.name);
                }

                // destructuring: take only the pocket you need
                const { role } = users[0];
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "No need to create a class just to hold data. A plain object does it.",
            points = listOf(
                "Objects are `{ key: value }`. Access with a dot: `user.name`.",
                "Arrays are `[a, b, c]`. Loop with `for (const x of list)`.",
                "`async ({ page }) => ...` is destructuring: pick `page` from the bigger object Playwright passes in.",
            ),
            quiz = Quiz("In `async ({ page }) => {}`, the curly braces mean...", listOf("An empty block of code", "Pick `page` out of a bigger object", "Create a new class"), 1, "That's destructuring: Playwright passes an object full of helpers and you pick the ones you want."),
        ),
        Lesson(
            id = "ts-async", emoji = "🍕", title = "async and await (the big one)",
            story = "You order a pizza. The shop says \"it's coming\" (that's a `Promise`). If you start eating before it arrives, you eat air. `await` means: wait here until the pizza arrives, then continue. Every Playwright action is a pizza order, so every action needs `await`.",
            javaCode = """
                // Selenium blocks by default, so no keyword needed
                driver.get(url);
                driver.findElement(By.id("go")).click();
            """.trimIndent(),
            code = """
                test('order pizza', async ({ page }) => {
                  await page.goto('https://example.com');
                  await page.getByText('More').click();
                  // forgetting await = the test races ahead and gets flaky
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`await` only works inside a function marked `async`.",
            points = listOf(
                "Browser actions take time, so they return a `Promise` (a \"will finish later\" ticket).",
                "`await` pauses your test until the ticket is done.",
                "Missing `await` is the number one beginner bug. Turn on the ESLint rule `no-floating-promises` to catch it.",
                "`async` goes before the function; `await` goes before the action.",
            ),
            quiz = Quiz("You forgot `await` before `click()`. What can happen?", listOf("The test runs the next step before the click finishes", "Java throws an error", "Nothing, it's optional"), 0, "Without await the code doesn't wait, so steps overlap and the test becomes flaky."),
        ),
        Lesson(
            id = "ts-classes", emoji = "🧱", title = "Classes, import and export",
            story = "Imagine borrowing a toy from a friend's room: you `import` it. And if you want others to borrow yours, you `export` it. Classes work like in Java, just with less ceremony. You'll use them for Page Objects later.",
            javaCode = """
                public class LoginPage {
                    private WebDriver driver;
                    public LoginPage(WebDriver driver) { this.driver = driver; }
                    public void open() { driver.get("/login"); }
                }
            """.trimIndent(),
            code = """
                // pages/LoginPage.ts
                import { Page } from '@playwright/test';

                export class LoginPage {
                  constructor(private page: Page) {}

                  async open() {
                    await this.page.goto('/login');
                  }
                }

                // in a test file
                import { LoginPage } from '../pages/LoginPage';
                const login = new LoginPage(page);
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`constructor(private page: Page) {}` declares and assigns the field in one go.",
            points = listOf(
                "`export` is like `public` for a whole file's thing.",
                "`import { X } from './file'` replaces Java's `import package.X;`. Use relative paths.",
                "Inside a class, use `this.` to reach fields.",
                "Methods that do browser work are `async`.",
            ),
            quiz = Quiz("What lets another file use your class?", listOf("export", "static", "final"), 0, "Mark it `export`, then `import` it elsewhere."),
        ),
    ),
)

val module3 = Module(
    id = "m3", emoji = "🧪", title = "Test basics", subtitle = "Structure, running and reports", accentIndex = 2,
    lessons = listOf(
        Lesson(
            id = "test-structure", emoji = "🏠", title = "Tests, groups and hooks",
            story = "A test is one small game. A `describe` is a box that holds games of the same family, like a class in Java. Hooks are the \"before we play, set up the board\" and \"after we play, clean up\" steps.",
            javaCode = """
                @BeforeMethod
                public void setUp() { driver.get(url); }

                @Test
                public void formShows() { ... }
            """.trimIndent(),
            code = """
                import { test, expect } from '@playwright/test';

                test.describe('Login feature', () => {
                  test.beforeEach(async ({ page }) => {
                    await page.goto('https://example.com/login');
                  });

                  test('shows the form', async ({ page }) => {
                    await expect(page.getByLabel('Email')).toBeVisible();
                  });

                  test('has a sign in button', async ({ page }) => {
                    await expect(page.getByRole('button', { name: 'Sign in' })).toBeVisible();
                  });
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Each test still gets its own fresh page, even with a shared beforeEach.",
            points = listOf(
                "`test.describe` is a class/group. `@BeforeMethod` is `test.beforeEach`.",
                "`@BeforeClass` is `test.beforeAll`; `@AfterMethod` is `test.afterEach`.",
                "`test.skip(...)` and `test.only(...)` skip or focus tests.",
            ),
            quiz = Quiz("TestNG's `@BeforeMethod` is Playwright's...", listOf("test.beforeAll", "test.beforeEach", "test.describe"), 1, "beforeEach runs before every single test."),
        ),
        Lesson(
            id = "run-tests", emoji = "▶️", title = "Running your tests",
            story = "The robot can work in secret (headless) or let you watch (headed). You can also tell it to run only certain games. All via the terminal, like `mvn test` but with options.",
            javaCode = """
                mvn test -Dtest=LoginTest
                mvn test -Dgroups=smoke
            """.trimIndent(),
            code = """
                npx playwright test                       # run everything
                npx playwright test login.spec.ts         # one file
                npx playwright test -g "login"            # by test name
                npx playwright test --headed              # watch the browser
                npx playwright test --project=chromium    # one browser
                npx playwright test --ui                  # friendly UI mode
                npx playwright test --debug               # step by step
            """.trimIndent(),
            codeLang = "Bash",
            codeCaption = "Tests are headless by default.",
            points = listOf(
                "`--ui` mode is a superb way to see, rerun and time-travel through tests.",
                "`--debug` opens the Playwright Inspector so you can step through.",
                "`-g` filters by title, a great way to run tags like `@smoke`.",
            ),
            quiz = Quiz("How do you watch the browser while tests run?", listOf("--headed", "--silent", "--fast"), 0, "`--headed` shows the real browser window."),
        ),
        Lesson(
            id = "reports-trace", emoji = "🎥", title = "Reports and the Trace Viewer",
            story = "When a test fails, you want to know why. Playwright can record a magic flight recorder called a trace: screenshots of every step, network calls, the page's HTML, all of it. Open it and you can scrub back in time like a video.",
            javaCode = """
                // Selenium: add ExtentReports / Allure,
                // write screenshot-on-failure listeners...
            """.trimIndent(),
            code = """
                // playwright.config.ts
                use: {
                  trace: 'on-first-retry',
                  screenshot: 'only-on-failure',
                  video: 'retain-on-failure',
                },

                // terminal
                // npx playwright show-report
                // npx playwright show-trace trace.zip
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "A pretty HTML report is generated for free after each run.",
            points = listOf(
                "`npx playwright show-report` opens the HTML report.",
                "Trace Viewer shows a timeline, before/after snapshots and console logs.",
                "No extra libraries needed, it's built in.",
            ),
            quiz = Quiz("What does the Trace Viewer let you do?", listOf("Rewrite your tests", "Scrub through a failed test step by step", "Install browsers"), 1, "It's a time machine for failed tests."),
        ),
    ),
)
