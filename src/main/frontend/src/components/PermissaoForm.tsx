import { useState, type FormEvent } from "react";
import type { Permissao, PermissaoDados } from "../types/Permissao";

interface PermissaoFormProps {
  permissaoEditando: Permissao | null;
  onSalvar: (dados: PermissaoDados) => Promise<void>;
  onCancelar: () => void;
}

function PermissaoForm({ permissaoEditando, onSalvar, onCancelar }: PermissaoFormProps) {
  const [nome, setNome] = useState(permissaoEditando?.nome ?? "");
  const [descricao, setDescricao] = useState(permissaoEditando?.descricao ?? "");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    await onSalvar({ nome, descricao });
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h3>{permissaoEditando ? "Editar permissão" : "Nova permissão"}</h3>
      <input value={nome} onChange={(e) => setNome(e.target.value)} placeholder="Nome" required />
      <input value={descricao} onChange={(e) => setDescricao(e.target.value)} placeholder="Descrição" />
      <div className="form-acoes">
        <button type="submit">{permissaoEditando ? "Salvar alterações" : "Cadastrar"}</button>
        {permissaoEditando && (
          <button type="button" onClick={onCancelar}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  );
}

export default PermissaoForm;
