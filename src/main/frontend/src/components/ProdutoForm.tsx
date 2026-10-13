import { useState, type FormEvent } from "react";
import type { Produto, ProdutoDados } from "../types/Produto";

interface ProdutoFormProps {
  produtoEditando: Produto | null;
  onSalvar: (dados: ProdutoDados) => Promise<void>;
  onCancelar: () => void;
}

function ProdutoForm({ produtoEditando, onSalvar, onCancelar }: ProdutoFormProps) {
  const [nome, setNome] = useState(produtoEditando?.nome ?? "");
  const [preco, setPreco] = useState(produtoEditando ? String(produtoEditando.preco) : "");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    await onSalvar({ nome, preco: Number(preco) });
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h3>{produtoEditando ? "Editar produto" : "Novo produto"}</h3>
      <input value={nome} onChange={(e) => setNome(e.target.value)} placeholder="Nome do produto" required />
      <input
        type="number"
        step="0.01"
        min="0.01"
        value={preco}
        onChange={(e) => setPreco(e.target.value)}
        placeholder="Preço"
        required
      />
      <div className="form-acoes">
        <button type="submit">{produtoEditando ? "Salvar alterações" : "Cadastrar"}</button>
        {produtoEditando && (
          <button type="button" onClick={onCancelar}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  );
}

export default ProdutoForm;
