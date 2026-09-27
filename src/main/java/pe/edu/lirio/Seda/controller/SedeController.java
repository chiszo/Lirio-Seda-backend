package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.model.dto.SedeDTO;
import pe.edu.lirio.Seda.service.SedeService;

@RestController
@RequestMapping("/api/sedes")
public class SedeController extends AbstractCrudController<Sede, Integer, SedeDTO> {
    public SedeController(SedeService service) {
        super(service);
    }

    @Override
    protected Integer parseId(String id) {
        return Integer.valueOf(id);
    }

    @Override
    protected Sede toEntity(SedeDTO dto, Integer id) {
        Sede entity = new Sede();
        entity.setIdSede(id != null ? id : dto.getIdSede());
        entity.setDescripcion(dto.getDescripcion());
        entity.setDireccion(dto.getDireccion());
        entity.setTelefono(dto.getTelefono());
        return entity;
    }

    @Override
    protected SedeDTO toDto(Sede entity) {
        return new SedeDTO(entity.getIdSede(), entity.getDescripcion(), entity.getDireccion(), entity.getTelefono());
    }
}