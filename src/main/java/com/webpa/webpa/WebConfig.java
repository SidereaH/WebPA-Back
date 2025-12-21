package com.webpa.webpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешить доступ ко всем URL
                .allowedOrigins("http://localhost:3000") // Укажите разрешенные источники
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Укажите разрешенные HTTP методы
                .allowedHeaders("*") // Разрешить все заголовки
                .allowCredentials(true); // Разрешить использование куки
    }
}
