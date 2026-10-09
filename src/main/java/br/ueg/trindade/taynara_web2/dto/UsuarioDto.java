package br.ueg.trindade.taynara_web2.dto;

import br.ueg.trindade.taynara_web2.model.Usuario;

/** Resposta de usuário: nunca inclui a senha. */
public record UsuarioDto(Long id, String nome, String username, String email, String perfil) {

    public UsuarioDto(Usuario u) {
        this(u.getId(), u.getNome(), u.getUsername(), u.getEmail(),
                u.getPerfil() != null ? u.getPerfil().getNome() : null);
    }
}
