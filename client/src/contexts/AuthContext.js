import React, { createContext, useState, useContext, useEffect, useCallback } from 'react';
import axios from 'axios';

const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [user, setUser] = useState(
    localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  );
  const [loading, setLoading] = useState(true);

  const login = useCallback((newToken, userData) => {
    setToken(newToken);
    setUser(userData);
    localStorage.setItem('token', newToken);
    localStorage.setItem('user', JSON.stringify(userData));
    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
  }, []);

  const logout = useCallback(async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (refreshToken) {
      try {
        await axios.post('/auth/logout');
      } catch (err) {
        console.error('Erro no logout:', err);
      }
    }
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('refreshToken');
    delete axios.defaults.headers.common['Authorization'];
    window.location.href = '/';
  }, []);

  const refreshAccessToken = useCallback(async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    const username = user?.name;
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
          // Verifica token com /users/me (seu endpoint)
          await axios.get('/users/me');
          axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
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
  }, [token, user, logout, refreshAccessToken]);

  const value = { token, user, loading, login, logout, refreshAccessToken };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};