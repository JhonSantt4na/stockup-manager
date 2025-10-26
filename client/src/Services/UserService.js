import axios from "axios";

// URL base da API (ajuste se necessário)
const API_URL = "http://localhost:8080/users";

// Cliente axios com interceptors para token
const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

const handleError = (error) => {
  console.error("Erro no UserService:", error.response || error.message);
  throw error.response?.data || error;
};

// ====================== USUÁRIOS ======================

export const getUsers = async (page = 0, size = 10, search = "", filter = "all", sort = ["username", "asc"]) => {
  try {
    let enabled;
    if (filter === "active") enabled = true;
    else if (filter === "inactive") enabled = false;

    const response = await api.get("/list", {
      params: {
        page,
        size,
        search: search || undefined,
        enabled: enabled !== undefined ? enabled : undefined,
        sort,
      },
    });
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const getUserByUsername = async (username) => {
  try {
    const response = await api.get(`/find/${username}`);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const createUser = async (userData) => {
  try {
    const response = await api.post("/register", userData);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const updateUser = async (username, userData) => {
  try {
    const response = await api.put(`/update/${username}`, userData);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const deleteUser = async (username) => {
  try {
    const response = await api.delete(`/delete/${username}`);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const toggleUser = async (username) => {
  try {
    const response = await api.put(`/toggle/${username}`);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

// ====================== ROLES ======================

export const getRoles = async (username) => {
  try {
    const response = await api.get(`/${username}/roles`);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const assignRole = async (username, roles) => {
  try {
    // roles: array de strings
    const rolesArray = Array.isArray(roles) ? roles : [roles];
    const response = await api.post(`/${username}/roles/assign`, rolesArray);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

export const removeRole = async (username, roles) => {
  try {
    const rolesArray = Array.isArray(roles) ? roles : [roles];
    const response = await api.post(`/${username}/roles/remove`, rolesArray);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

// ====================== SENHA ======================

export const changePassword = async (passwordData) => {
  try {
    const response = await api.put("/change-password", passwordData);
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

// ====================== BUSCA ======================

export const searchUsers = async (query) => {
  try {
    const response = await api.get("/list", { params: { search: query } });
    return response.data;
  } catch (error) {
    handleError(error);
  }
};

// ====================== EXPORT DEFAULT ======================

const UserService = {
  getUsers,
  getUserByUsername,
  createUser,
  updateUser,
  deleteUser,
  toggleUser,
  getRoles,
  assignRole,
  removeRole,
  changePassword,
  searchUsers,
};

export default UserService;