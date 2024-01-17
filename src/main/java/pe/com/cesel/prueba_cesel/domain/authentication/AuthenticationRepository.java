package pe.com.cesel.prueba_cesel.domain.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    UserDetails findByLogin(String username);
}
