#!/bin/bash

# Navigate to script directory just in case it's run from elsewhere
cd "$(dirname "$0")"

if [ ! -f "local.properties" ]; then
    echo "Error: local.properties not found!"
    exit 1
fi

# Extract variables from local.properties
PROJECT_ID=$(grep '^crowdin\.project\.id=' local.properties | cut -d'=' -f2 | tr -d '\r')
API_TOKEN=$(grep '^crowdin\.api\.token=' local.properties | cut -d'=' -f2 | tr -d '\r')

if [ -z "$PROJECT_ID" ] || [ -z "$API_TOKEN" ]; then
    echo "Error: Please add crowdin.project.id and crowdin.api.token to your local.properties file."
    exit 1
fi

# Export for Crowdin CLI to use
export CROWDIN_PROJECT_ID="$PROJECT_ID"
export CROWDIN_PERSONAL_TOKEN="$API_TOKEN"

echo "======================================"
echo " Syncing String Resources with Crowdin "
echo "======================================"

echo "Uploading new source strings to Crowdin..."
crowdin upload sources

echo "Uploading local Bengali translations to Crowdin..."
crowdin upload translations

echo "Downloading translated strings from Crowdin..."
crowdin download

echo "======================================"
echo " Sync Complete! "
echo "======================================"
