package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.model.bd.Estado;
import pe.edu.lirio.Seda.model.dto.EstadoDTO;
import pe.edu.lirio.Seda.service.EstadoService;

@RestController
@RequestMapping("/api/estados")
public class EstadoController extends AbstractCrudController<Estado, Integer, EstadoDTO> {
    public EstadoController(EstadoService service) {
        super(service);
    }

    @Override
    protected Integer parseId(String id) {
        return Integer.valueOf(id);
    }

    @Override
    protected Estado toEntity(EstadoDTO dto, Integer id) {
        Estado entity = new Estado();
        entity.setIdEstado(id != null ? id : dto.getIdEstado());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }

    @Override
    protected EstadoDTO toDto(Estado entity) {
        return new EstadoDTO(entity.getIdEstado(), entity.getDescripcion());
    }
}