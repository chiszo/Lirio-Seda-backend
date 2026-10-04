package pe.edu.lirio.Seda.controller;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.Marca;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.model.dto.ProductoDTO;
import pe.edu.lirio.Seda.repository.MarcaRepository;
import pe.edu.lirio.Seda.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController extends AbstractCrudController<Producto, String, ProductoDTO> {
    private final MarcaRepository marcaRepository;

    public ProductoController(ProductoService service, MarcaRepository marcaRepository) {
        super(service);
        this.marcaRepository = marcaRepository;
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Producto toEntity(ProductoDTO dto, String id) {
        Producto entity = new Producto();
        entity.setIdProducto(id != null ? id : dto.getIdProducto());
        entity.setNombre(dto.getNombre());
        Marca modelo = marcaRepository.findById(dto.getIdModelo())
            .orElseThrow(() -> new ResourceNotFoundException("Modelo no encontrado: " + dto.getIdModelo()));
        entity.setModelo(modelo);
        entity.setPrecio(dto.getPrecio() != null ? dto.getPrecio() : BigDecimal.ZERO);
        entity.setEstado(dto.getEstado());
        return entity;
    }

    @Override
    protected ProductoDTO toDto(Producto entity) {
        return new ProductoDTO(entity.getIdProducto(), entity.getNombre(), entity.getModelo().getIdModelo(),
            entity.getPrecio(), entity.getEstado());
    }
}