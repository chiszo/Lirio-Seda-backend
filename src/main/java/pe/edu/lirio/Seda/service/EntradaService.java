package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Entrada;
import pe.edu.lirio.Seda.repository.EntradaRepository;

@Service
public class EntradaService extends AbstractCrudService<Entrada, String> {
    public EntradaService(EntradaRepository repository) {
        super(repository);
    }
}