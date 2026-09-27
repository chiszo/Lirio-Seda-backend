package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.DetallePedido;
import pe.edu.lirio.Seda.repository.DetallePedidoRepository;

@Service
public class DetallePedidoService extends AbstractCrudService<DetallePedido, Integer> {
    public DetallePedidoService(DetallePedidoRepository repository) {
        super(repository);
    }
}