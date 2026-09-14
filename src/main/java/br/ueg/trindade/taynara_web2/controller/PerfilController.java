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

import br.ueg.trindade.taynara_web2.model.Perfil;
import br.ueg.trindade.taynara_web2.repository.PerfilRepository;

@RestController
@RequestMapping("/api/perfis")
@CrossOrigin(origins = "http://localhost:5173")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @GetMapping
    public List<Perfil> listar() {
        return perfilRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Perfil> buscarPorId(@PathVariable Long id) {
        return perfilRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Perfil criar(@RequestBody Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!perfilRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        perfilRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}