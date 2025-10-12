import React from "react";
import { useAuth } from "../../contexts/AuthContext";
import "./Header.css";

function Header() {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    console.log("Logout!");
    logout();
  };

  return (
    <header className="app-header">
      <div className="header-left">
        <img src="/Logo.png" alt="StockUp Manager" className="header-logo" />
        {user?.isAdmin && <span className="admin-badge">ADMIN</span>}
      </div>
      <div className="header-right">
        <span className="user-name">{user?.name || "Visitante"}</span>
        {user && (
          <button className="btn-logout" onClick={handleLogout}>
            Sair
          </button>
        )}
      </div>
    </header>
  );
}

export default Header;