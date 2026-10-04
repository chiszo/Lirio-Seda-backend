package pe.edu.lirio.Seda.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetallePedido;
import pe.edu.lirio.Seda.model.bd.Pedido;
import pe.edu.lirio.Seda.repository.EstadoRepository;
import pe.edu.lirio.Seda.model.dto.DetallePedidoDTO;
import pe.edu.lirio.Seda.model.dto.PedidoDTO;
import pe.edu.lirio.Seda.repository.ProductoRepository;
import pe.edu.lirio.Seda.repository.UsuariosRepository;
import pe.edu.lirio.Seda.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController extends AbstractCrudController<Pedido, String, PedidoDTO> {
    private final PedidoService service;
    private final UsuariosRepository usuariosRepository;
    private final EstadoRepository estadoRepository;
    private final ProductoRepository productoRepository;

    public PedidoController(PedidoService service, UsuariosRepository usuariosRepository,
            EstadoRepository estadoRepository, ProductoRepository productoRepository) {
        super(service);
        this.service = service;
        this.usuariosRepository = usuariosRepository;
        this.estadoRepository = estadoRepository;
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
        entity.setFechaPedido(dto.getFechaPedido() != null ? dto.getFechaPedido() : LocalDate.now());
        entity.setFechaAprobacion(dto.getFechaAprobacion());
        entity.setIdSedeUsuario(dto.getIdSedeUsuario());
        entity.setUsuario(usuariosRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.getIdUsuario())));
        entity.setEstado(estadoRepository.findById(dto.getIdEstado())
            .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + dto.getIdEstado())));

        var detalles = new ArrayList<DetallePedido>();
        if (dto.getDetalles() != null) {
            for (DetallePedidoDTO detalleDTO : dto.getDetalles()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(entity);
                detalle.setProducto(productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado: " + detalleDTO.getIdProducto())));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalles.add(detalle);
            }
        }
            entity.getDetalles().clear();
            entity.getDetalles().addAll(detalles);
        return entity;
    }

    @Override
    protected PedidoDTO toDto(Pedido entity) {
        return new PedidoDTO(entity.getIdPedido(), entity.getFechaPedido(), entity.getFechaAprobacion(),
            entity.getIdSedeUsuario(), entity.getEstado().getIdEstado(), entity.getUsuario().getIdUsuario(),
            entity.getDetalles().stream().map(detalle -> new DetallePedidoDTO(
                detalle.getCantidad(), entity.getIdPedido(), detalle.getProducto().getIdProducto(),
                detalle.getIdDetallePedido())).toList());
    }
}