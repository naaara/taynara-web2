import { useState, type FormEvent } from "react";
import api from "../services/api";
import { mensagemErro } from "../services/erro";

interface CadastroPageProps {
  onIrParaLogin: () => void;
}

function CadastroPage({ onIrParaLogin }: CadastroPageProps) {
  const [nome, setNome] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState(false);
  const [enviando, setEnviando] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro("");
    setEnviando(true);
    try {
      await api.post("/auth/registro", { nome, username, email, senha });
      setSucesso(true);
    } catch (e) {
      setErro(mensagemErro(e));
    } finally {
      setEnviando(false);
    }
  }

  if (sucesso) {
    return (
      <div className="auth">
        <div className="form auth-card">
          <h2>Cadastro realizado!</h2>
          <p className="sub">Agora é só entrar com o seu username e senha.</p>
          <button onClick={onIrParaLogin}>Ir para o login</button>
        </div>
      </div>
    );
  }

  return (
    <div className="auth">
      <form className="form auth-card" onSubmit={handleSubmit}>
        <h2>Criar conta</h2>
        <input
          value={nome}
          onChange={(e) => setNome(e.target.value)}
          placeholder="Nome"
          required
        />
        <input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Username"
          required
        />
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="E-mail"
          required
        />
        <input
          type="password"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          placeholder="Senha (mín. 6 caracteres)"
          required
        />
        {erro && <p className="erro">{erro}</p>}
        <button type="submit" disabled={enviando}>
          {enviando ? "Cadastrando..." : "Cadastrar"}
        </button>
        <button type="button" className="link" onClick={onIrParaLogin}>
          Já tem conta? Entrar
        </button>
      </form>
    </div>
  );
}

export default CadastroPage;
