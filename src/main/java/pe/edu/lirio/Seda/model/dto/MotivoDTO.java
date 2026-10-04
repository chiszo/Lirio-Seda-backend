package pe.edu.lirio.Seda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotivoDTO {
    private Integer idMotivo;
    private String descripcion;
}