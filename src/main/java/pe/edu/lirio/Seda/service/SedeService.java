package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.repository.SedeRepository;

@Service
public class SedeService extends AbstractCrudService<Sede, Integer> {
    public SedeService(SedeRepository repository) {
        super(repository);
    }
}