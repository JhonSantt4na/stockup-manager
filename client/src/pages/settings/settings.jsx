import React, { useState } from "react";
import "./settings.css";

const defaultConfigs = [
  { id: 1, key: "nome_sistema", label: "Nome do Sistema", value: "StockUp Manager", category: "geral", type: "text" },
  { id: 2, key: "email_suporte", label: "E-mail de Suporte", value: "suporte@stockup.com", category: "contato", type: "email" },
  { id: 3, key: "telefone_suporte", label: "Telefone de Suporte", value: "+55 (11) 99999-9999", category: "contato", type: "text" },
  { id: 4, key: "logo_sistema", label: "Logo do Sistema", value: "/logo.png", category: "aparencia", type: "text" },
  { id: 5, key: "tema_sistema", label: "Tema do Sistema", value: "Escuro", category: "aparencia", type: "select", options: ["Escuro", "Claro", "Auto"] },
  { id: 6, key: "idioma_sistema", label: "Idioma do Sistema", value: "Português", category: "geral", type: "select", options: ["Português", "Inglês", "Espanhol"] },
  { id: 7, key: "moeda_padrao", label: "Moeda Padrão", value: "BRL", category: "financeiro", type: "select", options: ["BRL", "USD", "EUR"] },
  { id: 8, key: "timezone", label: "Fuso Horário", value: "America/Sao_Paulo", category: "geral", type: "text" },
];

const categories = [
  { id: "geral", name: "Configurações Gerais", icon: "⚙️", color: "#45C35B" },
  { id: "aparencia", name: "Aparência", icon: "🎨", color: "#6554C0" },
  { id: "contato", name: "Contato & Suporte", icon: "📞", color: "#FF6B6B" },
  { id: "financeiro", name: "Financeiro", icon: "💰", color: "#FFA500" },
];

export default function Config() {
  const [configs, setConfigs] = useState(defaultConfigs);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ label: "", value: "", category: "geral", type: "text" });
  const [activeCategory, setActiveCategory] = useState("geral");
  const [searchTerm, setSearchTerm] = useState("");

  const filteredConfigs = configs.filter(cfg => 
    cfg.category === activeCategory && 
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

  return (
    <div className="config-page">
      <div className="config-header">
        <h1>⚙️ Configurações do Sistema</h1>
        <p>Gerencie as configurações do StockUp Manager</p>
      </div>

      <div className="config-layout">
        {/* Sidebar de Categorias */}
        <div className="config-sidebar">
          <div className="search-box">
            <input
              type="text"
              placeholder="🔍 Buscar configuração..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>
          
          <div className="categories-list">
            {categories.map(category => (
              <button
                key={category.id}
                className={`category-btn ${activeCategory === category.id ? 'active' : ''}`}
                onClick={() => setActiveCategory(category.id)}
                style={{ '--category-color': category.color }}
              >
                <span className="category-icon">{category.icon}</span>
                <span className="category-name">{category.name}</span>
                <span className="config-count">
                  {configs.filter(cfg => cfg.category === category.id).length}
                </span>
              </button>
            ))}
          </div>

          {/* Formulário para nova configuração */}
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
                  {editingId ? "💾 Salvar" : "➕ Adicionar"}
                </button>
                {editingId && (
                  <button type="button" onClick={cancelEdit} className="btn-cancel">
                    ❌ Cancelar
                  </button>
                )}
              </div>
            </form>
          </div>
        </div>

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
                      ✏️
                    </button>
                    <button 
                      onClick={() => deleteConfig(config.id)}
                      className="btn-delete"
                    >
                      🗑️
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
              <div className="empty-icon">🔧</div>
              <h3>Nenhuma configuração encontrada</h3>
              <p>Adicione uma nova configuração ou tente outra categoria</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}