package pe.edu.lirio.Seda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {
    private String idProveedor;
    private String nombre;
    private String telefono;
    private String ruc;
    private String correo;
    private String direccion;
}