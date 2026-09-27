package pe.edu.lirio.Seda.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleEntradaDTO {
    private Integer idDetalleEntrada;
    private String idEntrada;
    private String idProducto;
    private Integer cantidad;
    private BigDecimal precioUnidad;
    private BigDecimal importe;
}