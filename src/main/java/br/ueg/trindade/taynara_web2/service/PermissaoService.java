package br.ueg.trindade.taynara_web2.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.model.Permissao;
import br.ueg.trindade.taynara_web2.repository.PermissaoRepository;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional(readOnly = true)
    public List<Permissao> listar() {
        return permissaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Permissao buscarPorId(Long id) {
        return permissaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permissão não encontrada"));
    }

    @Transactional
    public Permissao criar(Permissao dados) {
        validar(dados, null);
        return permissaoRepository.save(new Permissao(dados.getNome().trim(), limpar(dados.getDescricao())));
    }

    @Transactional
    public Permissao atualizar(Long id, Permissao dados) {
        Permissao p = buscarPorId(id);
        validar(dados, id);
        p.setNome(dados.getNome().trim());
        p.setDescricao(limpar(dados.getDescricao()));
        return permissaoRepository.save(p);
    }

    @Transactional
    public void excluir(Long id) {
        Permissao p = buscarPorId(id);
        try {
            permissaoRepository.delete(p);
            permissaoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permissão em uso por algum perfil");
        }
    }

    private void validar(Permissao dados, Long idAtual) {
        if (dados == null || dados.getNome() == null || dados.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da permissão é obrigatório");
        }
        permissaoRepository.findAll().stream()
                .filter(x -> x.getNome().equalsIgnoreCase(dados.getNome().trim()))
                .filter(x -> !x.getId().equals(idAtual))
                .findAny()
                .ifPresent(x -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma permissão com esse nome"); });
    }

    private static String limpar(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
