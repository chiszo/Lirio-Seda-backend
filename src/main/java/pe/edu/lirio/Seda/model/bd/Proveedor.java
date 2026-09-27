package pe.edu.lirio.Seda.model.bd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tblproveedor")
public class Proveedor {
    @Id
    @Column(name = "idproveedor", columnDefinition = "char(4)")
    private String idProveedor;

    @Column(length = 120, nullable = false)
    private String nombre;

    @Column(length = 120)
    private String apellido;

    @Column(length = 45)
    private String telefono;

    @Column(length = 45, unique = true)
    private String ruc;

    @Column(length = 70)
    private String correo;

    @Column(length = 70)
    private String direccion;
}