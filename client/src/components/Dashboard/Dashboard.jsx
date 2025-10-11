// src/components/Dashboard/Dashboard.jsx
import React, { useEffect, useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';  // Ajuste pro novo caminho (mande o AuthContext pra eu refatorar)
import { Link, useNavigate } from 'react-router-dom';
import { getCurrentUser } from '../../services/userService';  // Extraída pra service
import RoleManager from '../RoleManager/RoleManager';  // Novo caminho
import PermissionManager from '../PermissionManager/PermissionManager';  // Novo caminho
import './Dashboard.css';  // CSS separado

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [userInfo, setUserInfo] = useState(null);
  const [activeTab, setActiveTab] = useState('roles'); // Padrão pra roles (admin)
  const navigate = useNavigate();
  const isAdmin = user?.roles?.includes('ADMIN') || userInfo?.roles?.includes('ADMIN');

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await getCurrentUser();  // Usa a service
        setUserInfo(response);
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
    <div className="dashboard-container">
      <h2 className="dashboard-title">Dashboard</h2>
      <p className="dashboard-welcome">Bem-vindo, {user?.username || userInfo?.username}!</p>
      
      {/* Tabs de Navegação - Removido tab 'profile', só link */}
      <nav className="dashboard-nav">
        <ul className="nav-list">
          <li><Link to="/profile" className="nav-link nav-link-primary">Meu Perfil</Link></li>
          <li><Link to="/users" className="nav-link nav-link-secondary">Lista de Usuários</Link></li>
          {isAdmin && <li><button onClick={() => setActiveTab('roles')} className={`nav-btn ${activeTab === 'roles' ? 'active' : ''}`}>Gerenciar Roles</button></li>}
          {isAdmin && <li><button onClick={() => setActiveTab('permissions')} className={`nav-btn ${activeTab === 'permissions' ? 'active' : ''}`}>Gerenciar Permissions</button></li>}
        </ul>
      </nav>

      {/* Conteúdo das Tabs - Removido 'profile' */}
      {activeTab === 'roles' && <RoleManager />}
      {activeTab === 'permissions' && <PermissionManager />}

      <button 
        onClick={handleLogout}
        className="logout-btn"
      >
        Logout
      </button>
    </div>
  );
};

export default Dashboard;