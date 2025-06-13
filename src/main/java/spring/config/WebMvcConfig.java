package spring.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.ProxyProperties;
import com.github.novicezk.midjourney.support.ApiAuthorizeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Resource
	private ApiAuthorizeInterceptor apiAuthorizeInterceptor;
	@Resource
	private ProxyProperties properties;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("🔧 [DEBUG] Configuring resource handlers...");
		
		// NUCLEAR OPTION: Serve ALL static files with custom resolver
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.setCachePeriod(0) // Disable cache for debugging
				.resourceChain(true)
				.addResolver(new PathResourceResolver() {
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						System.out.println("🔍 [DEBUG] Requesting resource: " + resourcePath + " from " + location);
						Resource resource = location.createRelative(resourcePath);
						if (resource.exists() && resource.isReadable()) {
							System.out.println("✅ [DEBUG] Found resource: " + resourcePath);
							return resource;
						} else {
							System.out.println("❌ [DEBUG] Resource NOT found: " + resourcePath);
							// Try without leading slash
							if (resourcePath.startsWith("/")) {
								String cleanPath = resourcePath.substring(1);
								System.out.println("🔄 [DEBUG] Trying clean path: " + cleanPath);
								Resource cleanResource = location.createRelative(cleanPath);
								if (cleanResource.exists() && cleanResource.isReadable()) {
									System.out.println("✅ [DEBUG] Found clean resource: " + cleanPath);
									return cleanResource;
								}
							}
							return null;
						}
					}
				});
		
		System.out.println("✅ [DEBUG] Resource handlers configured");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		System.out.println("🔧 [DEBUG] Configuring view controllers...");
		
		// Original API documentation redirect
		registry.addViewController("/").setViewName("redirect:doc.html");
		
		// React Admin UI routes
		registry.addViewController("/admin").setViewName("forward:/index.html");
		registry.addViewController("/admin/").setViewName("forward:/index.html");
		registry.addViewController("/admin/**").setViewName("forward:/index.html");
		
		System.out.println("✅ [DEBUG] View controllers configured");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("🔧 [DEBUG] Configuring interceptors...");
		if (CharSequenceUtil.isNotBlank(this.properties.getApiSecret())) {
			registry.addInterceptor(this.apiAuthorizeInterceptor)
					.addPathPatterns("/submit/**", "/task/**", "/account/**")
					.excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.html", "/**/*.png", "/**/*.webp", "/**/*.ico", "/**/*.svg");
			System.out.println("✅ [DEBUG] API interceptor configured with static file exclusions");
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
				.allowedHeaders("*")
				.exposedHeaders("*")
				.allowCredentials(true)
				.maxAge(86400); // 24 hours
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		// Allow all origins
		configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
		
		// Allow all methods
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
		
		// Allow all headers
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		
		// Expose all headers
		configuration.setExposedHeaders(Collections.singletonList("*"));
		
		// Allow credentials
		configuration.setAllowCredentials(true);
		
		// Cache preflight for 24 hours
		configuration.setMaxAge(86400L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public CorsFilter corsFilter() {
		return new CorsFilter(corsConfigurationSource());
	}

}
