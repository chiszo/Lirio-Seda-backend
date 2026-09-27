package pe.edu.lirio.Seda.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.lirio.Seda.model.bd.Usuarios;
import pe.edu.lirio.Seda.repository.UsuariosRepository;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {
    private final UsuariosRepository usuariosRepository;

    public UsuarioDetailsServiceImpl(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(usuario.getUsuario())
                .password(usuario.getContrasena())
                .authorities("ROLE_" + usuario.getRol().getNombre())
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();
    }
}