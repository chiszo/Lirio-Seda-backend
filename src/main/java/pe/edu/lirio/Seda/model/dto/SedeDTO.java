package pe.edu.lirio.Seda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeDTO {
    private Integer idSede;
    private String descripcion;
    private String direccion;
    private String telefono;
}