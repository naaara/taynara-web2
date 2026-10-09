import type { Postagem } from "../types/Postagem";

interface PostagemItemProps {
  postagem: Postagem;
  podeAlterar: boolean;
  onEditar: (postagem: Postagem) => void;
  onExcluir: (id: number) => void;
}

function PostagemItem({ postagem, podeAlterar, onEditar, onExcluir }: PostagemItemProps) {
  const ehMusica = postagem.tipo === "MUSICA";

  return (
    <li className="card">
      <div className="card-topo">
        <span className={ehMusica ? "selo musica" : "selo criacao"}>
          {ehMusica ? "🎧 Música favorita" : "✍️ Criação própria"}
        </span>
        <small>{new Date(postagem.dataCriacao).toLocaleString("pt-BR")}</small>
      </div>
      <h3>{postagem.titulo}</h3>
      {postagem.artista && <p className="artista">{postagem.artista}</p>}
      <p className="conteudo">{postagem.conteudo}</p>
      {postagem.link && (
        <a href={postagem.link} target="_blank" rel="noreferrer">
          Abrir link ↗
        </a>
      )}
      <div className="card-rodape">
        <small>por {postagem.autorNome} (@{postagem.autorUsername})</small>
        {podeAlterar && (
          <div className="item-acoes">
            <button onClick={() => onEditar(postagem)}>Editar</button>
            <button className="perigo" onClick={() => onExcluir(postagem.id)}>
              Excluir
            </button>
          </div>
        )}
      </div>
    </li>
  );
}

export default PostagemItem;
