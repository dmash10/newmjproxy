#!/bin/sh

echo "=== Environment Validation ==="
echo "PORT: ${PORT:-8080}"
echo "SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-railway}"

# Check if critical Discord environment variables are set
if [ -z "$DISCORD_GUILD_ID" ] || [ -z "$DISCORD_CHANNEL_ID" ] || [ -z "$DISCORD_USER_TOKEN" ]; then
    echo "WARNING: Discord credentials not fully configured!"
    echo "DISCORD_GUILD_ID: ${DISCORD_GUILD_ID:-NOT_SET}"
    echo "DISCORD_CHANNEL_ID: ${DISCORD_CHANNEL_ID:-NOT_SET}"
    echo "DISCORD_USER_TOKEN: ${DISCORD_USER_TOKEN:+SET}"
    echo ""
    echo "The application will start but Discord functionality may not work."
    echo "Please set these environment variables in Railway:"
    echo "- DISCORD_GUILD_ID"
    echo "- DISCORD_CHANNEL_ID"
    echo "- DISCORD_USER_TOKEN"
    echo ""
else
    echo "✓ Discord credentials configured"
fi

echo "MJ_API_SECRET: ${MJ_API_SECRET:+SET}"
<<<<<<< HEAD
echo "=== Starting Application ===" 
=======
echo "=== Starting Application ==="
>>>>>>> 06dea98822c7c2572ab34721d4df3cecd1b1ebf3
