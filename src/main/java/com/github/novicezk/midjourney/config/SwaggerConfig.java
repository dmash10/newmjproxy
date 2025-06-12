package com.github.novicezk.midjourney.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = false)
public class SwaggerConfig {
    // This will only enable Swagger if explicitly set to true
    // By default, it will be disabled
}
