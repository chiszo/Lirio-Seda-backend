package pe.edu.lirio.Seda.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.lirio.Seda.model.bd.ProductoSede;
import pe.edu.lirio.Seda.model.bd.ProductoSedeId;

public interface ProductoSedeRepository extends JpaRepository<ProductoSede, ProductoSedeId> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select ps from ProductoSede ps where ps.id.idProducto = :idProducto and ps.id.idSede = :idSede")
	Optional<ProductoSede> findByProductoAndSedeForUpdate(
			@Param("idProducto") String idProducto,
			@Param("idSede") Integer idSede);
}