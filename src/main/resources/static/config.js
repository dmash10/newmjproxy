// Railway API Configuration for Midjourney Admin UI
window.RAILWAY_CONFIG = {
  // API Configuration
  API_BASE_URL: '', // Same domain - no CORS issues
  API_SECRET: 'midjourneyproxy123',
  
  // Environment
  ENVIRONMENT: 'railway',
  
  // Features
  FEATURES: {
    DEMO_MODE: false,
    AUTO_LOGIN: true,
    BYPASS_AUTH: true
  },
  
  // Admin User (for auto-login)
  ADMIN_USER: {
    id: 'railway-admin',
    username: 'admin',
    role: 'admin',
    token: 'railway-admin-token'
  }
};

// Auto-configure API endpoints
if (window.location.hostname !== 'localhost') {
  // Production Railway environment
  window.RAILWAY_CONFIG.API_BASE_URL = window.location.origin;
} else {
  // Local development
  window.RAILWAY_CONFIG.API_BASE_URL = 'http://localhost:8080';
}

console.log('🚀 Railway Config Loaded:', window.RAILWAY_CONFIG); 