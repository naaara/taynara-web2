import type { Postagem } from "../types/Postagem";
import type { Usuario } from "../types/Usuario";
import PostagemItem from "./PostagemItem";

interface PostagemListProps {
  postagens: Postagem[];
  usuarioLogado: Usuario;
  onEditar: (postagem: Postagem) => void;
  onExcluir: (id: number) => void;
}

function PostagemList({ postagens, usuarioLogado, onEditar, onExcluir }: PostagemListProps) {
  if (postagens.length === 0) return <p className="vazio">Ainda não há postagens. Faça a primeira! 🎶</p>;

  return (
    <ul className="cards">
      {postagens.map((postagem) => (
        <PostagemItem
          key={postagem.id}
          postagem={postagem}
          podeAlterar={usuarioLogado.perfil === "ADMIN" || postagem.autorId === usuarioLogado.id}
          onEditar={onEditar}
          onExcluir={onExcluir}
        />
      ))}
    </ul>
  );
}

export default PostagemList;
