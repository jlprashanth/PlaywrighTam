package com.example.playwright.data

val deepB: Map<String, Extra> = mapOf(
    "loc-user" to Extra(
        deep = listOf(
            H("getByRole in depth"),
            P("Browsers give every element a hidden 'role' (what it is): button, link, heading, textbox, checkbox, row, listitem, dialog and more. The 'name' is its accessible name: its text, label or aria-label."),
            Code("Roles with options", "TypeScript", """
                page.getByRole('button', { name: 'Save' });                  // contains 'Save'
                page.getByRole('button', { name: 'Save', exact: true });     // exactly 'Save'
                page.getByRole('heading', { level: 2 });                     // an <h2>
                page.getByRole('checkbox', { name: 'Remember me', checked: true });
                page.getByRole('link', { name: /sign (in|up)/i });           // regex, ignore case
            """.trimIndent()),
            H("Role cheat sheet"),
            Table(
                listOf("HTML", "Role"),
                listOf(
                    listOf("`<button>`", "button"),
                    listOf("`<a href>`", "link"),
                    listOf("`<input type=text>`", "textbox"),
                    listOf("`<input type=checkbox>`", "checkbox"),
                    listOf("`<select>`", "combobox"),
                    listOf("`<h1>`..`<h6>`", "heading"),
                    listOf("`<tr>` / `<td>`", "row / cell"),
                    listOf("`<ul><li>`", "list / listitem"),
                ),
            ),
            H("getByText gotchas"),
            Code("exact vs partial", "TypeScript", """
                page.getByText('Welcome');                    // substring, case-insensitive
                page.getByText('Welcome', { exact: true });   // whole text, case-sensitive
                page.getByText(/^Welcome back, \w+/);          // regex
            """.trimIndent()),
            Tip("Not sure what role something has? Run `npx playwright codegen <url>` and click it, or use the Inspector's 'Pick locator'. It suggests the best locator."),
            Warn("`getByText` also matches text inside a paragraph, not just buttons. If it finds two things (strict mode error) switch to `getByRole` or add `exact: true`."),
        ),
        challenge = Challenge(
            "Write locators for: (a) a level-1 heading, (b) a link named exactly 'Log in', (c) an input with label 'Phone', (d) an element with data-testid='toast'.",
            """
                page.getByRole('heading', { level: 1 });
                page.getByRole('link', { name: 'Log in', exact: true });
                page.getByLabel('Phone');
                page.getByTestId('toast');
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `exact: true` change?", listOf("Matches the whole text, case-sensitive", "Makes it faster", "Ignores the role"), 0, "Without it, matching is a case-insensitive substring."),
            Quiz("The role of a normal `<a href>` link is...", listOf("button", "link", "anchor"), 1, "ARIA role for links is `link`."),
        ),
    ),
    "loc-css" to Extra(
        deep = listOf(
            H("CSS cheat sheet (the same as Selenium)"),
            Table(
                listOf("You want", "CSS"),
                listOf(
                    listOf("By id", "`#login`"),
                    listOf("By class", "`.btn.primary`"),
                    listOf("Attribute", "`input[name='q']`"),
                    listOf("Starts with", "`a[href^='/docs']`"),
                    listOf("Contains", "`div[class*='card']`"),
                    listOf("Child", "`ul > li`"),
                    listOf("n-th", "`li:nth-child(3)`"),
                ),
            ),
            H("Playwright's bonus selectors"),
            Code("Pseudo-classes", "TypeScript", """
                page.locator('button:has-text("Save")');       // contains text
                page.locator('li:has(a.active)');              // has a child matching
                page.locator('div.card:visible');              // only visible ones
                page.locator('text=Sign in');                  // old style, still works
            """.trimIndent()),
            H("XPath, if you must"),
            Code("XPath", "TypeScript", """
                page.locator('xpath=//table//tr[td[text()="Asha"]]');
                page.locator('//button[@type="submit"]');      // starts with // = XPath
            """.trimIndent()),
            H("Combining locators"),
            Code("and / or", "TypeScript", """
                const save = page.getByRole('button', { name: 'Save' });
                const submit = page.getByRole('button', { name: 'Submit' });
                await save.or(submit).click();           // whichever shows up

                const btn = page.getByRole('button').and(page.getByText('OK'));
            """.trimIndent()),
            Tip("Prefer attributes meant for testing (`data-testid`) over styling classes like `.css-1x2y3z`. Ask the developers to add them."),
            Warn("Never use index-based XPath like `(//div)[7]`. One new element on the page and your test breaks."),
        ),
        challenge = Challenge(
            "Write a CSS locator for a submit button inside a form with id 'signup', and a locator for any `li` that contains the text 'Pending'.",
            """
                page.locator('form#signup button[type="submit"]');
                page.locator('li', { hasText: 'Pending' });
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("What does `a[href^='/docs']` select?", listOf("Links starting with /docs", "Links ending with /docs", "Links containing docs"), 0, "`^=` means starts with."),
            Quiz("What does `locatorA.or(locatorB)` do?", listOf("Matches either one", "Matches both at once only", "Throws"), 0, "Great for optional popups and A/B variants."),
        ),
    ),
    "loc-filter" to Extra(
        deep = listOf(
            H("Filtering variations"),
            Code("filter options", "TypeScript", """
                const rows = page.getByRole('row');
                rows.filter({ hasText: 'Asha' });                       // has this text
                rows.filter({ hasNotText: 'Inactive' });                // lacks this text
                rows.filter({ has: page.getByRole('button', { name: 'Edit' }) });  // has a child
                rows.filter({ hasText: /Asha|Ravi/ });                  // regex
            """.trimIndent()),
            H("Working with tables, the classic Selenium pain"),
            Code("Read and click table cells", "TypeScript", """
                const table = page.getByRole('table');
                const row = table.getByRole('row', { name: /Asha/ });
                await expect(row.getByRole('cell').nth(2)).toHaveText('Admin');
                await row.getByRole('button', { name: 'Delete' }).click();

                // read the whole table into an array
                const names = await table.locator('tbody tr td:first-child').allTextContents();
                expect(names).toContain('Asha');
            """.trimIndent()),
            H("Iterating over many elements"),
            Code("all() and loops", "TypeScript", """
                const cards = await page.locator('.product').all();
                for (const card of cards) {
                  console.log(await card.locator('.price').textContent());
                }

                // or let Playwright do the work
                const prices = await page.locator('.product .price').allInnerTexts();
            """.trimIndent()),
            H("Parent from child"),
            Code("go up", "TypeScript", """
                const item = page.getByText('Milk').locator('..');           // parent
                const card = page.locator('.card', { has: page.getByText('Milk') });
            """.trimIndent()),
            Tip("`filter({ has: ... })` is the Playwright way to say 'the row that CONTAINS this button', no XPath parent gymnastics needed."),
            Warn("`.all()` takes a snapshot of the list right now. If the list is still loading, wait with `await expect(list).toHaveCount(n)` first."),
        ),
        challenge = Challenge(
            "In a table, click the 'Approve' button in the row that contains 'Order #1042' but does NOT contain 'Cancelled'. Then assert the row's status cell says 'Approved'.",
            """
                const row = page.getByRole('row')
                  .filter({ hasText: 'Order #1042' })
                  .filter({ hasNotText: 'Cancelled' });

                await row.getByRole('button', { name: 'Approve' }).click();
                await expect(row).toContainText('Approved');
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("`filter({ has: locator })` keeps elements that...", listOf("Contain a matching child", "Are inside a matching parent", "Equal the locator"), 0, "It checks descendants."),
            Quiz("Why wait before `.all()`?", listOf("It takes a snapshot of the current list", "It's slow", "It needs a license"), 0, "If items aren't loaded yet, you get a short list."),
        ),
    ),
    "act-click-fill" to Extra(
        deep = listOf(
            H("What happens inside click()"),
            P("Playwright scrolls the element into view, waits for it to be visible, stable, enabled and not covered, then moves the mouse and clicks at its centre. If something is covering it (a cookie banner), it keeps retrying and then fails with a message telling you what blocks it."),
            H("Click options"),
            Code("Useful options", "TypeScript", """
                await btn.click({ position: { x: 5, y: 5 } });     // click a spot
                await btn.click({ modifiers: ['Shift'] });         // shift+click
                await btn.click({ clickCount: 3 });                // triple click
                await btn.click({ timeout: 5000 });                // custom wait
                await btn.click({ trial: true });                  // only check, don't click
                await btn.click({ force: true });                  // skip checks (last resort!)
            """.trimIndent()),
            H("Typing and keys"),
            Code("Keyboard", "TypeScript", """
                await box.fill('hello');                    // set value instantly
                await box.clear();                          // empty it
                await box.press('Control+A');               // combos
                await box.press('Backspace');
                await page.keyboard.type('slow typing');    // types at the focused element
                await page.keyboard.press('Tab');

                const typed = await box.inputValue();       // read what's inside
            """.trimIndent()),
            H("Reading from elements"),
            Code("Get text and attributes", "TypeScript", """
                await el.textContent();          // all text, incl. hidden
                await el.innerText();            // visible text only
                await el.getAttribute('href');
                await el.isVisible();            // instant check, doesn't wait
                await el.isEnabled();
                await el.count();
            """.trimIndent()),
            Tip("Prefer `await expect(el).toHaveText(...)` over reading text and comparing yourself. The expect form retries automatically."),
            Warn("`force: true` bypasses the safety checks and can hide real bugs (a button users can't actually click). Use it only when you know why."),
        ),
        challenge = Challenge(
            "Write steps to: fill 'Search' with 'playwright', press Enter, then assert the first result link is visible. Also print the search box's current value.",
            """
                const box = page.getByLabel('Search');
                await box.fill('playwright');
                console.log(await box.inputValue());
                await box.press('Enter');
                await expect(page.getByRole('link').first()).toBeVisible();
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("`textContent()` vs `innerText()`: which returns only visible text?", listOf("textContent", "innerText", "Both"), 1, "innerText respects CSS visibility."),
            Quiz("What does `click({ trial: true })` do?", listOf("Runs the checks without clicking", "Clicks twice", "Clicks hidden items"), 0, "It's a dry run."),
        ),
    ),
    "act-forms" to Extra(
        deep = listOf(
            H("A full form in one go"),
            Code("Sign-up form", "TypeScript", """
                await page.getByLabel('Full name').fill('Asha Rao');
                await page.getByLabel('Email').fill('asha@example.com');
                await page.getByLabel('Country').selectOption('IN');
                await page.getByRole('radio', { name: 'Monthly' }).check();
                await page.getByRole('checkbox', { name: 'Accept terms' }).check();
                await page.getByRole('button', { name: 'Create account' }).click();
                await expect(page.getByText('Welcome, Asha')).toBeVisible();
            """.trimIndent()),
            H("selectOption variations"),
            Code("By value, label, index, many", "TypeScript", """
                await sel.selectOption('IN');                        // value attribute
                await sel.selectOption({ label: 'India' });          // visible text
                await sel.selectOption({ index: 2 });
                await multi.selectOption(['red', 'blue']);           // multi-select
                const chosen = await sel.inputValue();
            """.trimIndent()),
            H("Custom dropdowns (not a real <select>)"),
            P("Modern sites often build dropdowns out of divs. You can't call selectOption. Click to open, then click the option like any other element."),
            Code("Div-based dropdown", "TypeScript", """
                await page.getByRole('combobox', { name: 'City' }).click();
                await page.getByRole('option', { name: 'Chennai' }).click();
                await expect(page.getByRole('combobox', { name: 'City' })).toHaveText(/Chennai/);
            """.trimIndent()),
            H("Date pickers and autocomplete"),
            Code("Autocomplete", "TypeScript", """
                const box = page.getByLabel('From');
                await box.pressSequentially('Che', { delay: 100 });
                await page.getByRole('option', { name: 'Chennai' }).click();
            """.trimIndent()),
            Tip("For date inputs of type `date`, `fill('2026-12-31')` works directly. For fancy calendar widgets, click the field and click the day cell."),
            Warn("Don't `check()` a radio you want to switch off. Radios can't be unchecked; check a different one instead."),
        ),
        challenge = Challenge(
            "Handle a custom dropdown: open the 'Size' combobox, choose 'Large', and verify the dropdown now shows 'Large'. Then tick the 'Gift wrap' checkbox only if it is not already ticked.",
            """
                const size = page.getByRole('combobox', { name: 'Size' });
                await size.click();
                await page.getByRole('option', { name: 'Large' }).click();
                await expect(size).toHaveText(/Large/);

                await page.getByLabel('Gift wrap').check();   // check() is already idempotent
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("selectOption doesn't work on div-based dropdowns because...", listOf("They aren't real <select> elements", "It's buggy", "It needs a plugin"), 0, "Click it and click the option instead."),
            Quiz("`fill()` on `<input type=date>` expects...", listOf("2026-12-31", "31/12/2026", "A Date object"), 0, "The ISO format yyyy-mm-dd."),
        ),
    ),
    "act-more" to Extra(
        deep = listOf(
            H("Hover menus"),
            Code("Hover then click", "TypeScript", """
                await page.getByRole('menuitem', { name: 'Products' }).hover();
                await page.getByRole('menuitem', { name: 'Laptops' }).click();
            """.trimIndent()),
            H("Drag and drop, two ways"),
            Code("dragTo and manual", "TypeScript", """
                await src.dragTo(target);                 // simple

                // manual, for tricky widgets
                await src.hover();
                await page.mouse.down();
                await target.hover();
                await page.mouse.up();
            """.trimIndent()),
            H("File upload and download"),
            Code("Files", "TypeScript", """
                // upload (many files too)
                await page.getByLabel('Photos').setInputFiles(['a.png', 'b.png']);
                await page.getByLabel('Photos').setInputFiles([]);   // clear

                // upload when there's no visible <input>: use the file chooser
                const [chooser] = await Promise.all([
                  page.waitForEvent('filechooser'),
                  page.getByText('Browse').click(),
                ]);
                await chooser.setFiles('a.png');

                // download
                const [download] = await Promise.all([
                  page.waitForEvent('download'),
                  page.getByText('Export CSV').click(),
                ]);
                await download.saveAs('out/' + download.suggestedFilename());
            """.trimIndent()),
            H("Scrolling and infinite lists"),
            Code("Scroll", "TypeScript", """
                await page.mouse.wheel(0, 1000);                    // scroll down
                await page.getByText('Footer').scrollIntoViewIfNeeded();
                await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
            """.trimIndent()),
            Tip("`page.evaluate()` runs JavaScript inside the page, like Selenium's `JavascriptExecutor`."),
            Warn("Downloads are saved to a temp folder and deleted when the context closes. Call `saveAs` if you want to keep the file."),
        ),
        challenge = Challenge(
            "Click 'Export' and save the downloaded file into a folder called 'downloads' with its original name. Then assert the filename ends with '.csv'.",
            """
                const [download] = await Promise.all([
                  page.waitForEvent('download'),
                  page.getByRole('button', { name: 'Export' }).click(),
                ]);
                const name = download.suggestedFilename();
                await download.saveAs('downloads/' + name);
                expect(name.endsWith('.csv')).toBeTruthy();
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Equivalent of Selenium's JavascriptExecutor?", listOf("page.evaluate()", "page.run()", "page.script()"), 0, "evaluate runs JS in the page."),
            Quiz("Why use Promise.all with waitForEvent('download')?", listOf("Start listening before the click", "It's faster", "Syntax requirement"), 0, "Otherwise you may miss the event."),
        ),
    ),
    "act-switch" to Extra(
        deep = listOf(
            H("Dialogs: accept, dismiss, read"),
            Code("alert / confirm / prompt", "TypeScript", """
                page.once('dialog', async (dialog) => {
                  console.log(dialog.type());       // alert | confirm | prompt
                  console.log(dialog.message());
                  await dialog.accept('my answer');   // text only used for prompt
                  // or: await dialog.dismiss();
                });
                await page.getByText('Delete').click();
            """.trimIndent()),
            Warn("If no handler is registered, Playwright auto-dismisses dialogs. If you need to ACCEPT, you must register a handler first."),
            H("Nested iframes"),
            Code("frames inside frames", "TypeScript", """
                const inner = page.frameLocator('#outer').frameLocator('#inner');
                await inner.getByRole('button', { name: 'Pay' }).click();
            """.trimIndent()),
            H("Popups and multiple tabs"),
            Code("popup event", "TypeScript", """
                const popupPromise = page.waitForEvent('popup');
                await page.getByText('Login with Google').click();
                const popup = await popupPromise;
                await popup.waitForLoadState();
                await popup.getByLabel('Email').fill('a@b.com');
                await popup.close();

                // all open tabs
                const pages = page.context().pages();
            """.trimIndent()),
            H("Shadow DOM"),
            P("Good news: Playwright locators pierce open shadow DOM automatically. `getByRole` and CSS work through it with no special code (Selenium needed JavaScript for this)."),
            H("Browser permissions"),
            Code("Geolocation, notifications", "TypeScript", """
                // in a test file or config `use`
                test.use({
                  permissions: ['geolocation'],
                  geolocation: { latitude: 13.08, longitude: 80.27 },
                });
            """.trimIndent()),
            Tip("Window handles are gone. Each tab is just another `Page` variable. Name them (`mainPage`, `popup`) and you never 'switch'."),
        ),
        challenge = Challenge(
            "Click a link that opens in a new tab, wait for it, and assert the new tab's URL contains '/help'. Then close the new tab and keep working on the original.",
            """
                const popupPromise = page.waitForEvent('popup');
                await page.getByRole('link', { name: 'Help' }).click();
                const help = await popupPromise;
                await help.waitForLoadState();
                await expect(help).toHaveURL(/\/help/);
                await help.close();
                await expect(page.getByRole('heading', { level: 1 })).toBeVisible();
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("If you don't register a dialog handler, Playwright...", listOf("Auto-dismisses the dialog", "Hangs forever", "Accepts it"), 0, "So register a handler if you need to accept."),
            Quiz("Do you need special code for shadow DOM?", listOf("Usually no", "Always", "Only in Firefox"), 0, "Open shadow roots are pierced by default."),
        ),
    ),
    "auto-wait" to Extra(
        deep = listOf(
            H("Which checks run for which action?"),
            Table(
                listOf("Action", "Visible", "Stable", "Enabled", "Editable", "Receives events"),
                listOf(
                    listOf("click", "yes", "yes", "yes", "-", "yes"),
                    listOf("fill", "yes", "-", "yes", "yes", "-"),
                    listOf("check", "yes", "yes", "yes", "-", "yes"),
                    listOf("hover", "yes", "yes", "-", "-", "yes"),
                    listOf("selectOption", "yes", "-", "yes", "-", "-"),
                ),
            ),
            H("Timeouts, there are several"),
            Table(
                listOf("Timeout", "Default", "Set with"),
                listOf(
                    listOf("Whole test", "30s", "`timeout` in config / `test.setTimeout`"),
                    listOf("Each expect", "5s", "`expect: { timeout }`"),
                    listOf("Each action", "no limit (test timeout)", "`actionTimeout` in `use`"),
                    listOf("Navigation", "no limit", "`navigationTimeout`"),
                ),
            ),
            H("Explicit waits, only when needed"),
            Code("Real wait helpers", "TypeScript", """
                await page.waitForURL('**/dashboard');
                await page.waitForLoadState('networkidle');       // use sparingly
                await locator.waitFor({ state: 'visible' });      // attached | detached | hidden
                await page.waitForResponse((r) => r.url().includes('/api/user') && r.ok());
                await page.waitForFunction(() => window.appReady === true);
            """.trimIndent()),
            Warn("`networkidle` waits until no network for 500ms. Sites with analytics or websockets never go idle, so prefer waiting for a specific element or response."),
            Tip("A spinner that disappears? `await expect(page.getByRole('progressbar')).toBeHidden()` is better than a sleep."),
        ),
        challenge = Challenge(
            "After clicking 'Search', wait until the API call to `/api/search` returns successfully, then assert that at least one result is shown.",
            """
                const [response] = await Promise.all([
                  page.waitForResponse((r) => r.url().includes('/api/search') && r.ok()),
                  page.getByRole('button', { name: 'Search' }).click(),
                ]);
                await expect(page.getByRole('listitem').first()).toBeVisible();
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Default timeout of a single `expect`?", listOf("5 seconds", "30 seconds", "Forever"), 0, "Expect timeout defaults to 5s."),
            Quiz("Why avoid `waitForLoadState('networkidle')` generally?", listOf("Busy sites may never be idle", "It's deprecated", "It crashes"), 0, "Pending polling or analytics calls keep the network busy."),
        ),
    ),
    "assertions" to Extra(
        deep = listOf(
            H("The assertion menu"),
            Table(
                listOf("Assertion", "Checks"),
                listOf(
                    listOf("`toBeVisible / toBeHidden`", "shown or not"),
                    listOf("`toBeEnabled / toBeDisabled`", "usable or not"),
                    listOf("`toBeChecked`", "checkbox / radio"),
                    listOf("`toBeEditable`", "can type in it"),
                    listOf("`toBeEmpty`", "no text or value"),
                    listOf("`toBeFocused`", "has keyboard focus"),
                    listOf("`toHaveText / toContainText`", "full text / partial text"),
                    listOf("`toHaveValue`", "input value"),
                    listOf("`toHaveAttribute('href', /x/)`", "attribute"),
                    listOf("`toHaveClass`", "CSS class"),
                    listOf("`toHaveCSS('color', ...)`", "computed style"),
                    listOf("`toHaveCount(n)`", "number of matches"),
                    listOf("`toHaveURL / toHaveTitle`", "page level"),
                ),
            ),
            H("Lists of text"),
            Code("Check many at once", "TypeScript", """
                await expect(page.getByRole('listitem')).toHaveText(['Apple', 'Mango', 'Pear']); // exact order
                await expect(page.getByRole('listitem')).toContainText(['App', 'Man']);
            """.trimIndent()),
            H("Generic assertions (no retry)"),
            Code("For plain values", "TypeScript", """
                expect(5).toBe(5);                        // same value
                expect({ a: 1 }).toEqual({ a: 1 });       // deep compare
                expect([1, 2, 3]).toContain(2);
                expect('hello world').toMatch(/world/);
                expect(price).toBeGreaterThan(10);
                expect(value).toBeTruthy();
            """.trimIndent()),
            H("Custom messages"),
            Code("Helpful failure text", "TypeScript", """
                await expect(banner, 'promo banner should show for new users').toBeVisible();
            """.trimIndent()),
            Tip("`toHaveText` matches the whole text after trimming whitespace. For 'contains', use `toContainText`."),
            Warn("`toBe` on a locator's text read (`expect(await el.innerText()).toBe(..)`) checks once and never retries. Prefer the locator assertion."),
        ),
        challenge = Challenge(
            "Assert that the navigation list has exactly 4 items in the order Home, Docs, API, Blog, and that the 'Login' link points to '/login'.",
            """
                const nav = page.getByRole('navigation').getByRole('listitem');
                await expect(nav).toHaveCount(4);
                await expect(nav).toHaveText(['Home', 'Docs', 'API', 'Blog']);
                await expect(page.getByRole('link', { name: 'Login' })).toHaveAttribute('href', '/login');
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("Which assertion checks partial text?", listOf("toContainText", "toHaveText", "toBe"), 0, "toHaveText is the full text."),
            Quiz("Which one retries?", listOf("expect(locator).toBeVisible()", "expect(5).toBe(5)", "Both"), 0, "Only locator/page assertions are web-first."),
        ),
    ),
    "soft-steps" to Extra(
        deep = listOf(
            H("expect.poll: retry anything"),
            Code("Poll an API until ready", "TypeScript", """
                await expect.poll(async () => {
                  const res = await request.get('/api/job/42');
                  return (await res.json()).status;
                }, { timeout: 20_000, intervals: [1000] }).toBe('done');
            """.trimIndent()),
            H("expect.toPass: retry a block"),
            Code("Retry several lines together", "TypeScript", """
                await expect(async () => {
                  await page.reload();
                  await expect(page.getByText('Report ready')).toBeVisible({ timeout: 1000 });
                }).toPass({ timeout: 30_000 });
            """.trimIndent()),
            H("Timeouts at every level"),
            Code("Where to set them", "TypeScript", """
                test.setTimeout(120_000);                       // this test
                test.slow();                                    // triple the timeout
                await expect(el).toBeVisible({ timeout: 15_000 });  // one assertion
                await el.click({ timeout: 3_000 });             // one action
                // config: timeout, expect: { timeout }, use: { actionTimeout }
            """.trimIndent()),
            H("test.step returns values and nests"),
            Code("Readable reports", "TypeScript", """
                const orderId = await test.step('place order', async () => {
                  await page.getByText('Buy').click();
                  return await page.getByTestId('order-id').innerText();
                });
                await test.step('verify order', async () => {
                  await expect(page.getByText(orderId)).toBeVisible();
                });
            """.trimIndent()),
            Tip("A test full of soft assertions is great for 'checking a whole profile page'. For flow-critical checks (like 'did login work?') use normal assertions so the test stops early."),
        ),
        challenge = Challenge(
            "A background job takes up to 30 seconds. Using `request`, poll `/api/jobs/7` once per second until its JSON `status` equals 'done'.",
            """
                await expect.poll(async () => {
                  const res = await request.get('/api/jobs/7');
                  const body = await res.json();
                  return body.status;
                }, { timeout: 30_000, intervals: [1000] }).toBe('done');
            """.trimIndent(),
        ),
        quiz = listOf(
            Quiz("When is `expect.poll` handy?", listOf("Retrying a non-locator value like an API result", "Clicking faster", "Taking screenshots"), 0, "It re-evaluates your function until it matches."),
            Quiz("What does `test.slow()` do?", listOf("Triples the test timeout", "Runs the test slower", "Skips it"), 0, "Useful for long end-to-end flows."),
        ),
    ),
)
