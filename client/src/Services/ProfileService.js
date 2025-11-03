import api from "../Api";

const API_URL = "/profile";

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

const handleError = (error, context = "ProfileService") => {
  console.error(`Erro em ${context}:`, error.response || error.message);
  throw error.response?.data || error;
};

const getProfile = async () => {
  try {
    const response = await api.get(`${API_URL}`);
    return response.data;
  } catch (error) {
    handleError(error, "getProfile");
  }
};

const updateProfile = async (profileData) => {
  try {
    const response = await api.put(`${API_URL}`, profileData);
    return response.data;
  } catch (error) {
    handleError(error, "updateProfile");
  }
};

const uploadProfilePhoto = async (file) => {
  try {
    const formData = new FormData();
    formData.append('profileImage', file);
    
    const response = await api.post(`${API_URL}/upload-photo`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    handleError(error, "uploadProfilePhoto");
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

const ProfileService = {
  getProfile,
  updateProfile,
  uploadProfilePhoto,
  changePassword,
};

export default ProfileService;