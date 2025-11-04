// Config.jsx (updated with new defaults and localStorage persistence)
import React, { useState, useEffect } from "react";
import "./settings.css";
import PageStruct from "../Layout/PageStruct/PageStruct";
import { FaChevronLeft, FaChevronRight, FaBars, FaCog, FaPalette, FaPhone, FaDollarSign, FaPencilAlt, FaTrash, FaSave, FaPlus, FaTimes, FaWrench } from 'react-icons/fa';

const defaultConfigs = [
  { id: 1, key: "nome_sistema", label: "Nome do Sistema", value: "StockUp Manager", category: "geral", type: "text" },
  { id: 2, key: "email_suporte", label: "E-mail de Suporte", value: "suporte@stockup.com", category: "contato", type: "email" },
  { id: 3, key: "telefone_suporte", label: "Telefone de Suporte", value: "+55 (11) 99999-9999", category: "contato", type: "text" },
  { id: 4, key: "logo_sistema", label: "Logo do Sistema", value: "/logo.png", category: "aparencia", type: "text" },
  { id: 5, key: "tema_sistema", label: "Tema do Sistema", value: "Escuro", category: "aparencia", type: "select", options: ["Escuro", "Claro", "Auto"] },
  { id: 6, key: "idioma_sistema", label: "Idioma do Sistema", value: "Português", category: "geral", type: "select", options: ["Português", "Inglês", "Espanhol"] },
  { id: 7, key: "moeda_padrao", label: "Moeda Padrão", value: "BRL", category: "financeiro", type: "select", options: ["BRL", "USD", "EUR"] },
  { id: 8, key: "timezone", label: "Fuso Horário", value: "America/Sao_Paulo", category: "geral", type: "text" },
  { id: 9, key: "versao_sistema", label: "Versão do Sistema", value: "1.0.0", category: "geral", type: "text" },
  { id: 10, key: "cor_primaria", label: "Cor Primária", value: "#45C35B", category: "aparencia", type: "select", options: ["#45C35B", "#6554C0", "#FF6B6B", "#FFA500"] },
  { id: 11, key: "endereco_suporte", label: "Endereço de Suporte", value: "Rua Exemplo, 123 - São Paulo, SP", category: "contato", type: "text" },
  { id: 12, key: "taxa_imposto", label: "Taxa de Imposto Padrão", value: "0.18", category: "financeiro", type: "number" },
  { id: 13, key: "razao_social", label: "Razão Social", value: "Jhon Tec LTDA", category: "geral", type: "text" },
  { id: 14, key: "cnpj", label: "CNPJ", value: "785.421.556-22", category: "geral", type: "text" },
];

const categories = [
  { id: "geral", name: "Configurações Gerais", icon: <FaCog />, color: "#45C35B" },
  { id: "aparencia", name: "Aparência", icon: <FaPalette />, color: "#6554C0" },
  { id: "contato", name: "Contato & Suporte", icon: <FaPhone />, color: "#FF6B6B" },
  { id: "financeiro", name: "Financeiro", icon: <FaDollarSign />, color: "#FFA500" },
];

