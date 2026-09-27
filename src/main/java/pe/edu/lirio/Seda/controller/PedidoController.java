package pe.edu.lirio.Seda.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetallePedido;
import pe.edu.lirio.Seda.model.bd.Pedido;
import pe.edu.lirio.Seda.model.dto.DetallePedidoDTO;
import pe.edu.lirio.Seda.model.dto.PedidoDTO;
import pe.edu.lirio.Seda.repository.ProductoRepository;
import pe.edu.lirio.Seda.repository.ProveedorRepository;
import pe.edu.lirio.Seda.repository.UsuariosRepository;
import pe.edu.lirio.Seda.service.MovimientoCalculations;
import pe.edu.lirio.Seda.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController extends AbstractCrudController<Pedido, String, PedidoDTO> {
    private final PedidoService service;
    private final UsuariosRepository usuariosRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    public PedidoController(PedidoService service, UsuariosRepository usuariosRepository,
            ProveedorRepository proveedorRepository, ProductoRepository productoRepository) {
        super(service);
        this.service = service;
        this.usuariosRepository = usuariosRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Pedido toEntity(PedidoDTO dto, String id) {
        Pedido entity = id == null ? new Pedido() : service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        entity.setIdPedido(id != null ? id : dto.getIdPedido());
        entity.setFechaPedido(dto.getFechaPedido() != null ? dto.getFechaPedido() : LocalDateTime.now());
        entity.setUsuario(dto.getIdUsuario() == null ? null : usuariosRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.getIdUsuario())));
        entity.setProveedor(dto.getIdProveedor() == null ? null : proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + dto.getIdProveedor())));

        var detalles = new ArrayList<DetallePedido>();
        if (dto.getDetalles() != null) {
            for (DetallePedidoDTO detalleDTO : dto.getDetalles()) {
                DetallePedido detalle = new DetallePedido();
                if (id != null) {
                    detalle.setIdDetallePedido(detalleDTO.getIdDetallePedido());
                }
                detalle.setPedido(entity);
                detalle.setProducto(productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado: " + detalleDTO.getIdProducto())));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnidad(detalleDTO.getPrecioUnidad());
                detalle.setImporte(MovimientoCalculations.calcularImporte(
                    detalleDTO.getCantidad(), detalleDTO.getPrecioUnidad()));
                detalles.add(detalle);
            }
        }
            entity.getDetalles().clear();
            entity.getDetalles().addAll(detalles);
        BigDecimal total = detalles.stream().map(DetallePedido::getImporte)
                .filter(value -> value != null).reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setImporteTotal(total);
        return entity;
    }

    @Override
    protected PedidoDTO toDto(Pedido entity) {
        return new PedidoDTO(entity.getIdPedido(), entity.getFechaPedido(),
                entity.getUsuario() == null ? null : entity.getUsuario().getIdUsuario(),
                entity.getProveedor() == null ? null : entity.getProveedor().getIdProveedor(), entity.getImporteTotal(),
                entity.getDetalles().stream().map(detalle -> new DetallePedidoDTO(
                        detalle.getIdDetallePedido(), entity.getIdPedido(), detalle.getProducto().getIdProducto(),
                        detalle.getCantidad(), detalle.getPrecioUnidad(), detalle.getImporte())).toList());
    }
}