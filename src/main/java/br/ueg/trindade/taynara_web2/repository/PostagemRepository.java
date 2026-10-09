package br.ueg.trindade.taynara_web2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.taynara_web2.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    List<Postagem> findAllByOrderByDataCriacaoDesc();

    void deleteByAutorId(Long autorId);
}
