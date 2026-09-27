package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Marca;
import pe.edu.lirio.Seda.repository.MarcaRepository;

@Service
public class MarcaService extends AbstractCrudService<Marca, Integer> {
    public MarcaService(MarcaRepository repository) {
        super(repository);
    }
}