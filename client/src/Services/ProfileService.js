import api from "../Api";

const API_URL = "/users"; // <-- corresponde ao @RequestMapping("/users") do seu UserController

// Interceptor para anexar o token JWT a todas as requisições
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// Função genérica para tratar erros e manter logs consistentes
const handleError = (error, context = "ProfileService") => {
  const message =
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    error?.response?.data?.details ||
    error?.message ||
    "Erro desconhecido no servidor.";

  console.error(`❌ [${context}]`, message, error?.response || error);

  // Cria e lança um Error (evita throw literal => ESLint)
  const err = new Error(message);
  err.status = error?.response?.status || 500;
  err.raw = error?.response?.data;
  throw err;
};

// -------------------- MÉTODOS --------------------

/**
 * Busca os dados do perfil do usuário autenticado (endpoint /users/me)
 */
const getProfile = async () => {
  try {
    const response = await api.get(`${API_URL}/me`);
    return response.data;
  } catch (error) {
    handleError(error, "getProfile");
  }
};

/**
 * Atualiza dados básicos do perfil (ex: nome, email)
 * Observação: seu backend tem PUT /users/update conforme controller enviado.
 */
const updateProfile = async (profileData) => {
  try {
    const response = await api.put(`${API_URL}/update`, profileData);
    return response.data;
  } catch (error) {
    handleError(error, "updateProfile");
  }
};

/**
 * Envia nova foto de perfil
 */
const uploadProfilePhoto = async (file) => {
  try {
    const formData = new FormData();
    formData.append("profileImage", file);

    const response = await api.post(`${API_URL}/upload-photo`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    return response.data;
  } catch (error) {
    handleError(error, "uploadProfilePhoto");
  }
};

/**
 * Troca de senha
 */
const changePassword = async (passwordData) => {
  try {
    const response = await api.put(`${API_URL}/change-password`, passwordData);
    return response.data;
  } catch (error) {
    handleError(error, "changePassword");
  }
};

const ProfileService = {
  getProfile,
  updateProfile,
  uploadProfilePhoto,
  changePassword,
};

export default ProfileService;
