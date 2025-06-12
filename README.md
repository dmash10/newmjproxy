# 🚀 Midjourney Proxy - Railway Optimized

A production-ready, Railway-optimized proxy service for Midjourney Discord API that provides REST API endpoints for AI image generation.

[![Deploy on Railway](https://railway.app/button.svg)](https://railway.app/template/midjourney-proxy)

## ✨ Features

- 🎨 **Complete Midjourney Integration** - Imagine, Upscale, Variations, Blend, Describe
- 🚀 **Railway Optimized** - Multi-stage Docker build, health checks, auto-scaling
- 🔒 **Secure Configuration** - Environment variables, no hardcoded secrets
- 📊 **Monitoring Ready** - Health endpoints, metrics, structured logging
- 🛠️ **Developer Friendly** - Automated scripts, comprehensive documentation
- 🌐 **Production Ready** - Optimized JVM settings, error handling, rate limiting

## 🚀 Quick Deploy to Railway

### Option 1: One-Click Deploy (Recommended)
1. Click the "Deploy on Railway" button above
2. Connect your GitHub account and fork this repository
3. Set the required environment variables (see below)
4. Deploy automatically

### Option 2: Manual Deploy
1. Fork this repository to your GitHub account
2. Go to [railway.app](https://railway.app) and create a new project
3. Connect your GitHub repository
4. Set environment variables in Railway dashboard
5. Deploy

## ⚙️ Environment Variables

Set these in your Railway dashboard under Variables:

### Required Variables
```env
DISCORD_GUILD_ID=your_discord_server_id
DISCORD_CHANNEL_ID=your_discord_channel_id
DISCORD_USER_TOKEN=your_discord_user_token
MJ_API_SECRET=your_secure_api_secret_key
```

### Optional Variables (with defaults)
```env
SPRING_PROFILES_ACTIVE=railway
MJ_CORE_SIZE=3
MJ_QUEUE_SIZE=10
MJ_TIMEOUT_MINUTES=5
MJ_TASK_STORE_TYPE=in_memory
MJ_TRANSLATE_WAY=null
MJ_NOTIFY_POOL_SIZE=10
```

## 🔧 Discord Setup

### 1. Create Discord Server
- Create a new Discord server or use existing one
- Add Midjourney bot to your server
- Create a dedicated channel for Midjourney

### 2. Get Required IDs
- Enable Developer Mode in Discord (User Settings > Advanced > Developer Mode)
- Right-click your server → Copy ID (this is `DISCORD_GUILD_ID`)
- Right-click your channel → Copy ID (this is `DISCORD_CHANNEL_ID`)

### 3. Get User Token
- Open Discord in browser
- Press F12 → Network tab
- Send any message in Discord
- Look for requests to `discord.com/api`
- Copy the `authorization` header value (this is `DISCORD_USER_TOKEN`)

## 📡 API Usage

### Authentication
All API requests require the `mj-api-secret` header:
```bash
curl -H "mj-api-secret: your_api_secret" https://your-app.railway.app/mj/task/list
```

### Basic Imagine Request
```bash
curl -X POST https://your-app.railway.app/mj/submit/imagine \
  -H "Content-Type: application/json" \
  -H "mj-api-secret: your_api_secret" \
  -d '{
    "prompt": "a beautiful sunset over mountains, photorealistic, 4k"
  }'
```

### Check Task Status
```bash
curl -H "mj-api-secret: your_api_secret" \
  https://your-app.railway.app/mj/task/{task-id}/fetch
```

### Upscale Image
```bash
curl -X POST https://your-app.railway.app/mj/submit/action \
  -H "Content-Type: application/json" \
  -H "mj-api-secret: your_api_secret" \
  -d '{
    "taskId": "your-task-id",
    "action": "UPSCALE",
    "index": 1
  }'
```

## 🔍 API Documentation

Once deployed, access interactive API documentation at:
`https://your-app.railway.app/doc.html`

### Key Endpoints
- `GET /health` - Health check
- `GET /mj/task/list` - List all tasks
- `POST /mj/submit/imagine` - Submit imagine request
- `POST /mj/submit/blend` - Blend multiple images
- `POST /mj/submit/describe` - Describe image (image to text)
- `GET /mj/task/{id}/fetch` - Get task status
- `POST /mj/submit/action` - Submit action (upscale, variation, etc.)

## 🛠️ Development

### Local Development
1. Clone the repository
2. Copy `.env.example` to `.env` and fill in your values
3. Run with Maven: `mvn spring-boot:run`
4. Or use Docker: `docker build -t mjproxy . && docker run -p 8080:8080 mjproxy`

### Testing
Use the provided test script:
```bash
chmod +x test-api.sh
./test-api.sh https://your-app.railway.app your-api-secret
```

### Automated Deployment
Use the deployment script:
```bash
chmod +x deploy-to-railway.sh
./deploy-to-railway.sh
```

## 🏗️ Architecture

### Multi-Stage Docker Build
- **Builder Stage**: Maven with OpenJDK 17 for compilation
- **Runtime Stage**: Lightweight JRE for production
- **Size Optimization**: ~60% smaller final image

### Railway Optimizations
- **Port Binding**: Automatic `PORT` environment variable support
- **Health Checks**: `/health` endpoint for Railway monitoring
- **Logging**: Structured logging for Railway dashboard
- **JVM Tuning**: Optimized for cloud containers

### Security Features
- **Environment Variables**: No secrets in code
- **API Authentication**: Required `mj-api-secret` header
- **Input Validation**: Sanitized prompts and parameters
- **Rate Limiting**: Built-in request throttling

## 📊 Monitoring

### Health Checks
- **Endpoint**: `GET /health`
- **Railway Integration**: Automatic restart on failures
- **Custom Checks**: Discord connection, memory usage

### Metrics
- **Endpoint**: `GET /actuator/metrics`
- **Available Metrics**: JVM stats, HTTP requests, custom counters
- **Integration**: Compatible with Railway metrics

### Logging
- **Structured Format**: JSON logs for better parsing
- **Log Levels**: Configurable via environment variables
- **Request Tracing**: Full request/response logging

## 💰 Cost Estimation

Railway pricing for typical usage:
- **Free Tier**: $5 credit/month (good for testing)
- **Light Usage**: $10-15/month (occasional use)
- **Medium Usage**: $20-30/month (regular use)
- **Heavy Usage**: $40-60/month (high volume)

## 🚨 Important Notes

### Security
- **Never commit Discord tokens** to version control
- **Use strong API secrets** (minimum 32 characters)
- **Regularly rotate tokens** for security
- **Monitor usage** to prevent abuse

### Rate Limits
- **Respect Midjourney limits** to avoid account warnings
- **Monitor request frequency** in Railway logs
- **Implement client-side throttling** for high-volume usage

### Discord Token Stability
- **User tokens can expire** - monitor for authentication errors
- **Consider Discord bot tokens** for production use
- **Set up alerts** for token failures

## 🛠️ Troubleshooting

### Common Issues

1. **Deployment Fails**
   ```bash
   # Check Railway build logs
   railway logs --deployment
   ```

2. **Discord Connection Issues**
   - Verify token is valid and not expired
   - Check server and channel IDs are correct
   - Ensure Midjourney bot is in your server

3. **API Authentication Fails**
   - Verify `MJ_API_SECRET` environment variable is set
   - Check request includes `mj-api-secret` header
   - Ensure header value matches exactly

4. **Health Check Failures**
   - Check `/health` endpoint responds with 200
   - Verify all required environment variables are set
   - Review application logs for startup errors

### Debug Mode
Enable verbose logging:
```bash
railway variables set SPRING_PROFILES_ACTIVE=railway,debug
```

### Getting Help
- Check Railway deployment logs
- Review application logs in Railway dashboard
- Test API endpoints with provided scripts
- Verify environment variables are set correctly

## 📚 Additional Resources

- **Railway Documentation**: https://docs.railway.app
- **Midjourney Documentation**: https://docs.midjourney.com
- **Discord Developer Portal**: https://discord.com/developers
- **Spring Boot Actuator**: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## ⭐ Support

If this project helps you, please give it a star! ⭐

---

**🎉 Ready to deploy?** Click the Railway button above and start generating amazing AI images in minutes!
