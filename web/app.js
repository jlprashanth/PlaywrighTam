/* Playwright Academy — web (PWA) version.
 * Screens mirror the Android app (app/src/main/java/com/example/playwright/ui).
 * Lesson content comes from data/course-<lang>.json, exported from the Kotlin sources by web/tools/export-content.sh.
 */
(function () {
  'use strict';

  // ───────────────────────── Constants ─────────────────────────

  // Languages: 0 = English, 1 = Tamil, 2 = Tanglish, 3 = Telugu, 4 = Tenglish (same as Strings.kt).
  const LANG_NAMES = ['English', 'தமிழ்', 'Tanglish', 'తెలుగు', 'Tenglish'];
  const HTML_LANG = ['en', 'ta', 'ta-Latn', 'te', 'te-Latn'];
  const LANG_OPTIONS = [
    { glyph: 'Aa', name: 'English', sub: 'Learn in English' },
    { glyph: 'அ', name: 'தமிழ்', sub: 'Tamil' },
    { glyph: 'Ta', name: 'Tanglish', sub: 'Tamil in English letters' },
    { glyph: 'అ', name: 'తెలుగు', sub: 'Telugu' },
    { glyph: 'Te', name: 'Tenglish', sub: 'Telugu in English letters' },
  ];
  // Same order as `accents` in Components.kt.
  const ACCENTS = ['#1A73E8', '#EA4335', '#1E8E3E', '#F9AB00', '#9334E6', '#129EAF', '#E8710A', '#D01884'];

  const ICONS = {
    settings: 'M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58a.49.49 0 0 0 .12-.61l-1.92-3.32a.488.488 0 0 0-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54a.484.484 0 0 0-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58a.49.49 0 0 0-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z',
    back: 'M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z',
    forward: 'M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z',
    checkCircle: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z',
    check: 'M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z',
    chevronDown: 'M7.41 8.59L12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41z',
    close: 'M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z',
    share: 'M16 5l-1.42 1.42-1.59-1.59V16h-1.98V4.83L9.42 6.42 8 5l4-4 4 4zm4 5v11c0 1.1-.9 2-2 2H6a2 2 0 0 1-2-2V10c0-1.11.89-2 2-2h3v2H6v11h12V10h-3V8h3a2 2 0 0 1 2 2z',
  };
  const icon = (name, cls = '') => `<svg class="icon ${cls}" viewBox="0 0 24 24" aria-hidden="true"><path d="${ICONS[name]}"/></svg>`;

  // ───────────────────────── Storage (replaces SharedPreferences) ─────────────────────────

  const storage = {
    get(key, fallback) {
      try { const v = localStorage.getItem('pa.' + key); return v == null ? fallback : JSON.parse(v); } catch (e) { return fallback; }
    },
    set(key, value) { try { localStorage.setItem('pa.' + key, JSON.stringify(value)); } catch (e) { /* private mode */ } },
    remove(key) { try { localStorage.removeItem('pa.' + key); } catch (e) { /* ignore */ } },
    has(key) { try { return localStorage.getItem('pa.' + key) != null; } catch (e) { return false; } },
  };

  const store = {
    done: new Set(storage.get('done', [])),
    lang: clampLang(storage.get('lang', 0)),
    theme: storage.get('theme', 0),
    // Existing users who already picked a language or finished a lesson skip the start page.
    onboarded: storage.get('onboarded', storage.has('lang') || storage.has('done')),
    markDone(id) { if (!this.done.has(id)) { this.done.add(id); this.saveDone(); } },
    toggle(id) { if (this.done.has(id)) this.done.delete(id); else this.done.add(id); this.saveDone(); },
    resetProgress() { this.done.clear(); storage.remove('done'); },
    saveDone() { storage.set('done', Array.from(this.done)); },
    chooseLanguage(l) { this.lang = l; storage.set('lang', l); },
    setTheme(mode) { this.theme = mode; storage.set('theme', mode); },
    finishOnboarding() { this.onboarded = true; storage.set('onboarded', true); storage.set('lang', this.lang); },
  };
  // The first-launch start page always opens in English.
  if (!store.onboarded) store.lang = 0;

  function clampLang(l) { return Number.isInteger(l) && l >= 0 && l <= 4 ? l : 0; }

  // ───────────────────────── Helpers ─────────────────────────

  /** Picks the string for the current language (same signature as `t()` in Strings.kt). */
  function t(en, ta, tg, te, tn) {
    return [en, ta, tg, te === undefined ? en : te, tn === undefined ? en : tn][store.lang];
  }

  function esc(s) {
    return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
  }

  /** Turns `backtick` segments into inline code spans (richText in Components.kt). */
  function rich(text) {
    return String(text).split('`').map((part, i) => (i % 2 === 1 ? `<code class="ic">${esc(part)}</code>` : esc(part))).join('');
  }

  const KEYWORDS = new Set([
    'import', 'from', 'export', 'const', 'let', 'var', 'async', 'await', 'function', 'return', 'if', 'else', 'for', 'of',
    'new', 'class', 'extends', 'public', 'private', 'void', 'static', 'final', 'readonly', 'constructor', 'this', 'true',
    'false', 'null', 'string', 'number', 'boolean', 'String', 'int', 'List', 'Map', 'WebDriver', 'WebElement', 'default',
    'try', 'catch', 'throw', 'interface', 'type',
  ]);
  const TOKEN = /(\/\/[^\n]*|#[^\n]*)|("(?:\\.|[^"\\\n])*"|'(?:\\.|[^'\\\n])*'|`[^`]*`)|\b([A-Za-z_]+)\b|\b(\d[\d_]*)\b/g;

  /** Syntax highlighting, same rules as highlight() in Components.kt. */
  function highlight(code, lang) {
    const useKw = lang !== 'Bash';
    let out = '';
    let last = 0;
    TOKEN.lastIndex = 0;
    let m;
    while ((m = TOKEN.exec(code)) !== null) {
      if (m[0] === '') { TOKEN.lastIndex++; continue; }
      if (m.index > last) out += esc(code.slice(last, m.index));
      const text = m[0];
      let cls = null;
      if (m[1] !== undefined) cls = text.startsWith('#') && lang !== 'Bash' ? null : 'hl-c';
      else if (m[2] !== undefined) cls = 'hl-s';
      else if (m[3] !== undefined) cls = useKw && KEYWORDS.has(text) ? 'hl-k' : null;
      else cls = 'hl-n';
      out += cls ? `<span class="${cls}">${esc(text)}</span>` : esc(text);
      last = m.index + text.length;
    }
    return out + esc(code.slice(last));
  }

  function rgba(hex, a) {
    const n = parseInt(hex.slice(1), 16);
    return `rgba(${(n >> 16) & 255}, ${(n >> 8) & 255}, ${n & 255}, ${a})`;
  }
  const accentOf = (module) => ACCENTS[module.accentIndex % ACCENTS.length];

  function emojiBadge(emoji, accent, size = 48) {
    return `<span class="emoji-badge" aria-hidden="true" style="background:${rgba(accent, 0.16)};padding:${size / 4}px;font-size:${size / 2}px">${esc(emoji)}</span>`;
  }

  // ───────────────────────── Content ─────────────────────────

  const courses = {}; // lang -> { modules, lessons, moduleOf }
  const loading = {};

  function loadCourse(lang) {
    if (courses[lang]) return Promise.resolve(courses[lang]);
    if (!loading[lang]) {
      loading[lang] = fetch(`data/course-${lang}.json`)
        .then((r) => { if (!r.ok) throw new Error('HTTP ' + r.status); return r.json(); })
        .then((data) => {
          const lessons = [];
          const moduleOf = {};
          data.modules.forEach((m) => m.lessons.forEach((l) => { lessons.push(l); moduleOf[l.id] = m; }));
          courses[lang] = { modules: data.modules, lessons, moduleOf };
          return courses[lang];
        })
        .catch((err) => { delete loading[lang]; throw err; });
    }
    return loading[lang];
  }

  // ───────────────────────── Theme ─────────────────────────

  const darkQuery = window.matchMedia ? window.matchMedia('(prefers-color-scheme: dark)') : null;
  const isDark = () => store.theme === 2 || (store.theme === 0 && !!(darkQuery && darkQuery.matches));

  function applyTheme() {
    document.documentElement.setAttribute('data-theme', isDark() ? 'dark' : 'light');
    updateThemeColor();
  }
  function updateThemeColor() {
    const meta = document.querySelector('meta[name="theme-color"]');
    if (!meta) return;
    meta.content = route().name === 'welcome' ? '#0B1437' : isDark() ? '#202124' : '#F8F9FA';
  }
  if (darkQuery) {
    const onChange = () => { if (store.theme === 0) applyTheme(); };
    if (darkQuery.addEventListener) darkQuery.addEventListener('change', onChange); else darkQuery.addListener(onChange);
  }

  // ───────────────────────── Routing ─────────────────────────
  // #/            home
  // #/lesson/<id> lesson
  // #/settings    settings
  // #/start       start page (opened from Settings)

  function route() {
    if (!store.onboarded) return { name: 'welcome', fromSettings: false };
    const parts = location.hash.replace(/^#\/?/, '').split('/');
    if (parts[0] === 'lesson' && parts[1]) return { name: 'lesson', id: decodeURIComponent(parts[1]) };
    if (parts[0] === 'settings') return { name: 'settings' };
    if (parts[0] === 'start') return { name: 'welcome', fromSettings: true };
    return { name: 'home' };
  }

  /** Navigate inside the app. `replace` swaps the current entry (lesson → lesson), like Android which returns Home on Back. */
  function go(hash, { replace = false, dir = 'forward' } = {}) {
    const state = { inApp: true };
    if (replace) history.replaceState(history.state && history.state.inApp ? history.state : null, '', hash);
    else history.pushState(state, '', hash);
    render(dir);
  }

  /** Back arrow: pop the in-app history if we came from inside the app, otherwise go Home. */
  function goBack() {
    if (history.state && history.state.inApp) history.back();
    else go('#/', { replace: true, dir: 'back' });
  }

  window.addEventListener('popstate', () => render('back'));

  // ───────────────────────── Rendering ─────────────────────────

  const app = document.getElementById('app');
  const ui = {
    expanded: 'm1',          // expanded module on Home
    homeScroll: 0,           // restored when returning Home
    lessonId: null,          // lesson currently shown (quiz/challenge state resets when it changes)
    quiz: {},                // quiz key -> picked option index
    solution: {},            // lesson id -> solution shown
    copied: {},              // code key -> true
    codes: [],               // code strings for the copy buttons on the current screen
    current: null,           // name of the screen currently shown
    installPrompt: null,     // beforeinstallprompt event (Android/desktop Chrome)
  };

  function render(dir = 'fade') {
    const r = route();
    if (ui.current === 'home') ui.homeScroll = window.scrollY;
    document.documentElement.lang = HTML_LANG[store.lang];
    document.body.classList.toggle('on-welcome', r.name === 'welcome');
    updateThemeColor();
    closeDialog();

    if (r.name === 'welcome') { renderWelcome(r); ui.current = 'welcome'; return; }

    const course = courses[store.lang];
    if (!course) {
      app.innerHTML = '<div class="loading"><div class="spinner" role="progressbar" aria-label="Loading"></div></div>';
      loadCourse(store.lang).then(() => render(dir)).catch(showLoadError);
      return;
    }

    ui.codes = [];
    if (r.name === 'lesson') {
      if (ui.lessonId !== r.id) { ui.quiz = {}; ui.solution = {}; ui.copied = {}; ui.lessonId = r.id; }
      renderLesson(course, r.id, dir);
      window.scrollTo(0, 0);
    } else if (r.name === 'settings') {
      ui.lessonId = null;
      renderSettings(course, dir);
      window.scrollTo(0, 0);
    } else {
      ui.lessonId = null;
      renderHome(course, ui.current === 'lesson' || ui.current === 'settings' ? 'back' : dir);
      window.scrollTo(0, ui.homeScroll);
    }
    ui.current = r.name;
    updateTopBarShadow();
  }

  function showLoadError() {
    app.innerHTML = `<div class="loading"><div class="page stack gap-12" style="text-align:center">
      <p class="body-large">${esc(t("Couldn't load the lessons. Check your connection and try again.", 'பாடங்களை ஏற்ற முடியவில்லை. இணைப்பைச் சரிபார்த்து மீண்டும் முயலுங்கள்.', 'Lessons load aagala. Connection check panni thirumba try pannunga.', 'పాఠాలు లోడ్ కాలేదు. కనెక్షన్ చెక్ చేసి మళ్ళీ try చేయండి.', 'Lessons load kaaledu. Connection check chesi malli try cheyyandi.'))}</p>
      <button class="btn btn-filled" data-action="reload" style="align-self:center">${esc(t('Try again', 'மீண்டும் முயல்க', 'Thirumba try pannu', 'మళ్ళీ try చెయ్యి', 'Malli try cheyyi'))}</button>
    </div></div>`;
  }

  function screen(html, dir, extraClass = '') {
    app.innerHTML = `<div class="screen ${dir} ${extraClass}">${html}</div>`;
    // Keep the fixed bottom bar outside the animated screen so it stays pinned to the viewport during transitions.
    const bottom = app.querySelector('.screen > .bottom-bar');
    if (bottom) app.appendChild(bottom);
  }

  // ───────────────────────── Home ─────────────────────────

  function renderHome(course, dir) {
    const { modules, lessons } = course;
    const total = lessons.length;
    const doneCount = lessons.filter((l) => store.done.has(l.id)).length;
    const next = lessons.find((l) => !store.done.has(l.id));
    const ctaText = !next
      ? t('Revisit from the start', 'மீண்டும் முதலில் இருந்து', 'Mothalla irundhu thirumba', 'మళ్ళీ మొదటి నుండి', 'Malli modati nundi')
      : doneCount === 0
        ? t('Start learning', 'கற்க ஆரம்பிக்கலாம்', 'Kathukka aarambikkalaam', 'నేర్చుకోవడం మొదలుపెడదాం', 'Nerchukovadam modalupedadam')
        : t('Continue: ', 'தொடர்க: ', 'Continue: ', 'కంటిన్యూ: ', 'Continue: ') + next.title;
    const ctaId = (next || lessons[0]).id;

    const html = `
      <main class="page stack gap-12" style="padding-bottom:calc(32px + var(--safe-bottom))">
        <header class="home-header stack gap-6">
          <div class="row">
            <h1 class="headline-medium grow">Playwright Academy 🎭</h1>
            <button class="icon-btn" data-action="settings" aria-label="${esc(t('Settings', 'அமைப்புகள்', 'Settings', 'సెట్టింగ్స్', 'Settings'))}">${icon('settings')}</button>
          </div>
          <p class="body-large muted" style="margin:0">${esc(t(
            "TypeScript + Playwright for Selenium folks, explained like you're five.",
            'Selenium தெரிந்தவர்களுக்கான TypeScript + Playwright, ஐந்து வயதுக் குழந்தைக்குச் சொல்வது போல.',
            'Selenium theriyum-nu irukkura ungalukku TypeScript + Playwright, oru friend kitta pesura maadhiri.',
            'Selenium తెలిసిన వాళ్ళకి TypeScript + Playwright, ఫ్రెండ్ తో మాట్లాడినట్టు సింపుల్ గా.',
            'Selenium telisina vallaki TypeScript + Playwright, friend tho matladinattu simple ga.',
          ))}</p>
        </header>
        ${installBanner()}
        <section class="hero" aria-label="Progress">
          <div class="row" style="align-items:flex-end">
            <span class="count">${doneCount}</span>
            <span class="title-medium" style="padding-bottom:8px">${esc(t(` / ${total} lessons done`, ` / ${total} பாடங்கள் முடிந்தன`, ` / ${total} lessons mudinjadhu`, ` / ${total} పాఠాలు అయ్యాయి`, ` / ${total} lessons ayyayi`))}</span>
          </div>
          <div class="progress" role="progressbar" aria-valuemin="0" aria-valuemax="${total}" aria-valuenow="${doneCount}"><div style="width:${total ? (doneCount / total) * 100 : 0}%"></div></div>
          <button class="btn btn-filled" data-action="open" data-id="${esc(ctaId)}"><span class="ellipsis">${esc(ctaText)}</span></button>
        </section>
        ${modules.map(moduleCard).join('')}
      </main>`;
    screen(html, dir);
  }

  function moduleCard(module) {
    const accent = accentOf(module);
    const doneIn = module.lessons.filter((l) => store.done.has(l.id)).length;
    const all = doneIn === module.lessons.length;
    const expanded = ui.expanded === module.id;
    const doneLabel = t('Done', 'முடிந்தது', 'Mudinjadhu', 'అయిపోయింది', 'Ayipoyindi');
    const rows = module.lessons.map((l, i) => {
      const done = store.done.has(l.id);
      const mark = done
        ? `<span class="done-check" role="img" aria-label="${esc(doneLabel)}">${icon('checkCircle', 'done-check')}</span>`
        : `<span class="num-dot label-medium" style="background:${rgba(accent, 0.16)};color:${accent}">${i + 1}</span>`;
      return `<button class="lesson-row" data-action="open" data-id="${esc(l.id)}">
          ${mark}
          <span class="body-large grow">${esc(l.title)}</span>
          <span style="font-size:20px;line-height:1" aria-hidden="true">${esc(l.emoji)}</span>
        </button>`;
    }).join('');
    return `
      <section class="card module-card ${expanded ? 'expanded' : ''}" data-module="${esc(module.id)}">
        <button class="module-head" data-action="toggle-module" data-id="${esc(module.id)}" aria-expanded="${expanded}">
          ${emojiBadge(module.emoji, accent)}
          <span class="grow stack">
            <span class="title-medium on-surface">${esc(module.title)}</span>
            <span class="body-medium muted">${esc(module.subtitle)}</span>
          </span>
          <span class="label-medium" style="color:${all ? 'var(--green)' : 'var(--on-surface-variant)'}">${doneIn}/${module.lessons.length}</span>
          ${icon('chevronDown', 'chevron')}
        </button>
        <div class="module-body"><div><div class="inner">${rows}</div></div></div>
      </section>`;
  }

  function toggleModule(id) {
    ui.expanded = ui.expanded === id ? null : id;
    document.querySelectorAll('.module-card').forEach((card) => {
      const on = card.getAttribute('data-module') === ui.expanded;
      card.classList.toggle('expanded', on);
      card.querySelector('.module-head').setAttribute('aria-expanded', String(on));
    });
  }

  // ── "Add to Home Screen" hint ──

  const isStandalone = () => (window.matchMedia && matchMedia('(display-mode: standalone)').matches) || navigator.standalone === true;
  const isIOS = () => /iPad|iPhone|iPod/.test(navigator.userAgent) || (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1);

  function installBanner() {
    if (isStandalone() || storage.get('installDismissed', false)) return '';
    const close = `<button class="icon-btn" data-action="dismiss-install" aria-label="${esc(t('Close', 'மூடு', 'Close', 'మూసివేయి', 'Close'))}" style="width:40px;height:40px;margin-top:-6px">${icon('close', 'icon-18')}</button>`;
    if (isIOS()) {
      return `<aside class="install-banner">
        <span style="font-size:24px;line-height:28px" aria-hidden="true">📲</span>
        <div class="grow stack">
          <span class="title-medium on-surface">${esc(t('Install this app', 'இந்த ஆப்பை நிறுவுங்கள்', 'Indha app-a install pannunga', 'ఈ యాప్‌ను ఇన్‌స్టాల్ చేయండి', 'Ee app ni install cheyyandi'))}</span>
          <span class="body-medium muted">${t(
            'Tap <b>Share</b> SHARE then <b>Add to Home Screen</b>. It works offline after that.',
            '<b>Share</b> SHARE பட்டனைத் தட்டி, <b>Add to Home Screen</b> தேர்ந்தெடுங்கள். அதன் பிறகு இணையம் இல்லாமலும் வேலை செய்யும்.',
            '<b>Share</b> SHARE button-a thottu, <b>Add to Home Screen</b> choose pannunga. Apram internet illaamalum work aagum.',
            '<b>Share</b> SHARE బటన్ నొక్కి, <b>Add to Home Screen</b> ఎంచుకోండి. ఆ తర్వాత ఇంటర్నెట్ లేకపోయినా పని చేస్తుంది.',
            '<b>Share</b> SHARE button nokki, <b>Add to Home Screen</b> enchukondi. Tarvata internet lekapoyinaa pani chestundi.',
          ).replace('SHARE', `<span class="share-glyph">${icon('share', 'icon-18')}</span>`)}</span>
        </div>
        ${close}
      </aside>`;
    }
    if (ui.installPrompt) {
      return `<aside class="install-banner" style="align-items:center">
        <span style="font-size:24px;line-height:28px" aria-hidden="true">📲</span>
        <span class="title-medium on-surface grow">${esc(t('Install this app', 'இந்த ஆப்பை நிறுவுங்கள்', 'Indha app-a install pannunga', 'ఈ యాప్‌ను ఇన్‌స్టాల్ చేయండి', 'Ee app ni install cheyyandi'))}</span>
        <button class="btn btn-filled" data-action="install">${esc(t('Install', 'நிறுவு', 'Install', 'ఇన్‌స్టాల్', 'Install'))}</button>
        ${close}
      </aside>`;
    }
    return '';
  }

  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    ui.installPrompt = e;
    if (ui.current === 'home') render('fade');
  });
  window.addEventListener('appinstalled', () => { ui.installPrompt = null; if (ui.current === 'home') render('fade'); });

  // ───────────────────────── Lesson ─────────────────────────

  function renderLesson(course, id, dir) {
    const { lessons, moduleOf } = course;
    let index = lessons.findIndex((l) => l.id === id);
    if (index < 0) index = 0;
    const lesson = lessons[index];
    const module = moduleOf[lesson.id];
    const accent = accentOf(module);
    const isLast = index === lessons.length - 1;
    const quickCheck = t('🧠 Quick check', '🧠 விரைவுச் சோதனை', '🧠 Quick check', '🧠 క్విక్ చెక్', '🧠 Quick check');

    const html = `
      <header class="top-bar">
        <div class="bar">
          <button class="icon-btn" data-action="back" aria-label="${esc(t('Back', 'பின்செல்', 'Back', 'వెనక్కి', 'Venakki'))}" style="color:var(--on-surface)">${icon('back')}</button>
          <div class="grow stack titles">
            <span class="label-medium" style="color:${accent}">${esc(module.title)}</span>
            <span class="body-medium muted">${esc(t(`Lesson ${index + 1} of ${lessons.length}`, `பாடம் ${index + 1} / ${lessons.length}`, `Lesson ${index + 1} / ${lessons.length}`, `పాఠం ${index + 1} / ${lessons.length}`, `Paatham ${index + 1} / ${lessons.length}`))}</span>
          </div>
          <span id="done-toggle">${doneToggle(lesson.id)}</span>
        </div>
      </header>

      <main class="page lesson-body stack gap-16" style="--accent:${accent}">
        <div class="row gap-14" style="padding:0 4px">
          ${emojiBadge(lesson.emoji, accent, 56)}
          <h1 class="title-large lesson-title grow">${esc(lesson.title)}</h1>
        </div>

        ${lesson.translated ? '' : sectionCard(t('', 'ℹ️ இந்தப் பாடம் இன்னும் தமிழில் இல்லை (ஆங்கிலத்தில் உள்ளது)', 'ℹ️ Indha lesson innum Tamil-la illa (English-la irukku)', 'ℹ️ ఈ పాఠం ఇంకా తెలుగులో లేదు (ఇంగ్లీష్‌లో ఉంది)', 'ℹ️ Ee paatham inka Telugu lo ledu (English lo undi)'), '')}

        ${sectionCard(t('🧒 Story time', '🧒 கதை நேரம்', '🧒 Story time', '🧒 కథల టైమ్', '🧒 Katha time'),
          `<p class="prose body-large">${rich(lesson.story)}</p>`, { bg: rgba(accent, 0.12), bordered: false })}

        ${lesson.javaCode != null ? codeBlock(t('☕ In Selenium you did', '☕ Selenium-இல் நீங்கள் செய்தது', '☕ Selenium-la neenga pannadhu', '☕ Selenium లో మీరు చేసింది', '☕ Selenium lo meeru chesindi'), lesson.javaCode, 'Java', 'var(--on-surface-variant)') : ''}
        ${codeBlock(t('🎭 In Playwright', '🎭 Playwright-இல்', '🎭 Playwright-la', '🎭 Playwright లో', '🎭 Playwright lo'), lesson.code, lesson.codeLang)}
        <p class="caption body-medium muted">${rich(lesson.codeCaption)}</p>

        ${sectionCard(t('📌 Remember', '📌 நினைவில் கொள்க', '📌 Gnabagam vachukkonga', '📌 గుర్తుంచుకోండి', '📌 Gurtunchukondi'),
          `<ul class="points body-large">${lesson.points.map((p) => `<li><span>${rich(p)}</span></li>`).join('')}</ul>`)}

        ${lesson.deep.length ? deepDive(lesson.deep, accent) : ''}

        ${lesson.challenge ? `<div id="challenge">${challengeCard(lesson, accent)}</div>` : ''}

        <div id="quiz-${esc(lesson.id)}0">${quizCard(lesson.quiz, quickCheck, lesson.id + '0')}</div>
        ${lesson.moreQuiz.map((q, i) => `<div id="quiz-${esc(lesson.id)}${i + 1}">${quizCard(q, `${quickCheck} ${i + 2}`, lesson.id + (i + 1))}</div>`).join('')}
      </main>

      <nav class="bottom-bar">
        <div class="bar">
          <button class="btn btn-outlined" data-action="prev" ${index > 0 ? '' : 'disabled'}>${esc(t('Previous', 'முந்தைய', 'Previous', 'వెనక్కి', 'Previous'))}</button>
          <button class="btn btn-filled wide" data-action="next"><span class="ellipsis">${esc(!isLast
            ? t('Done · Next', 'முடிந்தது · அடுத்து', 'Mudinjadhu · Next', 'అయింది · తర్వాత', 'Ayindi · Tarvata')
            : t('Finish 🎉', 'முடித்தேன் 🎉', 'Finish 🎉', 'ముగించాను 🎉', 'Muginchaanu 🎉'))}</span></button>
        </div>
      </nav>`;
    screen(html, dir);
  }

  function doneToggle(id) {
    const done = store.done.has(id);
    return `<button class="btn btn-text" data-action="toggle-done" data-id="${esc(id)}" aria-pressed="${done}">
      ${done ? `<span style="color:var(--green);display:inline-flex">${icon('checkCircle', 'icon-18')}</span>` : ''}
      <span>${esc(done ? t('Done', 'முடிந்தது', 'Mudinjadhu', 'అయిపోయింది', 'Ayipoyindi') : t('Mark done', 'முடிந்தது என குறி', 'Mudinjadhu nu mark pannu', 'అయిపోయింది అని మార్క్ చెయ్యి', 'Ayipoyindi ani mark cheyyi'))}</span>
    </button>`;
  }

  function sectionCard(heading, body, { bg = null, bordered = true } = {}) {
    const style = bg ? ` style="background:${bg}"` : '';
    return `<section class="card section-card ${bordered ? '' : 'borderless'}"${style}>
      <h2 class="title-medium on-surface">${esc(heading)}</h2>
      ${body}
    </section>`;
  }

  function codeBlock(title, code, lang, labelColor = 'var(--primary)', key = null) {
    const i = ui.codes.push(code) - 1;
    const k = key || `${title}|${lang}|${i}`;
    const copied = !!ui.copied[k];
    return `<div class="code-block">
      <div class="code-head">
        <span class="label-medium" style="color:${labelColor}">${esc(title)} · ${esc(lang)}</span>
        <button class="btn btn-text" data-action="copy" data-code="${i}" data-key="${esc(k)}">${esc(copied ? t('Copied ✓', 'நகலெடுக்கப்பட்டது ✓', 'Copy aachu ✓', 'కాపీ అయింది ✓', 'Copy ayindi ✓') : t('Copy', 'நகலெடு', 'Copy', 'కాపీ', 'Copy'))}</button>
      </div>
      <pre><code>${highlight(code, lang)}</code></pre>
    </div>`;
  }

  function deepDive(blocks, accent) {
    const body = blocks.map((b) => {
      switch (b.t) {
        case 'h': return `<h3 class="title-medium deep-h" style="color:${accent}">${esc(b.text)}</h3>`;
        case 'p': return `<p class="prose body-large">${rich(b.text)}</p>`;
        case 'code': return codeBlock(b.title, b.code, b.lang);
        case 'tip': return callout('💡', b.text, 'tip');
        case 'warn': return callout('⚠️', b.text, 'warn');
        case 'table': return table(b);
        default: return '';
      }
    }).join('');
    return sectionCard(t('🔬 Deep dive', '🔬 ஆழ்ந்த பார்வை', '🔬 Deep dive', '🔬 డీప్ డైవ్', '🔬 Deep dive'), `<div class="stack gap-14">${body}</div>`);
  }

  function callout(emoji, text, kind) {
    return `<div class="callout ${kind}"><span class="body-large" aria-hidden="true">${emoji}</span><span class="body-medium on-surface grow">${rich(text)}</span></div>`;
  }

  function table(b) {
    const cols = `grid-template-columns:repeat(${b.header.length}, minmax(0, 1fr))`;
    return `<div class="table-card" role="table">
      <div class="table-row head" role="row" style="${cols}">${b.header.map((h) => `<div class="label-large" role="columnheader">${esc(h)}</div>`).join('')}</div>
      ${b.rows.map((r) => `<div class="table-row" role="row" style="${cols}">${r.map((c) => `<div class="body-medium on-surface" role="cell">${rich(c)}</div>`).join('')}</div>`).join('')}
    </div>`;
  }

  function challengeCard(lesson, accent) {
    const c = lesson.challenge;
    const show = !!ui.solution[lesson.id];
    let body = `<p class="prose body-large">${rich(c.prompt)}</p>
      <p class="prose body-medium muted">${esc(t('Try it yourself in VS Code first. Then peek.', 'முதலில் VS Code-இல் நீங்களே முயலுங்கள். பிறகு பாருங்கள்.', 'First VS Code-la neengale try pannunga. Appuram paarunga.', 'ముందు VS Code లో మీరే try చేయండి. ఆ తర్వాత చూడండి.', 'Mundu VS Code lo meere try cheyyandi. Tarvata chudandi.'))}</p>`;
    if (!show) {
      body += `<button class="btn btn-filled" data-action="solution" data-id="${esc(lesson.id)}" style="align-self:flex-start">${esc(t('Show solution', 'தீர்வைக் காட்டு', 'Solution kaattu', 'Solution చూపించు', 'Solution chupinchu'))}</button>`;
    } else {
      body += codeBlock(t('✅ Solution', '✅ தீர்வு', '✅ Solution', '✅ పరిష్కారం', '✅ Solution'), c.solution, 'TypeScript', 'var(--primary)', 'solution');
      body += `<button class="btn btn-text" data-action="solution" data-id="${esc(lesson.id)}" style="align-self:flex-start">${esc(t('Hide', 'மறை', 'Hide', 'దాచు', 'Daachu'))}</button>`;
    }
    return sectionCard(t('🏋️ Practice challenge', '🏋️ பயிற்சி சவால்', '🏋️ Practice challenge', '🏋️ ప్రాక్టీస్ ఛాలెంజ్', '🏋️ Practice challenge'), body, { bg: rgba(accent, 0.08) });
  }

  function quizCard(quiz, heading, key) {
    const picked = ui.quiz[key];
    const answered = picked !== undefined;
    const opts = quiz.options.map((o, i) => {
      const cls = answered && i === quiz.answer ? 'right' : answered && i === picked ? 'wrong' : '';
      return `<button class="quiz-opt body-large ${cls}" data-action="pick" data-key="${esc(key)}" data-i="${i}" ${answered ? 'disabled' : ''}>${rich(o)}</button>`;
    }).join('');
    let feedback = '';
    if (answered) {
      const right = picked === quiz.answer;
      feedback = `<div class="quiz-feedback" role="status">
        <span class="title-medium ${right ? 'right' : 'wrong'}">${esc(right ? t('Correct! 🎉', 'சரி! 🎉', 'Correct! 🎉', 'కరెక్ట్! 🎉', 'Correct! 🎉') : t('Not quite. 🤔', 'இல்லை. 🤔', 'Illa, thirumba try pannunga. 🤔', 'కాదు, మళ్ళీ try చేయండి. 🤔', 'Kaadu, malli try cheyyandi. 🤔'))}</span>
        <p class="prose body-medium muted">${rich(quiz.why)}</p>
        ${right ? '' : `<button class="btn btn-text" data-action="retry" data-key="${esc(key)}">${esc(t('Try again', 'மீண்டும் முயல்க', 'Thirumba try pannu', 'మళ్ళీ try చెయ్యి', 'Malli try cheyyi'))}</button>`}
      </div>`;
    }
    return sectionCard(heading, `<p class="prose body-large">${rich(quiz.question)}</p>${opts}${feedback}`);
  }

  function rerenderQuiz(key) {
    const course = courses[store.lang];
    const lesson = course.lessons.find((l) => l.id === ui.lessonId);
    if (!lesson) return;
    const idx = Number(key.slice(lesson.id.length));
    const quiz = idx === 0 ? lesson.quiz : lesson.moreQuiz[idx - 1];
    const quickCheck = t('🧠 Quick check', '🧠 விரைவுச் சோதனை', '🧠 Quick check', '🧠 క్విక్ చెక్', '🧠 Quick check');
    const el = document.getElementById('quiz-' + key);
    if (el) el.innerHTML = quizCard(quiz, idx === 0 ? quickCheck : `${quickCheck} ${idx + 1}`, key);
  }

  function rerenderChallenge() {
    const course = courses[store.lang];
    const lesson = course.lessons.find((l) => l.id === ui.lessonId);
    const el = document.getElementById('challenge');
    if (lesson && el) el.innerHTML = challengeCard(lesson, accentOf(course.moduleOf[lesson.id]));
  }

  function lessonNav(delta) {
    const { lessons } = courses[store.lang];
    const index = lessons.findIndex((l) => l.id === ui.lessonId);
    if (delta > 0) {
      store.markDone(ui.lessonId);
      if (index < lessons.length - 1) go('#/lesson/' + encodeURIComponent(lessons[index + 1].id), { replace: true });
      else goBack();
    } else if (index > 0) {
      go('#/lesson/' + encodeURIComponent(lessons[index - 1].id), { replace: true, dir: 'fade' });
    }
  }

  async function copyCode(btn) {
    const code = ui.codes[Number(btn.getAttribute('data-code'))];
    let ok = false;
    try { await navigator.clipboard.writeText(code); ok = true; } catch (e) {
      const ta = document.createElement('textarea');
      ta.value = code; ta.setAttribute('readonly', ''); ta.style.position = 'fixed'; ta.style.opacity = '0';
      document.body.appendChild(ta); ta.select();
      try { ok = document.execCommand('copy'); } catch (e2) { ok = false; }
      ta.remove();
    }
    if (ok) {
      ui.copied[btn.getAttribute('data-key')] = true;
      btn.textContent = t('Copied ✓', 'நகலெடுக்கப்பட்டது ✓', 'Copy aachu ✓', 'కాపీ అయింది ✓', 'Copy ayindi ✓');
    }
  }

  // ───────────────────────── Settings ─────────────────────────

  function renderSettings(course, dir) {
    const done = course.lessons.filter((l) => store.done.has(l.id)).length;
    const total = course.lessons.length;
    const themes = [t('System', 'சிஸ்டம்', 'System', 'సిస్టమ్', 'System'), t('Light', 'வெளிச்சம்', 'Light', 'లైట్', 'Light'), t('Dark', 'இருட்டு', 'Dark', 'డార్క్', 'Dark')];
    const chip = (label, selected, action, v) =>
      `<button class="chip ${selected ? 'selected' : ''}" data-action="${action}" data-v="${v}" aria-pressed="${selected}">${selected ? icon('check', 'icon-18') : ''}${esc(label)}</button>`;

    const html = `
      <header class="top-bar">
        <div class="bar">
          <button class="icon-btn" data-action="back" aria-label="${esc(t('Back', 'பின்செல்', 'Back', 'వెనక్కి', 'Venakki'))}" style="color:var(--on-surface)">${icon('back')}</button>
          <h1 class="title-large" style="margin:0">${esc(t('Settings', 'அமைப்புகள்', 'Settings', 'సెట్టింగ్స్', 'Settings'))}</h1>
        </div>
      </header>
      <main class="page settings-body stack gap-16">
        ${sectionCard(t('🎨 Appearance', '🎨 தோற்றம்', '🎨 Appearance', '🎨 రూపం', '🎨 Appearance'), `
          <p class="prose body-medium muted">${esc(t('Choose how the app looks.', 'ஆப் எப்படி தெரிய வேண்டும் என்பதைத் தேர்வு செய்யுங்கள்.', 'App eppadi theriyanum-nu choose pannunga.', 'యాప్ ఎలా కనిపించాలో ఎంచుకోండి.', 'App ela kanipinchaalo enchukondi.'))}</p>
          <div class="chips">${themes.map((label, i) => chip(label, store.theme === i, 'theme', i)).join('')}</div>`)}

        ${sectionCard(t('🌐 Language', '🌐 மொழி', '🌐 Language', '🌐 భాష', '🌐 Bhasha'), `
          <p class="prose body-medium muted">${esc(t(
            'Everything is translated, including deep dives and quizzes. Code stays in English.',
            'எல்லாமே மொழிபெயர்க்கப்பட்டுள்ளது: ஆழ்ந்த பார்வை, வினாக்கள் உட்பட. Code ஆங்கிலத்திலேயே இருக்கும்.',
            'Ellaamae translate aagirukku: deep dive, quiz ellaam serthu. Code English-la dhaan irukkum.',
            'అంతా తెలుగులోకి మార్చాం: deep dives, quizzes తో సహా. Code ఇంగ్లీష్‌లోనే ఉంటుంది.',
            'Anta Telugu loki marchaam: deep dives, quizzes tho saha. Code English lone untundi.',
          ))}</p>
          <div class="chips">${LANG_NAMES.map((label, i) => chip(label, store.lang === i, 'lang', i)).join('')}</div>
          <button class="btn btn-outlined" data-action="show-start">${esc(t('Show start page', 'தொடக்கப் பக்கத்தைக் காட்டு', 'Start page-a kaattu', 'స్టార్ట్ పేజీ చూపించు', 'Start page choopinchu'))}</button>`)}

        ${sectionCard(t('🧹 Progress', '🧹 முன்னேற்றம்', '🧹 Progress', '🧹 ప్రోగ్రెస్', '🧹 Progress'), `
          <p class="prose body-large">${esc(t(`You have completed ${done} of ${total} lessons.`, `நீங்கள் ${total} பாடங்களில் ${done} முடித்துள்ளீர்கள்.`, `Neenga ${total} lessons-la ${done} mudichirukkeenga.`, `మీరు ${total} పాఠాల్లో ${done} పూర్తి చేశారు.`, `Meeru ${total} lessons lo ${done} complete chesaaru.`))}</p>
          <button class="btn btn-outlined ${done > 0 ? 'btn-danger' : ''}" data-action="reset" ${done > 0 ? '' : 'disabled'}>${esc(t('Reset progress', 'முன்னேற்றத்தை அழி', 'Progress-a reset pannu', 'ప్రోగ్రెస్ రీసెట్ చేయి', 'Progress reset cheyyi'))}</button>
          <p class="prose body-medium muted">${esc(t(
            'Progress is saved on this device only.',
            'முன்னேற்றம் இந்தச் சாதனத்தில் மட்டுமே சேமிக்கப்படும்.',
            'Progress indha device-la mattum dhaan save aagum.',
            'ప్రోగ్రెస్ ఈ డివైస్‌లో మాత్రమే సేవ్ అవుతుంది.',
            'Progress ee device lo maatrame save avutundi.',
          ))}</p>`)}
      </main>`;
    screen(html, dir);
  }

  // ── Reset dialog (custom, so no blocking browser confirm()) ──

  let lastFocus = null;
  function openResetDialog() {
    lastFocus = document.activeElement;
    const scrim = document.createElement('div');
    scrim.className = 'scrim';
    scrim.setAttribute('data-action', 'dialog-cancel');
    scrim.innerHTML = `<div class="dialog" role="alertdialog" aria-modal="true" aria-labelledby="dlg-title" aria-describedby="dlg-text" data-stop>
      <h2 id="dlg-title">${esc(t('Reset all progress?', 'எல்லா முன்னேற்றத்தையும் அழிக்கவா?', 'Ellaa progress-um reset pannalaamaa?', 'ప్రోగ్రెస్ అంతా రీసెట్ చేయాలా?', 'Progress anta reset cheyyaalaa?'))}</h2>
      <p id="dlg-text">${esc(t("All lessons will be marked as not done. This can't be undone.", 'எல்லா பாடங்களும் முடிக்கப்படாதவை என குறிக்கப்படும். இதைத் திரும்பப் பெற முடியாது.', 'Ellaa lessons-um mudikkala-nu mark aagum. Ithai thirumba edukka mudiyaadhu.', 'అన్ని పాఠాలు పూర్తి కానివిగా మార్క్ అవుతాయి. దీన్ని వెనక్కి తీసుకోలేరు.', 'Anni paatalu complete kaanivigaa mark avutaayi. Deenni venakki teesukoleru.'))}</p>
      <div class="actions">
        <button class="btn btn-text" data-action="dialog-cancel">${esc(t('Cancel', 'ரத்து', 'Cancel', 'రద్దు', 'Raddu'))}</button>
        <button class="btn btn-text" data-action="dialog-reset" style="color:var(--red)">${esc(t('Reset', 'அழி', 'Reset', 'రీసెట్', 'Reset'))}</button>
      </div>
    </div>`;
    document.body.appendChild(scrim);
    scrim.querySelector('[data-action="dialog-cancel"].btn').focus();
  }
  function closeDialog() {
    const s = document.querySelector('.scrim');
    if (s) { s.remove(); if (lastFocus && document.contains(lastFocus)) lastFocus.focus(); }
  }

  // ───────────────────────── Welcome (start page) ─────────────────────────

  function renderWelcome(r) {
    const existing = app.querySelector('.welcome');
    if (existing && ui.current === 'welcome') { updateWelcomeText(true); return; }

    const cards = LANG_OPTIONS.map((o, i) => `
      <div class="reveal" style="--i:${4 + i}">
        <button class="lang-card ${store.lang === i ? 'selected' : ''}" data-action="welcome-lang" data-v="${i}" aria-pressed="${store.lang === i}">
          <span class="lang-glyph">${esc(o.glyph)}</span>
          <span class="lang-text"><span class="n" style="display:block">${esc(o.name)}</span><span class="s" style="display:block">${esc(o.sub)}</span></span>
          <span class="lang-check">${icon('check', 'icon-18')}</span>
        </button>
      </div>`).join('');

    app.innerHTML = `
      <div class="welcome">
        <div class="aurora" aria-hidden="true"><i class="b1"></i><i class="b2"></i><i class="b3"></i></div>
        <div class="welcome-scroll">
          <div class="reveal" style="--i:0">
            <div class="logo-wrap">
              <div class="logo-halo"></div>
              <div class="logo-ring"></div>
              <div class="logo-disc"><img src="icons/logo.svg" alt="Playwright Academy logo" width="170" height="170"></div>
            </div>
          </div>
          <div class="reveal brand" style="--i:1">
            <div class="name">Playwright</div>
            <div class="academy">ACADEMY</div>
          </div>
          <div class="reveal" style="--i:2"><p class="tagline" data-w="tagline" style="margin-bottom:0"></p></div>
          <div class="lang-list">
            <div class="reveal" style="--i:3"><h1 class="lang-heading" data-w="choose" style="margin:0"></h1></div>
            <div class="stack gap-10" role="radiogroup" aria-label="Language">${cards}</div>
            <div class="reveal" style="--i:9"><p class="lang-note" data-w="note" style="margin:0"></p></div>
          </div>
        </div>
        <div class="welcome-foot">
          <div class="reveal" style="--i:9">
            <button class="start-btn" data-action="welcome-start"><span data-w="start"></span>${icon('forward')}</button>
          </div>
        </div>
      </div>`;
    updateWelcomeText(false);
  }

  function updateWelcomeText(animate) {
    const texts = {
      tagline: t(
        "TypeScript + Playwright for Selenium folks, explained like you're five.",
        'Selenium தெரிந்தவர்களுக்கான TypeScript + Playwright, எளிமையாக.',
        'Selenium theriyum-nu irukkura ungalukku TypeScript + Playwright, simple-aa.',
        'Selenium తెలిసిన వాళ్ళకి TypeScript + Playwright, సింపుల్ గా.',
        'Selenium telisina vallaki TypeScript + Playwright, simple ga.',
      ),
      choose: t('Choose your language', 'உங்கள் மொழியைத் தேர்ந்தெடுங்கள்', 'Unga language-a choose pannunga', 'మీ భాషను ఎంచుకోండి', 'Mee bhaashanu enchukondi'),
      note: t('You can change this any time in Settings.', 'இதை எப்போது வேண்டுமானாலும் அமைப்புகளில் மாற்றலாம்.', 'Ithai eppo venaalum Settings-la maathalaam.', 'దీన్ని ఎప్పుడైనా సెట్టింగ్స్‌లో మార్చుకోవచ్చు.', 'Deenni eppudainaa Settings lo maarchukovachu.'),
      start: t("Let's start", 'தொடங்குவோம்', 'Start pannalaam', 'మొదలుపెడదాం', 'Modalupedadaam'),
    };
    Object.keys(texts).forEach((k) => {
      const el = app.querySelector(`[data-w="${k}"]`);
      if (!el || el.textContent === texts[k]) return;
      el.textContent = texts[k];
      if (animate) { el.classList.remove('lang-fade'); void el.offsetWidth; el.classList.add('lang-fade'); }
    });
    app.querySelectorAll('.lang-card').forEach((card) => {
      const on = Number(card.getAttribute('data-v')) === store.lang;
      card.classList.toggle('selected', on);
      card.setAttribute('aria-pressed', String(on));
    });
  }

  function welcomeStart() {
    const firstTime = !store.onboarded;
    store.finishOnboarding();
    if (firstTime) go('#/', { replace: true, dir: 'fade' });
    else goBack();
  }

  // ───────────────────────── Events ─────────────────────────

  document.addEventListener('click', (e) => {
    const el = e.target.closest('[data-action]');
    if (!el) return;
    // Clicks inside the dialog box shouldn't count as clicks on the scrim behind it.
    if (el.classList.contains('scrim') && e.target.closest('[data-stop]')) return;
    const id = el.getAttribute('data-id');
    const v = Number(el.getAttribute('data-v'));
    switch (el.getAttribute('data-action')) {
      case 'open': go('#/lesson/' + encodeURIComponent(id)); break;
      case 'toggle-module': toggleModule(id); break;
      case 'settings': go('#/settings'); break;
      case 'back': goBack(); break;
      case 'toggle-done':
        store.toggle(id);
        document.getElementById('done-toggle').innerHTML = doneToggle(id);
        break;
      case 'prev': lessonNav(-1); break;
      case 'next': lessonNav(1); break;
      case 'pick': {
        const key = el.getAttribute('data-key');
        ui.quiz[key] = Number(el.getAttribute('data-i'));
        rerenderQuiz(key);
        break;
      }
      case 'retry': {
        const key = el.getAttribute('data-key');
        delete ui.quiz[key];
        rerenderQuiz(key);
        break;
      }
      case 'solution': ui.solution[id] = !ui.solution[id]; delete ui.copied.solution; rerenderChallenge(); break;
      case 'copy': copyCode(el); break;
      case 'theme': store.setTheme(v); applyTheme(); render('fade'); break;
      case 'lang':
        store.chooseLanguage(v);
        loadCourse(v).then(() => render('fade')).catch(showLoadError);
        break;
      case 'show-start': go('#/start'); break;
      case 'reset': openResetDialog(); break;
      case 'dialog-cancel': closeDialog(); break;
      case 'dialog-reset': store.resetProgress(); closeDialog(); render('fade'); break;
      case 'welcome-lang':
        store.chooseLanguage(v);
        updateWelcomeText(true);
        loadCourse(v).catch(() => {});
        break;
      case 'welcome-start': welcomeStart(); break;
      case 'dismiss-install': storage.set('installDismissed', true); render('fade'); break;
      case 'install':
        if (ui.installPrompt) {
          ui.installPrompt.prompt();
          ui.installPrompt.userChoice.finally(() => { ui.installPrompt = null; render('fade'); });
        }
        break;
      case 'reload': render('fade'); break;
      default: break;
    }
  });

  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && document.querySelector('.scrim')) closeDialog();
  });

  function updateTopBarShadow() {
    const bar = document.querySelector('.top-bar');
    if (bar) bar.classList.toggle('scrolled', window.scrollY > 2);
  }
  window.addEventListener('scroll', updateTopBarShadow, { passive: true });

  // ───────────────────────── Start ─────────────────────────

  applyTheme();
  render('fade');
  loadCourse(store.lang).catch(() => {});

  // Offline support. Skipped on localhost (unless ?sw is in the URL) so local edits show up immediately.
  const local = /^(localhost|127\.0\.0\.1|\[::1\])$/.test(location.hostname);
  if ('serviceWorker' in navigator && (!local || /[?&]sw\b/.test(location.search))) {
    window.addEventListener('load', () => { navigator.serviceWorker.register('sw.js').catch(() => {}); });
  }
})();
