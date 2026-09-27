package pe.edu.lirio.Seda.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private String idPedido;
    private LocalDateTime fechaPedido;
    private Integer idUsuario;
    private String idProveedor;
    private BigDecimal importeTotal;
    private List<DetallePedidoDTO> detalles;
}