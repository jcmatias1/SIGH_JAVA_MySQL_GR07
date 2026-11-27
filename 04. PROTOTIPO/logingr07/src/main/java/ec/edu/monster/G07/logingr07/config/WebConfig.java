/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registra el interceptor de seguridad para todas las rutas, excepto las públicas
 * (login, recuperación de contraseña y recursos estáticos).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SeguridadInterceptor seguridadInterceptor;

    public WebConfig(SeguridadInterceptor seguridadInterceptor) {
        this.seguridadInterceptor = seguridadInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(seguridadInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/logout", "/recuperar-password", "/restablecer-password",
                        "/css/**", "/js/**", "/lib/**", "/img/**", "/error", "/favicon.ico");
    }
}
