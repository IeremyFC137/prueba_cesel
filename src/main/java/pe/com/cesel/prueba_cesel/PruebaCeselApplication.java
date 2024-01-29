package pe.com.cesel.prueba_cesel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PruebaCeselApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaCeselApplication.class, args);
	}

}
