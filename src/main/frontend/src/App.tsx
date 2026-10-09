import { useEffect, useState } from "react";
import api from "./services/api";
import type { Pagina } from "./types/Pagina";
import type { Usuario } from "./types/Usuario";
import Navbar from "./components/Navbar";
import LoginPage from "./pages/LoginPage";
import CadastroPage from "./pages/CadastroPage";
import PostagensPage from "./pages/PostagensPage";

function App() {
  const [usuarioLogado, setUsuarioLogado] = useState<Usuario | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [pagina, setPagina] = useState<Pagina>("postagens");
  const [mostrarCadastro, setMostrarCadastro] = useState(false);

  useEffect(() => {
    api
      .get<Usuario>("/auth/me")
      .then((resposta) => setUsuarioLogado(resposta.data))
      .catch(() => setUsuarioLogado(null))
      .finally(() => setCarregando(false));
  }, []);

  async function sair() {
    try {
      await api.post("/auth/logout");
    } catch {}
    setUsuarioLogado(null);
    setPagina("postagens");
  }

  if (carregando) return <p className="vazio">Agdadaras...</p>;

  if (!usuarioLogado) {
    return mostrarCadastro ? (
      <CadastroPage onIrParaLogin={() => setMostrarCadastro(false)} />
    ) : (
      <LoginPage
        onLogin={setUsuarioLogado}
        onIrParaCadastro={() => setMostrarCadastro(true)}
      />
    );
  }

  return (
    <div className="app">
      <Navbar
        usuario={usuarioLogado}
        pagina={pagina}
        onMudarPagina={setPagina}
        onSair={sair}
      />
      <main>
        {pagina === "postagens" && (
          <PostagensPage usuarioLogado={usuarioLogado} />
        )}
      </main>
    </div>
  );
}

export default App;
