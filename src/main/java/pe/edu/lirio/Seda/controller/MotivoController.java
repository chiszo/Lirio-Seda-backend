package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.model.bd.Motivo;
import pe.edu.lirio.Seda.model.dto.MotivoDTO;
import pe.edu.lirio.Seda.service.MotivoService;

@RestController
@RequestMapping("/api/motivos")
public class MotivoController extends AbstractCrudController<Motivo, Integer, MotivoDTO> {
    public MotivoController(MotivoService service) {
        super(service);
    }

    @Override
    protected Integer parseId(String id) {
        return Integer.valueOf(id);
    }

    @Override
    protected Motivo toEntity(MotivoDTO dto, Integer id) {
        Motivo entity = new Motivo();
        entity.setIdMotivo(id != null ? id : dto.getIdMotivo());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }

    @Override
    protected MotivoDTO toDto(Motivo entity) {
        return new MotivoDTO(entity.getIdMotivo(), entity.getDescripcion());
    }
}