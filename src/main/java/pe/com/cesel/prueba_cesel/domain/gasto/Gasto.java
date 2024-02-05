package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public void actualizarDatos(DatosActualizarGasto datosActualizarGasto) {

        if (datosActualizarGasto.c_costo() != null) {
            if(datosActualizarGasto.c_costo().isEmpty() || datosActualizarGasto.c_costo().isBlank()){
                throw new ValidationException("El campo c_costo no debe estar vacio");
            }
            this.c_costo = datosActualizarGasto.c_costo();
        }
        if (datosActualizarGasto.c_gasto()!= null) {
            if(datosActualizarGasto.c_gasto().isEmpty() || datosActualizarGasto.c_gasto().isBlank()){
                throw new ValidationException("El campo c_gasto no debe estar vacio");
            }
            this.c_gasto = datosActualizarGasto.c_gasto();
        }
        if (datosActualizarGasto.c_contable()!= null) {
            if(datosActualizarGasto.c_contable().isEmpty() || datosActualizarGasto.c_contable().isBlank()){
                throw new ValidationException("El campo c_contable no debe estar vacio");
            }
            this.c_contable = datosActualizarGasto.c_contable();
        }
        if(datosActualizarGasto.importe()!=null){
            if(datosActualizarGasto.importe().compareTo(BigDecimal.ZERO)<0){
                throw new ValidationException("El campo importe debe ser positivo");
            }
            this.importe = datosActualizarGasto.importe();
        }
        if(datosActualizarGasto.p_importe()!=null){
            if(datosActualizarGasto.p_importe().compareTo(BigDecimal.ZERO)<0 ||
                    datosActualizarGasto.p_importe().compareTo(BigDecimal.ONE) > 0){
                throw new ValidationException("El campo p_importe debe estar en el rango de 0 a 1");
            }
            this.p_importe = datosActualizarGasto.p_importe();
        }
    }


}
