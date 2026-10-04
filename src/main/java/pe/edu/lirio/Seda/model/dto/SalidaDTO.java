package pe.edu.lirio.Seda.model.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalidaDTO {
    private String idSalida;
    private LocalDate fechaSalida;
    private Integer idSedeUsuario;
    private Integer idMotivo;
    private Integer idUsuario;
    private List<DetalleSalidaDTO> detalles;
}