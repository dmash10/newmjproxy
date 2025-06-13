package spring.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<StaticFileFilter> staticFileFilter() {
        FilterRegistrationBean<StaticFileFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new StaticFileFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all URLs
        registrationBean.setOrder(1); // High priority - execute before other filters
        registrationBean.setName("staticFileFilter");
        
        return registrationBean;
    }
} 