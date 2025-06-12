package com.github.novicezk.midjourney;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import spring.config.BeanConfig;
import spring.config.CorsConfig;
import spring.config.WebMvcConfig;

@Slf4j
@EnableScheduling
@SpringBootApplication
@Import({BeanConfig.class, WebMvcConfig.class, CorsConfig.class})
public class ProxyApplication implements CommandLineRunner {

	@Autowired
	private ProxyProperties proxyProperties;

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Debug logging for API secret configuration
		String apiSecret = proxyProperties.getApiSecret();
		log.info("🔑 API Secret Configuration:");
		log.info("  - API Secret is set: {}", apiSecret != null && !apiSecret.trim().isEmpty());
		log.info("  - API Secret length: {}", apiSecret != null ? apiSecret.length() : 0);
		log.info("  - Environment MJ_API_SECRET: {}", System.getenv("MJ_API_SECRET") != null ? "[SET]" : "[NOT SET]");
		
		if (apiSecret != null && apiSecret.length() > 0) {
			log.info("  - API Secret first 3 chars: {}", apiSecret.substring(0, Math.min(3, apiSecret.length())));
			log.info("  - API Secret last 3 chars: {}", apiSecret.substring(Math.max(0, apiSecret.length() - 3)));
		}
	}
}
