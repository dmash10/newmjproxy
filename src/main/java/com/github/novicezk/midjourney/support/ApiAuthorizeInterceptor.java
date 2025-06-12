package com.github.novicezk.midjourney.support;


import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.Constants;
import com.github.novicezk.midjourney.ProxyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAuthorizeInterceptor implements HandlerInterceptor {
	private final ProxyProperties properties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("🔐 API Authorization Check - Method: {}, URI: {}", request.getMethod(), request.getRequestURI());
		
		if (CharSequenceUtil.isBlank(this.properties.getApiSecret())) {
			log.info("✅ No API secret configured, allowing request");
			return true;
		}
		
		String apiSecret = request.getHeader(Constants.API_SECRET_HEADER_NAME);
		String expectedSecret = this.properties.getApiSecret();
		
		log.info("🔑 Expected API secret: {}", expectedSecret != null ? "[SET]" : "[NOT SET]");
		log.info("🔑 Received API secret: {}", apiSecret != null ? "[PROVIDED]" : "[MISSING]");
		log.info("📋 All request headers: {}", java.util.Collections.list(request.getHeaderNames()));
		
		boolean authorized = CharSequenceUtil.equals(apiSecret, expectedSecret);
		
		if (!authorized) {
			log.warn("❌ API Authorization FAILED - Expected: [{}], Received: [{}]", 
				expectedSecret != null ? "***" : "NULL", 
				apiSecret != null ? "***" : "NULL");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			log.info("✅ API Authorization SUCCESS");
		}
		
		return authorized;
	}

}
