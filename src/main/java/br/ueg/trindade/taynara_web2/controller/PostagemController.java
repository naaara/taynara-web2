package br.ueg.trindade.taynara_web2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

import br.ueg.trindade.taynara_web2.dto.PostagemDto;
import br.ueg.trindade.taynara_web2.dto.PostagemRequest;
import br.ueg.trindade.taynara_web2.service.PostagemService;

@RestController
@RequestMapping("/api/postagens")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PostagemController {

    private final PostagemService postagemService;

    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @GetMapping
    public List<PostagemDto> listar() {
        return postagemService.listar();
    }

    @GetMapping("/{id}")
    public PostagemDto buscarPorId(@PathVariable Long id) {
        return postagemService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostagemDto criar(@RequestBody PostagemRequest request, Authentication authentication) {
        return postagemService.criar(request, authentication.getName());
    }

    @PutMapping("/{id}")
    public PostagemDto atualizar(@PathVariable Long id, @RequestBody PostagemRequest request,
                                 Authentication authentication) {
        return postagemService.atualizar(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, Authentication authentication) {
        postagemService.excluir(id, authentication.getName());
    }
}
