package pe.edu.lirio.Seda.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractCrudService<T, ID> {
    private final JpaRepository<T, ID> repository;

    protected AbstractCrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> listar() {
        return repository.findAll();
    }

    public Optional<T> buscarPorId(ID id) {
        return repository.findById(id);
    }

    public T guardar(T entidad) {
        return repository.save(entidad);
    }

    public void eliminar(ID id) {
        repository.deleteById(id);
    }
}