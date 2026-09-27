package pe.edu.lirio.Seda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetallePedido;
import pe.edu.lirio.Seda.model.dto.DetallePedidoDTO;
import pe.edu.lirio.Seda.service.DetallePedidoService;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
public class DetallePedidoController {
    private final DetallePedidoService service;

    public DetallePedidoController(DetallePedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DetallePedidoDTO> listar() {
        return service.listar().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public DetallePedidoDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de pedido no encontrado: " + id));
    }

    private DetallePedidoDTO toDto(DetallePedido entity) {
        return new DetallePedidoDTO(entity.getIdDetallePedido(), entity.getPedido().getIdPedido(),
                entity.getProducto().getIdProducto(), entity.getCantidad(), entity.getPrecioUnidad(), entity.getImporte());
    }
}