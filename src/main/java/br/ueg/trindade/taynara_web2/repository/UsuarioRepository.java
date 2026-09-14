package br.ueg.trindade.taynara_web2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.taynara_web2.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}