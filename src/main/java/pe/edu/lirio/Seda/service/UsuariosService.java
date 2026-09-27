package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Usuarios;
import pe.edu.lirio.Seda.repository.UsuariosRepository;

@Service
public class UsuariosService extends AbstractCrudService<Usuarios, Integer> {
    public UsuariosService(UsuariosRepository repository) {
        super(repository);
    }
}