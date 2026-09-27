package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Pedido;
import pe.edu.lirio.Seda.repository.PedidoRepository;

@Service
public class PedidoService extends AbstractCrudService<Pedido, String> {
    public PedidoService(PedidoRepository repository) {
        super(repository);
    }
}