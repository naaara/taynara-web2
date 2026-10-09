export interface Usuario {
  id: number;
  nome: string;
  username: string;
  email: string;
  perfil: string | null;
}

export interface UsuarioDados {
  nome: string;
  username: string;
  email: string;
  senha: string;
}
