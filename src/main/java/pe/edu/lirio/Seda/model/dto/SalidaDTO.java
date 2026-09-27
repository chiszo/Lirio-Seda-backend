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
public class SalidaDTO {
    private String idSalida;
    private LocalDateTime fechaSalida;
    private Integer idUsuario;
    private Integer idSede;
    private String destino;
    private BigDecimal importeTotal;
    private List<DetalleSalidaDTO> detalles;
}