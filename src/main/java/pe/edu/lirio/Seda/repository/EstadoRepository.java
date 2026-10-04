package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.lirio.Seda.model.bd.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}