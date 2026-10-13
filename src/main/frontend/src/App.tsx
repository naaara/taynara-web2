import { useState } from "react";
import type { Pagina } from "./types/Pagina";
import Navbar from "./components/Navbar";
import UsuariosPage from "./pages/UsuariosPage";
import PermissoesPage from "./pages/PermissoesPage";
import ProdutosPage from "./pages/ProdutosPage";

// App só escolhe qual página renderizar. Na Aula 08 isso vira o React Router.
function App() {
  const [pagina, setPagina] = useState<Pagina>("usuarios");

  return (
    <div>
      <Navbar pagina={pagina} onMudarPagina={setPagina} />
      {pagina === "usuarios" && <UsuariosPage />}
      {pagina === "permissoes" && <PermissoesPage />}
      {pagina === "produtos" && <ProdutosPage />}
    </div>
  );
}

export default App;
