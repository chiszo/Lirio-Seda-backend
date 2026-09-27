package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.model.bd.Marca;
import pe.edu.lirio.Seda.model.dto.MarcaDTO;
import pe.edu.lirio.Seda.service.MarcaService;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController extends AbstractCrudController<Marca, Integer, MarcaDTO> {
    public MarcaController(MarcaService service) {
        super(service);
    }

    @Override
    protected Integer parseId(String id) {
        return Integer.valueOf(id);
    }

    @Override
    protected Marca toEntity(MarcaDTO dto, Integer id) {
        Marca entity = new Marca();
        entity.setIdMarca(id != null ? id : dto.getIdMarca());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }

    @Override
    protected MarcaDTO toDto(Marca entity) {
        return new MarcaDTO(entity.getIdMarca(), entity.getDescripcion());
    }
}