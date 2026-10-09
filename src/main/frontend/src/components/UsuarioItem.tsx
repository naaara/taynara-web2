import type { Usuario } from "../types/Usuario";

interface UsuarioItemProps {
  usuario: Usuario;
  onEditar: (usuario: Usuario) => void;
  onExcluir: (id: number) => void;
}

function UsuarioItem({ usuario, onEditar, onExcluir }: UsuarioItemProps) {
  return (
    <li className="item">
      <div className="item-texto">
        <strong>{usuario.nome}</strong> ({usuario.username}) — {usuario.email}
        {usuario.perfil && <span className="selo">{usuario.perfil}</span>}
      </div>
      <div className="item-acoes">
        <button onClick={() => onEditar(usuario)}>Editar</button>
        <button className="perigo" onClick={() => onExcluir(usuario.id)}>
          Excluir
        </button>
      </div>
    </li>
  );
}

export default UsuarioItem;
