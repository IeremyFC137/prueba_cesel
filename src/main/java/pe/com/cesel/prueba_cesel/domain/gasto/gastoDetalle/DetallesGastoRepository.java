package pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetallesGastoRepository extends JpaRepository<GastoDetalle, Long> {

    @Transactional
    void deleteByIdIn(List<Long> ids);
}
