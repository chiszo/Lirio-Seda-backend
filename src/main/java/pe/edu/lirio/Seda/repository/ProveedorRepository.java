package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.lirio.Seda.model.bd.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, String> {
}