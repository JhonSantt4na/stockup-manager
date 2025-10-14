import React from "react";
import "./Dashboard.css";

const ProUserDashboard = () => {
  const menuItems = [
    { name: "PDV", icon: "💰", path: "/pdv" },
    { name: "Estoque", icon: "📦", path: "/stock" },
    { name: "Relatórios", icon: "📊", path: "/reports" },
    { name: "Meu Perfil", icon: "👤", path: "/profile" },
    { name: "Configurações", icon: "⚙️", path: "/config" }
  ];

  const insights = [
    {
      title: "Produtos em Falta",
      value: "12",
      change: "-3",
      changeType: "positive",
      icon: "📦"
    },
    {
      title: "Vendas Hoje",
      value: "R$ 2,450",
      change: "+15%",
      changeType: "positive",
      icon: "💰"
    },
    {
      title: "Novos Clientes",
      value: "8",
      change: "+2",
      changeType: "positive",
      icon: "👥"
    },
    {
      title: "Produto Mais Vendido",
      value: "Produto A",
      change: "23 vendas",
      changeType: "neutral",
      icon: "🏆"
    },
    {
      title: "Ticket Médio",
      value: "R$ 125",
      change: "-5%",
      changeType: "negative",
      icon: "📊"
    },
    {
      title: "Estoque Crítico",
      value: "5",
      change: "-1",
      changeType: "positive",
      icon: "⚠️"
    }
  ];

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Dashboard Pro</h1>
        <p className="dashboard-subtitle">Insights avançados para impulsionar seu negócio</p>
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
        <h3>Insights do PDV</h3>
        <div className="insights-grid">
          {insights.map((insight, index) => (
            <div key={index} className="insight-card">
              <div className="insight-header">
                <h4 className="insight-title">{insight.title}</h4>
                <span className="action-icon">{insight.icon}</span>
              </div>
              <div className="insight-value">{insight.value}</div>
              <div className={`insight-change ${insight.changeType}`}>
                {insight.changeType === "positive" && <span className="arrow-up"></span>}
                {insight.changeType === "negative" && <span className="arrow-down"></span>}
                <span>{insight.change}</span>
                {insight.changeType === "positive" && <span>vs ontem</span>}
                {insight.changeType === "negative" && <span>vs ontem</span>}
                {insight.changeType === "neutral" && <span>hoje</span>}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProUserDashboard;
