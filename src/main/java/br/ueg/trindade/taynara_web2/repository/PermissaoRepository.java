package br.ueg.trindade.taynara_web2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.taynara_web2.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
}