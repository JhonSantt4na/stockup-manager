// PermissionService.js
import axios from 'axios'; // Assuming axios is used for API calls. Install if needed: npm install axios

const API_BASE_URL = '/permissions'; // Adjust base URL if needed, e.g., 'http://localhost:8080/permissions'

const PermissionService = {
  createPermission: async (data) => {
    return axios.post(`${API_BASE_URL}/create`, data);
  },

  updatePermission: async (data) => {
    return axios.put(`${API_BASE_URL}/update`, data);
  },

  deletePermission: async (name) => {
    return axios.delete(`${API_BASE_URL}/delete/${name}`);
  },

  getPermissionByName: async (name) => {
    return axios.get(`${API_BASE_URL}/${name}`);
  },

  getAllPermissions: async (page = 0, size = 10, sort = ['name', 'asc']) => {
    return axios.get(`${API_BASE_URL}/list`, {
      params: { page, size, sort: sort.join(',') }
    }).then(res => res.data);
  },

  getAllActivePermissions: async (page = 0, size = 10, sort = ['name', 'asc']) => {
    return axios.get(`${API_BASE_URL}/listActive`, {
      params: { page, size, sort: sort.join(',') }
    }).then(res => res.data);
  }
};

export default PermissionService;