import axios from 'axios';

export const fetchRoles = async () => {
  const response = await axios.get('http://localhost:8080/roles/list');
  return response.data.content || response.data || [];
};

export const fetchPermissions = async (params) => {
  const response = await axios.get('http://localhost:8080/permissions/list', { params });
  return response.data.content;
};

export const fetchRolePermissions = async (roleName) => {
  const response = await axios.get(`http://localhost:8080/roles/${roleName}/permissions`);
  return response.data;
};

export const createRole = async (data) => {
  const response = await axios.post('http://localhost:8080/roles/create', data);
  return response.data;
};

export const updateRoleName = async (data) => {
  const response = await axios.put('http://localhost:8080/roles/update', data);
  return response.data;
};

export const assignPermissionsToRole = async (roleName, permissionDescriptions) => {
  const response = await axios.post(`http://localhost:8080/roles/${roleName}/permissions/assign`, permissionDescriptions);
  return response.data;
};

export const removePermissionsFromRole = async (roleName, permissionDescriptions) => {
  const response = await axios.post(`http://localhost:8080/roles/${roleName}/permissions/remove`, permissionDescriptions);
  return response.data;
};