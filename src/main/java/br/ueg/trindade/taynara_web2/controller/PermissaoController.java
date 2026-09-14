package br.ueg.trindade.taynara_web2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ueg.trindade.taynara_web2.model.Permissao;
import br.ueg.trindade.taynara_web2.repository.PermissaoRepository;

@RestController
@RequestMapping("/api/permissoes")
@CrossOrigin(origins = "http://localhost:5173")
public class PermissaoController {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @GetMapping
    public List<Permissao> listar() {
        return permissaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permissao> buscarPorId(@PathVariable Long id) {
        return permissaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Permissao criar(@RequestBody Permissao permissao) {
        return permissaoRepository.save(permissao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!permissaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        permissaoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}