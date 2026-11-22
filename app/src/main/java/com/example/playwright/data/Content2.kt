package com.example.playwright.data

val module4 = Module(
    id = "m4", emoji = "🔍", title = "Finding things (Locators)", subtitle = "Point at elements like a pro", accentIndex = 3,
    lessons = listOf(
        Lesson(
            id = "loc-user", emoji = "👀", title = "Locate like a human",
            story = "How do you find a friend in a crowd? You say \"the one wearing the red cap\", not \"person number 4,718\". Playwright likes this too. Find things by what they ARE (a button, a heading) or what they SAY (a label, some text) instead of ugly IDs and long XPaths.",
            javaCode = """
                driver.findElement(By.id("email"));
                driver.findElement(By.xpath("//button[text()='Sign in']"));
            """.trimIndent(),
            code = """
                page.getByRole('button', { name: 'Sign in' });
                page.getByLabel('Email');
                page.getByPlaceholder('Search');
                page.getByText('Welcome back');
                page.getByTestId('submit-btn');   // data-testid="submit-btn"
                page.getByAltText('Company logo');
                page.getByTitle('Close');
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Best-first order: role, label, placeholder, text, test id.",
            points = listOf(
                "`getByRole` is the favourite: buttons, links, headings, checkboxes, textboxes.",
                "These locators survive design changes because users still see the same words.",
                "`getByTestId` reads the `data-testid` attribute, which developers add for testing.",
            ),
            quiz = Quiz("Which locator does Playwright recommend first?", listOf("Long XPath", "getByRole", "Index like div[4]"), 1, "Locating by role/name matches how users and screen readers see the page."),
        ),
        Lesson(
            id = "loc-css", emoji = "🎯", title = "CSS, XPath and lazy locators",
            story = "Sometimes there's no nice name, so you can still use CSS or XPath, your old friends. But here's a surprise: a locator is like a treasure map, not the treasure. Making the map doesn't search anything. Playwright only goes hunting at the moment you act on it, so it is never stale.",
            javaCode = """
                WebElement el = driver.findElement(By.cssSelector("#login"));
                // searches NOW. Can go stale later!
            """.trimIndent(),
            code = """
                const login = page.locator('#login');           // CSS (id)
                const card  = page.locator('.card >> nth=0');  // CSS (class)
                const box   = page.locator('input[name="q"]');
                const byXp  = page.locator('//div[@id="main"]/h1'); // XPath

                await login.click();   // search happens HERE
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`page.locator()` takes CSS or XPath (it detects which).",
            points = listOf(
                "No more `StaleElementReferenceException`. Each action re-finds the element.",
                "A locator is cheap to create and reuse, so store them in variables.",
                "Use CSS/XPath only when role/label/text won't work.",
            ),
            quiz = Quiz("When does `page.locator('#login')` actually search the page?", listOf("When the line runs", "When you act on it, like click()", "Never"), 1, "Locators are lazy: they look for the element at action time, every time."),
        ),
        Lesson(
            id = "loc-filter", emoji = "🧲", title = "Filtering, chaining and lists",
            story = "Think of a classroom: \"find the row for Asha, then press her Delete button.\" You narrow down step by step. Playwright lets you chain locators, filter by text, and pick the first, last or n-th from a group.",
            javaCode = """
                List<WebElement> rows = driver.findElements(By.tagName("tr"));
                rows.get(2).findElement(By.tagName("button")).click();
            """.trimIndent(),
            code = """
                const row = page.getByRole('row').filter({ hasText: 'Asha' });
                await row.getByRole('button', { name: 'Delete' }).click();

                const items = page.getByRole('listitem');
                await items.first().click();
                await items.nth(2).click();          // 0-based
                await items.last().click();

                console.log(await items.count());
                console.log(await items.allTextContents());
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Chaining = search inside the first locator's result.",
            points = listOf(
                "Strict mode: if a locator matches MORE than one element and you try to click it, Playwright errors. This stops silent mistakes.",
                "Fix strict errors by being more specific, or by using `.first()` / `.nth()`.",
                "`count()` and `allTextContents()` replace `findElements().size()` and loops.",
            ),
            quiz = Quiz("A locator matches 3 buttons and you call `click()`. What happens?", listOf("Clicks the first one", "Strict mode error", "Clicks all three"), 1, "Playwright refuses to guess. Narrow it down or use first()/nth()."),
        ),
    ),
)

val module5 = Module(
    id = "m5", emoji = "🖱️", title = "Doing things (Actions)", subtitle = "Click, type, select, drag, switch", accentIndex = 4,
    lessons = listOf(
        Lesson(
            id = "act-click-fill", emoji = "⌨️", title = "Click, fill and press",
            story = "The robot has hands. `click()` presses things, `fill()` writes into boxes, and `press()` taps a keyboard key. `fill()` first clears the box, like erasing the chalkboard before writing.",
            javaCode = """
                el.clear();
                el.sendKeys("a@b.com");
                driver.findElement(By.id("go")).click();
                el.sendKeys(Keys.ENTER);
            """.trimIndent(),
            code = """
                await page.getByLabel('Email').fill('a@b.com');
                await page.getByLabel('Password').fill('secret');
                await page.getByRole('button', { name: 'Login' }).click();

                await page.getByLabel('Search').press('Enter');
                await page.getByLabel('Search').pressSequentially('hello', { delay: 100 });

                await page.getByText('Row').dblclick();
                await page.getByText('Menu').click({ button: 'right' });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`pressSequentially` types one letter at a time, for those fancy autocomplete boxes.",
            points = listOf(
                "`fill()` = `clear()` + `sendKeys()` in one step.",
                "Use `pressSequentially()` only when the site reacts to each keystroke.",
                "All of these auto-wait for the element to be ready.",
            ),
            quiz = Quiz("What does `fill()` do before typing?", listOf("Clears the field", "Takes a screenshot", "Nothing"), 0, "It replaces whatever was there, like clear() then sendKeys()."),
        ),
        Lesson(
            id = "act-forms", emoji = "☑️", title = "Checkboxes, radios and dropdowns",
            story = "Checkboxes are light switches, radio buttons are \"pick only one\" candies, and dropdowns are menus. Playwright has gentle commands for each so you don't need a special helper class like Selenium's `Select`.",
            javaCode = """
                Select s = new Select(driver.findElement(By.id("country")));
                s.selectByVisibleText("India");
                if (!box.isSelected()) box.click();
            """.trimIndent(),
            code = """
                await page.getByLabel('I agree').check();       // safe: only ticks if unticked
                await page.getByLabel('Newsletter').uncheck();
                await page.getByLabel('Male').check();           // radio

                await page.getByLabel('Country').selectOption('India');
                await page.getByLabel('Country').selectOption({ label: 'India' });

                await expect(page.getByLabel('I agree')).toBeChecked();
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "`check()` is smart: it won't untick a ticked box.",
            points = listOf(
                "`check()` and `uncheck()` replace the `if (!isSelected()) click()` dance.",
                "`selectOption()` handles value, label or index.",
                "Custom (non-`<select>`) dropdowns: just click it, then click the option text.",
            ),
            quiz = Quiz("Selenium's `Select` class is replaced in Playwright by...", listOf("selectOption()", "dropdown()", "pick()"), 0, "One method on the locator does the job."),
        ),
        Lesson(
            id = "act-more", emoji = "🪄", title = "Hover, drag, upload and scroll",
            story = "More robot tricks! Hovering is hovering a finger over a button. Dragging is picking up a toy and moving it. Uploading is handing the website a file. Selenium used the `Actions` class for most of these, but here they are plain commands.",
            javaCode = """
                new Actions(driver).moveToElement(el).perform();
                new Actions(driver).dragAndDrop(a, b).perform();
                fileInput.sendKeys("C:\\files\\cat.png");
            """.trimIndent(),
            code = """
                await page.getByText('Menu').hover();
                await page.locator('#a').dragTo(page.locator('#b'));
                await page.getByLabel('Upload').setInputFiles('files/cat.png');
                await page.getByText('Footer').scrollIntoViewIfNeeded();
                await page.keyboard.press('Control+A');
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Actions class? Not needed anymore.",
            points = listOf(
                "`hover()`, `dragTo()` and `setInputFiles()` are one-liners.",
                "Playwright scrolls automatically before clicking, so you rarely scroll by hand.",
                "`page.keyboard` and `page.mouse` give low-level control when needed.",
            ),
            quiz = Quiz("How do you upload a file?", listOf("setInputFiles()", "upload()", "sendFile()"), 0, "Point setInputFiles at the file input and pass the file path."),
        ),
        Lesson(
            id = "act-switch", emoji = "🪟", title = "Alerts, iframes and new tabs",
            story = "Selenium made you \"switch\" into frames, alerts and windows, like moving rooms with a key each time. Playwright has no switching. You just point at what you want and it goes there.",
            javaCode = """
                driver.switchTo().alert().accept();
                driver.switchTo().frame("myframe");
                driver.switchTo().window(handles.get(1));
            """.trimIndent(),
            code = """
                // Alert / confirm / prompt
                page.on('dialog', (dialog) => dialog.accept());
                await page.getByText('Delete').click();

                // iFrame: no switching!
                await page.frameLocator('#myframe').getByText('Hi').click();

                // New tab
                const [newPage] = await Promise.all([
                  page.context().waitForEvent('page'),
                  page.getByText('Open help').click(),
                ]);
                await newPage.waitForLoadState();
                console.log(await newPage.title());
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Register the dialog handler BEFORE the action that opens the dialog.",
            points = listOf(
                "Dialogs: listen with `page.on('dialog', ...)`, then accept or dismiss.",
                "iFrames: `frameLocator()` and carry on. No `switchTo()`, no `defaultContent()`.",
                "New tab: wait for the 'page' event while clicking, then use the new page object.",
            ),
            quiz = Quiz("How do you work inside an iframe?", listOf("switchTo().frame()", "frameLocator()", "You can't"), 1, "frameLocator() lets you chain straight into the iframe."),
        ),
    ),
)

val module6 = Module(
    id = "m6", emoji = "✅", title = "Waiting and checking", subtitle = "Auto-wait and assertions", accentIndex = 5,
    lessons = listOf(
        Lesson(
            id = "auto-wait", emoji = "⏳", title = "Auto-waiting explained",
            story = "Before the robot presses a button, it quickly asks five questions: Is it there? Is it visible? Is it holding still? Is it enabled? Is nobody covering it? Only when all answers are yes, it clicks. This is called actionability checks and it replaces most of your Selenium waits.",
            javaCode = """
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(el));
                Thread.sleep(3000); // oops
            """.trimIndent(),
            code = """
                // no waits needed for normal actions
                await page.getByRole('button', { name: 'Save' }).click();

                // when you REALLY must wait for something specific
                await page.waitForURL('**/dashboard');
                await page.getByText('Loading').waitFor({ state: 'hidden' });
                await page.waitForResponse('**/api/save');
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Avoid `page.waitForTimeout(3000)`. It's the Thread.sleep of Playwright.",
            points = listOf(
                "Checks: attached, visible, stable, enabled, receives events (and editable for typing).",
                "Default action timeout is unlimited within the test timeout (30s); configurable in config.",
                "Use `waitFor`, `waitForURL`, `waitForResponse` for special situations.",
            ),
            quiz = Quiz("What replaces most `WebDriverWait` code in Playwright?", listOf("Thread.sleep", "Auto-waiting", "Nothing, you must write it all"), 1, "Actions and assertions wait automatically."),
        ),
        Lesson(
            id = "assertions", emoji = "🧐", title = "Web-first assertions",
            story = "An assertion is the robot saying \"I believe the page looks like this. Let me check.\" Playwright's special assertions are patient: if the page isn't ready yet, they keep re-checking for up to 5 seconds before they say \"nope\".",
            javaCode = """
                Assert.assertEquals(el.getText(), "Welcome");
                Assert.assertTrue(el.isDisplayed());
            """.trimIndent(),
            code = """
                await expect(page).toHaveURL(/dashboard/);
                await expect(page).toHaveTitle('Home');

                const msg = page.getByRole('alert');
                await expect(msg).toBeVisible();
                await expect(msg).toHaveText('Saved!');
                await expect(msg).toContainText('Saved');
                await expect(page.getByLabel('Name')).toHaveValue('Asha');
                await expect(page.getByRole('listitem')).toHaveCount(5);
                await expect(page.getByText('Error')).not.toBeVisible();
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "Always `await expect(locator)...`. The retrying happens inside.",
            points = listOf(
                "Pass the locator itself to `expect`, not its text. `expect(await el.textContent())` does NOT retry.",
                "Add `.not` for the opposite: `.not.toBeVisible()`.",
                "Default expect timeout is 5s; change it in config with `expect: { timeout: 10000 }`.",
            ),
            quiz = Quiz("Which one retries until the text appears?", listOf("expect(await el.textContent()).toBe('x')", "await expect(el).toHaveText('x')", "Both"), 1, "Only the web-first form, with the locator inside expect, keeps retrying."),
        ),
        Lesson(
            id = "soft-steps", emoji = "🪜", title = "Soft assertions, steps and timeouts",
            story = "A normal assertion is a strict teacher: first mistake and you're out. A soft assertion is a kind teacher: it notes the mistake, lets the test continue, and reports all mistakes at the end. Steps give names to chunks of your test so reports read like a story.",
            javaCode = """
                SoftAssert soft = new SoftAssert();
                soft.assertEquals(a, b);
                soft.assertAll();
            """.trimIndent(),
            code = """
                test('profile page', async ({ page }) => {
                  test.setTimeout(60_000);   // this test may take 60s

                  await test.step('open profile', async () => {
                    await page.goto('/profile');
                  });

                  await expect.soft(page.getByTestId('name')).toHaveText('Asha');
                  await expect.soft(page.getByTestId('role')).toHaveText('Admin');
                });
            """.trimIndent(),
            codeLang = "TypeScript",
            codeCaption = "No assertAll() needed. Playwright reports soft failures when the test ends.",
            points = listOf(
                "`expect.soft` = TestNG `SoftAssert`, minus the final `assertAll()`.",
                "`test.step('name', ...)` groups actions in the report.",
                "`expect.poll(() => ...)` retries any function, e.g. an API call.",
                "Underscores in numbers like `60_000` are just for readability.",
            ),
            quiz = Quiz("What does `expect.soft` do on failure?", listOf("Stops the test", "Records it and continues", "Ignores it forever"), 1, "The test continues, but is marked failed at the end."),
        ),
    ),
)
