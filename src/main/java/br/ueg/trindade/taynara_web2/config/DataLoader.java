package br.ueg.trindade.taynara_web2.config;

import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ueg.trindade.taynara_web2.model.Perfil;
import br.ueg.trindade.taynara_web2.model.Permissao;
import br.ueg.trindade.taynara_web2.model.Usuario;
import br.ueg.trindade.taynara_web2.repository.PerfilRepository;
import br.ueg.trindade.taynara_web2.repository.PermissaoRepository;
import br.ueg.trindade.taynara_web2.repository.UsuarioRepository;

/** Dados iniciais: permissões, perfis ADMIN/USUARIO e o usuário admin (senha: admin123). */
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner carregarDadosIniciais(PermissaoRepository permissaoRepository,
                                            PerfilRepository perfilRepository,
                                            UsuarioRepository usuarioRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            if (perfilRepository.count() > 0) {
                return;
            }

            Permissao ler = permissaoRepository.save(new Permissao("LER", "Visualizar postagens"));
            Permissao escrever = permissaoRepository.save(new Permissao("ESCREVER", "Criar e editar as próprias postagens"));
            Permissao administrar = permissaoRepository.save(new Permissao("ADMINISTRAR", "Gerenciar qualquer postagem"));

            Perfil perfilUsuario = new Perfil();
            perfilUsuario.setNome("USUARIO");
            perfilUsuario.setDescricao("Usuário comum");
            perfilUsuario.setPermissoes(new HashSet<>(List.of(ler, escrever)));
            perfilUsuario = perfilRepository.save(perfilUsuario);

            Perfil perfilAdmin = new Perfil();
            perfilAdmin.setNome("ADMIN");
            perfilAdmin.setDescricao("Administrador");
            perfilAdmin.setPermissoes(new HashSet<>(List.of(ler, escrever, administrar)));
            perfilAdmin = perfilRepository.save(perfilAdmin);

            Usuario admin = new Usuario("Administrador", "admin", passwordEncoder.encode("admin123"), "admin@taynara.dev");
            admin.setPerfil(perfilAdmin);
            usuarioRepository.save(admin);
        };
    }
}
