#!/usr/bin/env sh
# Regenerates web/data/course-*.json from the Kotlin lesson content in the Android app.
# Run from anywhere:  sh web/tools/export-content.sh
# Then bump CACHE_VERSION in web/sw.js so installed copies pick up the new content.
set -e
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
cd "$ROOT"
export WEB_EXPORT_DIR="$ROOT/web/data"
./gradlew :app:testDebugUnitTest --tests "com.example.playwright.WebExport" --rerun
ls -l "$WEB_EXPORT_DIR"
