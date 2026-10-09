package br.ueg.trindade.taynara_web2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ueg.trindade.taynara_web2.dto.PerfilRequest;
import br.ueg.trindade.taynara_web2.model.Perfil;
import br.ueg.trindade.taynara_web2.service.PerfilService;

@RestController
@RequestMapping("/api/perfis")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public List<Perfil> listar() {
        return perfilService.listar();
    }

    @GetMapping("/{id}")
    public Perfil buscarPorId(@PathVariable Long id) {
        return perfilService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Perfil criar(@RequestBody PerfilRequest request) {
        return perfilService.criar(request);
    }

    @PutMapping("/{id}")
    public Perfil atualizar(@PathVariable Long id, @RequestBody PerfilRequest request) {
        return perfilService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        perfilService.excluir(id);
    }
}
