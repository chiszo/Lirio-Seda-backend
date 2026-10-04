package pe.edu.lirio.Seda.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.ProductoSede;
import pe.edu.lirio.Seda.model.bd.ProductoSedeId;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.repository.ProductoRepository;
import pe.edu.lirio.Seda.repository.ProductoSedeRepository;
import pe.edu.lirio.Seda.repository.SedeRepository;

@Service
public class InventarioService {
    private final ProductoSedeRepository productoSedeRepository;
    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;

    public InventarioService(
            ProductoSedeRepository productoSedeRepository,
            ProductoRepository productoRepository,
            SedeRepository sedeRepository) {
        this.productoSedeRepository = productoSedeRepository;
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Transactional
    public Producto ajustarStock(String idProducto, Integer idSede, int variacion) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + idProducto));
        if (idSede == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La sede es obligatoria para modificar el stock");
        }

        ProductoSede productoSede = null;
        long nuevoStockSede = 0;
        productoSede = productoSedeRepository.findByProductoAndSedeForUpdate(idProducto, idSede)
            .orElse(null);
        int stockActualSede = productoSede == null || productoSede.getStock() == null
            ? 0 : productoSede.getStock();
        nuevoStockSede = (long) stockActualSede + variacion;
        if (nuevoStockSede < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Stock insuficiente para el producto " + idProducto + " en la sede " + idSede);
        }
        validarLimiteStock(nuevoStockSede, idProducto);

        if (productoSede == null && variacion > 0) {
            Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada: " + idSede));
            productoSede = new ProductoSede();
            productoSede.setId(new ProductoSedeId(idProducto, idSede));
            productoSede.setProducto(producto);
            productoSede.setSede(sede);
        }
        if (productoSede != null) {
            productoSede.setStock((int) nuevoStockSede);
            productoSedeRepository.save(productoSede);
        }
        return producto;
    }

    private void validarLimiteStock(long stock, String idProducto) {
        if (stock > Integer.MAX_VALUE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El stock excede el límite permitido para el producto " + idProducto);
        }
    }
}