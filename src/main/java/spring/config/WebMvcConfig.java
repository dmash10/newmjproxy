package spring.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.ProxyProperties;
import com.github.novicezk.midjourney.support.ApiAuthorizeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
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
		// Serve static files for React admin UI
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.setCachePeriod(3600);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Original API documentation redirect
		registry.addViewController("/").setViewName("redirect:doc.html");
		
		// React Admin UI routes
		registry.addViewController("/admin").setViewName("forward:/index.html");
		registry.addViewController("/admin/").setViewName("forward:/index.html");
		registry.addViewController("/admin/**").setViewName("forward:/index.html");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (CharSequenceUtil.isNotBlank(this.properties.getApiSecret())) {
			registry.addInterceptor(this.apiAuthorizeInterceptor)
					.addPathPatterns("/submit/**", "/task/**", "/account/**");
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
