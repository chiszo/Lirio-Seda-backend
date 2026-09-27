package pe.edu.lirio.Seda.controller;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.Marca;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.model.bd.Proveedor;
import pe.edu.lirio.Seda.model.dto.ProductoDTO;
import pe.edu.lirio.Seda.repository.MarcaRepository;
import pe.edu.lirio.Seda.repository.ProveedorRepository;
import pe.edu.lirio.Seda.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController extends AbstractCrudController<Producto, String, ProductoDTO> {
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoController(
            ProductoService service,
            MarcaRepository marcaRepository,
            ProveedorRepository proveedorRepository) {
        super(service);
        this.marcaRepository = marcaRepository;
        this.proveedorRepository = proveedorRepository;
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
        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada: " + dto.getIdMarca()));
        entity.setMarca(marca);
        if (dto.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + dto.getIdProveedor()));
            entity.setProveedor(proveedor);
        }
        entity.setStock(dto.getStock() != null ? dto.getStock() : 0);
        entity.setPrecio(dto.getPrecio() != null ? dto.getPrecio() : BigDecimal.ZERO);
        return entity;
    }

    @Override
    protected ProductoDTO toDto(Producto entity) {
        return new ProductoDTO(entity.getIdProducto(), entity.getNombre(), entity.getMarca().getIdMarca(),
                entity.getProveedor() == null ? null : entity.getProveedor().getIdProveedor(),
                entity.getStock(), entity.getPrecio());
    }
}