export default function Config() {
  const [configs, setConfigs] = useState(() => {
    const saved = localStorage.getItem('systemConfigs');
    return saved ? JSON.parse(saved) : defaultConfigs;
  });
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ label: "", value: "", category: "geral", type: "text" });
  const [activeCategory, setActiveCategory] = useState("geral");
  const [searchTerm, setSearchTerm] = useState("");
  const [isCollapsed, setIsCollapsed] = useState(false);

  useEffect(() => {
    localStorage.setItem('systemConfigs', JSON.stringify(configs));
  }, [configs]);

  const filteredConfigs = configs.filter(cfg =>
    (activeCategory !== "geral" ? cfg.category === activeCategory : true) &&
    cfg.label.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const startEdit = (config) => {
    setEditingId(config.id);
    setForm(config);
  };

  const cancelEdit = () => {
    setEditingId(null);
    setForm({ label: "", value: "", category: "geral", type: "text" });
  };

  const saveConfig = (e) => {
    e.preventDefault();
    if (!form.label.trim()) return;
    if (editingId) {
      setConfigs(configs.map(cfg =>
        cfg.id === editingId ? { ...cfg, ...form } : cfg
      ));
    } else {
      const newConfig = {
        id: Date.now(),
        key: form.label.toLowerCase().replace(/\s+/g, '_'),
        ...form
      };
      setConfigs([...configs, newConfig]);
    }
    cancelEdit();
  };

  const deleteConfig = (id) => {
    if (window.confirm("Tem certeza que deseja excluir esta configuração?")) {
      setConfigs(configs.filter(cfg => cfg.id !== id));
    }
  };

  const renderInputField = (config) => {
    if (editingId === config.id) {
      if (config.type === "select" && config.options) {
        return (
          <select
            value={form.value}
            onChange={(e) => setForm({...form, value: e.target.value})}
            className="config-input"
          >
            {config.options.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        );
      }
      return (
        <input
          type={config.type}
          value={form.value}
          onChange={(e) => setForm({...form, value: e.target.value})}
          className="config-input"
        />
      );
    }
    return <span className="config-value">{config.value}</span>;
  };

  const headerContent = (
    <div className="config-header">
      <h1>Configurações do Sistema</h1>
    </div>
  );

  const sidebarContent = (
    <div className={`config-sidebar ${isCollapsed ? 'collapsed' : ''}`}>
      <div className="sidebar-header">
        <div className="header-content">
          <FaBars className="logo-icon" />
          {!isCollapsed && <p>Categorias</p>}
        </div>
        <button
          className="collapse-toggle"
          onClick={() => setIsCollapsed(!isCollapsed)}
        >
          {isCollapsed ? <FaChevronLeft /> : <FaChevronRight />}
        </button>
      </div>
      {!isCollapsed && (
        <div className="search-box">
          <input
            type="text"
            placeholder="🔍 Buscar configuração..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>
      )}
      <nav className="sidebar-menu">
        <ul>
          {categories.map(category => (
            <li
              key={category.id}
              className={activeCategory === category.id ? 'active' : ''}
              onClick={() => setActiveCategory(category.id)}
            >
              <span className="menu-icon">{category.icon}</span>
              {!isCollapsed && <span className="menu-item-label">{category.name}</span>}
              {!isCollapsed && (
                <span className="config-count">
                  {configs.filter(cfg => cfg.category === category.id).length}
                </span>
              )}
            </li>
          ))}
        </ul>
      </nav>
      {!isCollapsed && (
        <div className="add-config-section">
          <h3>Adicionar Configuração</h3>
          <form onSubmit={saveConfig} className="config-form">
            <input
              type="text"
              placeholder="Nome da configuração"
              value={form.label}
              onChange={(e) => setForm({...form, label: e.target.value})}
              className="form-input"
              required
            />
            <input
              type="text"
              placeholder="Valor"
              value={form.value}
              onChange={(e) => setForm({...form, value: e.target.value})}
              className="form-input"
              required
            />
            <select
              value={form.category}
              onChange={(e) => setForm({...form, category: e.target.value})}
              className="form-input"
            >
              {categories.map(cat => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
            <div className="form-actions">
              <button type="submit" className="btn-primary">
                {editingId ? <FaSave /> : <FaPlus />}
              </button>
              {editingId && (
                <button type="button" onClick={cancelEdit} className="btn-cancel">
                  <FaTimes />
                </button>
              )}
            </div>
          </form>
        </div>
      )}
    </div>
  );

  const bodyContent = (
    <div className="config-layout">
      {/* Lista de Configurações */}
      <div className="config-content">
        <div className="configs-grid">
          {filteredConfigs.map(config => (
            <div key={config.id} className="config-card">
              <div className="config-header-card">
                <h3>{config.label}</h3>
                <div className="config-actions">
                  <button
                    onClick={() => startEdit(config)}
                    className="btn-edit"
                  >
                    <FaPencilAlt />
                  </button>
                  <button
                    onClick={() => deleteConfig(config.id)}
                    className="btn-delete"
                  >
                    <FaTrash />
                  </button>
                </div>
              </div>
              <div className="config-body">
                {renderInputField(config)}
              </div>
              <div className="config-footer">
                <span className="config-key">Chave: {config.key}</span>
                <span className="config-type">{config.type}</span>
              </div>
            </div>
          ))}
        </div>
        {filteredConfigs.length === 0 && (
          <div className="empty-state">
            <div className="empty-icon"><FaWrench /></div>
            <h3>Nenhuma configuração encontrada</h3>
            <p>Adicione uma nova configuração ou tente outra categoria</p>
          </div>
        )}
      </div>
      {sidebarContent}
    </div>
  );

  const footer = (
    <div className="footer-empty"></div>
  );

  return (
    <PageStruct
      header={headerContent}
      body={bodyContent}
      footer={footer}
    />
  );
}