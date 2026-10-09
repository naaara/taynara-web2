import axios from "axios";

// withCredentials: envia o cookie de sessão (login) em todas as requisições
const api = axios.create({
  baseURL: "http://localhost:8080/api",
  withCredentials: true,
});

export default api;
