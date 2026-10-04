package pe.edu.lirio.Seda.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleEntradaDTO {
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String idEntrada;
    private String idProducto;
    private Integer idDetalleEntrada;
}