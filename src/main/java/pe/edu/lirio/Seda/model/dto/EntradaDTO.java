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
public class EntradaDTO {
    private String idEntrada;
    private LocalDateTime fechaEntrada;
    private Integer idUsuario;
    private String idProveedor;
    private Integer idSede;
    private BigDecimal importeTotal;
    private List<DetalleEntradaDTO> detalles;
}