package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.DatosDetalleGastoActualizar;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.GastoDetalle;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @Enumerated(EnumType.STRING)
    private Moneda moneda;
    private String ruta_imagen;
    @OneToMany(mappedBy = "gasto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoDetalle> detallesGasto;

    public Gasto(Usuario usuario, DatosRegistroGasto datosRegistroGastos, String rutaImagen) {
        this.usuario = usuario;
        this.proveedor = datosRegistroGastos.proveedor();
        this.ruc = datosRegistroGastos.ruc();
        this.tipo_documento = datosRegistroGastos.tipo_documento();
        this.documento = datosRegistroGastos.documento();
        this.fecha_emision = datosRegistroGastos.fecha_emision();
        this.sub_total = datosRegistroGastos.sub_total();
        this.igv = datosRegistroGastos.igv();
        this.moneda = datosRegistroGastos.moneda();
        this.ruta_imagen = rutaImagen;
        this.detallesGasto = datosRegistroGastos.detalles().stream()
                .map(detalle -> new GastoDetalle(
                        null,
                        this,
                        detalle.c_costo(),
                        detalle.c_gasto(),
                        detalle.c_contable(),
                        detalle.importe(),
                        detalle.p_importe()
                )).collect(Collectors.toList());
    }

    public void actualizarDatos(DatosActualizarGasto datosActualizarGasto, String ruta_imagen) {

        if(ruta_imagen!=null && !ruta_imagen.isBlank()){
            this.ruta_imagen = ruta_imagen;
        }

        for (DatosDetalleGastoActualizar detalleActualizar : datosActualizarGasto.detalles()) {

            GastoDetalle detalle = this.detallesGasto.stream()
                    .filter(d -> d.getId().equals(detalleActualizar.detalleId()))
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("Detalle de Gasto no encontrado con ID: " +
                            detalleActualizar.detalleId()));

            if (detalleActualizar.c_costo() != null) {
                if(detalleActualizar.c_costo().isEmpty() || detalleActualizar.c_costo().isBlank()){
                    throw new ValidationException("El campo c_costo no debe estar vacio");
                }
                detalle.setC_costo(detalleActualizar.c_costo());
            }
            if (detalleActualizar.c_gasto()!= null) {
                if(detalleActualizar.c_gasto().isEmpty() || detalleActualizar.c_gasto().isBlank()){
                    throw new ValidationException("El campo c_gasto no debe estar vacio");
                }
                detalle.setC_gasto(detalleActualizar.c_gasto());
            }
            if (detalleActualizar.c_contable()!= null) {
                if(detalleActualizar.c_contable().isEmpty() || detalleActualizar.c_contable().isBlank()){
                    throw new ValidationException("El campo c_contable no debe estar vacio");
                }
                detalle.setC_contable(detalleActualizar.c_contable());
            }
            if(detalleActualizar.importe()!=null){
                if(detalleActualizar.importe().compareTo(BigDecimal.ZERO)<0){
                    throw new ValidationException("El campo importe debe ser positivo");
                }
                detalle.setImporte(detalleActualizar.importe());
            }
            if(detalleActualizar.p_importe()!=null){
                if(detalleActualizar.p_importe().compareTo(BigDecimal.ZERO)<0 ||
                        detalleActualizar.p_importe().compareTo(BigDecimal.ONE) > 0){
                    throw new ValidationException("El campo p_importe debe estar en el rango de 0 a 1");
                }
                detalle.setP_importe(detalleActualizar.p_importe());
            }
        }

    }
}
