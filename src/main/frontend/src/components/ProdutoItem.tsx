import type { Produto } from "../types/Produto";

interface ProdutoItemProps {
  produto: Produto;
  onEditar: (produto: Produto) => void;
  onExcluir: (id: number) => void;
}

function ProdutoItem({ produto, onEditar, onExcluir }: ProdutoItemProps) {
  return (
    <li className="item">
      <div className="item-texto">
        <strong>{produto.nome}</strong> — {produto.preco.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
      </div>
      <div className="item-acoes">
        <button onClick={() => onEditar(produto)}>Editar</button>
        <button onClick={() => onExcluir(produto.id)}>Excluir</button>
      </div>
    </li>
  );
}

export default ProdutoItem;
