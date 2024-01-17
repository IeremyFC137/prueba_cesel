package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.cesel.prueba_cesel.domain.direccion.Direccion;
import pe.com.cesel.prueba_cesel.domain.usuario.DatosRegistroUsuario;
import pe.com.cesel.prueba_cesel.domain.usuario.Rol;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "gastos_prueba_ieremy")
@Entity(name = "Gasto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Usuario usuario;
    private String proveedor;
    private String ruc;
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipo_documento;
    private String documento;
    private LocalDateTime fecha_emision;
    private BigDecimal sub_total;
    private BigDecimal igv;
    private BigDecimal importe;
    private BigDecimal p_importe;
    @Enumerated(EnumType.STRING)
    private Moneda moneda;
    private String c_costo;
    private String c_gasto;
    private String c_contable;

    public Gasto(Usuario usuario, DatosRegistroGasto datosRegistroGastos) {
        this.usuario = usuario;
        this.proveedor = datosRegistroGastos.proveedor();
        this.ruc = datosRegistroGastos.ruc();
        this.tipo_documento = datosRegistroGastos.tipo_documento();
        this.documento = datosRegistroGastos.documento();
        this.fecha_emision = datosRegistroGastos.fecha_emision();
        this.sub_total = datosRegistroGastos.sub_total();
        this.igv = datosRegistroGastos.igv();
        this.importe = datosRegistroGastos.importe();
        this.p_importe = datosRegistroGastos.p_importe();
        this.moneda = datosRegistroGastos.moneda();
        this.c_costo = datosRegistroGastos.c_costo();
        this.c_gasto = datosRegistroGastos.c_gasto();
        this.c_contable = datosRegistroGastos.c_contable();
    }


}
