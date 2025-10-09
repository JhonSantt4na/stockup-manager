// src/components/Dashboard.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import RoleManager from './RoleManager';
import PermissionManager from './PermissionManager';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [userInfo, setUserInfo] = useState(null);
  const [activeTab, setActiveTab] = useState('roles'); // Padrão pra roles (admin)
  const navigate = useNavigate();
  const isAdmin = user?.roles?.includes('ADMIN') || userInfo?.roles?.includes('ADMIN');

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await axios.get('http://localhost:8080/users/me');
        setUserInfo(response.data);
      } catch (err) {
        console.error('Erro ao buscar usuário:', err);
      }
    };
    fetchUser();
  }, []);

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login', { replace: true });
    } catch (err) {
      console.error('Erro no logout:', err);
      navigate('/login', { replace: true });
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Dashboard</h2>
      <p>Bem-vindo, {user?.username || userInfo?.username}!</p>
      
      {/* Tabs de Navegação - Removido tab 'profile', só link */}
      <nav style={{ marginBottom: '20px' }}>
        <ul style={{ listStyle: 'none', padding: 0, display: 'flex', gap: '10px' }}>
          <li><Link to="/profile" style={{ padding: '10px', background: '#007bff', color: 'white', textDecoration: 'none' }}>Meu Perfil</Link></li>
          <li><Link to="/users" style={{ padding: '10px', background: '#6c757d', color: 'white', textDecoration: 'none' }}>Lista de Usuários</Link></li>
          {isAdmin && <li><button onClick={() => setActiveTab('roles')} style={{ padding: '10px', background: activeTab === 'roles' ? '#007bff' : '#6c757d', color: 'white', border: 'none' }}>Gerenciar Roles</button></li>}
          {isAdmin && <li><button onClick={() => setActiveTab('permissions')} style={{ padding: '10px', background: activeTab === 'permissions' ? '#007bff' : '#6c757d', color: 'white', border: 'none' }}>Gerenciar Permissions</button></li>}
        </ul>
      </nav>

      {/* Conteúdo das Tabs - Removido 'profile' */}
      {activeTab === 'roles' && <RoleManager />}
      {activeTab === 'permissions' && <PermissionManager />}

      <button 
        onClick={handleLogout}
        style={{ padding: '10px', background: '#dc3545', color: 'white', border: 'none', marginTop: '20px', cursor: 'pointer' }}
      >
        Logout
      </button>
    </div>
  );
};

export default Dashboard;