package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.lirio.Seda.model.bd.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
}