package pe.edu.lirio.Seda.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private String idProducto;
    private String nombre;
    private Integer idMarca;
    private String idProveedor;
    private Integer stock;
    private BigDecimal precio;
}