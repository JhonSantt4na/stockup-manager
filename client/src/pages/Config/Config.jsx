import React, { useState } from "react";
import "./Config.css";

const defaultConfigs = [
  { key: "Nome do Sistema", value: "StockUp Manager" },
  { key: "E-mail de suporte", value: "suporte@stockup.com" },
];

export default function Config() {
  const [configs, setConfigs] = useState(defaultConfigs);
  const [editIndex, setEditIndex] = useState(null);
  const [form, setForm] = useState({ key: "", value: "" });

  const onEdit = (cfg, idx) => {
    setEditIndex(idx);
    setForm(cfg);
  };
  const onRemove = (idx) => {
    setConfigs(cfgs => cfgs.filter((_, i) => i !== idx));
  };
  const onSubmit = (e) => {
    e.preventDefault();
    if (!form.key.trim()) return;
    if (editIndex !== null) {
      setConfigs(cfgs => cfgs.map((c, i) => i === editIndex ? {...form} : c));
      setEditIndex(null);
    } else {
      setConfigs(cfgs => [...cfgs, form]);
    }
    setForm({ key: "", value: "" });
  };
  const onCancel = () => {
    setEditIndex(null);
    setForm({ key: "", value: "" });
  };

  return (
    <div className="config-container">
      <h2>Configurações Gerais</h2>
      <form className="config-form" onSubmit={onSubmit}>
        <input
          type="text"
          placeholder="Nome da configuração"
          value={form.key}
          onChange={e => setForm(f => ({ ...f, key: e.target.value }))}
          required
        />
        <input
          type="text"
          placeholder="Valor"
          value={form.value}
          onChange={e => setForm(f => ({ ...f, value: e.target.value }))}
          required
        />
        <button type="submit">{editIndex !== null ? "Salvar" : "Adicionar"}</button>
        {editIndex !== null && (
          <button type="button" className="btn-cancel" onClick={onCancel}>Cancelar</button>
        )}
      </form>

      <div className="configs-list">
        {configs.map((cfg, idx) => (
          <div className="config-card" key={idx}>
            <div className="config-key">{cfg.key}</div>
            <div className="config-value">{cfg.value}</div>
            <div className="config-actions">
              <button onClick={() => onEdit(cfg, idx)}>Editar</button>
              <button className="btn-remove" onClick={() => onRemove(idx)}>Remover</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
