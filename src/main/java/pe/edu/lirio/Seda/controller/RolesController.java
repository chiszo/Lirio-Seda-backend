package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.model.bd.Roles;
import pe.edu.lirio.Seda.model.dto.RolesDTO;
import pe.edu.lirio.Seda.service.RolesService;

@RestController
@RequestMapping("/api/roles")
public class RolesController extends AbstractCrudController<Roles, Integer, RolesDTO> {
    public RolesController(RolesService service) {
        super(service);
    }

    @Override
    protected Integer parseId(String id) {
        return Integer.valueOf(id);
    }

    @Override
    protected Roles toEntity(RolesDTO dto, Integer id) {
        Roles entity = new Roles();
        entity.setIdRol(id != null ? id : dto.getIdRol());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }

    @Override
    protected RolesDTO toDto(Roles entity) {
        return new RolesDTO(entity.getIdRol(), entity.getNombre(), entity.getDescripcion());
    }
}