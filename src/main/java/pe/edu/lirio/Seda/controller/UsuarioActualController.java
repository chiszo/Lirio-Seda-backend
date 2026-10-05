package pe.edu.lirio.Seda.controller;

import java.security.Principal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.lirio.Seda.exception.ResourceNotFoundException;
import pe.edu.lirio.Seda.model.bd.Usuarios;
import pe.edu.lirio.Seda.model.dto.UsuariosDTO;
import pe.edu.lirio.Seda.repository.UsuariosRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioActualController {
    private final UsuariosRepository usuariosRepository;

    public UsuarioActualController(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UsuariosDTO usuarioActual(Principal principal) {
        Usuarios usuario = usuariosRepository.findByCorreo(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + principal.getName()));

        return new UsuariosDTO(usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellido(),
                usuario.getCorreo(), usuario.getTelefono(), usuario.getDocumento(),
                usuario.getFechaCreacion(), null, usuario.getRol().getIdRol(),
                usuario.getSede().getIdSede(), usuario.getActivo());
    }
}
