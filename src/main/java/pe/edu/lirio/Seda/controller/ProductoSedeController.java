package pe.edu.lirio.Seda.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.model.bd.ProductoSede;
import pe.edu.lirio.Seda.model.bd.ProductoSedeId;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.model.dto.ProductoSedeDTO;
import pe.edu.lirio.Seda.repository.ProductoRepository;
import pe.edu.lirio.Seda.repository.SedeRepository;
import pe.edu.lirio.Seda.service.ProductoSedeService;

@RestController
@RequestMapping("/api/productos-sedes")
public class ProductoSedeController {
    private final ProductoSedeService service;
    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public ProductoSedeController(
            ProductoSedeService service,
            ProductoRepository productoRepository,
            SedeRepository sedeRepository) {
        this.service = service;
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @GetMapping
    public List<ProductoSedeDTO> listar() {
        return service.listar().stream().map(this::toDto).toList();
    }

    @GetMapping("/{idProducto}/{idSede}")
    public ProductoSedeDTO buscarPorId(@PathVariable String idProducto, @PathVariable Integer idSede) {
        return service.buscarPorId(new ProductoSedeId(idProducto, idSede))
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación producto-sede no encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoSedeDTO crear(@RequestBody ProductoSedeDTO dto) {
        return toDto(service.guardar(toEntity(dto)));
    }

    @PutMapping("/{idProducto}/{idSede}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ProductoSedeDTO actualizar(
            @PathVariable String idProducto,
            @PathVariable Integer idSede,
            @RequestBody ProductoSedeDTO dto) {
        ProductoSedeId id = new ProductoSedeId(idProducto, idSede);
        if (service.buscarPorId(id).isEmpty()) {
            throw new ResourceNotFoundException("Asignación producto-sede no encontrada");
        }
        dto.setIdProducto(idProducto);
        dto.setIdSede(idSede);
        return toDto(service.guardar(toEntity(dto)));
    }

    @DeleteMapping("/{idProducto}/{idSede}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String idProducto, @PathVariable Integer idSede) {
        ProductoSedeId id = new ProductoSedeId(idProducto, idSede);
        if (service.buscarPorId(id).isEmpty()) {
            throw new ResourceNotFoundException("Asignación producto-sede no encontrada");
        }
        service.eliminar(id);
    }

    private ProductoSede toEntity(ProductoSedeDTO dto) {
        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + dto.getIdProducto()));
        Sede sede = sedeRepository.findById(dto.getIdSede())
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada: " + dto.getIdSede()));
        ProductoSede entity = new ProductoSede();
        entity.setId(new ProductoSedeId(dto.getIdProducto(), dto.getIdSede()));
        entity.setProducto(producto);
        entity.setSede(sede);
        entity.setStock(dto.getStock() != null ? dto.getStock() : 0);
        return entity;
    }

    private ProductoSedeDTO toDto(ProductoSede entity) {
        return new ProductoSedeDTO(entity.getProducto().getIdProducto(), entity.getSede().getIdSede(), entity.getStock());
    }
}