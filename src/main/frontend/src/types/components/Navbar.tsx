import type { Pagina } from "../types/Pagina";
import type { Usuario } from "../types/Usuario";

interface NavbarProps {
  usuario: Usuario;
  pagina: Pagina;
  onMudarPagina: (pagina: Pagina) => void;
  onSair: () => void;
}

const ABAS: { id: Pagina; rotulo: string }[] = [
  { id: "postagens", rotulo: "Postagens" },
  { id: "usuarios", rotulo: "Usuários" },
  { id: "permissoes", rotulo: "Permissões" },
];

function Navbar({ usuario, pagina, onMudarPagina, onSair }: NavbarProps) {
  return (
    <header className="navbar">
      <h1 className="logo">🎵 Meu Som</h1>
      <nav>
        {ABAS.map((aba) => (
          <button
            key={aba.id}
            className={aba.id === pagina ? "aba ativa" : "aba"}
            onClick={() => onMudarPagina(aba.id)}
          >
            {aba.rotulo}
          </button>
        ))}
      </nav>
      <div className="navbar-usuario">
        <span>Olá, {usuario.nome}</span>
        <button className="secundario" onClick={onSair}>
          Sair
        </button>
      </div>
    </header>
  );
}

export default Navbar;
