import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Permissions.css";

const Permissions = () => {
  const [permissions, setPermissions] = useState([]);
  const [editPermission, setEditPermission] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);
  const [form, setForm] = useState({ name: "", description: "" });
  const [editForm, setEditForm] = useState({ name: "", description: "" });

  // Busca as permissões do backend
  const fetchPermissions = async () => {
    try {
      const response = await axios.get("/permissions/list", {
        params: { page: 0, size: 100, sort: ["name", "asc"] },
      });
      setPermissions(response.data.content);
    } catch (error) {
      console.error("Erro ao buscar permissões:", error);
    }
  };

  useEffect(() => {
    fetchPermissions();
  }, []);

  // Criação de nova permissão
  const handleAddSubmit = async (e) => {
    e.preventDefault();
    if (!form.name.trim() || !form.description.trim()) return;

    try {
      await axios.post("/permissions/create", {
        name: form.name,
        description: form.description,
      });
      setForm({ name: "", description: "" });
      fetchPermissions();
    } catch (error) {
      console.error("Erro ao adicionar permissão:", error);
      alert("Erro ao criar permissão. Verifique se já existe uma com o mesmo nome.");
    }
  };

  // Atualização de permissão existente
  const handleEditSubmit = async (e) => {
    e.preventDefault();
    if (!editForm.name.trim() || !editForm.description.trim()) return;

    try {
      await axios.put("/permissions/update", {
        oldName: editPermission.name,
        newName: editForm.name,
        newDescription: editForm.description,
      });

      setShowEditModal(false);
      setEditPermission(null);
      fetchPermissions();
    } catch (error) {
      console.error("Erro ao atualizar permissão:", error);
      alert("Erro ao atualizar permissão. Tente novamente.");
    }
  };

  // Abre modal e preenche com dados da permissão
  const handleEdit = (perm) => {
    setEditPermission(perm);
    setEditForm({
      name: perm.name,
      description: perm.description || "",
    });
    setShowEditModal(true);
  };

  // Deleção de permissão
  const handleDelete = async (name) => {
    if (!window.confirm(`Tem certeza que deseja excluir a permissão "${name}"?`)) return;
    try {
      await axios.delete(`/permissions/delete/${name}`);
      fetchPermissions();
    } catch (error) {
      console.error("Erro ao deletar permissão:", error);
      alert("Erro ao excluir permissão. Verifique se ela está vinculada a alguma role.");
    }
  };

  const handleCancel = () => {
    setShowEditModal(false);
    setEditPermission(null);
    setEditForm({ name: "", description: "" });
  };

  return (
    <div className="permissions-container">
      <h2>Gerenciamento de Permissões</h2>

      {/* Formulário de criação */}
      <form onSubmit={handleAddSubmit} className="permissions-form">
        <div className="permissions-form-row">
          <input
            type="text"
            placeholder="Nome da permissão"
            value={form.name}
            required
            onChange={(e) => setForm({ ...form, name: e.target.value })}
          />
          <input
            type="text"
            placeholder="Descrição da permissão"
            value={form.description}
            required
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
          <button type="submit" className="btn-add-permission">
            Adicionar
          </button>
        </div>
      </form>

      {/* Tabela de permissões */}
      <table className="permissions-table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {permissions.length > 0 ? (
            permissions.map((perm) => (
              <tr key={perm.id || perm.name}>
                <td>{perm.name}</td>
                <td>{perm.description || "—"}</td>
                <td className="permissions-table-actions">
                  <button
                    className="btn-edit-permission"
                    onClick={() => handleEdit(perm)}
                  >
                    Editar
                  </button>
                  <button
                    className="btn-remove-permission"
                    onClick={() => handleDelete(perm.name)}
                  >
                    Remover
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3" style={{ textAlign: "center", padding: "1rem" }}>
                Nenhuma permissão encontrada.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {/* Modal de Edição */}
      {showEditModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Editar Permissão</h3>
              <button className="modal-close" onClick={handleCancel}>×</button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleEditSubmit}>
                <div className="edit-name-section">
                  <label>Nome</label>
                  <input
                    type="text"
                    className="role-name-input"
                    value={editForm.name}
                    onChange={(e) =>
                      setEditForm({ ...editForm, name: e.target.value })
                    }
                    placeholder="Nome da permissão"
                    required
                  />
                </div>
                <div className="edit-name-section">
                  <label>Descrição</label>
                  <input
                    type="text"
                    className="role-name-input"
                    value={editForm.description}
                    onChange={(e) =>
                      setEditForm({ ...editForm, description: e.target.value })
                    }
                    placeholder="Descrição da permissão"
                    required
                  />
                </div>
              </form>
            </div>
            <div className="modal-footer">
              <button onClick={handleCancel} className="btn-cancel">Cancelar</button>
              <button onClick={handleEditSubmit} className="btn-save">Salvar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Permissions;
