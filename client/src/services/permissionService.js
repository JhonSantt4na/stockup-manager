import axios from 'axios';

export const fetchPermissions = async (endpoint, params) => {
  const response = await axios.get(`http://localhost:8080/permissions${endpoint}`, { params });
  return response.data;
};

export const createPermission = async (data) => {
  const response = await axios.post('http://localhost:8080/permissions/create', data);
  return response.data;
};

export const updatePermission = async (data) => {
  const response = await axios.put('http://localhost:8080/permissions/update', data);
  return response.data;
};

export const deletePermission = async (description) => {
  const encodedDescription = encodeURIComponent(description);
  const response = await axios.delete(`http://localhost:8080/permissions/delete/${encodedDescription}`);
  return response.data;
};