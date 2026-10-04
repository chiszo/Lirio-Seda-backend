package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import pe.edu.lirio.Seda.model.bd.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
	List<DetallePedido> findAllByPedido_IdPedido(String idPedido);
}