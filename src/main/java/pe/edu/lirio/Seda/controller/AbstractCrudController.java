package pe.edu.lirio.Seda.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.service.AbstractCrudService;

public abstract class AbstractCrudController<T, ID, D> {
    private final AbstractCrudService<T, ID> service;

    protected AbstractCrudController(AbstractCrudService<T, ID> service) {
        this.service = service;
    }

    protected abstract ID parseId(String id);

    protected abstract T toEntity(D dto, ID id);

    protected abstract D toDto(T entity);

    @GetMapping
    @Transactional(readOnly = true)
    public List<D> listar() {
        return service.listar().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public D buscarPorId(@PathVariable String id) {
        return service.buscarPorId(parseId(id))
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado: " + id));
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public D crear(@RequestBody D dto) {
        return toDto(service.guardar(toEntity(dto, null)));
    }

    @PutMapping("/{id}")
    @Transactional
    public D actualizar(@PathVariable String id, @RequestBody D dto) {
        ID parsedId = parseId(id);
        if (service.buscarPorId(parsedId).isEmpty()) {
            throw new ResourceNotFoundException("Recurso no encontrado: " + id);
        }
        return toDto(service.guardar(toEntity(dto, parsedId)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        ID parsedId = parseId(id);
        if (service.buscarPorId(parsedId).isEmpty()) {
            throw new ResourceNotFoundException("Recurso no encontrado: " + id);
        }
        service.eliminar(parsedId);
    }
}