import { useState, type FormEvent } from "react";
import type { Postagem, PostagemDados, TipoPostagem } from "../types/Postagem";

interface PostagemFormProps {
  postagemEditando: Postagem | null;
  onSalvar: (dados: PostagemDados) => Promise<void>;
  onCancelar: () => void;
}

function PostagemForm({ postagemEditando, onSalvar, onCancelar }: PostagemFormProps) {
  const [titulo, setTitulo] = useState(postagemEditando?.titulo ?? "");
  const [tipo, setTipo] = useState<TipoPostagem>(postagemEditando?.tipo ?? "MUSICA");
  const [conteudo, setConteudo] = useState(postagemEditando?.conteudo ?? "");
  const [artista, setArtista] = useState(postagemEditando?.artista ?? "");
  const [link, setLink] = useState(postagemEditando?.link ?? "");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    await onSalvar({ titulo, tipo, conteudo, artista, link });
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h3>{postagemEditando ? "Editar postagem" : "Nova postagem"}</h3>
      <select value={tipo} onChange={(e) => setTipo(e.target.value as TipoPostagem)}>
        <option value="MUSICA">🎧 Música favorita</option>
        <option value="CRIACAO_PROPRIA">✍️ Criação própria</option>
      </select>
      <input value={titulo} onChange={(e) => setTitulo(e.target.value)} placeholder="Título" maxLength={120} required />
      {tipo === "MUSICA" && (
        <input value={artista} onChange={(e) => setArtista(e.target.value)} placeholder="Artista / banda" />
      )}
      <textarea
        value={conteudo}
        onChange={(e) => setConteudo(e.target.value)}
        placeholder={tipo === "MUSICA" ? "Por que você ama essa música?" : "Escreva sua criação (letra, poema, ideia...)"}
        rows={5}
        maxLength={2000}
        required
      />
      <input
        value={link}
        onChange={(e) => setLink(e.target.value)}
        placeholder="Link (opcional, começando com https://)"
      />
      <div className="form-acoes">
        <button type="submit">{postagemEditando ? "Salvar alterações" : "Publicar"}</button>
        {postagemEditando && (
          <button type="button" className="secundario" onClick={onCancelar}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  );
}

export default PostagemForm;
