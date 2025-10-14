import React, { createContext, useState, useContext, useEffect, useCallback } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [user, setUser] = useState(
    localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  );
  const [loading, setLoading] = useState(true);

  const login = useCallback((newToken, userData) => {
    const decoded = jwtDecode(newToken);
    const roles = decoded?.roles || [];

    const updatedUserData = {
      ...userData,
      roles,
      isAdmin: roles.includes('ADMIN') || roles.includes('ROLE_ADMIN') || false
    };

    setToken(newToken);
    setUser(updatedUserData);

    localStorage.setItem('token', newToken);
    localStorage.setItem('user', JSON.stringify(updatedUserData));

    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
  }, []);

  const logout = useCallback(() => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('refreshToken');
    delete axios.defaults.headers.common['Authorization'];
  }, []);

  const refreshAccessToken = useCallback(async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    const username = user?.username || user?.name;

    if (!refreshToken || !username) {
      logout();
      return false;
    }

    try {
      const response = await axios.put(`/auth/refresh/${username}`, {}, {
        headers: { 'X-Refresh-Token': refreshToken }
      });

      const { accessToken, refreshToken: newRefreshToken } = response.data;
      login(accessToken, user);

      if (newRefreshToken) {
        localStorage.setItem('refreshToken', newRefreshToken);
      }

      return true;
    } catch (err) {
      console.error('Refresh falhou:', err);
      logout();
      return false;
    }
  }, [user, login, logout]);

  useEffect(() => {
    const loadUser = async () => {
      if (token && user) {
        try {
          axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
          await axios.get('/users/me');
        } catch (err) {
          if (err.response?.status === 401) {
            await refreshAccessToken();
          } else {
            logout();
          }
        }
      } else {
        delete axios.defaults.headers.common['Authorization'];
      }
      setLoading(false);
    };
    loadUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  const value = { token, user, loading, login, logout, refreshAccessToken };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
