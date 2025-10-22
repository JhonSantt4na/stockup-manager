import React, { useState } from "react"; // Added useState import
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { FaBell, FaUserCircle, FaSearch, FaSignOutAlt } from "react-icons/fa";
import ProfileModal from "../../components/Modals/Profile/ProfileModal"; // Importar o novo modal
import "./Header.css";

function Header() {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [profileModalOpen, setProfileModalOpen] = useState(false);

  const isLoginPage = location.pathname === "/login";

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const handleProfileClick = () => {
    setProfileModalOpen(true);
  };

  const handleHelpClick = () => {
    navigate("/help");
  };

  const getRoleBadge = () => {
    if (user?.isAdmin) return <span className="role-badge admin-badge">ADMIN</span>;
    if (user?.roles?.some(role => ["PRO", "ROLE_PRO"].includes(role))) return <span className="role-badge pro-badge">PRO</span>;
    return <span className="role-badge free-badge">FREE</span>;
  };

  const getCurrentDateTime = () => {
    const now = new Date();
    const date = now.toLocaleDateString('pt-BR', { weekday: 'short', day: '2-digit', month: 'short' });
    const time = now.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    return { date, time };
  };

  const { date, time } = getCurrentDateTime();

  return (
    <header className="app-header">
      {isLoginPage || !user ? (
        <div className="header-login">
          <div className="logo-center">
            <img src="/Logo.png" alt="StockUp Manager" className="logo-img" />
          </div>
          <button className="help-btn" onClick={handleHelpClick}>
            Ajuda
          </button>
        </div>
      ) : (
        <>
          <div className="header-left">
            <div className="logo">
              <img src="/Logo.png" alt="StockUp Manager" className="logo-img" />
            </div>
            {user && getRoleBadge()}
            <div className="date-time-container">
              <div className="time-highlight">{time}</div>
              <div className="date-below">{date}</div>
            </div>
          </div>

          <div className="header-center">
            <div className="search-box">
              <FaSearch className="search-icon" />
              <input
                type="text"
                placeholder="Pesquisar ..."
                className="search-input"
              />
            </div>
          </div>

          <div className="header-right">
            <FaBell className="header-icon" title="Notificações" />

            <div className="user-profile">
              <FaUserCircle className="user-avatar" />
              <button
                className="user-button"
                onClick={handleProfileClick}
                title="Ver perfil"
              >
                {user?.name ? user.name : "Usuário"}
              </button>
            </div>

            <button
              className="logout-btn"
              onClick={handleLogout}
              title="Sair da conta"
            >
              <FaSignOutAlt className="logout-icon" />
              <span className="logout-text">Sair</span>
            </button>
          </div>
        </>
      )}

      {/* Modal de Perfil */}
      {profileModalOpen && (
        <ProfileModal 
          user={user} 
          onClose={() => setProfileModalOpen(false)} 
        />
      )}
    </header>
  );
}

export default Header;