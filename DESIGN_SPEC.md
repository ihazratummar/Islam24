# Islam 24 — Complete Design Specification
## Screen-by-Screen Color & Typography Reference

---

## 1. GLOBAL DESIGN TOKENS

### 1.1 Color Palette

| Token | Light Mode | Dark Mode | Usage |
| :--- | :--- | :--- | :--- |
| **bg-page** | gray-50 (#F9FAFB) | surface-dark (#0F1B1E) | Page background |
| **bg-card** | white (#FFFFFF) | surface-cardDark (#1A2E32) | Card/container background |
| **bg-card-secondary** | surface-cardLight (#F8FAF9) | surface-cardDark (#1A2E32) | Subtle card backgrounds |
| **bg-hero** | primary-600 (#0D7377) | primary-700 (#0A5A5E) | Hero banners, CTA headers |
| **text-primary** | gray-900 (#111827) | white (#FFFFFF) | Primary headings, names |
| **text-secondary** | gray-500 (#6B7280) | gray-400 (#9CA3AF) | Descriptions, captions |
| **text-tertiary** | gray-400 (#9CA3AF) | gray-500 (#6B7280) | Timestamps, hints, meta |
| **text-inverse** | white | white | Text on dark/hero backgrounds |
| **text-brand** | primary-600 (#0D7377) | primary-400 (#26A490) | Accent text, links, numbers |
| **text-gold** | accent-500 (#D4AF37) | accent-400 (#EDC848) | Premium badges, Kaaba indicators |
| **border-subtle** | gray-100 (#F3F4F6) | white/5 (rgba(255,255,255,0.05)) | Card dividers, separators |
| **border-input** | gray-200 (#E5E7EB) | white/10 (rgba(255,255,255,0.10)) | Input field borders |
| **shadow-card** | shadow-sm | none | Card elevation |
| **bg-blur-hero** | white/15 + backdrop-blur-sm | white/10 + backdrop-blur-sm | Glassmorphism overlays |

### 1.2 Semantic Accent Colors (Used Inline)

| Color | Light | Dark | Purpose |
| :--- | :--- | :--- | :--- |
| **Success** | emerald-500 (#10B981) | emerald-500 | Completed prayers, checkmarks, valid states |
| **Error / Destructive** | rose-500 (#F43F5E) | rose-500 | Logout, danger actions |
| **Warning** | amber-500 (#F59E0B) | amber-500 | Friday highlights, special events |
| **Orange** | orange-500 (#F97316) | orange-500 | Streak indicators, fire icons |

### 1.3 Typography Scale

| Token | Font Family | Weight | Size | Line Height | Letter Spacing | Usage |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **display-xl** | Inter | 700 (Bold) | 36px | 44px | 0 | Hero prayer name (e.g., “Maghrib”) |
| **display-lg** | Inter | 700 (Bold) | 32px | 40px | 0 | Page titles (Calendar, Quran) |
| **display-md** | Inter | 700 (Bold) | 24px | 32px | 0 | Section prices, stats |
| **heading-lg** | Inter | 600 (SemiBold) | 22px | 28px | 0 | Card titles, modal headers |
| **heading-md** | Inter | 600 (SemiBold) | 18px | 24px | 0 | Surah names, item labels |
| **heading-sm** | Inter | 600 (SemiBold) | 16px | 20px | 0.15px | List item titles, button text |
| **body-lg** | Inter | 400 (Regular) | 16px | 24px | 0.5px | Primary body text, translations |
| **body-md** | Inter | 400 (Regular) | 14px | 20px | 0.25px | Standard body text, descriptions |
| **body-sm** | Inter | 400 (Regular) | 12px | 16px | 0.4px | Captions, timestamps, hints |
| **body-xs** | Inter | 400 (Regular) | 11px | 16px | 0.4px | Meta labels, badge text, micro-copy |
| **body-2xs** | Inter | 500 (Medium) | 10px | 12px | 0.5px | Calendar dots, progress labels, day abbreviations |
| **label-md** | Inter | 500 (Medium) | 14px | 20px | 0.1px | Button labels, tab text |
| **label-sm** | Inter | 500 (Medium) | 12px | 16px | 0.5px | Category pills, tag text |
| **label-xs** | Inter | 500 (Medium) | 11px | 16px | 0.5px | Uppercase section headers (e.g., “HIJRI DATE”) |
| **arabic-lg** | Amiri | 400 | 24px | 36px | 0 | Large Quranic verses, Mushaf view |
| **arabic-md** | Amiri | 400 | 20px | 32px | 0 | Standard Arabic verses |
| **arabic-sm** | Amiri | 400 | 18px | 28px | 0 | Inline Arabic, Bismillah |
| **arabic-xs** | Amiri | 400 | 16px | 24px | 0 | Compact Arabic, names in lists |
| **arabic-2xs** | Amiri | 400 | 14px | 20px | 0 | Surah names in cards, mini labels |
| **counter-xl** | Inter | 700 | 48px | 56px | -0.5px | Tasbih/Dhikr main counter |
| **counter-lg** | Inter | 700 | 36px | 44px | -0.5px | Timer digits, countdown |
| **counter-md** | Inter | 700 | 24px | 32px | 0 | Percentage stats, streak counts |

### 1.4 Arabic Font Variants (Surah Detail Only)

| Font | Class | Usage |
| :--- | :--- | :--- |
| **Amiri** | font-arabic | Default classical Quranic text |
| **Scheherazade New** | font-scheherazade | Traditional Naskh, larger body |
| **Noto Naskh Arabic** | font-notonaskh | IndoPak / compact Hifz style |
| **Uthmani** | font-uthmani | Medina Mushaf script |

### 1.5 Border Radius Tokens

| Token | Value | Usage |
| :--- | :--- | :--- |
| **radius-sm** | 8px | Small buttons, toggles |
| **radius-md** | 12px | Input fields, day cells |
| **radius-lg** | 16px | Cards, containers |
| **radius-xl** | 20px | Hero cards, modals |
| **radius-2xl** | 24px | Bottom sheets, large modals |
| **radius-full** | 9999px | Avatars, circular progress |

### 1.6 Shadow Tokens

| Token | Value | Usage |
| :--- | :--- | :--- |
| **shadow-none** | none | Dark mode cards |
| **shadow-sm** | 0 1px 2px 0 rgba(0,0,0,0.05) | Light mode cards, inputs |
| **shadow-md** | 0 4px 6px -1px rgba(0,0,0,0.1) | Active tabs, floating elements |
| **shadow-lg** | 0 10px 15px -3px rgba(0,0,0,0.1) | Primary buttons, hero overlays |
| **shadow-primary** | shadow-lg shadow-primary-500/25 | Primary CTA glow |

---

## 2. SCREEN-BY-SCREEN SPECIFICATIONS

### 2.1 HOME SCREEN (/)

| Element | Background | Text Style | Text Color | Icon Color | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Page Background** | bg-page | — | — | — | Full screen |
| **Status Bar Area** | Transparent | — | — | — | Inherits page bg |
| **Header Container** | Transparent | — | — | — | px-5 pt-6 pb-3 |
| **App Logo Container** | primary-500/10 (light) / primary-500/20 (dark) | — | — | — | 40x40px, radius-lg |
| **App Logo Image** | — | — | — | — | 28x28px, contain |
| **App Name** | — | heading-md | text-primary | — | “Islam 24” |
| **App Tagline** | — | body-xs | text-secondary | — | “Your Daily Companion” |
| **Location Pill** | primary-500/10 | body-2xs weight 500 | primary-600 (light) / primary-400 (dark) | — | radius-full, uppercase city |
| **Hero Card (Prayer)** | bg-hero | — | — | — | radius-xl, relative, overflow hidden |
| **Hero Background Blur Circle 1** | accent-400/20 | — | — | — | Top-right decorative blur |
| **Hero Background Blur Circle 2** | primary-400/20 | — | — | — | Bottom-left decorative blur |
| **“Next Prayer” Label** | — | body-2xs weight 500 | primary-100 | — | Uppercase tracking |
| **Prayer Name (Hero)** | — | display-xl | text-inverse | — | White, e.g., “Asr” |
| **Prayer Icon Circle** | bg-blur-hero | — | — | — | 48x48px |
| **Prayer Icon** | — | — | — | accent-300 | Sun/moon icon, 24px |
| **“Starts at” Label** | — | body-2xs | primary-200 | — | — |
| **Prayer Time** | — | body-lg weight 600 | text-inverse | — | White, e.g., “4:32 PM” |
| **“Time Remaining” Label** | — | body-2xs uppercase | primary-200 | — | Tracking wider |
| **Countdown Box** | bg-blur-hero | counter-lg | text-inverse | — | 36x36px min, radius-lg |
| **Countdown Colon** | — | body-sm | white/70 | — | Between digits |
| **Date Row Container** | Flex row, gap-3 | — | — | — | Two cards side by side |
| **Hijri Date Card** | bg-card | — | — | — | radius-lg, shadow-card (light) |
| **Hijri Icon** | — | — | — | primary-500 | Calendar icon, 14px |
| **“Hijri Date” Label** | — | body-2xs uppercase | text-secondary | — | Tracking wider |
| **Hijri Day/Month** | — | body-md weight 600 | text-primary | — | e.g., “13 Ramadan” |
| **Hijri Year** | — | body-xs | text-secondary | — | e.g., “1445 AH” |
| **Ramadan Card** | bg-card | — | — | — | Same structure as Hijri |
| **Ramadan Icon** | — | — | — | accent-500 | Moon icon |
| **“Ramadan” Label** | — | body-2xs uppercase | text-secondary | — | — |
| **Days Count** | — | body-md weight 600 | text-primary | — | e.g., “12 Days” |
| **Ramadan Target** | — | body-xs | text-secondary | — | “Until Eid al-Fitr” |
| **Prayer Streak Card** | bg-card | — | — | — | radius-xl |
| **Streak Icon Container** | orange-500/10 | — | — | — | 32x32px, radius-md |
| **Streak Icon** | — | — | — | orange-500 | Fire icon |
| **“Prayer Streak” Title** | — | heading-sm | text-primary | — | — |
| **Streak Subtitle** | — | body-xs | text-secondary | — | “3 of 5 prayers today” |
| **Percentage Text** | — | counter-md | orange-500 | — | e.g., “60%” |
| **Percentage Label** | — | body-2xs | text-secondary | — | “completed” |
| **Progress Bar Background** | gray-200 (light) / white/10 (dark) | — | — | — | 10px height, radius-full |
| **Progress Bar Fill** | emerald-500 | — | — | — | Animated width |
| **Prayer Abbreviation** | — | body-2xs uppercase | gray-400 (light) / gray-500 (dark) | — | “FAJ”, “DHU”, etc. |
| **“Quick Access” Section Title** | — | heading-sm | text-primary | — | — |
| **Quick Action Button** | Transparent (touch target) | — | — | — | grid-cols-3 |
| **Quick Action Icon Circle** | {color}/15 (e.g., primary-500/15) | — | — | — | 56x56px, radius-xl |
| **Quick Action Icon** | — | — | — | {color} (e.g., primary-500) | 20px Remix Icon |
| **Quick Action Label** | — | body-xs weight 500 | gray-700 (light) / gray-300 (dark) | — | Centered below icon |
| **“Continue Reading” Title** | — | heading-sm | text-primary | — | — |
| **“View All” Link** | — | body-xs weight 500 | primary-500 | — | Right-aligned |
| **Reading Card** | bg-card | — | — | — | 144px wide, horizontal scroll |
| **Surah Name (Reading)** | — | body-sm weight 700 | primary-600 (light) / primary-400 (dark) | — | — |
| **Arabic Name (Reading)** | — | arabic-2xs | primary-500 | — | Right-aligned in card |
| **Verse Label** | — | body-2xs | text-secondary | — | “Verse 12” |
