# Midjourney Proxy - Railway Deployment Guide

This is a Railway-optimized version of the Midjourney Proxy that allows you to deploy the service easily on Railway.app.

## 🚀 Quick Deploy to Railway

[![Deploy on Railway](https://railway.app/button.svg)](https://railway.app/template/midjourney-proxy)

## 📋 Prerequisites

1. **Discord Account with Midjourney Subscription**
   - Active Midjourney subscription
   - Your own Discord server and channel
   - Discord user token

2. **Railway Account**
   - Sign up at [railway.app](https://railway.app)
   - Connect your GitHub account

## 🔧 Setup Instructions

### Step 1: Get Discord Information

1. **Create a Discord Server** (if you don't have one)
   - Create a new server in Discord
   - Create a channel for Midjourney

2. **Get Server and Channel IDs**
   - Enable Developer Mode in Discord (User Settings > Advanced > Developer Mode)
   - Right-click your server → Copy ID (this is your `DISCORD_GUILD_ID`)
   - Right-click your channel → Copy ID (this is your `DISCORD_CHANNEL_ID`)

3. **Get User Token**
   - Open Discord in browser
   - Press F12 → Network tab
   - Send any message
   - Look for requests to `discord.com/api`
   - Copy the `authorization` header value (this is your `DISCORD_USER_TOKEN`)

### Step 2: Deploy to Railway

#### Option A: One-Click Deploy
1. Click the "Deploy on Railway" button above
2. Connect your GitHub account
3. Fork this repository
4. Set environment variables (see below)

#### Option B: Manual Deploy
1. Fork this repository to your GitHub account
2. Go to [railway.app](https://railway.app)
3. Click "New Project" → "Deploy from GitHub repo"
4. Select your forked repository
5. Railway will automatically detect the Dockerfile and deploy

### Step 3: Configure Environment Variables

In your Railway dashboard, go to your project → Variables tab and set:

**Required Variables:**
```
DISCORD_GUILD_ID=your_discord_server_id
DISCORD_CHANNEL_ID=your_discord_channel_id  
DISCORD_USER_TOKEN=your_discord_user_token
MJ_API_SECRET=your_secure_api_secret_key
```

**Optional Variables:**
```
MJ_CORE_SIZE=3
MJ_QUEUE_SIZE=10
MJ_TIMEOUT_MINUTES=5
MJ_TASK_STORE_TYPE=in_memory
MJ_TRANSLATE_WAY=null
SPRING_PROFILES_ACTIVE=railway
```

### Step 4: Test Your Deployment

1. Wait for deployment to complete (usually 2-3 minutes)
2. Get your Railway app URL from the dashboard
3. Test the health endpoint: `https://your-app.railway.app/health`
4. Test the API documentation: `https://your-app.railway.app/doc.html`

## 📡 API Usage

### Authentication
All API requests require the `mj-api-secret` header:
```bash
curl -H "mj-api-secret: your_api_secret" https://your-app.railway.app/mj/submit/imagine
```

### Basic Imagine Request
```bash
curl -X POST https://your-app.railway.app/mj/submit/imagine \
  -H "Content-Type: application/json" \
  -H "mj-api-secret: your_api_secret" \
  -d '{
    "prompt": "a beautiful sunset over mountains"
  }'
```

### Check Task Status
```bash
curl -H "mj-api-secret: your_api_secret" \
  https://your-app.railway.app/mj/task/{task-id}/fetch
```

## 🔍 Monitoring and Logs

### Health Check
- Endpoint: `/health`
- Railway automatically monitors this endpoint

### Logs
- View logs in Railway dashboard → Deployments → View Logs
- Logs include request/response details and error information

### Metrics
- Endpoint: `/actuator/metrics`
- Monitor application performance

## ⚙️ Configuration Options

### Task Storage
- `in_memory`: Default, tasks stored in memory (lost on restart)
- `redis`: Persistent storage (requires Redis service)

### Translation
- `null`: No translation (default)
- `baidu`: Use Baidu Translate API
- `gpt`: Use GPT for translation

### Performance Tuning
```
MJ_CORE_SIZE=5          # Number of concurrent tasks
MJ_QUEUE_SIZE=20        # Queue size for pending tasks
MJ_TIMEOUT_MINUTES=10   # Task timeout
```

## 🚨 Important Notes

### Security
- **Never commit your Discord token to Git**
- Use strong API secrets
- Regularly rotate your tokens

### Rate Limits
- Respect Midjourney's rate limits
- Don't abuse the service
- Monitor your usage

### Discord Token
- User tokens can be unstable
- Consider using Discord bot tokens for production
- Monitor for token expiration

## 🛠️ Troubleshooting

### Common Issues

1. **Deployment Fails**
   - Check build logs in Railway dashboard
   - Ensure all required environment variables are set
   - Verify Dockerfile syntax

2. **Discord Connection Issues**
   - Verify Discord token is valid
   - Check server and channel IDs
   - Ensure bot has proper permissions

3. **API Requests Fail**
   - Check API secret header
   - Verify endpoint URLs
   - Review application logs

### Debug Mode
Set `SPRING_PROFILES_ACTIVE=railway,debug` for verbose logging.

## 📚 API Documentation

Once deployed, visit `https://your-app.railway.app/doc.html` for interactive API documentation.

## 🔄 Updates

To update your deployment:
1. Pull latest changes to your fork
2. Railway will automatically redeploy
3. Or manually trigger deployment in Railway dashboard

## 💰 Cost Estimation

Railway pricing:
- Free tier: $5 credit monthly
- Pro plan: $20/month
- Usage-based pricing for resources

Typical usage:
- Small scale: Free tier sufficient
- Medium scale: $10-20/month
- High scale: $30-50/month

## 📞 Support

- [Railway Documentation](https://docs.railway.app)
- [Original Project Issues](https://github.com/novicezk/midjourney-proxy/issues)
- [Discord Community](https://discord.gg/railway)

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details. 