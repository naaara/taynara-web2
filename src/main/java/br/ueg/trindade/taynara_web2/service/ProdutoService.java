package br.ueg.trindade.taynara_web2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.taynara_web2.model.Produto;
import br.ueg.trindade.taynara_web2.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    public Produto criar(Produto dados) {
        validar(dados, null);
        Produto produto = new Produto();
        copiar(produto, dados);
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);
        validar(dados, id);
        copiar(produto, dados);
        return produtoRepository.save(produto);
    }

    public void excluir(Long id) {
        produtoRepository.delete(buscarPorId(id));
    }

    private void copiar(Produto destino, Produto origem) {
        destino.setNome(origem.getNome().trim());
        destino.setPreco(origem.getPreco());
    }

    // Regras de negócio: nome obrigatório e único; preço obrigatório e maior que zero
    private void validar(Produto dados, Long idAtual) {
        if (dados.getNome() == null || dados.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do produto é obrigatório");
        }
        if (dados.getPreco() == null || dados.getPreco() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço deve ser maior que zero");
        }
        produtoRepository.findByNomeIgnoreCase(dados.getNome().trim())
                .filter(p -> !p.getId().equals(idAtual))
                .ifPresent(p -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um produto com esse nome"); });
    }
}
