package pe.edu.lirio.Seda.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradaDTO {
    private String idEntrada;
    private LocalDate fechaEntrada;
    private BigDecimal importeTotal;
    private Integer idSedeUsuario;
    private String idProveedor;
    private Integer idUsuario;
    private List<DetalleEntradaDTO> detalles;
}