// src/pages/Roles/Roles.jsx
import React, { useEffect, useState, useCallback } from "react";
import "./Roles.css";
import Button from "../../components/Roles/Button";
import Modal from "../../components/Roles/Modal";
import ChecklistItem from "../../components/Roles/ChecklistItem";

const Roles = () => {
  const [roles, setRoles] = useState([]);
  const [allPermissions, setAllPermissions] = useState([]);
  const [roleEdit, setRoleEdit] = useState(null);
  const [form, setForm] = useState({ name: "" });
  const [selectedPermissions, setSelectedPermissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [permissions, setPermissions] = useState([]);

  // Estados para modais
  const [selectedRoleForView, setSelectedRoleForView] = useState(null);
  const [selectedRoleForManage, setSelectedRoleForManage] = useState(null);
  const [selectedRoleForCreateManage, setSelectedRoleForCreateManage] = useState(null);
  const [currentPermissions, setCurrentPermissions] = useState([]);
  const [selectedToAdd, setSelectedToAdd] = useState([]);
  const [selectedToRemove, setSelectedToRemove] = useState([]);

  // Token
  const getToken = () => localStorage.getItem("token");

  const apiFetch = useCallback(async (url, options = {}) => {
    const token = getToken();
    if (!token) throw new Error("Você precisa fazer login para acessar esta página.");

    const headers = {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
      ...options.headers,
    };

    const response = await fetch(url, { ...options, headers });
    return response;
  }, []);

  // Buscar roles
  const fetchRoles = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await apiFetch("/roles/list?page=0&size=100");
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }
      const data = await response.json();
      setRoles(data.content || []);
    } catch (err) {
      setError(err.message);
      console.error("Erro ao carregar roles:", err);
    } finally {
      setLoading(false);
    }
  }, [apiFetch]);

  // Buscar todas as permissions
  const fetchAllPermissions = useCallback(async () => {
    try {
      const response = await apiFetch("/permissions/list?page=0&size=100");
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }
      const data = await response.json();
      setAllPermissions(data.content || []);
    } catch (err) {
      setError(err.message);
      console.error("Erro ao carregar permissions:", err);
    }
  }, [apiFetch]);

  useEffect(() => {
    fetchRoles();
    fetchAllPermissions();
  }, [fetchRoles, fetchAllPermissions]);

  // Buscar permissions de uma role específica
  const fetchPermissions = async (roleName) => {
    try {
      const response = await apiFetch(`/roles/${roleName}/permissions`);
      if (!response.ok) throw new Error("Erro ao carregar permissions");
      const perms = await response.json();
      return perms;
    } catch (err) {
      setError(err.message);
      return [];
    }
  };

  // Criar ou editar Role
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.name.trim() === "") {
      setError("Nome da role é obrigatório");
      return;
    }
    try {
      setError(null);
      const url = roleEdit ? "/roles/update" : "/roles/create";
      const method = roleEdit ? "PUT" : "POST";
      const body = roleEdit
        ? JSON.stringify({ oldName: roleEdit.name, newName: form.name })
        : JSON.stringify({ name: form.name });

      const response = await apiFetch(url, { method, body });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }

      const savedRole = await response.json();

      if (!roleEdit && selectedPermissions.length > 0) {
        await apiFetch(`/roles/${savedRole.name}/permissions/assign`, {
          method: "POST",
          body: JSON.stringify(selectedPermissions),
        });
      }

      if (roleEdit) {
        setRoles((prev) =>
          prev.map((r) => (r.name === roleEdit.name ? { ...r, name: savedRole.name } : r))
        );
        setRoleEdit(null);
      } else {
        setRoles((prev) => [
          ...prev,
          { ...savedRole, id: savedRole.id, permissions: selectedPermissions },
        ]);
        setSelectedPermissions([]);
      }

      setForm({ name: "" });
    } catch (err) {
      setError(err.message);
      console.error("Erro ao salvar role:", err);
    }
  };

  const handleEdit = (role) => {
    setRoleEdit(role);
    setForm({ name: role.name });
    setSelectedPermissions([]);
    setError(null);
  };

  const handleDelete = async (roleName) => {
    if (!window.confirm(`Tem certeza que deseja remover a role "${roleName}"?`)) return;

    try {
      setError(null);
      const response = await apiFetch(`/roles/delete/${roleName}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }

      setRoles((prev) => prev.filter((r) => r.name !== roleName));
      await fetchRoles();
    } catch (err) {
      setError(err.message);
      console.error("Erro ao remover role:", err);
    }
  };

  // Abrir modais
  const openManagePermissions = async (role) => {
    const currentPerms = await fetchPermissions(role.name);
    setCurrentPermissions(currentPerms);
    setSelectedToAdd([]);
    setSelectedToRemove([]);
    setSelectedRoleForManage(role);
    setForm({ name: role.name });
  };

  const openCreateManagePermissions = () => {
    setCurrentPermissions([]);
    setSelectedToAdd([]);
    setSelectedToRemove([]);
    setSelectedRoleForCreateManage(true);
    setSelectedPermissions([]);
  };

  // Adicionar/remover permissions
  const handleApplyAdd = async () => {
    if (selectedToAdd.length === 0) return;
    try {
      const response = await apiFetch(
        `/roles/${selectedRoleForManage.name}/permissions/assign`,
        {
          method: "POST",
          body: JSON.stringify(selectedToAdd),
        }
      );
      if (!response.ok) throw new Error("Erro ao adicionar permissions");
      setCurrentPermissions((prev) => [...new Set([...prev, ...selectedToAdd])]);
      setSelectedToAdd([]);
      await fetchRoles();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleApplyRemove = async () => {
    if (selectedToRemove.length === 0) return;
    try {
      const response = await apiFetch(
        `/roles/${selectedRoleForManage.name}/permissions/remove`,
        {
          method: "POST",
          body: JSON.stringify(selectedToRemove),
        }
      );
      if (!response.ok) throw new Error("Erro ao remover permissions");
      setCurrentPermissions((prev) =>
        prev.filter((p) => !selectedToRemove.includes(p))
      );
      setSelectedToRemove([]);
      await fetchRoles();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleApplyCreateSelection = () => {
    setSelectedPermissions(selectedToAdd);
    setSelectedRoleForCreateManage(null);
  };

  const handleCancel = () => {
    setRoleEdit(null);
    setForm({ name: "" });
    setSelectedPermissions([]);
    setError(null);
  };

  // Toggles
  const toggleToAddCreate = (perm) => {
    setSelectedToAdd((prev) =>
      prev.includes(perm) ? prev.filter((p) => p !== perm) : [...prev, perm]
    );
  };

  const toggleToAdd = (perm) => {
    setSelectedToAdd((prev) =>
      prev.includes(perm) ? prev.filter((p) => p !== perm) : [...prev, perm]
    );
  };

  const toggleToRemove = (perm) => {
    setSelectedToRemove((prev) =>
      prev.includes(perm) ? prev.filter((p) => p !== perm) : [...prev, perm]
    );
  };

  const closeModal = () => {
    setSelectedRoleForView(null);
    setSelectedRoleForManage(null);
    setSelectedRoleForCreateManage(null);
    setCurrentPermissions([]);
    setSelectedToAdd([]);
    setSelectedToRemove([]);
    setPermissions([]);
    setForm({ name: "" });
  };

  const openViewPermissions = async (role) => {
    setSelectedRoleForView(role);
    const perms = await fetchPermissions(role.name);
    setPermissions(perms);
  };

  if (loading) {
    return <div className="roles-container">Carregando roles...</div>;
  }

  const renderPermissionsTags = (role) => {
    const rolePermissions = role.permissions || [];
    const visible = rolePermissions.slice(0, 3);
    const hasMore = rolePermissions.length > 3;

    return (
      <div className="permissions-tags">
        {visible.map((perm, idx) => (
          <span key={idx} className={`permission-tag perm-color-${idx % 5}`}>
            {perm}
          </span>
        ))}
        {hasMore && (
          <button onClick={() => openViewPermissions(role)} className="view-more-btn">
            Ver mais ({rolePermissions.length - 3})
          </button>
        )}
      </div>
    );
  };

  return (
    <div className="roles-container">
      <h2>Roles</h2>
      {error && <div className="roles-error">{error}</div>}
      <form onSubmit={handleSubmit} className="roles-form">
        <div className="roles-form-row">
          <input
            type="text"
            placeholder="Nome da role"
            value={form.name}
            required
            onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
          />
          <Button className="btn-manage-perm" onClick={openCreateManagePermissions}>
            Gerenciar Permissions
          </Button>
          <Button type="submit" className="btn-add-role">
            {roleEdit ? "Salvar" : "Adicionar"}
          </Button>
          {roleEdit && (
            <Button onClick={handleCancel} className="roles-cancel-btn">
              Cancelar
            </Button>
          )}
        </div>
      </form>

      <table className="roles-table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Ações Role</th>
            <th>Permissions</th>
            <th>Ações Permissions</th>
          </tr>
        </thead>
        <tbody>
          {roles.map((role) => (
            <tr key={role.id || role.name}>
              <td>{role.name}</td>
              <td className="roles-table-actions-role">
                <Button className="btn-remove-role" onClick={() => handleDelete(role.name)}>
                  Remover
                </Button>
              </td>
              <td>{renderPermissionsTags(role)}</td>
              <td className="roles-table-actions-perm">
                <Button className="btn-manage-perm" onClick={() => openManagePermissions(role)}>
                  Gerenciar
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {roles.length === 0 && !loading && <p>Nenhuma role encontrada.</p>}

      {/* Modal Ver Todas Permissions */}
      <Modal
        isOpen={!!selectedRoleForView}
        onClose={closeModal}
        title={`Permissions de ${selectedRoleForView?.name}`}
        footer={<Button onClick={closeModal}>Fechar</Button>}
      >
        <div className="permissions-grid">
          {permissions.map((perm, idx) => (
            <span key={idx} className={`permission-tag perm-color-${idx % 5}`}>
              {perm}
            </span>
          ))}
          {permissions.length === 0 && (
            <p className="empty-state">Nenhuma permission atribuída.</p>
          )}
        </div>
      </Modal>

      {/* Modal Gerenciar Permissions Existente */}
      <Modal
        isOpen={!!selectedRoleForManage}
        onClose={closeModal}
        title={`Gerenciar Permissions de ${selectedRoleForManage?.name}`}
        footer={<Button onClick={closeModal}>Fechar</Button>}
        className="manage-modal"
      >
        <div className="manage-sections">
          <div className="section">
            <div className="edit-name-section">
              <h4>Editar Nome da Role</h4>
              <input
                type="text"
                value={form.name}
                onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
                placeholder="Novo nome da role"
                className="role-name-input"
              />
            </div>
            <Button
              type="button"
              className="btn-edit"
              onClick={() => handleEdit(selectedRoleForManage)}
            >
              Salvar Nome
            </Button>
          </div>

          <div className="divider"></div>

          <div className="section">
            <h4>Permissions Atuais ({currentPermissions.length})</h4>
            <div className="checklist-container">
              {currentPermissions.sort().map((perm) => (
                <ChecklistItem
                  key={perm}
                  perm={perm}
                  checked={selectedToRemove.includes(perm)}
                  onChange={toggleToRemove}
                  className="remove"
                />
              ))}
            </div>
            {selectedToRemove.length > 0 && (
              <Button onClick={handleApplyRemove} className="section-btn remove-btn">
                Remover Selecionadas ({selectedToRemove.length})
              </Button>
            )}
          </div>

          <div className="divider"></div>

          <div className="section">
            <h4>Adicionar Permissions</h4>
            <div className="checklist-container">
              {allPermissions
                .filter((p) => !currentPermissions.includes(p.name))
                .sort((a, b) => a.name.localeCompare(b.name))
                .map((perm) => (
                  <ChecklistItem
                    key={perm.name}
                    perm={perm.name}
                    checked={selectedToAdd.includes(perm.name)}
                    onChange={toggleToAdd}
                  />
                ))}
            </div>
            {selectedToAdd.length > 0 && (
              <Button onClick={handleApplyAdd} className="section-btn add-btn">
                Adicionar Selecionadas ({selectedToAdd.length})
              </Button>
            )}
          </div>
        </div>
      </Modal>

      {/* Modal Gerenciar Permissions ao Criar Nova Role */}
      <Modal
        isOpen={!!selectedRoleForCreateManage}
        onClose={closeModal}
        title="Gerenciar Permissions para Nova Role"
        footer={<Button onClick={closeModal}>Cancelar</Button>}
        className="manage-modal"
      >
        <div className="manage-sections">
          <div className="section">
            <h4>Adicionar Permissions</h4>
            <div className="checklist-container">
              {allPermissions
                .sort((a, b) => a.name.localeCompare(b.name))
                .map((perm) => (
                  <ChecklistItem
                    key={perm.name}
                    perm={perm.name}
                    checked={selectedToAdd.includes(perm.name)}
                    onChange={toggleToAddCreate}
                  />
                ))}
            </div>
            {selectedToAdd.length > 0 && (
              <Button
                onClick={handleApplyCreateSelection}
                className="section-btn add-btn"
              >
                Selecionar ({selectedToAdd.length})
              </Button>
            )}
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default Roles;