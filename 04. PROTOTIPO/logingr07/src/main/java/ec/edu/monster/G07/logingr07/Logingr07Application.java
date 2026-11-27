/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal: arranca Spring Boot, detecta las entidades, repositorios,
 * servicios y controladores de los subpaquetes y levanta Tomcat en el puerto 8080.
 */
@SpringBootApplication
public class Logingr07Application {

    public static void main(String[] args) {
        SpringApplication.run(Logingr07Application.class, args);
    }
}
