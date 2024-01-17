package pe.com.cesel.prueba_cesel.domain.usuario;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.cesel.prueba_cesel.domain.direccion.Direccion;

import java.util.List;

@Table(name = "usuarios_prueba_ieremy")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String documento;
    private Boolean activo;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    @Embedded
    private Direccion direccion;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_roles_prueba_ieremy", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private List<Rol> roles;


    public Usuario(DatosRegistroUsuario datosRegistroUsuario) {
        this.activo = true;
        this.nombre = datosRegistroUsuario.nombre();
        this.email = datosRegistroUsuario.email();
        this.documento = datosRegistroUsuario.documento();
        this.telefono = datosRegistroUsuario.telefono();
        this.especialidad = datosRegistroUsuario.especialidad();
        this.direccion = new Direccion(datosRegistroUsuario.direccion());
        this.roles = List.of(Rol.RENDIDOR);
    }

    public void desactivarUsuario() {
        this.activo = false;
    }

    public void actualizarDatos(DatosActualizarUsuario datosActualizarUsuario) {
        if (datosActualizarUsuario.nombre() != null) {
            this.nombre = datosActualizarUsuario.nombre();
        }
        if (datosActualizarUsuario.documento() != null) {
            this.documento = datosActualizarUsuario.documento();
        }
        if (datosActualizarUsuario.direccion() != null) {
            this.direccion = direccion.actualizarDatos(datosActualizarUsuario.direccion());
        }
    }
}
