package pe.edu.lirio.Seda.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.lirio.Seda.model.bd.Producto;

public interface ProductoRepository extends JpaRepository<Producto, String> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Producto p where p.idProducto = :idProducto")
	Optional<Producto> findByIdForUpdate(@Param("idProducto") String idProducto);
}