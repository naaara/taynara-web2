package br.ueg.trindade.taynara_web2.dto;

import java.util.List;

public record PerfilRequest(String nome, String descricao, List<Long> permissaoIds) {
}
