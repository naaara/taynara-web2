package br.ueg.trindade.taynara_web2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.taynara_web2.model.Permissao;
import br.ueg.trindade.taynara_web2.model.Usuario;
import br.ueg.trindade.taynara_web2.repository.UsuarioRepository;

/** Ensina o Spring Security a buscar o usuário no banco (login). */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        List<GrantedAuthority> autoridades = new ArrayList<>();
        if (u.getPerfil() != null) {
            autoridades.add(new SimpleGrantedAuthority("ROLE_" + u.getPerfil().getNome()));
            for (Permissao p : u.getPerfil().getPermissoes()) {
                autoridades.add(new SimpleGrantedAuthority(p.getNome()));
            }
        }
        return new User(u.getUsername(), u.getSenha(), autoridades);
    }
}
