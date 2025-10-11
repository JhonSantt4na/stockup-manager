// src/components/Login/Login.jsx
import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';  // Novo caminho
import { loginUser } from '../../services/authService';  // Extraída pra service
import './Login.css';  // CSS separado

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();
  const submitTimeoutRef = useRef(null); // Pra debounce

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (loading) return;

    setLoading(true);
    setError('');
    clearTimeout(submitTimeoutRef.current);

    try {
      console.log('Login: Enviando POST pra /auth/login com username:', username);
      const data = await loginUser({ username, password });  // Usa service

      // Verifica se autenticou
      if (!data.authenticated) {
        throw new Error('Falha na autenticação. Verifique username e senha.');
      }

      // Pega o accessToken como token principal
      const token = data.accessToken;
      if (!token) {
        throw new Error('accessToken não encontrado no response.');
      }

      // Cria userData básico (expande com mais campos se o backend retornar)
      const userData = {
        username: data.username,
        // Opcional: roles do token (pra isso, decodificaria o JWT, mas por agora usa só username)
        // created: data.created, etc. se quiser
      };

      // Salva refreshToken também (pra refresh futuro)
      if (data.refreshToken) {
        localStorage.setItem('refreshToken', data.refreshToken);
      }

      login(token, userData);
      
      setTimeout(() => {
        const savedToken = localStorage.getItem('token');
        const savedRefresh = localStorage.getItem('refreshToken');
        console.log('Login: Token salvo?', !!savedToken, 'Refresh salvo?', !!savedRefresh);
        if (savedToken) {
          console.log('Login: Sucesso! Navegando pro dashboard.');
          navigate('/dashboard', { replace: true });
        } else {
          throw new Error('Falha ao salvar token');
        }
      }, 100);

    } catch (err) {
      console.error('Login: Erro completo:', err);
      const msg = err.response?.data?.message || err.message || 'Erro desconhecido.';
      setError('Erro no login: ' + msg);
    } finally {
      submitTimeoutRef.current = setTimeout(() => setLoading(false), 2000);
    }
  };

  return (
    <div className="login-container">
      <h2 className="login-title">Login</h2>
      <form onSubmit={handleSubmit} className="login-form">
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          disabled={loading}
          className="login-input"
          autoComplete="username"
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          disabled={loading}
          className="login-input"
          autoComplete="current-password"
        />
        <button 
          type="submit" 
          disabled={loading}
          className={`login-btn ${loading ? 'disabled' : ''}`}
        >
          {loading ? 'Entrando... (aguarde)' : 'Entrar'}
        </button>
      </form>
      {error && <p className="error-message">{error}</p>}
      <p className="register-link">
        <a href="/register">Não tem conta? Registre-se</a>
      </p>
    </div>
  );
};

export default Login;