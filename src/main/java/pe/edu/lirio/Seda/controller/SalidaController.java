package pe.edu.lirio.Seda.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetalleSalida;
import pe.edu.lirio.Seda.model.bd.Salida;
import pe.edu.lirio.Seda.repository.MotivoRepository;
import pe.edu.lirio.Seda.model.dto.DetalleSalidaDTO;
import pe.edu.lirio.Seda.model.dto.SalidaDTO;
import pe.edu.lirio.Seda.repository.UsuariosRepository;
import pe.edu.lirio.Seda.service.SalidaService;
import pe.edu.lirio.Seda.service.InventarioService;

@RestController
@RequestMapping("/api/salidas")
public class SalidaController extends AbstractCrudController<Salida, String, SalidaDTO> {
    private final SalidaService service;
    private final UsuariosRepository usuariosRepository;
    private final MotivoRepository motivoRepository;
    private final InventarioService inventarioService;

    public SalidaController(SalidaService service, UsuariosRepository usuariosRepository,
            MotivoRepository motivoRepository, InventarioService inventarioService) {
        super(service);
        this.service = service;
        this.usuariosRepository = usuariosRepository;
        this.motivoRepository = motivoRepository;
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
            Integer sedeAnterior = entity.getIdSedeUsuario();
            entity.getDetalles().forEach(detalle -> inventarioService.ajustarStock(
                detalle.getProducto().getIdProducto(), sedeAnterior, detalle.getCantidad()));
        }
        if (dto.getIdSedeUsuario() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
            HttpStatus.BAD_REQUEST, "idSedeUsuario es obligatorio para registrar una salida");
        }
        entity.setIdSalida(id != null ? id : dto.getIdSalida());
        entity.setFechaSalida(dto.getFechaSalida() != null ? dto.getFechaSalida() : LocalDate.now());
        entity.setUsuario(usuariosRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.getIdUsuario())));
        entity.setIdSedeUsuario(dto.getIdSedeUsuario());
        entity.setMotivo(motivoRepository.findById(dto.getIdMotivo())
            .orElseThrow(() -> new ResourceNotFoundException("Motivo no encontrado: " + dto.getIdMotivo())));

        var detalles = new ArrayList<DetalleSalida>();
        if (dto.getDetalles() != null) {
            for (DetalleSalidaDTO detalleDTO : dto.getDetalles()) {
                DetalleSalida detalle = new DetalleSalida();
                detalle.setSalida(entity);
                detalle.setProducto(inventarioService.ajustarStock(
                    detalleDTO.getIdProducto(), dto.getIdSedeUsuario(), -validarCantidad(detalleDTO.getCantidad())));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setIdProductoDuplicado(detalleDTO.getIdProducto());
                detalle.setIdSalidaDuplicado(id != null ? id : dto.getIdSalida());
                detalles.add(detalle);
            }
        }
        entity.getDetalles().clear();
        entity.getDetalles().addAll(detalles);
        return entity;
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        Salida entity = service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salida no encontrada: " + id));
        Integer idSede = entity.getIdSedeUsuario();
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
        return new SalidaDTO(entity.getIdSalida(), entity.getFechaSalida(), entity.getIdSedeUsuario(),
            entity.getMotivo().getIdMotivo(), entity.getUsuario().getIdUsuario(),
            entity.getDetalles().stream().map(detalle -> new DetalleSalidaDTO(
                detalle.getCantidad(), detalle.getProducto().getIdProducto(), entity.getIdSalida(),
                detalle.getIdDetalleSalida())).toList());
    }
}