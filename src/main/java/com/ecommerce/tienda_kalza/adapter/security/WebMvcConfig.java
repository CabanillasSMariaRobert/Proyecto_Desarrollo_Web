package com.ecommerce.tienda_kalza.adapter.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // Rutas que exigen sesión obligatoria
                .addPathPatterns("/perfil/**", "/pedidos/**", "/admin/usuarios/**")
                // Rutas públicas
                .excludePathPatterns(
                        "/login",
                        "/registro",
                        "/logout",
                        "/css/**",
                        "/js/**",
                        "/icons/**",
                        "/img/**"
                );
    }
}
