package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Estado;
import pe.edu.lirio.Seda.repository.EstadoRepository;

@Service
public class EstadoService extends AbstractCrudService<Estado, Integer> {
    public EstadoService(EstadoRepository repository) {
        super(repository);
    }
}