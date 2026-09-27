package pe.edu.lirio.Seda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSedeDTO {
    private String idProducto;
    private Integer idSede;
    private Integer stock;
}