package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetalleEntrada;
import pe.edu.lirio.Seda.model.dto.DetalleEntradaDTO;
import pe.edu.lirio.Seda.service.DetalleEntradaService;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-entrada")
public class DetalleEntradaController {
    private final DetalleEntradaService service;

    public DetalleEntradaController(DetalleEntradaService service) {
        this.service = service;
    }

    @GetMapping
    public List<DetalleEntradaDTO> listar() {
        return service.listar().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public DetalleEntradaDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de entrada no encontrado: " + id));
    }

    private DetalleEntradaDTO toDto(DetalleEntrada entity) {
        return new DetalleEntradaDTO(entity.getIdDetalleEntrada(), entity.getEntrada().getIdEntrada(),
                entity.getProducto().getIdProducto(), entity.getCantidad(), entity.getPrecioUnidad(), entity.getImporte());
    }
}