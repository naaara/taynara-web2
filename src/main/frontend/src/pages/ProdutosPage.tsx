import { useEffect, useState } from "react";
import api from "../services/api";
import { mensagemErro } from "../services/erro";
import type { Produto, ProdutoDados } from "../types/Produto";
import ProdutoList from "../components/ProdutoList";
import ProdutoForm from "../components/ProdutoForm";

function ProdutosPage() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [editando, setEditando] = useState<Produto | null>(null);
  const [versaoForm, setVersaoForm] = useState(0);
  const [erro, setErro] = useState("");

  async function carregarProdutos() {
    try {
      const resposta = await api.get<Produto[]>("/produtos");
      setProdutos(resposta.data);
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  useEffect(() => {
    carregarProdutos();
  }, []);

  async function salvar(dados: ProdutoDados) {
    setErro("");
    try {
      if (editando) {
        await api.put(`/produtos/${editando.id}`, dados);
      } else {
        await api.post("/produtos", dados);
      }
      setEditando(null);
      setVersaoForm((v) => v + 1);
      await carregarProdutos();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  async function excluir(id: number) {
    if (!window.confirm("Excluir este produto?")) return;
    setErro("");
    try {
      await api.delete(`/produtos/${id}`);
      if (editando?.id === id) setEditando(null);
      await carregarProdutos();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  return (
    <section>
      <h2>Produtos cadastrados</h2>
      {erro && <p className="erro">{erro}</p>}
      <ProdutoForm
        key={editando?.id ?? `novo-${versaoForm}`}
        produtoEditando={editando}
        onSalvar={salvar}
        onCancelar={() => setEditando(null)}
      />
      <ProdutoList produtos={produtos} onEditar={setEditando} onExcluir={excluir} />
    </section>
  );
}

export default ProdutosPage;
