// Service worker: caches the whole app (shell + all 5 languages of content) so it works fully offline.
// BUILD_ID is replaced with the commit hash by the GitHub Pages workflow, so every deploy refreshes the cache.
const BUILD_ID = '__BUILD__';
const CACHE = 'pa-' + BUILD_ID;

const FILES = [
  './',
  'index.html',
  'styles.css',
  'app.js',
  'manifest.webmanifest',
  'icons/logo.svg',
  'icons/icon.svg',
  'icons/icon-192.png',
  'icons/icon-512.png',
  'icons/apple-touch-icon.png',
  'icons/maskable-512.png',
  'data/course-0.json',
  'data/course-1.json',
  'data/course-2.json',
  'data/course-3.json',
  'data/course-4.json',
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE)
      .then((cache) => cache.addAll(FILES.map((f) => new Request(f, { cache: 'reload' }))))
      .then(() => self.skipWaiting()),
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys()
      .then((keys) => Promise.all(keys.filter((k) => k.startsWith('pa-') && k !== CACHE).map((k) => caches.delete(k))))
      .then(() => self.clients.claim()),
  );
});

self.addEventListener('fetch', (event) => {
  const req = event.request;
  if (req.method !== 'GET' || new URL(req.url).origin !== self.location.origin) return;
  // Cache first (ignoring ?query and #hash); fall back to the network, and to the app shell for page loads.
  event.respondWith(
    caches.match(req, { ignoreSearch: true }).then((hit) => hit || fetch(req).catch(() => {
      if (req.mode === 'navigate') return caches.match('index.html');
      return Response.error();
    })),
  );
});
