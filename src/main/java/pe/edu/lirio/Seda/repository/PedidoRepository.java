package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.lirio.Seda.model.bd.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
}