import sqlite3
import requests
import os

DB_NAME = "quran_prepopulated.db"

# Remove the old database if it already exists
if os.path.exists(DB_NAME):
    os.remove(DB_NAME)

print(f"Creating database {DB_NAME}...")
conn = sqlite3.connect(DB_NAME)
cursor = conn.cursor()

# 1. Create the Surah table exactly matching SurahEntity
cursor.execute('''
CREATE TABLE surah (
    surahNumber INTEGER PRIMARY KEY NOT NULL,
    nameArabic TEXT NOT NULL,
    nameEnglish TEXT NOT NULL,
    nameTransliterated TEXT NOT NULL,
    type TEXT NOT NULL,
    totalAyahs INTEGER NOT NULL
)
''')

# 2. Create the Ayah table exactly matching AyahEntity
cursor.execute('''
CREATE TABLE ayah (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    surahNumber INTEGER NOT NULL,
    ayahNumber INTEGER NOT NULL,
    globalAyahNumber INTEGER NOT NULL,
    arabicText TEXT NOT NULL,
    englishTranslation TEXT NOT NULL,
    transliteration TEXT NOT NULL,
    isBookmarked INTEGER NOT NULL DEFAULT 0
)
''')

# 3. Create the Indices matching your Room Entity indices
cursor.execute('CREATE INDEX index_ayah_surahNumber ON ayah (surahNumber)')
cursor.execute('CREATE UNIQUE INDEX index_ayah_globalAyahNumber ON ayah (globalAyahNumber)')

# 4. Fetch the data from AlQuran Cloud APIs
print("Downloading Arabic text (quran-uthmani)...")
ar_data = requests.get("https://api.alquran.cloud/v1/quran/quran-uthmani").json()['data']['surahs']

print("Downloading English translation (en.asad)...")
en_data = requests.get("https://api.alquran.cloud/v1/quran/en.asad").json()['data']['surahs']

print("Downloading Transliteration (en.transliteration)...")
tr_data = requests.get("https://api.alquran.cloud/v1/quran/en.transliteration").json()['data']['surahs']

print("Populating database... Please wait a few seconds.")
# 5. Loop through all 114 Surahs
for i in range(114):
    ar_surah = ar_data[i]
    en_surah = en_data[i]
    tr_surah = tr_data[i]
    
    surah_num = ar_surah['number']
    
    # Insert the Surah data
    cursor.execute('''
        INSERT INTO surah (surahNumber, nameArabic, nameEnglish, nameTransliterated, type, totalAyahs)
        VALUES (?, ?, ?, ?, ?, ?)
    ''', (
        surah_num,
        ar_surah['name'],
        ar_surah['englishNameTranslation'], # e.g. "The Opening"
        ar_surah['englishName'],            # e.g. "Al-Faatiha"
        ar_surah['revelationType'],
        len(ar_surah['ayahs'])
    ))
    
    # Insert all Ayahs for this Surah
    for j in range(len(ar_surah['ayahs'])):
        ar_ayah = ar_surah['ayahs'][j]
        en_ayah = en_surah['ayahs'][j]
        tr_ayah = tr_surah['ayahs'][j]
        
        cursor.execute('''
            INSERT INTO ayah (
                surahNumber, 
                ayahNumber, 
                globalAyahNumber, 
                arabicText, 
                englishTranslation, 
                transliteration, 
                isBookmarked
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        ''', (
            surah_num,
            ar_ayah['numberInSurah'],
            ar_ayah['number'],
            ar_ayah['text'],
            en_ayah['text'],
            tr_ayah['text'],
            0 # 0 in SQLite means false for isBookmarked
        ))

# Commit the changes and close the database connection
conn.commit()
conn.close()

print(f"Success! {DB_NAME} has been generated perfectly.")
print("Next steps:")
print("1. Copy 'quran_prepopulated.db' into your core:database module under: src/main/assets/databases/")
print("2. Use Room's .createFromAsset(\"databases/quran_prepopulated.db\") in your Database Builder.")
