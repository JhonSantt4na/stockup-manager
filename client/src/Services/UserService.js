import api from "../Api";

const API_URL = "/users";
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

const handleError = (error, context = "UserService") => {
  console.error(`Erro em ${context}:`, error.response || error.message);
  throw error.response?.data || error;
};

const getUsers = async (page = 0, size = 10, search = "", filter = "all", sort = ["username", "asc"]) => {
  try {
    let enabled;
    if (filter === "active") enabled = true;
    else if (filter === "inactive") enabled = false;

    const response = await api.get(`${API_URL}/list`, {
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
    handleError(error, "getUsers");
  }
};

const getUserByUsername = async (username) => {
  try {
    const response = await api.get(`${API_URL}/find/${username}`);
    return response.data;
  } catch (error) {
    handleError(error, "getUserByUsername");
  }
};

const createUser = async (userData) => {
  try {
    const response = await api.post(`${API_URL}/register`, userData);
    return response.data;
  } catch (error) {
    handleError(error, "createUser");
  }
};

const updateUser = async (username, userData) => {
  try {
    const response = await api.put(`${API_URL}/update/${username}`, userData);
    return response.data;
  } catch (error) {
    handleError(error, "updateUser");
  }
};

const deleteUser = async (username) => {
  try {
    const response = await api.delete(`${API_URL}/delete/${username}`);
    return response.data;
  } catch (error) {
    handleError(error, "deleteUser");
  }
};

const toggleUser = async (username) => {
  try {
    const response = await api.put(`${API_URL}/toggle/${username}`);
    return response.data;
  } catch (error) {
    handleError(error, "toggleUser");
  }
};

const getRoles = async (username) => {
  try {
    const response = await api.get(`${API_URL}/${username}/roles`);
    return response.data;
  } catch (error) {
    handleError(error, "getRoles");
  }
};

const assignRole = async (username, roles) => {
  try {
    const rolesArray = Array.isArray(roles) ? roles : [roles];
    const response = await api.post(`${API_URL}/${username}/roles/assign`, rolesArray);
    return response.data;
  } catch (error) {
    handleError(error, "assignRole");
  }
};

const removeRole = async (username, roles) => {
  try {
    const rolesArray = Array.isArray(roles) ? roles : [roles];
    const response = await api.post(`${API_URL}/${username}/roles/remove`, rolesArray);
    return response.data;
  } catch (error) {
    handleError(error, "removeRole");
  }
};

const changePassword = async (passwordData) => {
  try {
    const response = await api.put(`${API_URL}/change-password`, passwordData);
    return response.data;
  } catch (error) {
    handleError(error, "changePassword");
  }
};

const searchUsers = async (query) => {
  try {
    const response = await api.get(`${API_URL}/list`, { params: { search: query } });
    return response.data;
  } catch (error) {
    handleError(error, "searchUsers");
  }
};

const getAllAvailableRoles = async () => {
  try {
    const response = await api.get(`${API_URL}/roles/available`);
    return response.data;
  } catch (error) {
    handleError(error, "getAllAvailableRoles");
  }
};

const getAllRoles = async () => {
  try {
    const response = await api.get(`${API_URL}/roles/all`);
    return response.data;
  } catch (error) {
    handleError(error, "getAllRoles");
  }
};

const updateUserAsAdmin = async (username, userData) => {
  try {
    const response = await api.put(`${API_URL}/update/${username}`, userData);
    return response.data;
  } catch (error) {
    handleError(error, "updateUserAsAdmin");
  }
};

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
  getAllRoles,
  getAllAvailableRoles,
  updateUserAsAdmin,
};

export default UserService;