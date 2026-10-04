package pe.edu.lirio.Seda.model.bd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(length = 150)
    private String nombre;

    @Column(length = 150)
    private String apellido;

    @Column(length = 150)
    private String correo;

    @Column(length = 50)
    private String telefono;

    @Column(length = 20)
    private String documento;

    @Column(name = "fechacreacion")
    private LocalDate fechaCreacion;

    @Column(length = 250)
    private String clave;

    @Column(length = 1)
    private String activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Rol_idrol", nullable = false)
    private Roles rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Sede_idsede", nullable = false)
    private Sede sede;
}