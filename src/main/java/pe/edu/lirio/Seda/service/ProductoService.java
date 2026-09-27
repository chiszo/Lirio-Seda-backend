package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.repository.ProductoRepository;

@Service
public class ProductoService extends AbstractCrudService<Producto, String> {
    public ProductoService(ProductoRepository repository) {
        super(repository);
    }
}