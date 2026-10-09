export type TipoPostagem = "MUSICA" | "CRIACAO_PROPRIA";

export interface Postagem {
  id: number;
  titulo: string;
  tipo: TipoPostagem;
  conteudo: string;
  artista: string | null;
  link: string | null;
  dataCriacao: string;
  autorId: number;
  autorNome: string;
  autorUsername: string;
}

export interface PostagemDados {
  titulo: string;
  tipo: TipoPostagem;
  conteudo: string;
  artista: string;
  link: string;
}
