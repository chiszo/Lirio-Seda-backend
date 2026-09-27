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
import pe.edu.lirio.Seda.model.bd.DetalleSalida;
import pe.edu.lirio.Seda.model.bd.Salida;
import pe.edu.lirio.Seda.model.dto.DetalleSalidaDTO;
import pe.edu.lirio.Seda.model.dto.SalidaDTO;
import pe.edu.lirio.Seda.repository.SedeRepository;
import pe.edu.lirio.Seda.repository.UsuariosRepository;
import pe.edu.lirio.Seda.service.SalidaService;
import pe.edu.lirio.Seda.service.InventarioService;
import pe.edu.lirio.Seda.service.MovimientoCalculations;

@RestController
@RequestMapping("/api/salidas")
public class SalidaController extends AbstractCrudController<Salida, String, SalidaDTO> {
    private final SalidaService service;
    private final UsuariosRepository usuariosRepository;
        private final SedeRepository sedeRepository;
    private final InventarioService inventarioService;

    public SalidaController(SalidaService service, UsuariosRepository usuariosRepository,
            SedeRepository sedeRepository, InventarioService inventarioService) {
        super(service);
        this.service = service;
        this.usuariosRepository = usuariosRepository;
        this.sedeRepository = sedeRepository;
        this.inventarioService = inventarioService;
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Salida toEntity(SalidaDTO dto, String id) {
        Salida entity = id == null ? new Salida() : service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salida no encontrada: " + id));
        if (id != null) {
            Integer sedeAnterior = entity.getSede() == null ? null : entity.getSede().getIdSede();
            entity.getDetalles().forEach(detalle -> inventarioService.ajustarStock(
                detalle.getProducto().getIdProducto(), sedeAnterior, detalle.getCantidad()));
        }
        if (dto.getIdSede() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST, "idSede es obligatorio para registrar una salida");
        }
        entity.setIdSalida(id != null ? id : dto.getIdSalida());
        entity.setFechaSalida(dto.getFechaSalida() != null ? dto.getFechaSalida() : LocalDateTime.now());
        entity.setUsuario(usuariosRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.getIdUsuario())));
        entity.setSede(sedeRepository.findById(dto.getIdSede())
            .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada: " + dto.getIdSede())));
        entity.setDestino(dto.getDestino());

        var detalles = new ArrayList<DetalleSalida>();
        if (dto.getDetalles() != null) {
            for (DetalleSalidaDTO detalleDTO : dto.getDetalles()) {
                DetalleSalida detalle = new DetalleSalida();
                detalle.setSalida(entity);
                detalle.setProducto(inventarioService.ajustarStock(
                        detalleDTO.getIdProducto(), dto.getIdSede(), -validarCantidad(detalleDTO.getCantidad())));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnidad(detalleDTO.getPrecioUnidad());
                detalle.setImporte(MovimientoCalculations.calcularImporte(
                    detalleDTO.getCantidad(), detalleDTO.getPrecioUnidad()));
                detalles.add(detalle);
            }
        }
        entity.getDetalles().clear();
        entity.getDetalles().addAll(detalles);
        BigDecimal total = detalles.stream().map(DetalleSalida::getImporte)
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
        Salida entity = service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salida no encontrada: " + id));
        Integer idSede = entity.getSede() == null ? null : entity.getSede().getIdSede();
        entity.getDetalles().forEach(detalle -> inventarioService.ajustarStock(
                detalle.getProducto().getIdProducto(), idSede, detalle.getCantidad()));
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
    protected SalidaDTO toDto(Salida entity) {
        return new SalidaDTO(entity.getIdSalida(), entity.getFechaSalida(), entity.getUsuario().getIdUsuario(),
            entity.getSede() == null ? null : entity.getSede().getIdSede(), entity.getDestino(),
            entity.getImporteTotal(),
                entity.getDetalles().stream().map(detalle -> new DetalleSalidaDTO(
                        detalle.getIdDetalleSalida(), entity.getIdSalida(), detalle.getProducto().getIdProducto(),
                        detalle.getCantidad(), detalle.getPrecioUnidad(), detalle.getImporte())).toList());
    }
}