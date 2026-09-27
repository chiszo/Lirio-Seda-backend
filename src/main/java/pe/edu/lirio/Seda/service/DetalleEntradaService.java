package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.DetalleEntrada;
import pe.edu.lirio.Seda.repository.DetalleEntradaRepository;

@Service
public class DetalleEntradaService extends AbstractCrudService<DetalleEntrada, Integer> {
    public DetalleEntradaService(DetalleEntradaRepository repository) {
        super(repository);
    }
}