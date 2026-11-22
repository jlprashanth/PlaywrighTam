# Playwright Academy — web version (PWA)

The same course as the Android app, as an installable Progressive Web App so it also works on iPhone and iPad (and any browser).

Live site: https://jlprashanth.github.io/PlaywrighTam/

## Install on iPhone / iPad

1. Open the link above in **Safari**.
2. Tap **Share** → **Add to Home Screen**.
3. Open it from the new icon. It runs full-screen and works offline.

Progress is saved on each device separately; it does not sync with the Android app.

## How it is built

Plain HTML, CSS and JavaScript, with no build step and no dependencies.

| File | What it does |
| --- | --- |
| `index.html` | App shell, iOS / PWA meta tags |
| `app.js` | Screens (start page, home, lesson, settings), routing, progress in `localStorage` |
| `styles.css` | Material 3 look, light/dark theme, matching the Android app |
| `sw.js` | Service worker: caches everything for offline use |
| `manifest.webmanifest` | Name, icons, colours for installing |
| `data/course-<lang>.json` | Lesson content (0 English, 1 Tamil, 2 Tanglish, 3 Telugu, 4 Tenglish) — **generated, don't edit** |
| `icons/` | App icons, made from the Android launcher icon |
| `tools/` | Developer scripts (not deployed) |

### Lesson content stays in Kotlin

The lessons are written once, in the Android app (`app/src/main/java/com/example/playwright/data/`).
After changing them, regenerate the JSON and commit it with your change:

```sh
sh web/tools/export-content.sh
```

This runs the `WebExport` unit test with `WEB_EXPORT_DIR` set. Without that variable, the test is skipped, so normal Android builds and test runs are unaffected.

### Icons

`icons/logo.svg` is the Android launcher foreground converted to SVG. To rebuild the PNGs (uses headless Edge/Chrome):

```sh
node web/tools/make-icons.mjs
```

### Run locally

Serve the `web/` folder with any static server, for example:

```sh
npx serve web        # or: python -m http.server -d web 8080
```

The service worker is skipped on `localhost` so edits show up immediately; add `?sw` to the URL to test offline mode.

### Deploy

`.github/workflows/pages.yml` publishes `web/` to GitHub Pages on every push to `main` that touches `web/`.
It stamps the commit hash into `sw.js`, so installed copies pick up the new version the next time they open online.
