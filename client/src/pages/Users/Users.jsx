// src/pages/Users/Users.jsx
import React, { useEffect, useState, useCallback } from "react";
import "./Users.css";
import Button from "../../components/Roles/Button";
import Modal from "../../components/Roles/Modal";
import ChecklistItem from "../../components/Roles/ChecklistItem";

const Users = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [allRoles, setAllRoles] = useState([]);
  const [form, setForm] = useState({ username: "", fullName: "", email: "", password: "", confirmPassword: "", oldPassword: "" });
  const [selectedRoles, setSelectedRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [rolesView, setRolesView] = useState([]);
  const [showActiveOnly, setShowActiveOnly] = useState(true);
  const [showPassword, setShowPassword] = useState(false);
  const [showOldPassword, setShowOldPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);
  const [confirmAction, setConfirmAction] = useState(null); // {username, enabled}
  const [searchTerm, setSearchTerm] = useState("");

  // Estados para modais
  const [selectedUserForView, setSelectedUserForView] = useState(null);
  const [selectedUserForManage, setSelectedUserForManage] = useState(null);
  const [selectedForCreateManage, setSelectedForCreateManage] = useState(null);
  const [currentRoles, setCurrentRoles] = useState([]);
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

  // Buscar users
  const fetchUsers = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const params = new URLSearchParams({ page: 0, size: 100 });
      if (showActiveOnly) params.append('enabled', true);
      const response = await apiFetch(`/users?${params.toString()}`);
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }
      const data = await response.json();
      setUsers(data.content || []);
      setFilteredUsers(data.content || []);
    } catch (err) {
      setError(err.message);
      console.error("Erro ao carregar users:", err);
    } finally {
      setLoading(false);
    }
  }, [apiFetch, showActiveOnly]);

  // Buscar todas as roles
  const fetchAllRoles = useCallback(async () => {
    try {
      const response = await apiFetch("/roles/list?page=0&size=100");
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }
      const data = await response.json();
      setAllRoles(data.content || []);
    } catch (err) {
      setError(err.message);
      console.error("Erro ao carregar roles:", err);
    }
  }, [apiFetch]);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  useEffect(() => {
    fetchAllRoles();
  }, [fetchAllRoles]);

  useEffect(() => {
    const filtered = users.filter(user => user.username.toLowerCase().includes(searchTerm.toLowerCase()));
    setFilteredUsers(filtered);
  }, [searchTerm, users]);

  // Buscar roles de um user específico
  const fetchUserRoles = async (username) => {
    try {
      const response = await apiFetch(`/users/${username}/roles`);
      if (!response.ok) throw new Error("Erro ao carregar roles");
      const roles = await response.json();
      return roles;
    } catch (err) {
      setError(err.message);
      return [];
    }
  };

  // Criar User
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.username.trim() === "" || form.fullName.trim() === "" || form.email.trim() === "" || form.password.trim() === "") {
      setError("Todos os campos são obrigatórios para criar um usuário");
      return;
    }
    if (form.password !== form.confirmPassword) {
      setError("As senhas não coincidem");
      return;
    }
    try {
      setError(null);
      const body = JSON.stringify({
        username: form.username,
        fullName: form.fullName,
        email: form.email,
        password: form.password
      });
      const response = await apiFetch("/users/register", { method: "POST", body });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }

      const saved = await response.json();
      const savedUser = saved.user; // Assuming RegistrationResponseDTO

      if (selectedRoles.length > 0) {
        await apiFetch(`/users/${savedUser.username}/roles/assign`, {
          method: "POST",
          body: JSON.stringify(selectedRoles),
        });
      }

      setUsers((prev) => [
        ...prev,
        { ...savedUser, roles: selectedRoles, enabled: true },
      ]);
      setSelectedRoles([]);
      setForm({ username: "", fullName: "", email: "", password: "", confirmPassword: "", oldPassword: "" });
      setShowAddModal(false);
      await fetchUsers();
    } catch (err) {
      setError(err.message);
      console.error("Erro ao salvar user:", err);
    }
  };

  const handleToggleEnabled = (username, enabled) => {
    setConfirmAction({ username, enabled });
  };

  const confirmToggleEnabled = async () => {
    const { username, enabled } = confirmAction;
    try {
      setError(null);
      const response = await apiFetch(`/users/toggle/${username}`, {
        method: "PUT",
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }

      await fetchUsers();
      setConfirmAction(null);
    } catch (err) {
      setError(err.message);
      console.error("Erro ao alterar status do user:", err);
      setConfirmAction(null);
    }
  };

  // Atualizar user no modal
  const handleUpdateUser = async () => {
    if (form.fullName.trim() === "" && form.email.trim() === "" && (form.password.trim() === "" || form.oldPassword.trim() === "")) {
      return;
    }
    if (form.password && form.password !== form.confirmPassword) {
      setError("As novas senhas não coincidem");
      return;
    }
    try {
      setError(null);
      let response;
      if (form.password) {
        const changePassBody = JSON.stringify({
          currentPassword: form.oldPassword,
          newPassword: form.password
        });
        response = await apiFetch(`/users/change-password`, { method: "PUT", body: changePassBody });
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Erro ao alterar senha: ${response.status}: ${errorText}`);
        }
      }
      const updateBody = JSON.stringify({
        fullName: form.fullName,
        email: form.email,
      });
      response = await apiFetch(`/users/update/${selectedUserForManage.username}`, { method: "PUT", body: updateBody });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ${response.status}: ${errorText}`);
      }

      const saved = await response.json();
      setUsers((prev) =>
        prev.map((u) => (u.username === selectedUserForManage.username ? { ...u, ...saved } : u))
      );
      setSelectedUserForManage((prev) => ({ ...prev, ...saved }));
      await fetchUsers();
    } catch (err) {
      setError(err.message);
      console.error("Erro ao atualizar user:", err);
    }
  };

  // Abrir modais
  const openManageRoles = async (user) => {
    const current = await fetchUserRoles(user.username);
    setCurrentRoles(current);
    setSelectedToAdd([]);
    setSelectedToRemove([]);
    setSelectedUserForManage(user);
    setForm({ username: user.username, fullName: user.fullName, email: user.email, password: "", confirmPassword: "", oldPassword: "" });
    setShowPassword(false);
    setShowOldPassword(false);
    setShowConfirmPassword(false);
  };

  const openCreateManageRoles = () => {
    setSelectedForCreateManage(true);
    setSelectedToAdd(selectedRoles);
  };

  // Adicionar/remover roles
  const handleApplyAdd = async () => {
    if (selectedToAdd.length === 0) return;
    try {
      const response = await apiFetch(
        `/users/${selectedUserForManage.username}/roles/assign`,
        {
          method: "POST",
          body: JSON.stringify(selectedToAdd),
        }
      );
      if (!response.ok) throw new Error("Erro ao adicionar roles");
      setCurrentRoles((prev) => [...new Set([...prev, ...selectedToAdd])]);
      setSelectedToAdd([]);
      await fetchUsers();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleApplyRemove = async () => {
    if (selectedToRemove.length === 0) return;
    try {
      const response = await apiFetch(
        `/users/${selectedUserForManage.username}/roles/remove`,
        {
          method: "POST",
          body: JSON.stringify(selectedToRemove),
        }
      );
      if (!response.ok) throw new Error("Erro ao remover roles");
      setCurrentRoles((prev) =>
        prev.filter((p) => !selectedToRemove.includes(p))
      );
      setSelectedToRemove([]);
      await fetchUsers();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleApplyCreateSelection = () => {
    setSelectedRoles(selectedToAdd);
    setSelectedForCreateManage(null);
  };

  // Toggles
  const toggleToAddCreate = (role) => {
    setSelectedToAdd((prev) =>
      prev.includes(role) ? prev.filter((p) => p !== role) : [...prev, role]
    );
  };

  const toggleToAdd = (role) => {
    setSelectedToAdd((prev) =>
      prev.includes(role) ? prev.filter((p) => p !== role) : [...prev, role]
    );
  };

  const toggleToRemove = (role) => {
    setSelectedToRemove((prev) =>
      prev.includes(role) ? prev.filter((p) => p !== role) : [...prev, role]
    );
  };

  const closeModal = () => {
    setSelectedUserForView(null);
    setSelectedUserForManage(null);
    setSelectedForCreateManage(null);
    setCurrentRoles([]);
    setSelectedToAdd([]);
    setSelectedToRemove([]);
    setRolesView([]);
    setForm({ username: "", fullName: "", email: "", password: "", confirmPassword: "", oldPassword: "" });
    setShowPassword(false);
    setShowOldPassword(false);
    setShowConfirmPassword(false);
    setShowAddModal(false);
    setConfirmAction(null);
  };

  const openViewRoles = async (user) => {
    setSelectedUserForView(user);
    const roles = await fetchUserRoles(user.username);
    setRolesView(roles);
  };

  if (loading) {
    return <div className="users-container">Carregando users...</div>;
  }

  const renderRolesTags = (user) => {
    const userRoles = user.roles || [];
    const visible = userRoles.slice(0, 3);
    const hasMore = userRoles.length > 3;

    return (
      <div className="roles-tags">
        {visible.map((role, idx) => (
          <span key={idx} className={`role-tag perm-color-${idx % 5}`}>
            {role}
          </span>
        ))}
        {hasMore && (
          <button onClick={() => openViewRoles(user)} className="view-more-btn">
            Ver mais ({userRoles.length - 3})
          </button>
        )}
      </div>
    );
  };

  return (
    <div className="users-container">
      <h2>Users</h2>
      {error && <div className="users-error">{error}</div>}
      <div className="header-actions">
        <Button className="btn-toggle-active" onClick={() => setShowActiveOnly(!showActiveOnly)}>
          {showActiveOnly ? "Exibir Todos" : "Exibir Apenas Ativos"}
        </Button>
        <input
          type="text"
          placeholder="Buscar por username"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
        <Button className="btn-add-user" onClick={() => setShowAddModal(true)}>
          Adicionar User
        </Button>
      </div>

      <table className="users-table">
        <thead>
          <tr>
            <th>Username</th>
            <th>Nome Completo</th>
            <th>Email</th>
            <th>Roles</th>
            <th>Ativos</th>
            <th>Ações Roles</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.map((user) => (
            <tr key={user.id || user.username}>
              <td>{user.username}</td>
              <td>{user.fullName}</td>
              <td>{user.email}</td>
              <td>{renderRolesTags(user)}</td>
              <td className="users-table-actions-user">
                <Button 
                  className={user.enabled ? "btn-deactivate" : "btn-activate"} 
                  onClick={() => handleToggleEnabled(user.username, user.enabled)}
                >
                  {user.enabled ? "Desativar" : "Ativar"}
                </Button>
              </td>
              <td className="users-table-actions-role">
                <Button className="btn-manage-role" onClick={() => openManageRoles(user)}>
                  Gerenciar
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {filteredUsers.length === 0 && !loading && <p>Nenhum user encontrado.</p>}

      {/* Modal Adicionar User */}
      <Modal
        isOpen={showAddModal}
        onClose={closeModal}
        title="Adicionar Novo User"
        footer={
          <div className="modal-footer">
            <Button className="btn-cancel" onClick={closeModal}>Cancelar</Button>
            <Button type="submit" className="btn-manage-role" onClick={handleSubmit}>Adicionar</Button>
          </div>
        }
        className="add-modal"
      >
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username"
            value={form.username}
            onChange={(e) => setForm((f) => ({ ...f, username: e.target.value }))}
          />
          <input
            type="text"
            placeholder="Nome completo"
            value={form.fullName}
            onChange={(e) => setForm((f) => ({ ...f, fullName: e.target.value }))}
          />
          <input
            type="email"
            placeholder="Email"
            value={form.email}
            onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))}
          />
          <div className="password-container">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Senha"
              value={form.password}
              onChange={(e) => setForm((f) => ({ ...f, password: e.target.value }))}
            />
            <button type="button" onClick={() => setShowPassword(!showPassword)}>
              {showPassword ? "Esconder" : "Mostrar"}
            </button>
          </div>
          <div className="password-container">
            <input
              type={showConfirmPassword ? "text" : "password"}
              placeholder="Confirme Senha"
              value={form.confirmPassword}
              onChange={(e) => setForm((f) => ({ ...f, confirmPassword: e.target.value }))}
            />
            <button type="button" onClick={() => setShowConfirmPassword(!showConfirmPassword)}>
              {showConfirmPassword ? "Esconder" : "Mostrar"}
            </button>
          </div>
          <Button className="btn-manage-role" onClick={openCreateManageRoles}>
            Gerenciar Roles
          </Button>
        </form>
      </Modal>

      {/* Modal Ver Todas Roles */}
      <Modal
        isOpen={!!selectedUserForView}
        onClose={closeModal}
        title={`Roles de ${selectedUserForView?.username}`}
        footer={<Button onClick={closeModal}>Fechar</Button>}
      >
        <div className="roles-grid">
          {rolesView.map((role, idx) => (
            <span key={idx} className={`role-tag perm-color-${idx % 5}`}>
              {role}
            </span>
          ))}
          {rolesView.length === 0 && (
            <p className="empty-state">Nenhuma role atribuída.</p>
          )}
        </div>
      </Modal>

      {/* Modal Gerenciar Roles Existente */}
      <Modal
        isOpen={!!selectedUserForManage}
        onClose={closeModal}
        title={`Gerenciar Roles de ${selectedUserForManage?.username}`}
        footer={<Button onClick={closeModal}>Fechar</Button>}
        className="manage-modal"
      >
        <div className="manage-sections">
          <div className="section">
            <div className="edit-user-section">
              <h4>Editar Usuário</h4>
              <input
                type="text"
                placeholder="Nome completo"
                value={form.fullName}
                onChange={(e) => setForm((f) => ({ ...f, fullName: e.target.value }))}
                className="user-fullname-input"
              />
              <input
                type="email"
                placeholder="Email"
                value={form.email}
                onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))}
                className="user-email-input"
              />
              <div className="password-container">
                <input
                  type={showOldPassword ? "text" : "password"}
                  placeholder="Senha Antiga (para alterar senha)"
                  value={form.oldPassword}
                  onChange={(e) => setForm((f) => ({ ...f, oldPassword: e.target.value }))}
                />
                <button type="button" onClick={() => setShowOldPassword(!showOldPassword)}>
                  {showOldPassword ? "Esconder" : "Mostrar"}
                </button>
              </div>
              <div className="password-container">
                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="Nova Senha (opcional)"
                  value={form.password}
                  onChange={(e) => setForm((f) => ({ ...f, password: e.target.value }))}
                />
                <button type="button" onClick={() => setShowPassword(!showPassword)}>
                  {showPassword ? "Esconder" : "Mostrar"}
                </button>
              </div>
              <div className="password-container">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  placeholder="Confirme Nova Senha"
                  value={form.confirmPassword}
                  onChange={(e) => setForm((f) => ({ ...f, confirmPassword: e.target.value }))}
                />
                <button type="button" onClick={() => setShowConfirmPassword(!showConfirmPassword)}>
                  {showConfirmPassword ? "Esconder" : "Mostrar"}
                </button>
              </div>
            </div>
            <Button
              type="button"
              className="btn-edit"
              onClick={handleUpdateUser}
            >
              Salvar Alterações
            </Button>
          </div>

          <div className="divider"></div>

          <div className="section">
            <h4>Roles Atuais ({currentRoles.length})</h4>
            <div className="checklist-container">
              {currentRoles.sort().map((role) => (
                <ChecklistItem
                  key={role}
                  perm={role}
                  checked={selectedToRemove.includes(role)}
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
            <h4>Adicionar Roles</h4>
            <div className="checklist-container">
              {allRoles
                .filter((r) => !currentRoles.includes(r.name))
                .sort((a, b) => a.name.localeCompare(b.name))
                .map((role) => (
                  <ChecklistItem
                    key={role.name}
                    perm={role.name}
                    checked={selectedToAdd.includes(role.name)}
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

      {/* Modal Gerenciar Roles ao Criar Novo User */}
      <Modal
        isOpen={!!selectedForCreateManage}
        onClose={closeModal}
        title="Gerenciar Roles para Novo User"
        footer={<Button onClick={closeModal}>Cancelar</Button>}
        className="manage-modal"
      >
        <div className="manage-sections">
          <div className="section">
            <h4>Adicionar Roles</h4>
            <div className="checklist-container">
              {allRoles
                .sort((a, b) => a.name.localeCompare(b.name))
                .map((role) => (
                  <ChecklistItem
                    key={role.name}
                    perm={role.name}
                    checked={selectedToAdd.includes(role.name)}
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

      {/* Modal Confirmação Toggle Enabled */}
      <Modal
        isOpen={!!confirmAction}
        onClose={closeModal}
        title="Confirmação"
        footer={
          <>
            <Button onClick={closeModal}>Cancelar</Button>
            <Button onClick={confirmToggleEnabled} className="btn-confirm">
              Confirmar
            </Button>
          </>
        }
      >
        <p>Tem certeza que deseja {confirmAction?.enabled ? 'desativar' : 'ativar'} o usuário "{confirmAction?.username}"?</p>
      </Modal>
    </div>
  );
};

export default Users;