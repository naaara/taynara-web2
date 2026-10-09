import { useEffect, useState } from "react";
import api from "../services/api";
import { mensagemErro } from "../services/erro";
import type { Permissao, PermissaoDados } from "../types/Permissao";
import PermissaoList from "../components/PermissaoList";
import PermissaoForm from "../components/PermissaoForm";

function PermissoesPage() {
  const [permissoes, setPermissoes] = useState<Permissao[]>([]);
  const [editando, setEditando] = useState<Permissao | null>(null);
  const [versaoForm, setVersaoForm] = useState(0);
  const [erro, setErro] = useState("");

  async function carregarPermissoes() {
    try {
      const resposta = await api.get<Permissao[]>("/permissoes");
      setPermissoes(resposta.data);
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  useEffect(() => {
    carregarPermissoes();
  }, []);

  async function salvar(dados: PermissaoDados) {
    setErro("");
    try {
      if (editando) {
        await api.put(`/permissoes/${editando.id}`, dados);
      } else {
        await api.post("/permissoes", dados);
      }
      setEditando(null);
      setVersaoForm((v) => v + 1);
      await carregarPermissoes();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  async function excluir(id: number) {
    if (!window.confirm("Excluir esta permissão?")) return;
    setErro("");
    try {
      await api.delete(`/permissoes/${id}`);
      if (editando?.id === id) setEditando(null);
      await carregarPermissoes();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  return (
    <section>
      <h2>Permissões</h2>
      {erro && <p className="erro">{erro}</p>}
      <PermissaoForm
        key={editando?.id ?? `novo-${versaoForm}`}
        permissaoEditando={editando}
        onSalvar={salvar}
        onCancelar={() => setEditando(null)}
      />
      <PermissaoList permissoes={permissoes} onEditar={setEditando} onExcluir={excluir} />
    </section>
  );
}

export default PermissoesPage;
