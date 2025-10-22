import axios from 'axios';

const API_URL = '/roles';
const PERM_API_URL = '/permissions';

const getAuthHeader = () => {
  const token = localStorage.getItem('token');
  return { Authorization: `Bearer ${token}` };
};

const RolesService = {
  // GET /roles/list-with-users - Com search (adicionado no backend)
  getRolesWithUsers: async (page = 0, size = 10, search = '') => {
    try {
      const response = await axios.get(
        `${API_URL}/list-with-users?page=${page}&size=${size}${search ? `&search=${search}` : ''}`,
        { headers: getAuthHeader() }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching roles:', error);
      throw error;
    }
  },

  // POST /roles/create
  createRole: async (roleData) => {
    try {
      const response = await axios.post(`${API_URL}/create`, roleData, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error creating role:', error);
      throw error;
    }
  },

  // PUT /roles/update
  updateRole: async (updateData) => {
    try {
      const response = await axios.put(`${API_URL}/update`, updateData, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error updating role:', error);
      throw error;
    }
  },

  // DELETE /roles/delete/{name}
  deleteRole: async (name) => {
    try {
      const response = await axios.delete(`${API_URL}/delete/${name}`, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error deleting role:', error);
      throw error;
    }
  },

  // POST /roles/{roleName}/permissions/assign
  assignPermissions: async (roleName, permissions) => {
    try {
      const response = await axios.post(`${API_URL}/${roleName}/permissions/assign`, permissions, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error assigning permissions:', error);
      throw error;
    }
  },

  // POST /roles/{roleName}/permissions/remove
  removePermissions: async (roleName, permissions) => {
    try {
      const response = await axios.post(`${API_URL}/${roleName}/permissions/remove`, permissions, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error removing permissions:', error);
      throw error;
    }
  },

  // GET /roles/{roleName}/permissions
  getRolePermissions: async (roleName) => {
    try {
      const response = await axios.get(`${API_URL}/${roleName}/permissions`, { headers: getAuthHeader() });
      return response.data;
    } catch (error) {
      console.error('Error fetching role permissions:', error);
      throw error;
    }
  },

  // GET /permissions/list (novo endpoint para list all permissions)
  getAllPermissions: async () => {
    try {
      const response = await axios.get(`${PERM_API_URL}/list`, { headers: getAuthHeader() });
      // Assumindo que retorna Page<PermissionWithRolesDTO>, extrair content e mapear para names
      return response.data.content.map(perm => perm.name);
    } catch (error) {
      console.error('Error fetching all permissions:', error);
      throw error;
    }
  },
};

export default RolesService;