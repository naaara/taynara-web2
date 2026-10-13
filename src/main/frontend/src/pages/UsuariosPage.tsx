import { useEffect, useState } from "react";
import api from "../services/api";
import { mensagemErro } from "../services/erro";
import type { Usuario, UsuarioDados } from "../types/Usuario";
import UsuarioList from "../components/UsuarioList";
import UsuarioForm from "../components/UsuarioForm";

function UsuariosPage() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [editando, setEditando] = useState<Usuario | null>(null);
  const [versaoForm, setVersaoForm] = useState(0);
  const [erro, setErro] = useState("");

  async function carregarUsuarios() {
    try {
      const resposta = await api.get<Usuario[]>("/usuarios");
      setUsuarios(resposta.data);
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  useEffect(() => {
    carregarUsuarios();
  }, []);

  async function salvar(dados: UsuarioDados) {
    setErro("");
    try {
      if (editando) {
        await api.put(`/usuarios/${editando.id}`, dados);
      } else {
        await api.post("/usuarios", dados);
      }
      setEditando(null);
      setVersaoForm((v) => v + 1);
      await carregarUsuarios();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  async function excluir(id: number) {
    if (!window.confirm("Excluir este usuário?")) return;
    setErro("");
    try {
      await api.delete(`/usuarios/${id}`);
      if (editando?.id === id) setEditando(null);
      await carregarUsuarios();
    } catch (e) {
      setErro(mensagemErro(e));
    }
  }

  return (
    <section>
      <h2>Usuários cadastrados</h2>
      {erro && <p className="erro">{erro}</p>}
      <UsuarioForm
        key={editando?.id ?? `novo-${versaoForm}`}
        usuarioEditando={editando}
        onSalvar={salvar}
        onCancelar={() => setEditando(null)}
      />
      <UsuarioList usuarios={usuarios} onEditar={setEditando} onExcluir={excluir} />
    </section>
  );
}

export default UsuariosPage;
