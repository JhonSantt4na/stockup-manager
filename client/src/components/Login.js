// src/components/Login.js
import React, { useState, useRef } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

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
      const response = await axios.post('http://localhost:8080/auth/login', { username, password });
      console.log('Login: Response.data (formato exato):', response.data);

      const data = response.data;

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
    <div style={{ maxWidth: '400px', margin: '100px auto', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          disabled={loading}
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
          autocomplete="username"
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          disabled={loading}
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
          autocomplete="current-password"
        />
        <button 
          type="submit" 
          disabled={loading}
          style={{ 
            width: '100%', 
            padding: '10px', 
            background: loading ? '#6c757d' : '#007bff', 
            color: 'white', 
            border: 'none',
            cursor: loading ? 'not-allowed' : 'pointer'
          }}
        >
          {loading ? 'Entrando... (aguarde)' : 'Entrar'}
        </button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <p style={{ textAlign: 'center' }}><a href="/register" style={{ color: '#007bff' }}>Não tem conta? Registre-se</a></p>
    </div>
  );
};

export default Login;