import React from "react";
import "./Dashboard.css";

const FreeUserDashboard = () => {
  const menuItems = [
    { name: "PDV", icon: "💰", path: "/pdv" },
    { name: "Estoque", icon: "📦", path: "/stock" },
    { name: "Relatórios", icon: "📊", path: "/reports" },
    { name: "Meu Perfil", icon: "👤", path: "/profile" },
    { name: "Configurações", icon: "⚙️", path: "/config" }
  ];

  const quickActions = [
    {
      icon: "➕",
      title: "Adicionar Produto",
      description: "Cadastre novos produtos no estoque",
      action: () => console.log("Adicionar produto")
    },
    {
      icon: "📊",
      title: "Verificar Relatório",
      description: "Visualize relatórios de vendas e estoque",
      action: () => console.log("Ver relatório")
    },
    {
      icon: "📦",
      title: "Verificar Estoque",
      description: "Consulte produtos disponíveis",
      action: () => console.log("Ver estoque")
    }
  ];

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Bem-vindo ao StockUp Manager</h1>
        <p className="dashboard-subtitle">Gerencie seu negócio de forma simples e eficiente</p>
      </div>

      <nav className="dashboard-menu">
        {menuItems.map((item, index) => (
          <a key={index} href={item.path} className="menu-item">
            <span>{item.icon}</span>
            <span>{item.name}</span>
          </a>
        ))}
      </nav>

      <div className="quick-actions">
        <h3>Atalhos Rápidos</h3>
        <div className="actions-grid">
          {quickActions.map((action, index) => (
            <div key={index} className="action-card" onClick={action.action}>
              <div className="action-icon">{action.icon}</div>
              <h4 className="action-title">{action.title}</h4>
              <p className="action-description">{action.description}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default FreeUserDashboard;
