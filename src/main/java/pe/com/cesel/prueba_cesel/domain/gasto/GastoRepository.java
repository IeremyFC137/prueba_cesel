package pe.com.cesel.prueba_cesel.domain.gasto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    Page<Gasto> findByUsuarioId(Long userId, Pageable paginacion);
}
