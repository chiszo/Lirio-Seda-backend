package pe.edu.lirio.Seda.service;

import org.springframework.stereotype.Service;
import pe.edu.lirio.Seda.model.bd.Roles;
import pe.edu.lirio.Seda.repository.RolesRepository;

@Service
public class RolesService extends AbstractCrudService<Roles, Integer> {
    public RolesService(RolesRepository repository) {
        super(repository);
    }
}