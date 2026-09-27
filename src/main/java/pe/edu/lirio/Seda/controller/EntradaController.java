package pe.edu.lirio.Seda.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetalleEntrada;
import pe.edu.lirio.Seda.model.bd.Entrada;
import pe.edu.lirio.Seda.model.dto.DetalleEntradaDTO;
import pe.edu.lirio.Seda.model.dto.EntradaDTO;
import pe.edu.lirio.Seda.repository.ProveedorRepository;
import pe.edu.lirio.Seda.repository.SedeRepository;
import pe.edu.lirio.Seda.repository.UsuariosRepository;
import pe.edu.lirio.Seda.service.EntradaService;
import pe.edu.lirio.Seda.service.InventarioService;
import pe.edu.lirio.Seda.service.MovimientoCalculations;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController extends AbstractCrudController<Entrada, String, EntradaDTO> {
    private final EntradaService service;
    private final UsuariosRepository usuariosRepository;
    private final ProveedorRepository proveedorRepository;
    private final SedeRepository sedeRepository;
    private final InventarioService inventarioService;

    public EntradaController(EntradaService service, UsuariosRepository usuariosRepository,
            ProveedorRepository proveedorRepository, SedeRepository sedeRepository,
            InventarioService inventarioService) {
        super(service);
        this.service = service;
        this.usuariosRepository = usuariosRepository;
        this.proveedorRepository = proveedorRepository;
        this.sedeRepository = sedeRepository;
        this.inventarioService = inventarioService;
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Entrada toEntity(EntradaDTO dto, String id) {
        Entrada entity = id == null ? new Entrada() : service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada no encontrada: " + id));
        if (id != null) {
            Integer sedeAnterior = entity.getSede() == null ? null : entity.getSede().getIdSede();
            entity.getDetalles().forEach(detalle -> inventarioService.ajustarStock(
                    detalle.getProducto().getIdProducto(), sedeAnterior, -detalle.getCantidad()));
        }
        if (dto.getIdSede() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "idSede es obligatorio para registrar una entrada");
        }
        entity.setIdEntrada(id != null ? id : dto.getIdEntrada());
        entity.setFechaEntrada(dto.getFechaEntrada() != null ? dto.getFechaEntrada() : LocalDateTime.now());
        entity.setUsuario(usuariosRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.getIdUsuario())));
        entity.setProveedor(proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + dto.getIdProveedor())));
        entity.setSede(sedeRepository.findById(dto.getIdSede())
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada: " + dto.getIdSede())));

        var detalles = new ArrayList<DetalleEntrada>();
        if (dto.getDetalles() != null) {
            for (DetalleEntradaDTO detalleDTO : dto.getDetalles()) {
                DetalleEntrada detalle = new DetalleEntrada();
                detalle.setEntrada(entity);
                detalle.setProducto(inventarioService.ajustarStock(
                        detalleDTO.getIdProducto(), dto.getIdSede(), validarCantidad(detalleDTO.getCantidad())));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnidad(detalleDTO.getPrecioUnidad());
                detalle.setImporte(MovimientoCalculations.calcularImporte(
                        detalleDTO.getCantidad(), detalleDTO.getPrecioUnidad()));
                detalles.add(detalle);
            }
        }
        entity.getDetalles().clear();
        entity.getDetalles().addAll(detalles);
        BigDecimal total = detalles.stream().map(DetalleEntrada::getImporte)
                .filter(value -> value != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        entity.setImporteTotal(total);
        return entity;
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        Entrada entity = service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada no encontrada: " + id));
        Integer idSede = entity.getSede() == null ? null : entity.getSede().getIdSede();
        entity.getDetalles().forEach(detalle -> inventarioService.ajustarStock(
                detalle.getProducto().getIdProducto(), idSede, -detalle.getCantidad()));
        service.eliminar(id);
    }

    private int validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que cero");
        }
        return cantidad;
    }

    @Override
    protected EntradaDTO toDto(Entrada entity) {
        return new EntradaDTO(entity.getIdEntrada(), entity.getFechaEntrada(), entity.getUsuario().getIdUsuario(),
                entity.getProveedor().getIdProveedor(), entity.getSede() == null ? null : entity.getSede().getIdSede(),
                entity.getImporteTotal(),
                entity.getDetalles().stream().map(detalle -> new DetalleEntradaDTO(
                        detalle.getIdDetalleEntrada(), entity.getIdEntrada(), detalle.getProducto().getIdProducto(),
                        detalle.getCantidad(), detalle.getPrecioUnidad(), detalle.getImporte())).toList());
    }
}