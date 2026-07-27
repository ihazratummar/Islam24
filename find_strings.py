import os
import re

search_dir = "/Volumes/SSD/Coding/Android/Main Project/islam24/app/Islam24"
patterns = [
    r'Text\s*\(\s*"([^"]+)"',
    r'text\s*=\s*"([^"]+)"',
    r'title\s*=\s*\{?\s*Text\s*\(\s*"([^"]+)"',
    r'label\s*=\s*\{?\s*Text\s*\(\s*"([^"]+)"',
    r'contentDescription\s*=\s*"([^"]+)"',
    r'Toast\.makeText\([^,]+,\s*"([^"]+)"',
]

results = []

for root, _, files in os.walk(search_dir):
    for file in files:
        if file.endswith(".kt"):
            filepath = os.path.join(root, file)
            try:
                with open(filepath, 'r') as f:
                    content = f.read()
                    
                for pattern in patterns:
                    matches = re.finditer(pattern, content)
                    for match in matches:
                        string_val = match.group(1)
                        if len(string_val) > 1 and not string_val.isspace(): # Ignore empty or just spaces
                            results.append((filepath, string_val))
            except:
                pass

print(f"Found {len(results)} potential hardcoded strings:")
# Deduplicate
unique_strings = set([s for _, s in results])
for s in sorted(list(unique_strings)):
    print(f'- "{s}"')

