import React, { useEffect, useState } from "react";
import "./Roles.css";

const initialRoles = [
  { id: 1, name: "ADMIN", description: "Administrador completo", removed: false },
  { id: 2, name: "PRO", description: "Usuário Pro com acesso avançado", removed: false },
  { id: 3, name: "FREE", description: "Usuário Grátis", removed: false },
];

const Roles = () => {
  const [roles, setRoles] = useState(initialRoles);
  const [roleEdit, setRoleEdit] = useState(null);
  const [form, setForm] = useState({ name: "", description: "" });

  // Submit handler para Add/Edit
  const handleSubmit = (e) => {
    e.preventDefault();
    if (form.name.trim() === "") return;
    if (roleEdit) {
      setRoles((prev) =>
        prev.map((role) =>
          role.id === roleEdit.id ? { ...role, name: form.name, description: form.description } : role
        )
      );
      setRoleEdit(null);
    } else {
      setRoles((prev) => [
        ...prev,
        { id: Date.now(), name: form.name, description: form.description, removed: false },
      ]);
    }
    setForm({ name: "", description: "" });
  };

  const handleEdit = (role) => {
    setRoleEdit(role);
    setForm({ name: role.name, description: role.description });
  };

  const handleSoftDelete = (id) => {
    setRoles((prev) => prev.map(role => (role.id === id ? { ...role, removed: !role.removed } : role)));
  };

  // Ao cancelar edição
  const handleCancel = () => {
    setRoleEdit(null);
    setForm({ name: "", description: "" });
  };

  return (
    <div className="roles-container">
      <h2>Roles</h2>
      <form onSubmit={handleSubmit} className="roles-form">
        <div className="roles-form-row">
          <input
            type="text"
            placeholder="Nome da role"
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
            {roleEdit ? "Salvar" : "Adicionar"}
          </button>
          {roleEdit && (
            <button onClick={handleCancel} type="button" className="roles-cancel-btn">Cancelar</button>
          )}
        </div>
      </form>
      <table className="roles-table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Removido?</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {roles.map((role) => (
            <tr key={role.id} className={role.removed ? "roles-row-removed" : ""}>
              <td>{role.name}</td>
              <td>{role.description}</td>
              <td>{role.removed ? "Sim" : "Não"}</td>
              <td className="roles-table-actions">
                <button onClick={() => handleEdit(role)}>Editar</button>
                <button onClick={() => handleSoftDelete(role.id)}>
                  {role.removed ? "Restaurar" : "Remover"}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Roles;
