package pe.edu.lirio.Seda.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String documento;
    private LocalDate fechaCreacion;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clave;
    private Integer idRol;
    private Integer idSede;
    private String activo;
}