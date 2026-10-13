package br.ueg.trindade.taynara_web2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.model.Permissao;
import br.ueg.trindade.taynara_web2.repository.PermissaoRepository;

@Service
public class PermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    public List<Permissao> listarTodas() {
        return permissaoRepository.findAll();
    }

    public Permissao buscarPorId(Long id) {
        return permissaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permissão não encontrada"));
    }

    public Permissao criar(Permissao dados) {
        validar(dados, null);
        Permissao permissao = new Permissao();
        copiar(permissao, dados);
        return permissaoRepository.save(permissao);
    }

    public Permissao atualizar(Long id, Permissao dados) {
        Permissao permissao = buscarPorId(id);
        validar(dados, id);
        copiar(permissao, dados);
        return permissaoRepository.save(permissao);
    }

    public void excluir(Long id) {
        permissaoRepository.delete(buscarPorId(id));
    }

    private void copiar(Permissao destino, Permissao origem) {
        destino.setNome(origem.getNome().trim());
        destino.setDescricao(origem.getDescricao() == null || origem.getDescricao().isBlank()
                ? null : origem.getDescricao().trim());
    }

    // Regras de negócio: nome obrigatório e sem duplicidade
    private void validar(Permissao dados, Long idAtual) {
        if (dados.getNome() == null || dados.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da permissão é obrigatório");
        }
        permissaoRepository.findByNomeIgnoreCase(dados.getNome().trim())
                .filter(p -> !p.getId().equals(idAtual))
                .ifPresent(p -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma permissão com esse nome"); });
    }
}
