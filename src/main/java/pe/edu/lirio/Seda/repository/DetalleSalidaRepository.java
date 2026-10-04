package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import pe.edu.lirio.Seda.model.bd.DetalleSalida;

public interface DetalleSalidaRepository extends JpaRepository<DetalleSalida, Integer> {
	List<DetalleSalida> findAllBySalida_IdSalida(String idSalida);
}