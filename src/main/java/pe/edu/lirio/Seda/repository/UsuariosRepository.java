package pe.edu.lirio.Seda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import pe.edu.lirio.Seda.model.bd.Usuarios;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
	Optional<Usuarios> findByUsuario(String usuario);
}