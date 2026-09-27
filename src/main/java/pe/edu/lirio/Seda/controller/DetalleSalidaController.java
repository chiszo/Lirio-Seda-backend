package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetalleSalida;
import pe.edu.lirio.Seda.model.dto.DetalleSalidaDTO;
import pe.edu.lirio.Seda.service.DetalleSalidaService;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-salida")
public class DetalleSalidaController {
    private final DetalleSalidaService service;

    public DetalleSalidaController(DetalleSalidaService service) {
        this.service = service;
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

    private DetalleSalidaDTO toDto(DetalleSalida entity) {
        return new DetalleSalidaDTO(entity.getIdDetalleSalida(), entity.getSalida().getIdSalida(),
                entity.getProducto().getIdProducto(), entity.getCantidad(), entity.getPrecioUnidad(), entity.getImporte());
    }
}