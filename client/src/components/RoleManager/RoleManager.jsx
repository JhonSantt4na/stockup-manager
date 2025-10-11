// src/components/RoleManager/RoleManager.jsx
import React, { useEffect, useState, useCallback } from 'react';
import { fetchRoles, fetchPermissions, createRole, updateRoleName, fetchRolePermissions, assignPermissionsToRole, removePermissionsFromRole } from '../../services/roleService';  // Extraídas pra service
import './RoleManager.css';  // CSS separado

const RoleManager = () => {
  const [roles, setRoles] = useState([]);
  const [permissions, setPermissions] = useState([]);
  const [rolePermissions, setRolePermissions] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showAssignModal, setShowAssignModal] = useState(false);
  const [showManagePermissionsModal, setShowManagePermissionsModal] = useState(false);
  const [selectedRole, setSelectedRole] = useState(null);
  const [createForm, setCreateForm] = useState({ name: '', enabled: true, permissions: [] });
  const [assignForm, setAssignForm] = useState({ permissionDescriptions: [] });
  const [editingRoleId, setEditingRoleId] = useState(null);
  const [editingName, setEditingName] = useState('');

  useEffect(() => {
    fetchRolesCallback();
    fetchPermissionsCallback();
  }, []);

  const fetchRolesCallback = useCallback(async () => {
    try {
      const rolesArray = await fetchRoles();  // Usa service
      setRoles(Array.isArray(rolesArray) ? rolesArray : []);
      setError('');
    } catch (err) {
      console.error('Erro no fetchRoles:', err);
      setError('Erro ao listar roles: ' + err.message);
    }
  }, []);

  const fetchPermissionsCallback = useCallback(async () => {
    try {
      const perms = await fetchPermissions({ page: 0, size: 100 });  // Usa service
      setPermissions(perms || []);
    } catch (err) {
      setError('Erro ao listar permissions: ' + err.message);
    }
  }, []);

  const fetchRolePermissionsCallback = useCallback(async (roleName) => {
    try {
      const perms = await fetchRolePermissions(roleName);  // Usa service
      setRolePermissions(perms);
    } catch (err) {
      setError('Erro ao listar permissions da role: ' + err.message);
    }
  }, []);

  const handleCreateRole = async (e) => {
    e.preventDefault();
    try {
      const createData = {
        name: createForm.name.trim(),
        enabled: createForm.enabled,
        permissions: createForm.permissions
      };
      if (!createData.name) {
        setError('Nome da role é obrigatório!');
        return;
      }
      await createRole(createData);  // Usa service
      setSuccess('Role criada com sucesso!');
      setShowCreateModal(false);
      setCreateForm({ name: '', enabled: true, permissions: [] });
      fetchRolesCallback();
    } catch (err) {
      setError('Erro ao criar role: ' + err.response?.data?.message || err.message);
    }
  };

  const handleInlineUpdateRole = async (roleId, oldName) => {
    const newName = editingName.trim();
    if (!newName) {
      setError('Novo nome é obrigatório!');
      return;
    }
    if (newName === oldName) {
      setSuccess('Nome não alterado.');
      setEditingRoleId(null);
      setEditingName('');
      return;
    }
    try {
      const updateData = {
        oldName: oldName,
        newName: newName
      };
      await updateRoleName(updateData);  // Usa service
      setSuccess('Role atualizada com sucesso!');
      setEditingRoleId(null);
      setEditingName('');
      fetchRolesCallback();
    } catch (err) {
      console.error('Erro no update role:', err);
      setError('Erro ao atualizar role: ' + err.response?.data?.message || err.message);
    }
  };

  const startInlineEdit = (role) => {
    setEditingRoleId(role.id || role.name);
    setEditingName(role.name);
  };

  const cancelInlineEdit = () => {
    setEditingRoleId(null);
    setEditingName('');
  };

  const handleAssignPermission = async (e) => {
    e.preventDefault();
    try {
      await assignPermissionsToRole(selectedRole.name, assignForm.permissionDescriptions);  // Usa service
      setSuccess('Permission atribuída com sucesso!');
      setShowAssignModal(false);
      setAssignForm({ permissionDescriptions: [] });
      fetchRolesCallback();
    } catch (err) {
      console.error('Erro no assign:', err);
      setError('Erro ao atribuir permission: ' + err.response?.data?.message || err.message);
    }
  };

  const handleRemovePermission = async (permissionDescription) => {
    if (window.confirm('Remover permission?')) {
      try {
        await removePermissionsFromRole(selectedRole.name, [permissionDescription]);  // Usa service
        setSuccess('Permission removida com sucesso!');
        fetchRolePermissionsCallback(selectedRole.name);
      } catch (err) {
        console.error('Erro no remove:', err);
        setError('Erro ao remover permission: ' + err.response?.data?.message || err.message);
      }
    }
  };

  const openAssignModal = (role) => {
    setSelectedRole(role);
    setAssignForm({ permissionDescriptions: [] });
    setShowAssignModal(true);
  };

  const openManagePermissionsModal = async (role) => {
    setSelectedRole(role);
    await fetchRolePermissionsCallback(role.name);
    setShowManagePermissionsModal(true);
  };

  const handleInputChange = (stateSetter, e) => {
    stateSetter({ ...stateSetter, [e.target.name]: e.target.value });
  };

  const handlePermissionSelect = (e) => {
    const selectedDesc = e.target.value;
    if (selectedDesc && !assignForm.permissionDescriptions.includes(selectedDesc)) {
      setAssignForm({ ...assignForm, permissionDescriptions: [...assignForm.permissionDescriptions, selectedDesc] });
    }
  };

  return (
    <div className="role-manager-container">
      <h3 className="role-manager-title">Gerenciar Roles</h3>
      {error && <p className="error-message">{error}</p>}
      {success && <p className="success-message">{success}</p>}
      <button onClick={() => setShowCreateModal(true)} className="create-btn">
        Criar Nova Role
      </button>

      <table className="roles-table">
        <thead>
          <tr>
            <th className="table-header">Nome</th>
            <th className="table-header">Permissions</th>
            <th className="table-header">Ações</th>
          </tr>
        </thead>
        <tbody>
          {roles.map((role) => (
            <tr key={role.id || role.name} className="table-row">
              <td className="table-cell">
                {editingRoleId === (role.id || role.name) ? (
                  <div className="inline-edit">
                    <input
                      type="text"
                      value={editingName}
                      onChange={(e) => setEditingName(e.target.value)}
                      className="edit-input"
                    />
                    <button onClick={() => handleInlineUpdateRole(role.id || role.name, role.name)} className="save-btn">Salvar</button>
                    <button onClick={cancelInlineEdit} className="cancel-btn">Cancelar</button>
                  </div>
                ) : (
                  <>
                    {role.name}
                    <button onClick={() => startInlineEdit(role)} className="edit-btn">Editar</button>
                  </>
                )}
              </td>
              <td className="table-cell">{role.permissions ? role.permissions.length : 0}</td>
              <td className="table-cell">
                <button onClick={() => openAssignModal(role)} className="assign-btn">Atribuir Permission</button>
                <button onClick={() => openManagePermissionsModal(role)} className="manage-btn">Gerenciar Permissions</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal Manage Permissions */}
      {showManagePermissionsModal && (
        <div className="modal-overlay">
          <div className="modal-content manage-modal">
            <h4 className="modal-title">Permissions da Role: {selectedRole.name}</h4>
            <ul className="permissions-list">
              {rolePermissions.map((perm) => (
                <li key={perm} className="permission-item">
                  <span>{perm}</span>
                  <button onClick={() => handleRemovePermission(perm)} className="remove-btn">Remover</button>
                </li>
              ))}
            </ul>
            <button onClick={() => setShowManagePermissionsModal(false)} className="close-btn">Fechar</button>
          </div>
        </div>
      )}

      {/* Modal Create */}
      {showCreateModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h4 className="modal-title">Criar Role</h4>
            <form onSubmit={handleCreateRole}>
              <input
                type="text"
                name="name"
                placeholder="Nome da Role"
                value={createForm.name}
                onChange={(e) => handleInputChange(setCreateForm, e)}
                required
                className="modal-input"
              />
              <div className="modal-buttons">
                <button type="submit" className="submit-btn">Criar</button>
                <button type="button" onClick={() => setShowCreateModal(false)} className="cancel-btn">Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Assign */}
      {showAssignModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h4 className="modal-title">Atribuir Permission a Role: {selectedRole.name}</h4>
            <form onSubmit={handleAssignPermission}>
              <select
                name="permissionId"
                onChange={handlePermissionSelect}
                className="modal-select"
              >
                <option value="">Selecione uma Permission</option>
                {permissions.map((perm) => (
                  <option key={perm.description} value={perm.description}>{perm.description}</option>
                ))}
              </select>
              <ul className="selected-permissions">
                {assignForm.permissionDescriptions.map((desc) => (
                  <li key={desc} className="selected-item">
                    <span>{desc}</span>
                    <button type="button" onClick={() => setAssignForm({ ...assignForm, permissionDescriptions: assignForm.permissionDescriptions.filter(d => d !== desc) })} className="remove-btn-small">Remover</button>
                  </li>
                ))}
              </ul>
              <div className="modal-buttons">
                <button type="submit" className="submit-btn">Atribuir</button>
                <button type="button" onClick={() => setShowAssignModal(false)} className="cancel-btn">Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default RoleManager;