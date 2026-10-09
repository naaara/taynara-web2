import { useState, type FormEvent } from "react";
import type { Usuario, UsuarioDados } from "../types/Usuario";

interface UsuarioFormProps {
  usuarioEditando: Usuario | null;
  onSalvar: (dados: UsuarioDados) => Promise<void>;
  onCancelar: () => void;
}

function UsuarioForm({ usuarioEditando, onSalvar, onCancelar }: UsuarioFormProps) {
  const [nome, setNome] = useState(usuarioEditando?.nome ?? "");
  const [username, setUsername] = useState(usuarioEditando?.username ?? "");
  const [email, setEmail] = useState(usuarioEditando?.email ?? "");
  const [senha, setSenha] = useState("");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    await onSalvar({ nome, username, email, senha });
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h3>{usuarioEditando ? "Editar usuário" : "Novo usuário"}</h3>
      <input value={nome} onChange={(e) => setNome(e.target.value)} placeholder="Nome" required />
      <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Username" required />
      <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="E-mail" required />
      <input
        type="password"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
        placeholder={usuarioEditando ? "Nova senha (deixe vazio para manter)" : "Senha (mín. 6 caracteres)"}
        required={!usuarioEditando}
      />
      <div className="form-acoes">
        <button type="submit">{usuarioEditando ? "Salvar alterações" : "Cadastrar"}</button>
        {usuarioEditando && (
          <button type="button" className="secundario" onClick={onCancelar}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  );
}

export default UsuarioForm;
