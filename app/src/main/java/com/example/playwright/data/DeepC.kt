package com.example.playwright.data

val deepC: Map<String, Extra> = mapOf(
    "config" to Extra(
        deep = listOf(
            H("Settings you'll touch most"),
            Table(
                listOf("Setting", "Purpose"),
                listOf(
                    listOf("`testDir`", "Where specs live"),
                    listOf("`fullyParallel`", "Run tests in one file in parallel too"),
                    listOf("`forbidOnly`", "Fail if test.only is left in"),
                    listOf("`retries`", "Retry failures (e.g. 2 on CI, 0 locally)"),
                    listOf("`workers`", "Parallel processes"),
                    listOf("`use.viewport`", "Window size"),
                    listOf("`use.locale / timezoneId`", "Test other regions"),
                    listOf("`use.ignoreHTTPSErrors`", "Accept bad certificates"),
                    listOf("`webServer`", "Start your app before tests"),
                ),
            ),
            H("Different values for CI vs local"),
            Code("process.env.CI", "TypeScript", """
                export default defineConfig({
                  retries: process.env.CI ? 2 : 0,
                  workers: process.env.CI ? 2 : undefined,
                  forbidOnly: !!process.env.CI,
                  use: { baseURL: process.env.BASE_URL ?? 'http://localhost:3000' },
                });
            """.trimIndent()),
            H("Start your app automatically"),
            Code("webServer", "TypeScript", """
                webServer: {
                  command: 'npm run start',
                  url: 'http://localhost:3000',
                  reuseExistingServer: !process.env.CI,
                },
            """.trimIndent()),
            H("Per-file and per-describe overrides"),
            Code("test.use", "TypeScript", """
                test.use({ viewport: { width: 390, height: 844 }, locale: 'fr-FR' });
                test.describe('mobile layout', () => { /* ... */ });
            """.trimIndent()),
            Tip("Run the same suite against staging and prod by changing only `BASE_URL`: `BASE_URL=https://staging.x.com npx playwright test`."),
        ),
        challenge = Challenge(
            "Write a config that: finds tests in './e2e', retries twice only on CI, uses BASE_URL from the environment (default http://localhost:4200), and runs just Chromium.",
            """
                import { defineConfig, devices } from '@playwright/test';

                export default defineConfig({
                  testDir: './e2e',
                  retries: process.env.CI ? 2 : 0,
                  use: { baseURL: process.env.BASE_URL ?? 'http://localhost:4200' },
                  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `forbidOnly` protect against?", listOf("A forgotten test.only skipping everything", "Slow tests", "Missing awaits"), 0, "The run fails instead of silently running one test."),
            Quiz("What does `reuseExistingServer` do?", listOf("Uses a server that's already running", "Restarts the server", "Mocks the server"), 0, "Saves startup time locally."),
        ),
    ),
    "pom" to Extra(
        deep = listOf(
            H("A fuller, realistic Page Object"),
            Code("pages/CartPage.ts", "TypeScript", """
                import { Page, Locator, expect } from '@playwright/test';

                export class CartPage {
                  readonly rows: Locator;
                  readonly total: Locator;
                  readonly checkout: Locator;

                  constructor(private readonly page: Page) {
                    this.rows = page.getByTestId('cart-row');
                    this.total = page.getByTestId('cart-total');
                    this.checkout = page.getByRole('button', { name: 'Checkout' });
                  }

                  async open() { await this.page.goto('/cart'); }

                  itemRow(name: string): Locator {
                    return this.rows.filter({ hasText: name });
                  }

                  async remove(name: string) {
                    await this.itemRow(name).getByRole('button', { name: 'Remove' }).click();
                  }

                  async proceed() { await this.checkout.click(); }
                }
            """.trimIndent()),
            H("Rules of thumb"),
            Table(
                listOf("Do", "Don't"),
                listOf(
                    listOf("Expose actions (`login`, `remove`)", "Expose raw clicks everywhere"),
                    listOf("Return locators for tests to assert on", "Hide all asserts in page classes"),
                    listOf("One class per page or component", "One giant Utils class"),
                    listOf("Return the next page object after navigation", "Use hard sleeps"),
                ),
            ),
            H("Components"),
            P("Headers, menus and modals appear on many pages. Make them their own small classes and compose them: `this.header = new HeaderComponent(page)`."),
            Code("Compose", "TypeScript", """
                class HeaderComponent {
                  constructor(private page: Page) {}
                  async search(text: string) { await this.page.getByLabel('Search').fill(text); }
                }
                class HomePage {
                  readonly header = new HeaderComponent(this.page);
                  constructor(private page: Page) {}
                }
            """.trimIndent()),
            Tip("Return another page object from a method when navigation happens: `async login(): Promise<DashboardPage>`. Tests then read like a story."),
            Warn("A Page Object is not required. For a tiny suite, a few helper functions are enough. Introduce POM when you see duplicated locators."),
        ),
        challenge = Challenge(
            "Write a `SearchPage` with a `search(term)` method and a `results` locator (list items inside a region named 'Results'). In a test, search 'laptop' and assert there are at least 1 results.",
            """
                import { Page, Locator } from '@playwright/test';

                export class SearchPage {
                  readonly results: Locator;
                  constructor(private page: Page) {
                    this.results = page.getByRole('region', { name: 'Results' }).getByRole('listitem');
                  }
                  async search(term: string) {
                    await this.page.getByLabel('Search').fill(term);
                    await this.page.keyboard.press('Enter');
                  }
                }

                test('search works', async ({ page }) => {
                  const sp = new SearchPage(page);
                  await page.goto('/');
                  await sp.search('laptop');
                  await expect(sp.results.first()).toBeVisible();
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Where should assertions mostly live?", listOf("In the tests", "In page classes", "In config"), 0, "Page objects describe the page; tests decide what's correct."),
            Quiz("What is a component object?", listOf("A reusable part like a header or modal", "A React class", "A fixture"), 0, "Reusable parts shared by many pages."),
        ),
    ),
    "fixtures" to Extra(
        deep = listOf(
            H("Fixtures with data and setup"),
            Code("Several fixtures together", "TypeScript", """
                type Fixtures = {
                  loginPage: LoginPage;
                  todoPage: TodoPage;
                  testUser: { name: string; pass: string };
                };

                export const test = base.extend<Fixtures>({
                  testUser: async ({}, use) => {
                    await use({ name: 'asha', pass: process.env.PW ?? 'pw' });
                  },
                  loginPage: async ({ page }, use) => { await use(new LoginPage(page)); },
                  todoPage: async ({ page, testUser }, use) => {
                    // fixtures can depend on other fixtures
                    const login = new LoginPage(page);
                    await login.open();
                    await login.login(testUser.name, testUser.pass);
                    await use(new TodoPage(page));
                  },
                });
            """.trimIndent()),
            H("Auto fixtures and teardown"),
            Code("auto: true", "TypeScript", """
                export const test = base.extend<{ cleanDb: void }>({
                  cleanDb: [async ({ request }, use) => {
                    await request.post('/api/reset');     // before every test
                    await use();
                    await request.delete('/api/temp');    // after every test
                  }, { auto: true }],
                });
            """.trimIndent()),
            H("Worker-scoped fixtures"),
            P("By default a fixture is created for every test. Mark it `{ scope: 'worker' }` to create it once per worker process, e.g. for an expensive API token."),
            Code("scope", "TypeScript", """
                token: [async ({}, use) => {
                  const t = await fetchToken();
                  await use(t);
                }, { scope: 'worker' }],
            """.trimIndent()),
            Tip("Fixtures replace `BaseTest` inheritance, `@BeforeMethod`, and Spring-style dependency injection, all in one concept."),
            Warn("Note the odd `async ({}, use)` when you need no other fixtures. The empty braces are required."),
        ),
        challenge = Challenge(
            "Create a fixture `apiUser` that creates a user via POST /api/users before the test and deletes it after the test, handing the user's id to the test.",
            """
                import { test as base } from '@playwright/test';

                export const test = base.extend<{ apiUser: { id: number } }>({
                  apiUser: async ({ request }, use) => {
                    const res = await request.post('/api/users', { data: { name: 'temp' } });
                    const user = await res.json();
                    await use({ id: user.id });
                    await request.delete('/api/users/' + user.id);
                  },
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `{ auto: true }` do?", listOf("Runs the fixture for every test automatically", "Auto-fixes bugs", "Makes it faster"), 0, "You don't need to request it in the test."),
            Quiz("Worker-scoped fixtures are created...", listOf("Once per worker", "Per test", "Never"), 0, "Great for expensive setup."),
        ),
    ),
    "data" to Extra(
        deep = listOf(
            H("JSON test data"),
            Code("data/users.json + loader", "TypeScript", """
                // data/users.json: [{ "name": "asha", "role": "admin" }, ...]
                import users from '../data/users.json';

                for (const u of users) {
                  test(`${'$'}{u.role} can open dashboard`, async ({ page }) => { /* ... */ });
                }
            """.trimIndent()),
            P("Enable `resolveJsonModule` in `tsconfig.json` for JSON imports."),
            H("CSV and Excel"),
            Code("Read a CSV", "TypeScript", """
                import fs from 'fs';
                import { parse } from 'csv-parse/sync';

                const rows = parse(fs.readFileSync('data/users.csv'), { columns: true });
                for (const r of rows) {
                  test(`login ${'$'}{r.name}`, async ({ page }) => { /* ... */ });
                }
                // npm i csv-parse
            """.trimIndent()),
            H(".env files with dotenv"),
            Code("Secrets out of code", "TypeScript", """
                // npm i -D dotenv
                // playwright.config.ts (top)
                import dotenv from 'dotenv';
                dotenv.config({ path: '.env' });

                // .env  (never commit!)
                // APP_USER=asha
                // APP_PASS=secret

                const pass = process.env.APP_PASS!;   // the ! says "trust me, it's set"
            """.trimIndent()),
            H("Random data"),
            Code("Unique emails", "TypeScript", """
                const email = `user${'$'}{Date.now()}@example.com`;
                // or: npm i -D @faker-js/faker
                import { faker } from '@faker-js/faker';
                const name = faker.person.fullName();
            """.trimIndent()),
            Tip("Make test data unique per run (timestamp or faker) so tests don't collide when running in parallel."),
            Warn("Never hard-code real passwords or tokens in specs. Use env vars locally and secrets in CI."),
        ),
        challenge = Challenge(
            "Generate one test per browser-language pair from the array ['en','fr','de']: open `/?lang=<code>` and assert the `html` element's `lang` attribute equals the code.",
            """
                const langs = ['en', 'fr', 'de'];
                for (const code of langs) {
                  test(`page language ${'$'}{code}`, async ({ page }) => {
                    await page.goto('/?lang=' + code);
                    await expect(page.locator('html')).toHaveAttribute('lang', code);
                  });
                }
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `process.env.X!` the exclamation mean?", listOf("Trust me, it's defined", "Throw if missing", "Delete it"), 0, "It silences TypeScript's 'might be undefined' warning."),
            Quiz("Why make test data unique?", listOf("Parallel tests may collide otherwise", "It looks nicer", "Playwright demands it"), 0, "Shared data causes flaky parallel runs."),
        ),
    ),
    "api" to Extra(
        deep = listOf(
            H("Full CRUD example"),
            Code("GET, POST, PUT, DELETE", "TypeScript", """
                const base = 'https://api.example.com';
                const created = await request.post(base + '/users', { data: { name: 'Asha' } });
                expect(created.ok()).toBeTruthy();
                const { id } = await created.json();

                const got = await request.get(base + '/users/' + id);
                await expect(got).toBeOK();                       // status 200..299
                expect(await got.json()).toMatchObject({ name: 'Asha' });

                await request.put(base + '/users/' + id, { data: { name: 'Asha R' } });
                const del = await request.delete(base + '/users/' + id);
                expect(del.status()).toBe(204);
            """.trimIndent()),
            H("Headers, auth and query"),
            Code("Options", "TypeScript", """
                await request.get('/api/orders', {
                  headers: { Authorization: 'Bearer ' + token },
                  params: { status: 'open', page: 2 },     // ?status=open&page=2
                });
                await request.post('/login', { form: { user: 'a', pass: 'b' } });   // form data
            """.trimIndent()),
            H("Share the API with config"),
            Code("baseURL + headers for all requests", "TypeScript", """
                use: {
                  baseURL: 'https://api.example.com',
                  extraHTTPHeaders: { Accept: 'application/json' },
                },
            """.trimIndent()),
            H("Hybrid tests: API setup, UI check"),
            Code("Create data fast, verify in the UI", "TypeScript", """
                test('new todo shows in UI', async ({ page, request }) => {
                  await request.post('/api/todos', { data: { title: 'Buy milk' } });
                  await page.goto('/todos');
                  await expect(page.getByText('Buy milk')).toBeVisible();
                });
            """.trimIndent()),
            Tip("Use `await expect(response).toBeOK()` for a readable status check with the response body in the error."),
            Warn("Selenium + RestAssured had two toolchains. Here, API calls and the browser can share cookies if you use `page.request` instead of the `request` fixture."),
        ),
        challenge = Challenge(
            "Write an API test: POST a todo {title:'Learn PW'} to /api/todos, check status 201 and the returned title, then GET the todo by its id and verify `done` is false.",
            """
                test('todo api', async ({ request }) => {
                  const created = await request.post('/api/todos', { data: { title: 'Learn PW' } });
                  expect(created.status()).toBe(201);
                  const todo = await created.json();
                  expect(todo.title).toBe('Learn PW');

                  const got = await request.get('/api/todos/' + todo.id);
                  expect((await got.json()).done).toBe(false);
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Which option sends a query string?", listOf("params", "query", "search"), 0, "`params: { a: 1 }` becomes ?a=1."),
            Quiz("`page.request` vs `request` fixture?", listOf("page.request shares the page's cookies", "No difference", "request is slower"), 0, "Good for logging in via API and continuing in the UI."),
        ),
    ),
    "mock" to Extra(
        deep = listOf(
            H("Modify a real response"),
            Code("Fetch then change", "TypeScript", """
                await page.route('**/api/user', async (route) => {
                  const response = await route.fetch();          // real call
                  const json = await response.json();
                  json.plan = 'premium';                         // tweak
                  await route.fulfill({ response, json });
                });
            """.trimIndent()),
            H("Simulate slow and failing servers"),
            Code("Errors and delays", "TypeScript", """
                await page.route('**/api/save', (r) => r.fulfill({ status: 500, body: 'Server exploded' }));
                await page.route('**/api/list', async (r) => {
                  await new Promise((res) => setTimeout(res, 3000));   // slow
                  await r.continue();
                });
                await page.route('**/*.{png,jpg}', (r) => r.abort());   // block images = faster tests
            """.trimIndent()),
            H("Spy on requests without changing them"),
            Code("Assert what the page sent", "TypeScript", """
                const reqPromise = page.waitForRequest('**/api/order');
                await page.getByText('Buy').click();
                const req = await reqPromise;
                expect(req.method()).toBe('POST');
                expect(req.postDataJSON()).toMatchObject({ qty: 1 });
            """.trimIndent()),
            H("Mock from a HAR file"),
            Code("Record & replay", "Bash", """
                npx playwright codegen --save-har=mock.har https://example.com
                # then in tests: await page.routeFromHAR('mock.har');
            """.trimIndent()),
            Tip("Mocking lets you test 'what if the server returns 500?' states that are painful to trigger for real."),
            Warn("Mocks can drift from the real API. Keep a few real end-to-end tests too."),
        ),
        challenge = Challenge(
            "Make `/api/orders` return a 500 error, open `/orders`, and assert the page shows 'Something went wrong'.",
            """
                test('shows error on 500', async ({ page }) => {
                  await page.route('**/api/orders', (route) =>
                    route.fulfill({ status: 500, json: { error: 'boom' } })
                  );
                  await page.goto('/orders');
                  await expect(page.getByText('Something went wrong')).toBeVisible();
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `route.continue()` do?", listOf("Lets the real request through", "Fails it", "Fakes the reply"), 0, "Optionally with changed headers or body."),
            Quiz("Why block images in some tests?", listOf("Faster and more stable", "Required", "Images can't load"), 0, "Less noise and bandwidth."),
        ),
    ),
    "auth" to Extra(
        deep = listOf(
            H("The whole setup, step by step"),
            P("1) A setup file logs in and saves state. 2) The config makes a 'setup' project and makes other projects depend on it. 3) Tests start already logged in."),
            Code("playwright.config.ts", "TypeScript", """
                projects: [
                  { name: 'setup', testMatch: /.*\.setup\.ts/ },
                  {
                    name: 'chromium',
                    use: { ...devices['Desktop Chrome'], storageState: '.auth/user.json' },
                    dependencies: ['setup'],
                  },
                ],
            """.trimIndent()),
            H("Different roles"),
            Code("admin and user", "TypeScript", """
                // save two files: .auth/admin.json and .auth/user.json
                test.describe('admin area', () => {
                  test.use({ storageState: '.auth/admin.json' });
                  test('sees users table', async ({ page }) => { /* ... */ });
                });
                test.describe('guest', () => {
                  test.use({ storageState: { cookies: [], origins: [] } });   // logged out
                });
            """.trimIndent()),
            H("Faster: log in via API"),
            Code("No UI at all", "TypeScript", """
                setup('api login', async ({ request }) => {
                  await request.post('/api/login', { data: { user: 'asha', pass: 'pw' } });
                  await request.storageState({ path: '.auth/user.json' });
                });
            """.trimIndent()),
            H("What storageState contains"),
            P("Cookies and `localStorage` for each origin. Session storage isn't included. If your app keeps tokens in sessionStorage, restore them with `page.addInitScript`."),
            Tip("Add `.auth/` to `.gitignore`. That file is basically a password."),
            Warn("If the session expires during a long run, tests start failing with 401. Generate the file fresh each run (that's what the setup project does)."),
        ),
        challenge = Challenge(
            "Write a test that overrides the project-level login so it runs as a logged-out visitor, and asserts that /account redirects to /login.",
            """
                test.describe('guest', () => {
                  test.use({ storageState: { cookies: [], origins: [] } });

                  test('account redirects to login', async ({ page }) => {
                    await page.goto('/account');
                    await expect(page).toHaveURL(/\/login/);
                  });
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What makes the 'chromium' project wait for 'setup'?", listOf("dependencies: ['setup']", "order of the array", "retries"), 0, "dependencies guarantees setup runs first."),
            Quiz("Is sessionStorage saved in storageState?", listOf("No", "Yes", "Only in Firefox"), 0, "Only cookies and localStorage are saved."),
        ),
    ),
    "codegen-shots" to Extra(
        deep = listOf(
            H("Codegen power moves"),
            Code("Handy flags", "Bash", """
                npx playwright codegen https://example.com
                npx playwright codegen --device="iPhone 13" https://example.com
                npx playwright codegen --load-storage=.auth/user.json https://example.com
                npx playwright codegen --viewport-size=1280,720 -o tests/new.spec.ts https://example.com
            """.trimIndent()),
            P("In the codegen window use the 'Assert visibility / text / value' buttons to generate assertions, and 'Pick locator' to find locators without recording."),
            H("Screenshots in depth"),
            Code("Variations", "TypeScript", """
                await page.screenshot({ path: 'a.png', fullPage: true });
                await locator.screenshot({ path: 'card.png' });
                await page.screenshot({ path: 'x.png', mask: [page.getByTestId('clock')] });  // hide changing parts
                const buffer = await page.screenshot();
                await test.info().attach('page', { body: buffer, contentType: 'image/png' });
            """.trimIndent()),
            H("Visual regression testing"),
            Code("toHaveScreenshot", "TypeScript", """
                await expect(page).toHaveScreenshot('home.png', {
                  maxDiffPixelRatio: 0.01,                      // 1% tolerance
                  mask: [page.getByTestId('ad')],
                  animations: 'disabled',
                });
                await expect(page.getByRole('main')).toHaveScreenshot();
            """.trimIndent()),
            Bash2(),
            Tip("Mask dates, ads and random content, and disable animations. Otherwise visual tests are noisy."),
            Warn("Baselines are per OS and browser (`home-chromium-win32.png`). Generate them on the same OS as CI, ideally inside the official Docker image."),
        ),
        challenge = Challenge(
            "Take a visual regression snapshot of the page, masking an element with data-testid='clock', allowing 2% difference.",
            """
                await expect(page).toHaveScreenshot('landing.png', {
                  mask: [page.getByTestId('clock')],
                  maxDiffPixelRatio: 0.02,
                });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("How do you hide a changing element in visual tests?", listOf("mask", "delete", "skip"), 0, "mask paints over those areas in both images."),
            Quiz("Why do snapshots differ across OSes?", listOf("Fonts and rendering differ", "Playwright bug", "File size"), 0, "So generate baselines where CI runs."),
        ),
    ),
    "parallel-ci" to Extra(
        deep = listOf(
            H("How parallelism works"),
            P("Playwright starts N workers (processes). Each worker runs whole test files. With `fullyParallel: true`, tests inside a file are also spread across workers. Tests never share a worker's browser: each test still gets its own context."),
            Code("Control it", "TypeScript", """
                // config
                fullyParallel: true,
                workers: process.env.CI ? 4 : '50%',

                // one file must run in order (e.g. a flow)
                test.describe.configure({ mode: 'serial' });
            """.trimIndent()),
            Warn("`serial` mode: if one test fails, the rest in that group are skipped. Use it sparingly. Independent tests are better."),
            H("Tags, the modern way"),
            Code("Tags", "TypeScript", """
                test('checkout', { tag: ['@smoke', '@payments'] }, async ({ page }) => { /* ... */ });
                // npx playwright test --grep @smoke
                // npx playwright test --grep-invert @slow
            """.trimIndent()),
            H("A GitHub Actions workflow"),
            Code("pw.yml", "YAML", """
                name: Playwright
                on: [push, pull_request]
                jobs:
                  test:
                    runs-on: ubuntu-latest
                    steps:
                      - uses: actions/checkout@v4
                      - uses: actions/setup-node@v4
                        with: { node-version: 20 }
                      - run: npm ci
                      - run: npx playwright install --with-deps
                      - run: npx playwright test
                      - uses: actions/upload-artifact@v4
                        if: always()
                        with:
                          name: playwright-report
                          path: playwright-report/
            """.trimIndent()),
            H("Sharding across machines"),
            Code("Split the suite", "Bash", """
                npx playwright test --shard=1/4
                npx playwright test --shard=2/4
            """.trimIndent()),
            H("Docker"),
            Code("Official image has everything", "Bash", """
                docker run --rm -v ${'$'}PWD:/work -w /work mcr.microsoft.com/playwright:v1.50.0-noble npx playwright test
            """.trimIndent()),
            Tip("Match the Docker image tag to your installed `@playwright/test` version."),
        ),
        challenge = Challenge(
            "Tag a test '@smoke', write the command to run only smoke tests on Chromium, and the CI command to run the second quarter of a 4-way split.",
            """
                test('home loads', { tag: '@smoke' }, async ({ page }) => { /* ... */ });

                // npx playwright test --grep @smoke --project=chromium
                // npx playwright test --shard=2/4
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What is a shard?", listOf("A slice of the suite run on one machine", "A browser", "A report"), 0, "Sharding splits tests across CI machines."),
            Quiz("Why upload the report with `if: always()`?", listOf("To get it even when tests fail", "To save space", "It's required"), 0, "Failures are exactly when you need the report."),
        ),
    ),
)

private fun Bash2(): Block = Tip("Update snapshots intentionally with `npx playwright test --update-snapshots` after reviewing the visual change.")
