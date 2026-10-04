package pe.edu.lirio.Seda.model.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private String idPedido;
    private LocalDate fechaPedido;
    private LocalDate fechaAprobacion;
    private Integer idSedeUsuario;
    private Integer idEstado;
    private Integer idUsuario;
    private List<DetallePedidoDTO> detalles;
}