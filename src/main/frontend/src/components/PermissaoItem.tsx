import type { Permissao } from "../types/Permissao";

interface PermissaoItemProps {
  permissao: Permissao;
  onEditar: (permissao: Permissao) => void;
  onExcluir: (id: number) => void;
}

function PermissaoItem({ permissao, onEditar, onExcluir }: PermissaoItemProps) {
  return (
    <li className="item">
      <div className="item-texto">
        <strong>{permissao.nome}</strong>
        {permissao.descricao && <> — {permissao.descricao}</>}
      </div>
      <div className="item-acoes">
        <button onClick={() => onEditar(permissao)}>Editar</button>
        <button onClick={() => onExcluir(permissao.id)}>
          Excluir
        </button>
      </div>
    </li>
  );
}

export default PermissaoItem;
