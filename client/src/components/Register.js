// src/components/Register.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Register = () => {
  const [formData, setFormData] = useState({ 
    username: '', 
    fullName: '', 
    email: '', 
    password: '' 
  }); // Adicionei fullName conforme o request body
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      console.log('Registro: Enviando POST pra /users/register com:', formData);
      const response = await axios.post('http://localhost:8080/users/register', formData);
      console.log('Registro: Response.data (sucesso):', response.data);

      const data = response.data;

      // Extrai user e token do response
      const userData = data.user;
      const token = data.token.accessToken;
      const refreshToken = data.token.refreshToken;

      if (!token || !userData) {
        throw new Error('Token ou user não encontrados no response.');
      }

      // Salva refreshToken
      if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
      }

      // Auto-login: chama login e vai pro dashboard
      login(token, userData);
      alert('Usuário registrado e logado com sucesso!');
      navigate('/dashboard', { replace: true });

    } catch (err) {
      console.error('Registro: Erro completo:', err);
      let msg = 'Erro no registro: ';
      if (err.response?.status === 400 && err.response?.data?.errors) {
        // Parseia erros de validação (padrão Spring)
        const errors = err.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ');
        msg += `Validação falhou: ${errors}`;
      } else {
        msg += err.response?.data?.message || err.message || 'Tente novamente';
      }
      setError(msg);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div style={{ maxWidth: '400px', margin: '100px auto', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
      <h2>Registro</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Username"
          onChange={handleChange}
          required
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <input
          type="text"
          name="fullName"
          placeholder="Nome Completo"
          onChange={handleChange}
          required
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          onChange={handleChange}
          required
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <input
          type="password"
          name="password"
          placeholder="Password (mín. 8 chars)"
          onChange={handleChange}
          required
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <button type="submit" style={{ width: '100%', padding: '10px', background: '#28a745', color: 'white', border: 'none' }}>Registrar</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <p style={{ textAlign: 'center' }}><a href="/login" style={{ color: '#007bff' }}>Já tem conta? Faça login</a></p>
    </div>
  );
};

export default Register;