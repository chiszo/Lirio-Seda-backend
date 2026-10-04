package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import pe.edu.lirio.Seda.model.bd.DetalleEntrada;

public interface DetalleEntradaRepository extends JpaRepository<DetalleEntrada, Integer> {
	List<DetalleEntrada> findAllByEntrada_IdEntrada(String idEntrada);
}