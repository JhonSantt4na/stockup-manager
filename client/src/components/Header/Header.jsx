import { useAuth } from "../../contexts/AuthContext";
import "./Header.css";

function Header() {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    console.log("Logout!");
    logout();
  };

  const getRoleBadge = () => {
    if (user?.isAdmin) {
      return <span className="role-badge admin-badge">ADMIN</span>;
    }
    if (user?.roles?.some(role => role === "PRO" || role === "USER" || role === "ROLE_PRO")) {
      return <span className="role-badge pro-badge">PRO</span>;
    }
    return <span className="role-badge free-badge">FREE</span>;
  };

  return (
    <header className="app-header">
      <div className="header-left">
        <img src="/Logo.png" alt="StockUp Manager" className="header-logo" />
        {user && getRoleBadge()}
      </div>
      <div className="header-right">
        <span className="user-name"> {user?.name ? `Bem-vindo, ${user.name}` : "Ajuda"}</span>
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