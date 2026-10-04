package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Motivo;
import pe.edu.lirio.Seda.repository.MotivoRepository;

@Service
public class MotivoService extends AbstractCrudService<Motivo, Integer> {
    public MotivoService(MotivoRepository repository) {
        super(repository);
    }
}