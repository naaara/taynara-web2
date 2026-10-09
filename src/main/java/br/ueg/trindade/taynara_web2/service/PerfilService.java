package br.ueg.trindade.taynara_web2.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.dto.PerfilRequest;
import br.ueg.trindade.taynara_web2.model.Perfil;
import br.ueg.trindade.taynara_web2.model.Permissao;
import br.ueg.trindade.taynara_web2.repository.PerfilRepository;
import br.ueg.trindade.taynara_web2.repository.PermissaoRepository;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final PermissaoRepository permissaoRepository;

    public PerfilService(PerfilRepository perfilRepository, PermissaoRepository permissaoRepository) {
        this.perfilRepository = perfilRepository;
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional(readOnly = true)
    public List<Perfil> listar() {
        return perfilRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Perfil buscarPorId(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));
    }

    @Transactional
    public Perfil criar(PerfilRequest r) {
        validar(r, null);
        Perfil p = new Perfil();
        preencher(p, r);
        return perfilRepository.save(p);
    }

    @Transactional
    public Perfil atualizar(Long id, PerfilRequest r) {
        Perfil p = buscarPorId(id);
        validar(r, id);
        preencher(p, r);
        return perfilRepository.save(p);
    }

    @Transactional
    public void excluir(Long id) {
        Perfil p = buscarPorId(id);
        try {
            perfilRepository.delete(p);
            perfilRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Perfil em uso por algum usuário");
        }
    }

    private void preencher(Perfil p, PerfilRequest r) {
        p.setNome(r.nome().trim());
        p.setDescricao(r.descricao() == null || r.descricao().isBlank() ? null : r.descricao().trim());
        Set<Permissao> permissoes = new HashSet<>();
        if (r.permissaoIds() != null) {
            permissoes.addAll(permissaoRepository.findAllById(r.permissaoIds()));
        }
        p.setPermissoes(permissoes);
    }

    private void validar(PerfilRequest r, Long idAtual) {
        if (r == null || r.nome() == null || r.nome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do perfil é obrigatório");
        }
        perfilRepository.findByNome(r.nome().trim())
                .filter(x -> !x.getId().equals(idAtual))
                .ifPresent(x -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um perfil com esse nome"); });
    }
}
