package br.ueg.trindade.taynara_web2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.model.Usuario;
import br.ueg.trindade.taynara_web2.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Usuario criar(Usuario dados) {
        validar(dados, null);
        if (dados.getSenha() == null || dados.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha é obrigatória");
        }
        Usuario usuario = new Usuario();
        copiar(usuario, dados);
        usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscarPorId(id);
        validar(dados, id);
        copiar(usuario, dados);
        // a senha só muda se uma nova for informada
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.delete(buscarPorId(id));
    }

    private void copiar(Usuario destino, Usuario origem) {
        destino.setNome(origem.getNome().trim());
        destino.setUsername(origem.getUsername().trim());
        destino.setEmail(origem.getEmail().trim().toLowerCase());
    }

    // Regras de negócio: campos obrigatórios, e-mail válido e username/e-mail sem duplicidade
    private void validar(Usuario dados, Long idAtual) {
        if (vazio(dados.getNome()) || vazio(dados.getUsername()) || vazio(dados.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome, username e e-mail são obrigatórios");
        }
        String email = dados.getEmail().trim().toLowerCase();
        if (!email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido");
        }
        if (dados.getSenha() != null && !dados.getSenha().isBlank() && dados.getSenha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter ao menos 6 caracteres");
        }
        usuarioRepository.findByEmail(email)
                .filter(u -> !u.getId().equals(idAtual))
                .ifPresent(u -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado"); });
        usuarioRepository.findByUsername(dados.getUsername().trim())
                .filter(u -> !u.getId().equals(idAtual))
                .ifPresent(u -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já cadastrado"); });
    }

    private static boolean vazio(String s) {
        return s == null || s.isBlank();
    }
}
