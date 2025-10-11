// src/components/Register/Register.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';  // Novo caminho
import { registerUser } from '../../services/authService';  // Extraída pra service
import './Register.css';  // CSS separado

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
      const data = await registerUser(formData);  // Usa service

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
    <div className="register-container">
      <h2 className="register-title">Registro</h2>
      <form onSubmit={handleSubmit} className="register-form">
        <input
          type="text"
          name="username"
          placeholder="Username"
          onChange={handleChange}
          required
          className="register-input"
        />
        <input
          type="text"
          name="fullName"
          placeholder="Nome Completo"
          onChange={handleChange}
          required
          className="register-input"
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          onChange={handleChange}
          required
          className="register-input"
        />
        <input
          type="password"
          name="password"
          placeholder="Password (mín. 8 chars)"
          onChange={handleChange}
          required
          className="register-input"
        />
        <button type="submit" className="register-btn">Registrar</button>
      </form>
      {error && <p className="error-message">{error}</p>}
      <p className="login-link">
        <a href="/login">Já tem conta? Faça login</a>
      </p>
    </div>
  );
};

export default Register;