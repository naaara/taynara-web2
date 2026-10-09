import { useEffect, useState } from "react";
import api from "../services/api";
import { mensagemErro } from "../services/erro";
import type { Postagem, PostagemDados } from "../types/Postagem";
import type { Usuario } from "../types/Usuario";
import PostagemItem from "../components/PostagemItem";
import PostagemForm from "../components/PostagemForm";

interface PostagensPageProps {
  usuarioLogado: Usuario;
}

function PostagensPage({ usuarioLogado }: PostagensPageProps) {
  const [postagens, setPostagens] = useState<Postagem[]>([]);
  const [editando, setEditando] = useState<Postagem | null>(null);
  const [versaoForm, setVersaoForm] = useState(0);
  const [erro, setErro] = useState("");

  async function carregarPostagens() {
    try {
      const resposta = await api.get<Postagem[]>("/postagens");
      setPostagens(resposta.data);
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  useEffect(() => {
    carregarPostagens();
  }, []);

  async function salvar(dados: PostagemDados) {
    setErro("");
    try {
      if (editando) {
        await api.put(`/postagens/${editando.id}`, dados);
      } else {
        await api.post("/postagens", dados);
      }
      setEditando(null);
      setVersaoForm((v) => v + 1);
      await carregarPostagens();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  async function excluir(id: number) {
    if (!window.confirm("Excluir esta postagem?")) return;
    setErro("");
    try {
      await api.delete(`/postagens/${id}`);
      if (editando?.id === id) setEditando(null);
      await carregarPostagens();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  return (
    <section>
      <h2>Postagens</h2>
      {erro && <p className="erro">{erro}</p>}
      <PostagemForm
        key={editando?.id ?? `novo-${versaoForm}`}
        postagemEditando={editando}
        onSalvar={salvar}
        onCancelar={() => setEditando(null)}
      />

      <ul className="lista-postagens">
        {postagens.map((postagem) => {
          // Utiliza 'as any' para contornar restrições de tipo do TypeScript no objeto perfil
          const perfilUsuario = usuarioLogado.perfil as any;
          const nomePerfil =
            typeof perfilUsuario === "string"
              ? perfilUsuario
              : perfilUsuario?.nome;

          const ehAdmin = nomePerfil === "ADMIN";
          const ehAutor =
            postagem.autorUsername === usuarioLogado.username ||
            postagem.autorId === usuarioLogado.id;
          const podeAlterar = ehAdmin || ehAutor;

          return (
            <PostagemItem
              key={postagem.id}
              postagem={postagem}
              podeAlterar={podeAlterar}
              onEditar={setEditando}
              onExcluir={excluir}
            />
          );
        })}
      </ul>
    </section>
  );
}

export default PostagensPage;
