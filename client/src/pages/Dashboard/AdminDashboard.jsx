import React from "react";
import "./Dashboard.css";

const AdminDashboard = () => {
  const menuItems = [
    { name: "Usuários", icon: "👥", path: "/users" },
    { name: "Roles", icon: "🔐", path: "/roles" },
    { name: "Permissions", icon: "🛡️", path: "/permissions" },
    { name: "PDV", icon: "💰", path: "/pdv" },
    { name: "Configurações", icon: "⚙️", path: "/config" }
  ];

  const adminCards = [
    {
      title: "Usuários Ativos",
      value: "1,247",
      icon: "👥",
      type: "admin",
      description: "Usuários cadastrados no sistema"
    },
    {
      title: "Vendas Hoje",
      value: "R$ 12,450",
      icon: "💰",
      type: "admin",
      description: "Total de vendas realizadas hoje"
    },
    {
      title: "Produtos Cadastrados",
      value: "3,891",
      icon: "📦",
      type: "admin",
      description: "Produtos disponíveis no estoque"
    },
    {
      title: "Sistema Online",
      value: "99.9%",
      icon: "🟢",
      type: "admin",
      description: "Uptime do sistema"
    }
  ];

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Painel Administrativo</h1>
        <p className="dashboard-subtitle">Gerencie usuários, permissões e configurações do sistema</p>
      </div>

      <nav className="dashboard-menu">
        {menuItems.map((item, index) => (
          <a key={index} href={item.path} className="menu-item">
            <span>{item.icon}</span>
            <span>{item.name}</span>
          </a>
        ))}
      </nav>

      <div className="cards-grid">
        {adminCards.map((card, index) => (
          <div key={index} className="card">
            <div className="card-header">
              <h3 className="card-title">{card.title}</h3>
              <div className={`card-icon ${card.type}`}>
                {card.icon}
              </div>
            </div>
            <div className="insight-value">{card.value}</div>
            <p style={{ color: "#64748b", fontSize: "0.9rem" }}>{card.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminDashboard;
