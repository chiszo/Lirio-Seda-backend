package pe.edu.lirio.Seda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetalleSalida;
import pe.edu.lirio.Seda.model.bd.Salida;
import pe.edu.lirio.Seda.model.dto.DetalleSalidaDTO;
import pe.edu.lirio.Seda.repository.SalidaRepository;
import pe.edu.lirio.Seda.service.DetalleSalidaService;
import pe.edu.lirio.Seda.service.InventarioService;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-salida")
public class DetalleSalidaController {
    private final DetalleSalidaService service;
    private final SalidaRepository salidaRepository;
    private final InventarioService inventarioService;

    public DetalleSalidaController(DetalleSalidaService service, SalidaRepository salidaRepository,
            InventarioService inventarioService) {
        this.service = service;
        this.salidaRepository = salidaRepository;
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<DetalleSalidaDTO> listar() {
        return service.listar().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public DetalleSalidaDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de salida no encontrado: " + id));
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public DetalleSalidaDTO crear(@RequestBody DetalleSalidaDTO dto) {
        return toDto(service.guardar(toEntity(dto, null)));
    }

    @PutMapping("/{id}")
    @Transactional
    public DetalleSalidaDTO actualizar(@PathVariable Integer id, @RequestBody DetalleSalidaDTO dto) {
        DetalleSalida anterior = service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de salida no encontrado: " + id));
        Integer sedeAnterior = anterior.getSalida().getIdSedeUsuario();
        if (sedeAnterior != null && anterior.getCantidad() != null) {
            inventarioService.ajustarStock(anterior.getProducto().getIdProducto(), sedeAnterior,
                    anterior.getCantidad());
        }
        return toDto(service.guardar(toEntity(dto, id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        DetalleSalida detalle = service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de salida no encontrado: " + id));
        Integer idSede = detalle.getSalida().getIdSedeUsuario();
        if (idSede != null && detalle.getCantidad() != null) {
            inventarioService.ajustarStock(detalle.getProducto().getIdProducto(), idSede, detalle.getCantidad());
        }
        service.eliminar(id);
    }

    private DetalleSalida toEntity(DetalleSalidaDTO dto, Integer id) {
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que cero");
        }
        Salida salida = salidaRepository.findById(dto.getIdSalida())
                .orElseThrow(() -> new ResourceNotFoundException("Salida no encontrada: " + dto.getIdSalida()));
        if (salida.getIdSedeUsuario() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La salida no tiene sede asignada");
        }
        DetalleSalida detalle = id == null ? new DetalleSalida() : service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de salida no encontrado: " + id));
        detalle.setIdDetalleSalida(id);
        detalle.setSalida(salida);
        detalle.setProducto(inventarioService.ajustarStock(dto.getIdProducto(), salida.getIdSedeUsuario(),
                -dto.getCantidad()));
        detalle.setCantidad(dto.getCantidad());
        detalle.setIdProductoDuplicado(dto.getIdProducto());
        detalle.setIdSalidaDuplicado(dto.getIdSalida());
        return detalle;
    }

    private DetalleSalidaDTO toDto(DetalleSalida entity) {
        return new DetalleSalidaDTO(entity.getCantidad(), entity.getProducto().getIdProducto(),
            entity.getSalida().getIdSalida(), entity.getIdDetalleSalida());
    }
}