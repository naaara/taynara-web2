package br.ueg.trindade.taynara_web2.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ueg.trindade.taynara_web2.model.Usuario;

@RestController 
@RequestMapping("/api")
public class Api {
    
    @GetMapping("/usuarios")
    public List<Usuario> getAllUsuariosWithClass() {
        List<Usuario> users = new ArrayList<>();
        users.add(new Usuario("João","joao123","senha123","joao@example.com"));
        users.add(new Usuario("Maria","maria456","senha456","maria@example.com"));
        return users;
    }
}