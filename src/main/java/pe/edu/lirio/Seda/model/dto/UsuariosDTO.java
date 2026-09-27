package pe.edu.lirio.Seda.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
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
    private String dni;
    private String usuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;
    private Integer idRol;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}