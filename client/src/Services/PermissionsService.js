import Api from "../Api";

const PermissionsService = {
  createPermission: async (data) => {
    const response = await Api.post("/permissions/create", data);
    return response.data;
  },

  updatePermission: async (data) => {
    const response = await Api.put("/permissions/update", data);
    return response.data;
  },

  deletePermission: async (description) => {
    const response = await Api.delete(`/permissions/delete/${description}`);
    return response.data;
  },

  getPermissionByDescription: async (description) => {
    const response = await Api.get(`/permissions/${description}`);
    return response.data;
  },

  getAllPermissions: async (page = 0, size = 10, sort = ["description", "asc"]) => {
    const response = await Api.get("/permissions/list", {
      params: { page, size, sort: sort.join(",") },
    });
    return response.data;
  },

  getAllActivePermissions: async (page = 0, size = 10, sort = ["description", "asc"]) => {
    const response = await Api.get("/permissions/listActive", {
      params: { page, size, sort: sort.join(",") },
    });
    return response.data;
  },
};

export default PermissionsService;