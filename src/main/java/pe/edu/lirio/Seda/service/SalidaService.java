package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Salida;
import pe.edu.lirio.Seda.repository.SalidaRepository;

@Service
public class SalidaService extends AbstractCrudService<Salida, String> {
    public SalidaService(SalidaRepository repository) {
        super(repository);
    }
}