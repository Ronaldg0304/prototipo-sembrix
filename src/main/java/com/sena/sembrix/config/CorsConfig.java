package com.sena.sembrix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos los endpoints de la API
                .allowedOrigins("http://localhost:5173") // Permite solo a tu frontend Svelte
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Verbos permitidos
                .allowedHeaders("*") // Permite todos los headers (Authorization, X-API-KEY, etc.)
                .allowCredentials(true); // Permite enviar cookies o credenciales si fuera necesario
    }
}