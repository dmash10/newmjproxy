#!/bin/bash

# Test script for Midjourney Proxy API
# Usage: ./test-api.sh <your-railway-app-url> <your-api-secret>

if [ $# -ne 2 ]; then
    echo "Usage: $0 <railway-app-url> <api-secret>"
    echo "Example: $0 https://your-app.railway.app your-api-secret"
    exit 1
fi

APP_URL=$1
API_SECRET=$2

echo "🧪 Testing Midjourney Proxy API"
echo "================================"
echo "App URL: $APP_URL"
echo "API Secret: ${API_SECRET:0:10}..."
echo ""

# Test health endpoint
echo "1. Testing health endpoint..."
HEALTH_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/health_response "$APP_URL/health")
HEALTH_CODE=${HEALTH_RESPONSE: -3}

if [ "$HEALTH_CODE" = "200" ]; then
    echo "✅ Health check passed"
    cat /tmp/health_response | jq . 2>/dev/null || cat /tmp/health_response
else
    echo "❌ Health check failed (HTTP $HEALTH_CODE)"
    cat /tmp/health_response
fi
echo ""

# Test API documentation
echo "2. Testing API documentation..."
DOC_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/doc_response "$APP_URL/doc.html")
DOC_CODE=${DOC_RESPONSE: -3}

if [ "$DOC_CODE" = "200" ]; then
    echo "✅ API documentation accessible"
else
    echo "❌ API documentation failed (HTTP $DOC_CODE)"
fi
echo ""

# Test API with authentication
echo "3. Testing API authentication..."
AUTH_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/auth_response \
    -H "mj-api-secret: $API_SECRET" \
    "$APP_URL/mj/task/list")
AUTH_CODE=${AUTH_RESPONSE: -3}

if [ "$AUTH_CODE" = "200" ]; then
    echo "✅ API authentication successful"
    cat /tmp/auth_response | jq . 2>/dev/null || cat /tmp/auth_response
else
    echo "❌ API authentication failed (HTTP $AUTH_CODE)"
    cat /tmp/auth_response
fi
echo ""

# Test without authentication (should fail)
echo "4. Testing API without authentication..."
NO_AUTH_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/no_auth_response \
    "$APP_URL/mj/task/list")
NO_AUTH_CODE=${NO_AUTH_RESPONSE: -3}

if [ "$NO_AUTH_CODE" = "401" ] || [ "$NO_AUTH_CODE" = "403" ]; then
    echo "✅ API properly rejects unauthenticated requests"
else
    echo "⚠️  API security check: Expected 401/403, got $NO_AUTH_CODE"
fi
echo ""

# Test imagine endpoint (dry run)
echo "5. Testing imagine endpoint structure..."
IMAGINE_RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/imagine_response \
    -X POST \
    -H "Content-Type: application/json" \
    -H "mj-api-secret: $API_SECRET" \
    -d '{"prompt": "test prompt --dry-run"}' \
    "$APP_URL/mj/submit/imagine")
IMAGINE_CODE=${IMAGINE_RESPONSE: -3}

echo "Imagine endpoint response (HTTP $IMAGINE_CODE):"
cat /tmp/imagine_response | jq . 2>/dev/null || cat /tmp/imagine_response
echo ""

# Summary
echo "📊 Test Summary"
echo "==============="
echo "Health Check: $([ "$HEALTH_CODE" = "200" ] && echo "✅ PASS" || echo "❌ FAIL")"
echo "Documentation: $([ "$DOC_CODE" = "200" ] && echo "✅ PASS" || echo "❌ FAIL")"
echo "Authentication: $([ "$AUTH_CODE" = "200" ] && echo "✅ PASS" || echo "❌ FAIL")"
echo "Security: $([ "$NO_AUTH_CODE" = "401" ] || [ "$NO_AUTH_CODE" = "403" ] && echo "✅ PASS" || echo "⚠️  CHECK")"
echo ""

if [ "$HEALTH_CODE" = "200" ] && [ "$DOC_CODE" = "200" ] && [ "$AUTH_CODE" = "200" ]; then
    echo "🎉 All basic tests passed! Your Midjourney Proxy is ready to use."
    echo ""
    echo "📚 Next steps:"
    echo "   - Visit $APP_URL/doc.html for API documentation"
    echo "   - Test with a real Midjourney prompt"
    echo "   - Monitor logs in Railway dashboard"
else
    echo "⚠️  Some tests failed. Check the Railway logs for more details."
fi

# Cleanup
rm -f /tmp/health_response /tmp/doc_response /tmp/auth_response /tmp/no_auth_response /tmp/imagine_response 