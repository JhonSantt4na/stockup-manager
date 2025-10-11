import axios from 'axios';

export const loginUser = async (credentials) => {
  const response = await axios.post('http://localhost:8080/auth/login', credentials);
  return response.data;
};

export const registerUser = async (formData) => {
  const response = await axios.post('http://localhost:8080/users/register', formData);
  return response.data;
};