package pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle;

import jakarta.persistence.*;
import lombok.*;
import pe.com.cesel.prueba_cesel.domain.gasto.Gasto;

import java.math.BigDecimal;


@Table(name = "gastos_detalle_prueba_ieremy")
@Entity(name = "GastoDetalle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class GastoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasto_id")
    private Gasto gasto;
    private String c_costo;
    private String c_gasto;
    private String c_contable;
    private BigDecimal importe;
    private BigDecimal p_importe;

    public GastoDetalle(Gasto gasto, DatosDetalleGastoRegistro datos){
        this.gasto = gasto;
        this.c_costo = datos.c_costo();
        this.c_gasto = datos.c_gasto();
        this.c_contable = datos.c_contable();
        this.importe = datos.importe();
        this.p_importe = datos.p_importe();
    }

}
