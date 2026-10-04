package pe.edu.lirio.Seda.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
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
import pe.edu.lirio.Seda.model.bd.DetallePedido;
import pe.edu.lirio.Seda.model.dto.DetallePedidoDTO;
import pe.edu.lirio.Seda.repository.PedidoRepository;
import pe.edu.lirio.Seda.repository.ProductoRepository;
import pe.edu.lirio.Seda.service.DetallePedidoService;

@RestController
@RequestMapping("/api/detalles-pedido")
public class DetallePedidoController {
    private final DetallePedidoService service;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public DetallePedidoController(DetallePedidoService service, PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {
        this.service = service;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
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

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public DetallePedidoDTO crear(@RequestBody DetallePedidoDTO dto) {
        return toDto(service.guardar(toEntity(dto, null)));
    }

    @PutMapping("/{id}")
    @Transactional
    public DetallePedidoDTO actualizar(@PathVariable Integer id, @RequestBody DetallePedidoDTO dto) {
        if (service.buscarPorId(id).isEmpty()) {
            throw new ResourceNotFoundException("Detalle de pedido no encontrado: " + id);
        }
        return toDto(service.guardar(toEntity(dto, id)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) {
            throw new ResourceNotFoundException("Detalle de pedido no encontrado: " + id);
        }
        service.eliminar(id);
    }

    private DetallePedido toEntity(DetallePedidoDTO dto, Integer id) {
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que cero");
        }
        DetallePedido entity = id == null ? new DetallePedido() : service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de pedido no encontrado: " + id));
        entity.setIdDetallePedido(id);
        entity.setPedido(pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + dto.getIdPedido())));
        entity.setProducto(productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + dto.getIdProducto())));
        entity.setCantidad(dto.getCantidad());
        return entity;
    }

    private DetallePedidoDTO toDto(DetallePedido entity) {
        return new DetallePedidoDTO(entity.getCantidad(), entity.getPedido().getIdPedido(),
            entity.getProducto().getIdProducto(), entity.getIdDetallePedido());
    }
}