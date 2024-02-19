package pe.com.cesel.prueba_cesel.domain.gasto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    Page<Gasto> findByUsuarioId(Long userId, Pageable paginacion);

    @Query(value = "SELECT cc_costo + ' - ' + COALESCE((SELECT ct_descripcion FROM PUB_TA_CCOSTOS WHERE cc_cia = C.cc_cia AND cc_costo = C.cc_costo_padre), ct_descripcion) AS desc_cc FROM PUB_TA_CCOSTOS C WHERE cc_cia = '01' AND cfl_vigencia = '0' AND cfl_bloqueo = '0' AND cc_costo NOT LIKE '%O%' AND ((cfl_virtual = '0' AND cc_costo_padre NOT IN (SELECT cc_costo_padre FROM PUB_TA_CCOSTOS T WHERE cfl_virtual = '1' AND T.cc_cia = C.cc_cia AND T.cc_costo_padre = C.cc_costo_padre)) OR cfl_virtual = '1') ORDER BY cc_costo_padre, cc_costo, cfl_virtual, cc_area", nativeQuery = true)
    List<String> obtenerCentroDeCosto();

}
