import axios from "axios";

export function mensagemErro(erro: unknown): string {
  if (axios.isAxiosError(erro)) {
    const dados = erro.response?.data as { message?: string } | undefined;
    if (dados?.message) return dados.message;
    if (!erro.response) return "Não foi possível conectar ao servidor.";
  }
  return "Ocorreu um erro inesperado.";
}
