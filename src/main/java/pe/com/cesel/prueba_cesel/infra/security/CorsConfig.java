package pe.com.cesel.prueba_cesel.infra.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aquí defines los patrones de URL para los cuales se aplicará la política CORS.
                        .allowedOrigins("http://192.168.1.192") // Utiliza allowedOriginPatterns para patrones.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos.
                        .allowedHeaders("*") // Cabeceras permitidas.
                        .allowCredentials(true) // Para permitir el envío de cookies.
                        .maxAge(3600); // Tiempo en segundos que el navegador puede cachear la respuesta a una solicitud preflight.
            }
        };
    }
}