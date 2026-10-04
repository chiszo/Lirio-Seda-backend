package pe.edu.lirio.Seda.controller;

import java.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.Roles;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.model.bd.Usuarios;
import pe.edu.lirio.Seda.model.dto.UsuariosDTO;
import pe.edu.lirio.Seda.repository.RolesRepository;
import pe.edu.lirio.Seda.repository.SedeRepository;
import pe.edu.lirio.Seda.service.UsuariosService;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UsuariosController extends AbstractCrudController<Usuarios, Integer, UsuariosDTO> {
    private final UsuariosService service;
    private final RolesRepository rolesRepository;
    private final SedeRepository sedeRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosController(UsuariosService service, RolesRepository rolesRepository,
            SedeRepository sedeRepository, PasswordEncoder passwordEncoder) {
        super(service);
        this.service = service;
        this.rolesRepository = rolesRepository;
        this.sedeRepository = sedeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Integer parseId(String id) {
        return Integer.valueOf(id);
    }

    @Override
    protected Usuarios toEntity(UsuariosDTO dto, Integer id) {
        Usuarios entity = id == null ? new Usuarios() : service.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
        entity.setIdUsuario(id != null ? id : dto.getIdUsuario());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setDocumento(dto.getDocumento());
        if (dto.getClave() != null && !dto.getClave().isBlank()) {
            entity.setClave(passwordEncoder.encode(dto.getClave()));
        }
        Roles rol = rolesRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + dto.getIdRol()));
        entity.setRol(rol);
        Sede sede = sedeRepository.findById(dto.getIdSede())
                .orElseThrow(() -> new ResourceNotFoundException("Sede no encontrada: " + dto.getIdSede()));
        entity.setSede(sede);
        if (id == null) {
            entity.setFechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion() : LocalDate.now());
        } else if (dto.getFechaCreacion() != null) {
            entity.setFechaCreacion(dto.getFechaCreacion());
        }
        if (dto.getActivo() != null) {
            entity.setActivo(dto.getActivo());
        } else if (id == null) {
            entity.setActivo("S");
        }
        return entity;
    }

    @Override
    protected UsuariosDTO toDto(Usuarios entity) {
        return new UsuariosDTO(entity.getIdUsuario(), entity.getNombre(), entity.getApellido(), entity.getCorreo(),
            entity.getTelefono(), entity.getDocumento(), entity.getFechaCreacion(), null,
            entity.getRol().getIdRol(), entity.getSede().getIdSede(), entity.getActivo());
    }
}