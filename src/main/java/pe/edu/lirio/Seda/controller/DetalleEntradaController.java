package pe.edu.lirio.Seda.controller;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.DetalleEntrada;
import pe.edu.lirio.Seda.model.bd.Entrada;
import pe.edu.lirio.Seda.model.dto.DetalleEntradaDTO;
import pe.edu.lirio.Seda.repository.DetalleEntradaRepository;
import pe.edu.lirio.Seda.service.EntradaService;
import pe.edu.lirio.Seda.service.DetalleEntradaService;
import pe.edu.lirio.Seda.service.InventarioService;
import pe.edu.lirio.Seda.service.MovimientoCalculations;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-entrada")
public class DetalleEntradaController {
    private final DetalleEntradaService service;
    private final DetalleEntradaRepository repository;
    private final EntradaService entradaService;
    private final InventarioService inventarioService;

    public DetalleEntradaController(DetalleEntradaService service, DetalleEntradaRepository repository,
            EntradaService entradaService, InventarioService inventarioService) {
        this.service = service;
        this.repository = repository;
        this.entradaService = entradaService;
        this.inventarioService = inventarioService;
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

        @PostMapping
        @Transactional
        @ResponseStatus(HttpStatus.CREATED)
        public DetalleEntradaDTO crear(@RequestBody DetalleEntradaDTO dto) {
        DetalleEntrada detalle = service.guardar(toEntity(dto, null));
        recalcularImporteEntrada(detalle.getEntrada().getIdEntrada());
        return toDto(detalle);
        }

        @PutMapping("/{id}")
        @Transactional
        public DetalleEntradaDTO actualizar(@PathVariable Integer id, @RequestBody DetalleEntradaDTO dto) {
        DetalleEntrada anterior = service.buscarPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de entrada no encontrado: " + id));
        String entradaAnteriorId = anterior.getEntrada().getIdEntrada();
        Integer sedeAnterior = anterior.getEntrada().getIdSedeUsuario();
        if (sedeAnterior != null && anterior.getCantidad() != null) {
            inventarioService.ajustarStock(anterior.getProducto().getIdProducto(), sedeAnterior,
                -anterior.getCantidad());
        }

        DetalleEntrada actualizado = service.guardar(toEntity(dto, id));
        recalcularImporteEntrada(entradaAnteriorId);
        if (!entradaAnteriorId.equals(actualizado.getEntrada().getIdEntrada())) {
            recalcularImporteEntrada(actualizado.getEntrada().getIdEntrada());
        }
        return toDto(actualizado);
        }

        @DeleteMapping("/{id}")
        @Transactional
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void eliminar(@PathVariable Integer id) {
        DetalleEntrada detalle = service.buscarPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de entrada no encontrado: " + id));
        String entradaId = detalle.getEntrada().getIdEntrada();
        Integer idSede = detalle.getEntrada().getIdSedeUsuario();
        if (idSede != null && detalle.getCantidad() != null) {
            inventarioService.ajustarStock(detalle.getProducto().getIdProducto(), idSede,
                -detalle.getCantidad());
        }
        service.eliminar(id);
        recalcularImporteEntrada(entradaId);
        }

        private DetalleEntrada toEntity(DetalleEntradaDTO dto, Integer id) {
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que cero");
        }
        Entrada entrada = entradaService.buscarPorId(dto.getIdEntrada())
            .orElseThrow(() -> new ResourceNotFoundException("Entrada no encontrada: " + dto.getIdEntrada()));
        if (entrada.getIdSedeUsuario() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La entrada no tiene sede asignada");
        }
        DetalleEntrada detalle = id == null ? new DetalleEntrada() : service.buscarPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de entrada no encontrado: " + id));
        detalle.setIdDetalleEntrada(id);
        detalle.setEntrada(entrada);
        detalle.setProducto(inventarioService.ajustarStock(dto.getIdProducto(), entrada.getIdSedeUsuario(),
            dto.getCantidad()));
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        MovimientoCalculations.calcularImporte(dto.getCantidad(), dto.getPrecioUnitario());
        return detalle;
        }

        private void recalcularImporteEntrada(String entradaId) {
        Entrada entrada = entradaService.buscarPorId(entradaId)
            .orElseThrow(() -> new ResourceNotFoundException("Entrada no encontrada: " + entradaId));
        BigDecimal total = repository.findAllByEntrada_IdEntrada(entradaId).stream()
            .map(detalle -> MovimientoCalculations.calcularImporte(
                detalle.getCantidad(), detalle.getPrecioUnitario()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        entrada.setImporteTotal(total);
        entradaService.guardar(entrada);
        }

    private DetalleEntradaDTO toDto(DetalleEntrada entity) {
        return new DetalleEntradaDTO(entity.getCantidad(), entity.getPrecioUnitario(),
            entity.getEntrada().getIdEntrada(), entity.getProducto().getIdProducto(), entity.getIdDetalleEntrada());
    }
}