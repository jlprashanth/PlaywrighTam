# Playwright Academy 🎭

An offline Android app that teaches **TypeScript + Playwright** to people who already know **Selenium (Java)**. Every concept is explained in plain, friendly language, with the Selenium code you already know shown next to its Playwright equivalent.

## Features

- **9 modules, 43 lessons** that take you from "what is Playwright?" to building a full framework.
- **Every lesson includes**
  - 🧒 a short everyday-life story that explains the idea
  - ☕ the Selenium (Java) code vs 🎭 the Playwright (TypeScript) code, with syntax highlighting and a copy button
  - 📌 key points to remember
  - 🔬 a deep dive with tables, tips, warnings and extra code samples
  - 🏋️ a practice challenge with a hidden solution to check yourself
  - 🧠 one or more quizzes with instant feedback and explanations
- **Progress tracking**: mark lessons done, see overall and per-module progress, and continue where you left off. Progress is stored on the device.
- **5 languages**, switchable any time in Settings. All lesson content, deep dives, quizzes, challenges and UI text are translated; code stays in English.

  | Language | Description |
  | --- | --- |
  | English | Default |
  | தமிழ் | Tamil |
  | Tanglish | Tamil written in English letters |
  | తెలుగు | Telugu |
  | Tenglish | Telugu written in English letters |

- **Light, dark or system theme**, with edge-to-edge Material 3 design.
- **Works fully offline.** No account, no network access, no ads.

## Course outline

| # | Module | What you learn |
| --- | --- | --- |
| 1 | Say hello to Playwright | What it is, why it's better, setup, your first test, Node and npm |
| 2 | Just enough TypeScript | Variables, functions, objects, async/await, classes, types and generics |
| 3 | Test basics | Test structure and hooks, running tests, reports and traces, debugging |
| 4 | Finding things (Locators) | Role/label/text locators, CSS and XPath, filtering, tough cases |
| 5 | Doing things (Actions) | Click, type, forms, hover, drag and drop, uploads, dialogs, tabs and iframes |
| 6 | Waiting and checking | Auto-wait, assertions, soft assertions and steps, listening to events |
| 7 | Organize like a pro | Config, Page Objects, fixtures, test data, global setup |
| 8 | Power-ups | API testing, mocking, reusing login, codegen and screenshots, parallel runs and CI, device emulation |
| 9 | Hero zone | Capstone framework, best practices, Selenium to Playwright cheat map, interview prep, cheat sheet |

## Tech stack

- Kotlin and Jetpack Compose (Material 3)
- Single-activity app, no third-party runtime dependencies beyond AndroidX
- `minSdk 24`, `targetSdk 37`
- Progress, theme and language saved in `SharedPreferences`

## Project structure

```
app/src/main/java/com/example/playwright/
├── MainActivity.kt          # entry point, theme and language wiring
├── data/
│   ├── Model.kt             # Lesson, Module, Quiz, content blocks
│   ├── Content1-3.kt        # lessons for modules 1-8
│   ├── NewLessons.kt        # extra lessons and module 9
│   ├── DeepA-C.kt           # English deep dives, extra quizzes, challenges
│   ├── Translations*.kt     # lesson text + challenge prompts (Tamil, Tanglish, Telugu, Tenglish)
│   └── DeepTrT*.kt          # translated deep dives and extra quizzes
└── ui/
    ├── Screens.kt           # home, lesson and quiz screens
    ├── Deep.kt              # deep-dive and challenge cards
    ├── Settings.kt          # theme, language, reset progress
    ├── Components.kt        # code blocks, syntax highlighting, shared widgets
    └── Strings.kt           # language selection helper
```

## Build and run

Requirements: Android Studio (recent version) with an Android SDK that supports API 37.

```bash
./gradlew :app:assembleDebug       # build the debug APK
./gradlew :app:installDebug        # install on a connected device or emulator
./gradlew :app:testDebugUnitTest   # run unit tests
```

Or open the project in Android Studio and press **Run**.

## Adding or changing content

1. Add the English lesson in `data/Content*.kt` or `data/NewLessons.kt`, and a deep dive in `data/Deep*.kt`.
2. Add its translations for all four languages: the lesson text in `Translations*.kt`, and the deep dive and extra quizzes in `DeepTrT*.kt`.
3. Run `./gradlew :app:testDebugUnitTest`. `TranslationCoverageTest` fails if any lesson, module, challenge, deep dive or extra quiz is missing in a language, or if a translated deep dive doesn't match the English structure (block order, table shape, quiz options and answers).

In translated deep dives, write code blocks as `C("translated title")`. The code and its language are copied from the English original, so code can never drift between languages.

## License

No license has been chosen yet. All rights reserved by the author until one is added.
