import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../../contexts/AuthContext';
import { MdOutlineInsights } from "react-icons/md";
import { PiUsersFourFill } from "react-icons/pi";
import { GrShieldSecurity } from "react-icons/gr";
import './Navbar.css';

import { 
  FaShoppingCart, 
  FaBox, 
  FaUsers, 
  FaChartBar, 
  FaCog,
  FaBars,
  FaChevronLeft,
  FaUsersCog,
  FaChevronRight
} from 'react-icons/fa';

const Navbar = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuth();

  const menuItems = () => {
    if (user?.isAdmin) {
      return [
        { id: 'dashboard', label: 'Dashboard', path: '/dashboard', icon: <MdOutlineInsights /> },
        { id: 'users', label: 'Usuários', path: '/users', icon: <FaUsersCog /> },
        { id: 'roles', label: 'Roles', path: '/roles', icon: <PiUsersFourFill /> },
        { id: 'permissions', label: 'Permissões', path: '/permissions', icon: <GrShieldSecurity /> },
        { id: 'modules', label: 'Vendas', path: '/modules', icon: <FaShoppingCart /> },
        { id: 'stock', label: 'Estoque', path: '/stock', icon: <FaBox /> },
        { id: 'clients', label: 'Clientes', path: '/clients', icon: <FaUsers /> },
        { id: 'reports', label: 'Relatórios', path: '/reports', icon: <FaChartBar /> },
        { id: 'settings', label: 'Configurações', path: '/settings', icon: <FaCog /> }
      ];
    }

    if (user?.roles?.some(role => ["PRO"].includes(role))) {
      return [
        { id: 'dashboard', label: 'Dashboard', path: '/dashboard', icon: <MdOutlineInsights /> },
        { id: 'modules', label: 'Vendas', path: '/modules', icon: <FaShoppingCart /> },
        { id: 'stock', label: 'Estoque', path: '/stock', icon: <FaBox /> },
        { id: 'clients', label: 'Clientes', path: '/clients', icon: <FaUsers /> },
        { id: 'reports', label: 'Relatórios', path: '/reports', icon: <FaChartBar /> },
        { id: 'settings', label: 'Configurações', path: '/settings', icon: <FaCog /> }
      ];
    }

    return [
      { id: 'dashboard', label: 'Dashboard', path: '/dashboard', icon: <MdOutlineInsights /> },
      { id: 'modules', label: 'Vendas', path: '/modules', icon: <FaShoppingCart /> },
      { id: 'stock', label: 'Estoque', path: '/stock', icon: <FaBox /> },
      { id: 'clients', label: 'Clientes', path: '/clients', icon: <FaUsers /> },
      { id: 'settings', label: 'Configurações', path: '/settings', icon: <FaCog /> }
    ];
  };

  const handleItemClick = (path) => {
    navigate(path);
  };

  const getActiveItem = () => {
    const currentPath = location.pathname;
    const items = menuItems();
    const activeItem = items.find(item => currentPath.startsWith(item.path));
    return activeItem ? activeItem.id : '';
  };

  const items = menuItems();

  return (
    <div className={`navbar ${isCollapsed ? 'collapsed' : ''}`}>
      <div className="navbar-header">
        <div className="header-content">
          <FaBars className="logo-icon" />
          {!isCollapsed && <p>Olá, <span className="username">{user?.name || 'Usuário'}</span>!</p>}
        </div>
        <button 
          className="collapse-toggle"
          onClick={() => setIsCollapsed(!isCollapsed)}
        >
          {isCollapsed ? <FaChevronRight /> : <FaChevronLeft />}
        </button>
      </div>
      
      <nav className="navbar-menu">
        <ul>
          {items.map(item => (
            <li 
              key={item.id}
              className={getActiveItem() === item.id ? 'active' : ''}
              onClick={() => handleItemClick(item.path)}
            >
              <span className="menu-icon">{item.icon}</span>
              {!isCollapsed && <span className="menu-item-label">{item.label}</span>}
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
};

export default Navbar;