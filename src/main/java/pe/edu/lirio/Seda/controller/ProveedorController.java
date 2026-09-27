package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.model.bd.Proveedor;
import pe.edu.lirio.Seda.model.dto.ProveedorDTO;
import pe.edu.lirio.Seda.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController extends AbstractCrudController<Proveedor, String, ProveedorDTO> {
    public ProveedorController(ProveedorService service) {
        super(service);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Proveedor toEntity(ProveedorDTO dto, String id) {
        Proveedor entity = new Proveedor();
        entity.setIdProveedor(id != null ? id : dto.getIdProveedor());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setTelefono(dto.getTelefono());
        entity.setRuc(dto.getRuc());
        entity.setCorreo(dto.getCorreo());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }

    @Override
    protected ProveedorDTO toDto(Proveedor entity) {
        return new ProveedorDTO(entity.getIdProveedor(), entity.getNombre(), entity.getApellido(),
                entity.getTelefono(), entity.getRuc(), entity.getCorreo(), entity.getDireccion());
    }
}