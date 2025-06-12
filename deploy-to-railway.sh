#!/bin/bash

# Railway Deployment Script for Midjourney Proxy
# This script helps you deploy the Midjourney Proxy to Railway

set -e

echo "🚀 Midjourney Proxy - Railway Deployment Script"
echo "================================================"

# Check if Railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo "❌ Railway CLI is not installed."
    echo "📦 Installing Railway CLI..."
    
    # Install Railway CLI based on OS
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        curl -fsSL https://railway.app/install.sh | sh
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        brew install railway
    elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "win32" ]]; then
        echo "Please install Railway CLI manually from: https://docs.railway.app/develop/cli"
        exit 1
    fi
fi

echo "✅ Railway CLI is available"

# Login to Railway
echo "🔐 Logging into Railway..."
railway login

# Create new project or link existing
echo "📁 Setting up Railway project..."
if [ ! -f ".railway" ]; then
    echo "Creating new Railway project..."
    railway init
else
    echo "Using existing Railway project..."
fi

# Set environment variables
echo "⚙️  Setting up environment variables..."
echo "Please provide the following information:"

read -p "Discord Guild ID: " DISCORD_GUILD_ID
read -p "Discord Channel ID: " DISCORD_CHANNEL_ID
read -s -p "Discord User Token: " DISCORD_USER_TOKEN
echo
read -p "API Secret Key: " MJ_API_SECRET

# Set variables in Railway
railway variables set DISCORD_GUILD_ID="$DISCORD_GUILD_ID"
railway variables set DISCORD_CHANNEL_ID="$DISCORD_CHANNEL_ID"
railway variables set DISCORD_USER_TOKEN="$DISCORD_USER_TOKEN"
railway variables set MJ_API_SECRET="$MJ_API_SECRET"
railway variables set SPRING_PROFILES_ACTIVE="railway"

# Optional variables
echo "Setting optional variables with defaults..."
railway variables set MJ_CORE_SIZE="3"
railway variables set MJ_QUEUE_SIZE="10"
railway variables set MJ_TIMEOUT_MINUTES="5"
railway variables set MJ_TASK_STORE_TYPE="in_memory"
railway variables set MJ_TRANSLATE_WAY="null"
railway variables set MJ_NOTIFY_POOL_SIZE="10"

# Deploy
echo "🚀 Deploying to Railway..."
railway up

echo "✅ Deployment initiated!"
echo "📊 You can monitor the deployment at: https://railway.app/dashboard"
echo "🔗 Your app will be available at the URL shown in the Railway dashboard"
echo ""
echo "📚 Once deployed, you can access:"
echo "   - Health check: https://your-app.railway.app/health"
echo "   - API docs: https://your-app.railway.app/doc.html"
echo ""
echo "🎉 Happy generating with Midjourney!" 