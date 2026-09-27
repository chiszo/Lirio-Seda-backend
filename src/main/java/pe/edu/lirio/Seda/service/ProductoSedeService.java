package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.ProductoSede;
import pe.edu.lirio.Seda.model.bd.ProductoSedeId;
import pe.edu.lirio.Seda.repository.ProductoSedeRepository;

@Service
public class ProductoSedeService extends AbstractCrudService<ProductoSede, ProductoSedeId> {
    public ProductoSedeService(ProductoSedeRepository repository) {
        super(repository);
    }
}