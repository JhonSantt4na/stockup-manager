import axios from 'axios';

const API_URL = '/users';

const getAuthHeader = () => {
  const token = localStorage.getItem('token');
  return { Authorization: `Bearer ${token}` };
};

const UserService = {
  // GET /users - Ajustado para mapear filter para enabled e adicionar search
  getUsers: async (page = 0, size = 10, search = '', filter = 'all') => {
    let enabled = null;
    if (filter === 'active') enabled = true;
    if (filter === 'inactive') enabled = false;

    try {
      const response = await axios.get(
        `${API_URL}?page=${page}&size=${size}${search ? `&search=${search}` : ''}${enabled !== null ? `&enabled=${enabled}` : ''}`,
        { headers: getAuthHeader() }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching users:', error);
      throw error;
    }
  },

  // GET /users/{username}/roles
  getRoles: async (username) => {
    try {
      const response = await axios.get(`${API_URL}/${username}/roles`, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error fetching roles:', error);
      throw error;
    }
  },

  // GET /users/me
  getMe: async () => {
    try {
      const response = await axios.get(`${API_URL}/me`, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error fetching current user:', error);
      throw error;
    }
  },

  // GET /users/find/{username}
  findUser: async (username) => {
    try {
      const response = await axios.get(`${API_URL}/find/${username}`, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error finding user:', error);
      throw error;
    }
  },

  // POST /users/register
  register: async (userData) => {
    try {
      const response = await axios.post(`${API_URL}/register`, userData, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error registering user:', error);
      throw error;
    }
  },

  // PUT /users/update
  updateSelf: async (updateData) => {
    try {
      const response = await axios.put(`${API_URL}/update`, updateData, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error updating self:', error);
      throw error;
    }
  },

  // PUT /users/update/{username}
  updateUser: async (username, updateData) => {
    try {
      const response = await axios.put(`${API_URL}/update/${username}`, updateData, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error updating user:', error);
      throw error;
    }
  },

  // PUT /users/toggle/{username}
  toggleUser: async (username) => {
    try {
      const response = await axios.put(`${API_URL}/toggle/${username}`, {}, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error toggling user:', error);
      throw error;
    }
  },

  // PUT /users/change-password
  changePassword: async (passwordData) => {
    try {
      const response = await axios.put(`${API_URL}/change-password`, passwordData, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error changing password:', error);
      throw error;
    }
  },

  // POST /users/{username}/roles/assign
  assignRole: async (username, role) => {
    try {
      const response = await axios.post(`${API_URL}/${username}/roles/assign`, { role }, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error assigning role:', error);
      throw error;
    }
  },

  // POST /users/{username}/roles/remove
  removeRole: async (username, role) => {
    try {
      const response = await axios.post(`${API_URL}/${username}/roles/remove`, { role }, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error removing role:', error);
      throw error;
    }
  },

  // DELETE /users/delete/{username}
  deleteUser: async (username) => {
    try {
      const response = await axios.delete(`${API_URL}/delete/${username}`, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error deleting user:', error);
      throw error;
    }
  },
};

export default UserService;