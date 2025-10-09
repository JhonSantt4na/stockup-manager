// src/context/AuthContext.js
import React, { createContext, useState, useContext, useCallback, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [user, setUser] = useState(null);

  // Carrega user inicial se token existir (pra sobreviver reloads)
  useEffect(() => {
    if (token) {
      // Opcional: Busca /users/me pra pegar user fresco, mas por agora usa null e deixa Dashboard fetchar
      console.log('AuthContext: Token carregado do localStorage:', !!token);
    }
  }, [token]);

  const login = useCallback((newToken, userData) => {
    console.log('AuthContext login chamado com token:', !!newToken, 'e user:', userData); // Debug
    setToken(newToken);
    setUser(userData);
    localStorage.setItem('token', newToken);
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + newToken;
  }, []);

  const logout = useCallback(() => {
    console.log('AuthContext logout chamado'); // Debug
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];
  }, []);

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};