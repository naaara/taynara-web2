import type { Pagina } from "../types/Pagina";

interface NavbarProps {
  pagina: Pagina;
  onMudarPagina: (pagina: Pagina) => void;
}

const ABAS: { id: Pagina; rotulo: string }[] = [
  { id: "usuarios", rotulo: "Usuários" },
  { id: "permissoes", rotulo: "Permissões" },
  { id: "produtos", rotulo: "Produtos" },
];

function Navbar({ pagina, onMudarPagina }: NavbarProps) {
  return (
    <nav>
      {ABAS.map((aba) => (
        <button key={aba.id} disabled={aba.id === pagina} onClick={() => onMudarPagina(aba.id)}>
          {aba.rotulo}
        </button>
      ))}
    </nav>
  );
}

export default Navbar;
