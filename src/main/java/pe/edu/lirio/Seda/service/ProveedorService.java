package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Proveedor;
import pe.edu.lirio.Seda.repository.ProveedorRepository;

@Service
public class ProveedorService extends AbstractCrudService<Proveedor, String> {
    public ProveedorService(ProveedorRepository repository) {
        super(repository);
    }
}