import axios from 'axios';

export const getCurrentUser = async () => {
  const response = await axios.get('http://localhost:8080/users/me');
  return response.data;
};

export const updateProfile = async (data) => {
  const response = await axios.put('http://localhost:8080/users/update', data);
  return response.data;
};

export const changePassword = async (data) => {
  const response = await axios.put('http://localhost:8080/users/change-password', data);
  return response.data;
};

export const listUsers = async (params) => {
  const response = await axios.get('http://localhost:8080/users/listActive', { params });
  return response.data;
};

export const findUser = async (username) => {
  const response = await axios.get(`http://localhost:8080/users/find/${username}`);
  return response.data;
};

export const deleteUser = async (username) => {
  const response = await axios.delete(`http://localhost:8080/users/delete/${username}`);
  return response.data;
};

export const getUserRoles = async (username) => {
  const response = await axios.get(`http://localhost:8080/users/${username}/roles`);
  return response.data;
};

export const assignRolesToUser = async (username, roleNames) => {
  const response = await axios.post(`http://localhost:8080/users/${username}/roles/assign`, roleNames);
  return response.data;
};

export const removeRolesFromUser = async (username, roleNames) => {
  const response = await axios.post(`http://localhost:8080/users/${username}/roles/remove`, roleNames);
  return response.data;
};

export const fetchAllRoles = async () => {
  const response = await axios.get('http://localhost:8080/roles/list');
  return response.data.content || response.data || [];
};