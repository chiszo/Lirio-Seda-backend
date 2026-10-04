package pe.edu.lirio.Seda.model.bd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Producto")
public class Producto {
    @Id
    @Column(name = "idproducto", length = 10)
    private String idProducto;

    @Column(length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Modelo_idmodelo", nullable = false)
    private Marca modelo;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 1)
    private String estado;
}