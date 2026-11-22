// Builds the PWA icons from web/icons/logo.svg (the Android launcher foreground, converted to SVG).
// Writes icon.svg (favicon), and renders PNGs with headless Microsoft Edge / Chrome.
// Usage: node web/tools/make-icons.mjs   (set BROWSER=path\to\chrome.exe to override)
import { readFileSync, writeFileSync, mkdtempSync, rmSync } from 'node:fs';
import { execFileSync } from 'node:child_process';
import { tmpdir } from 'node:os';
import { join, dirname } from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';

const icons = join(dirname(fileURLToPath(import.meta.url)), '..', 'icons');
const logo = readFileSync(join(icons, 'logo.svg'), 'utf8');
const inner = logo.slice(logo.indexOf('>', logo.indexOf('<svg')) + 1, logo.lastIndexOf('</svg>'));

// viewBox crops the 108x108 adaptive-icon canvas: 72 = what Android launchers show; 84 keeps the masks inside the maskable safe zone.
const svg = (box) => `<svg xmlns="http://www.w3.org/2000/svg" viewBox="${(108 - box) / 2} ${(108 - box) / 2} ${box} ${box}">
  <rect width="108" height="108" fill="#1A73E8"/>${inner}</svg>
`;
writeFileSync(join(icons, 'icon.svg'), svg(72));

const browser = process.env.BROWSER || 'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe';
const tmp = mkdtempSync(join(tmpdir(), 'pa-icons-'));
const targets = [
  ['icon-192.png', 192, 72],
  ['icon-512.png', 512, 72],
  ['apple-touch-icon.png', 180, 72],
  ['maskable-512.png', 512, 84],
];
try {
  for (const [name, size, box] of targets) {
    const html = join(tmp, `${name}.html`);
    writeFileSync(html, `<!doctype html><html><body style="margin:0;background:#1A73E8">${svg(box).replace('<svg ', `<svg width="${size}" height="${size}" style="display:block" `)}</body></html>`);
    execFileSync(browser, [
      '--headless', '--disable-gpu', '--hide-scrollbars', '--force-device-scale-factor=1',
      `--window-size=${size},${size}`, `--screenshot=${join(icons, name)}`, pathToFileURL(html).href,
    ], { stdio: 'ignore' });
    console.log('wrote', name);
  }
} finally {
  rmSync(tmp, { recursive: true, force: true });
}
