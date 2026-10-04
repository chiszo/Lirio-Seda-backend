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
@Table(name = "Proveedor")
public class Proveedor {
    @Id
    @Column(name = "idproveedor", length = 10)
    private String idProveedor;

    @Column(length = 250)
    private String nombre;

    @Column(length = 50)
    private String telefono;

    @Column(length = 150)
    private String correo;

    @Column(length = 250)
    private String direccion;

    @Column(length = 20)
    private String ruc;
}