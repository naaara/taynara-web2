package br.ueg.trindade.taynara_web2.dto;

import java.time.LocalDateTime;

import br.ueg.trindade.taynara_web2.model.Postagem;
import br.ueg.trindade.taynara_web2.model.TipoPostagem;

public record PostagemDto(
        Long id,
        String titulo,
        TipoPostagem tipo,
        String conteudo,
        String artista,
        String link,
        LocalDateTime dataCriacao,
        Long autorId,
        String autorNome,
        String autorUsername) {

    public PostagemDto(Postagem p) {
        this(p.getId(), p.getTitulo(), p.getTipo(), p.getConteudo(), p.getArtista(), p.getLink(),
                p.getDataCriacao(), p.getAutor().getId(), p.getAutor().getNome(), p.getAutor().getUsername());
    }
}
