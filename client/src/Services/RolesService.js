import api from "../Api";

const handleError = (error) => {
  console.error("Erro no RolesService:", error.response || error.message);
  throw error.response?.data || error;
};

const RolesService = {
  getRolesWithUsers: async (page = 0, size = 10, search = "") => {
    try {
      const response = await api.get("/roles/list-with-users", {
        params: {
          page,
          size,
          search: search || undefined,
        },
      });
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  createRole: async (roleData) => {
    try {
      const response = await api.post("/roles/create", roleData);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  updateRole: async (updateData) => {
    try {
      const response = await api.put("/roles/update", updateData);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  deleteRole: async (name) => {
    try {
      const response = await api.delete(`/roles/delete/${name}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  getRolePermissions: async (roleName) => {
    try {
      const response = await api.get(`/roles/${roleName}/permissions`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  assignPermissions: async (roleName, permissions) => {
    try {
      const response = await api.post(
        `/roles/${roleName}/permissions/assign`,
        permissions
      );
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  removePermissions: async (roleName, permissions) => {
    try {
      const response = await api.post(
        `/roles/${roleName}/permissions/remove`,
        permissions
      );
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },


  getAllPermissions: async () => {
    try {
      const response = await api.get("/permissions/list");
      return response.data?.content?.map((perm) => perm.name) || [];
    } catch (error) {
      handleError(error);
    }
  },
};

export default RolesService;