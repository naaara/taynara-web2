package br.ueg.trindade.taynara_web2.dto;

import br.ueg.trindade.taynara_web2.model.TipoPostagem;

public record PostagemRequest(String titulo, TipoPostagem tipo, String conteudo, String artista, String link) {
}
