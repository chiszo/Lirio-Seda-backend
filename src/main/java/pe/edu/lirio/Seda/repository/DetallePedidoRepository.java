package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.lirio.Seda.model.bd.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
}