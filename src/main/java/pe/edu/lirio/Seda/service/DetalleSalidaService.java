package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.DetalleSalida;
import pe.edu.lirio.Seda.repository.DetalleSalidaRepository;

@Service
public class DetalleSalidaService extends AbstractCrudService<DetalleSalida, Integer> {
    public DetalleSalidaService(DetalleSalidaRepository repository) {
        super(repository);
    }
}