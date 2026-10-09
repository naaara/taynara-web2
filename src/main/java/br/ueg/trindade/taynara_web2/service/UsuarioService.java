package br.ueg.trindade.taynara_web2.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.dto.UsuarioDto;
import br.ueg.trindade.taynara_web2.dto.UsuarioRequest;
import br.ueg.trindade.taynara_web2.model.Usuario;
import br.ueg.trindade.taynara_web2.repository.PerfilRepository;
import br.ueg.trindade.taynara_web2.repository.PostagemRepository;
import br.ueg.trindade.taynara_web2.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PostagemRepository postagemRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PerfilRepository perfilRepository,
                          PostagemRepository postagemRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.postagemRepository = postagemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioDto::new).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorId(Long id) {
        return new UsuarioDto(obter(id));
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorUsername(String username) {
        return new UsuarioDto(usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")));
    }

    /** Cadastro: valida, criptografa a senha e atribui o perfil padrão USUARIO. */
    @Transactional
    public UsuarioDto criar(UsuarioRequest r) {
        validar(r, null, true);
        Usuario u = new Usuario();
        u.setNome(r.nome().trim());
        u.setUsername(r.username().trim());
        u.setEmail(r.email().trim().toLowerCase());
        u.setSenha(passwordEncoder.encode(r.senha()));
        u.setPerfil(perfilRepository.findByNome("USUARIO").orElse(null));
        return new UsuarioDto(usuarioRepository.save(u));
    }

    /** Atualização: a senha só é trocada se for informada. */
    @Transactional
    public UsuarioDto atualizar(Long id, UsuarioRequest r) {
        Usuario u = obter(id);
        validar(r, id, false);
        u.setNome(r.nome().trim());
        u.setUsername(r.username().trim());
        u.setEmail(r.email().trim().toLowerCase());
        if (r.senha() != null && !r.senha().isBlank()) {
            u.setSenha(passwordEncoder.encode(r.senha()));
        }
        return new UsuarioDto(usuarioRepository.save(u));
    }

    @Transactional
    public void excluir(Long id) {
        Usuario u = obter(id);
        if ("admin".equals(u.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O usuário admin não pode ser excluído");
        }
        postagemRepository.deleteByAutorId(id);
        usuarioRepository.delete(u);
    }

    private Usuario obter(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private void validar(UsuarioRequest r, Long idAtual, boolean senhaObrigatoria) {
        if (r == null || vazio(r.nome()) || vazio(r.username()) || vazio(r.email())) {
            throw erro("Nome, username e e-mail são obrigatórios");
        }
        String username = r.username().trim();
        String email = r.email().trim().toLowerCase();
        if (username.length() < 3 || username.contains(" ")) {
            throw erro("O username deve ter ao menos 3 caracteres e não pode ter espaços");
        }
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            throw erro("E-mail inválido");
        }
        if (senhaObrigatoria && vazio(r.senha())) {
            throw erro("A senha é obrigatória");
        }
        if (!vazio(r.senha()) && r.senha().length() < 6) {
            throw erro("A senha deve ter ao menos 6 caracteres");
        }
        usuarioRepository.findByUsername(username)
                .filter(x -> !x.getId().equals(idAtual))
                .ifPresent(x -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já está em uso"); });
        usuarioRepository.findByEmail(email)
                .filter(x -> !x.getId().equals(idAtual))
                .ifPresent(x -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já está em uso"); });
    }

    private static boolean vazio(String s) {
        return s == null || s.isBlank();
    }

    private static ResponseStatusException erro(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
