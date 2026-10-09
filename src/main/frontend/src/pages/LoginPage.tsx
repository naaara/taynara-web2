import { useState, type FormEvent } from "react";
import api from "../services/api";
import { mensagemErro } from "../services/erro";
import type { Usuario } from "../types/Usuario";

interface LoginPageProps {
  onLogin: (usuario: Usuario) => void;
  onIrParaCadastro: () => void;
}

function LoginPage({ onLogin, onIrParaCadastro }: LoginPageProps) {
  const [username, setUsername] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro("");
    setEnviando(true);
    try {
      const resposta = await api.post<Usuario>("/auth/login", {
        username,
        senha,
      });
      onLogin(resposta.data);
    } catch (e) {
      setErro(mensagemErro(e));
      setEnviando(false);
    }
  }

  return (
    <div className="auth">
      <form className="form auth-card" onSubmit={handleSubmit}>
        <h1 className="logo">Meu Som</h1>
        <p className="sub">
          Compartilhe suas músicas favoritas e criações próprias.
        </p>
        <input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Username"
          required
        />
        <input
          type="password"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          placeholder="Senha"
          required
        />
        {erro && <p className="erro">{erro}</p>}
        <button type="submit" disabled={enviando}>
          {enviando ? "Entrando..." : "Entrar"}
        </button>
        <button type="button" className="link" onClick={onIrParaCadastro}>
          Não tem conta? Cadastre-se
        </button>
      </form>
    </div>
  );
}

export default LoginPage;
