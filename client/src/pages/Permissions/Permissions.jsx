import React, { useState } from "react";
import "./Permissions.css";

const initialPermissions = [
  { id: 1, name: "READ_USERS", description: "Pode visualizar usuários", removed: false },
  { id: 2, name: "EDIT_PRODUCTS", description: "Pode editar produtos", removed: false },
  { id: 3, name: "ACCESS_PDV", description: "Pode acessar PDV", removed: false },
];

const Permissions = () => {
  const [permissions, setPermissions] = useState(initialPermissions);
  const [editPermission, setEditPermission] = useState(null);
  const [form, setForm] = useState({ name: "", description: "" });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name.trim()) return;
    if (editPermission) {
      setPermissions((prev) =>
        prev.map((perm) => perm.id === editPermission.id ? { ...perm, ...form } : perm)
      );
      setEditPermission(null);
    } else {
      setPermissions((prev) => [
        ...prev,
        { id: Date.now(), name: form.name, description: form.description, removed: false },
      ]);
    }
    setForm({ name: "", description: "" });
  };

  const handleEdit = (perm) => {
    setEditPermission(perm);
    setForm({ name: perm.name, description: perm.description });
  };

  const handleSoftDelete = (id) => {
    setPermissions((prev) => prev.map(p => (p.id === id ? { ...p, removed: !p.removed } : p)));
  };

  const handleCancel = () => {
    setEditPermission(null);
    setForm({ name: "", description: "" });
  };

  return (
    <div className="permissions-container">
      <h2>Permissões</h2>
      <form onSubmit={handleSubmit} className="permissions-form">
        <div className="permissions-form-row">
          <input
            type="text"
            placeholder="Nome da permissão"
            value={form.name}
            required
            onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
          />
          <input
            type="text"
            placeholder="Descrição"
            value={form.description}
            onChange={e => setForm(f => ({ ...f, description: e.target.value }))}
          />
          <button type="submit">
            {editPermission ? "Salvar" : "Adicionar"}
          </button>
          {editPermission && (
            <button onClick={handleCancel} type="button" className="permissions-cancel-btn">Cancelar</button>
          )}
        </div>
      </form>
      <table className="permissions-table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Removido?</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {permissions.map((perm) => (
            <tr key={perm.id} className={perm.removed ? "permissions-row-removed" : ""}>
              <td>{perm.name}</td>
              <td>{perm.description}</td>
              <td>{perm.removed ? "Sim" : "Não"}</td>
              <td className="permissions-table-actions">
                <button onClick={() => handleEdit(perm)}>Editar</button>
                <button onClick={() => handleSoftDelete(perm.id)}>
                  {perm.removed ? "Restaurar" : "Remover"}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Permissions;
