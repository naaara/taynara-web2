package br.ueg.trindade.taynara_web2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.dto.PostagemDto;
import br.ueg.trindade.taynara_web2.dto.PostagemRequest;
import br.ueg.trindade.taynara_web2.model.Postagem;
import br.ueg.trindade.taynara_web2.model.Usuario;
import br.ueg.trindade.taynara_web2.repository.PostagemRepository;
import br.ueg.trindade.taynara_web2.repository.UsuarioRepository;

@Service
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;

    public PostagemService(PostagemRepository postagemRepository, UsuarioRepository usuarioRepository) {
        this.postagemRepository = postagemRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PostagemDto> listar() {
        return postagemRepository.findAllByOrderByDataCriacaoDesc().stream().map(PostagemDto::new).toList();
    }

    @Transactional(readOnly = true)
    public PostagemDto buscarPorId(Long id) {
        return new PostagemDto(obter(id));
    }

    @Transactional
    public PostagemDto criar(PostagemRequest r, String usernameLogado) {
        validar(r);
        Postagem p = new Postagem();
        preencher(p, r);
        p.setDataCriacao(LocalDateTime.now());
        p.setAutor(usuarioLogado(usernameLogado));
        return new PostagemDto(postagemRepository.save(p));
    }

    /** Regra de negócio: só o autor (ou um ADMIN) pode editar a postagem. */
    @Transactional
    public PostagemDto atualizar(Long id, PostagemRequest r, String usernameLogado) {
        Postagem p = obter(id);
        verificarDono(p, usernameLogado);
        validar(r);
        preencher(p, r);
        return new PostagemDto(postagemRepository.save(p));
    }

    /** Regra de negócio: só o autor (ou um ADMIN) pode excluir a postagem. */
    @Transactional
    public void excluir(Long id, String usernameLogado) {
        Postagem p = obter(id);
        verificarDono(p, usernameLogado);
        postagemRepository.delete(p);
    }

    private Postagem obter(Long id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Postagem não encontrada"));
    }

    private Usuario usuarioLogado(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado"));
    }

    private void verificarDono(Postagem p, String usernameLogado) {
        Usuario logado = usuarioLogado(usernameLogado);
        boolean admin = logado.getPerfil() != null && "ADMIN".equals(logado.getPerfil().getNome());
        boolean autor = p.getAutor().getId().equals(logado.getId());
        if (!admin && !autor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode alterar suas próprias postagens");
        }
    }

    private void preencher(Postagem p, PostagemRequest r) {
        p.setTitulo(r.titulo().trim());
        p.setTipo(r.tipo());
        p.setConteudo(r.conteudo().trim());
        p.setArtista(limpar(r.artista()));
        p.setLink(limpar(r.link()));
    }

    private void validar(PostagemRequest r) {
        if (r == null || r.titulo() == null || r.titulo().isBlank()) {
            throw erro("O título é obrigatório");
        }
        if (r.titulo().trim().length() > 120) {
            throw erro("O título deve ter no máximo 120 caracteres");
        }
        if (r.tipo() == null) {
            throw erro("Informe o tipo da postagem (música ou criação própria)");
        }
        if (r.conteudo() == null || r.conteudo().isBlank()) {
            throw erro("O conteúdo é obrigatório");
        }
        if (r.conteudo().trim().length() > 2000) {
            throw erro("O conteúdo deve ter no máximo 2000 caracteres");
        }
        String link = limpar(r.link());
        if (link != null && !(link.startsWith("http://") || link.startsWith("https://"))) {
            throw erro("O link deve começar com http:// ou https://");
        }
    }

    private static String limpar(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }

    private static ResponseStatusException erro(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
