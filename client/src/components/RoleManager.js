// src/components/RoleManager.js (Corrigido: Body direto array pra assign/remove, Inline Edit Name)
import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';

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
    fetchRoles();
    fetchPermissions();
  }, []);

  const fetchRoles = useCallback(async () => {
    try {
      const response = await axios.get('http://localhost:8080/roles/list');
      const rolesArray = response.data.content || response.data || [];
      console.log('Roles response:', rolesArray);
      setRoles(Array.isArray(rolesArray) ? rolesArray : []);
      setError('');
    } catch (err) {
      console.error('Erro no fetchRoles:', err);
      setError('Erro ao listar roles: ' + err.message);
    }
  }, []);

  const fetchPermissions = useCallback(async () => {
    try {
      const response = await axios.get('http://localhost:8080/permissions/list?page=0&size=100');
      setPermissions(response.data.content || []);
    } catch (err) {
      setError('Erro ao listar permissions: ' + err.message);
    }
  }, []);

  const fetchRolePermissions = useCallback(async (roleName) => {
    try {
      const response = await axios.get(`http://localhost:8080/roles/${roleName}/permissions`);
      setRolePermissions(response.data);
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
      await axios.post('http://localhost:8080/roles/create', createData);
      setSuccess('Role criada com sucesso!');
      setShowCreateModal(false);
      setCreateForm({ name: '', enabled: true, permissions: [] });
      fetchRoles();
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
      console.log('Enviando updateData pra role:', updateData);
      await axios.put('http://localhost:8080/roles/update', updateData);
      setSuccess('Role atualizada com sucesso!');
      setEditingRoleId(null);
      setEditingName('');
      fetchRoles();
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
      // Backend espera List<String> permissionDescriptions direto no body (array JSON)
      console.log('Enviando assign permissionDescriptions:', assignForm.permissionDescriptions);
      await axios.post(`http://localhost:8080/roles/${selectedRole.name}/permissions/assign`, assignForm.permissionDescriptions);
      setSuccess('Permission atribuída com sucesso!');
      setShowAssignModal(false);
      setAssignForm({ permissionDescriptions: [] });
      fetchRoles();
    } catch (err) {
      console.error('Erro no assign:', err);
      setError('Erro ao atribuir permission: ' + err.response?.data?.message || err.message);
    }
  };

  const handleRemovePermission = async (permissionDescription) => {
    if (window.confirm('Remover permission?')) {
      try {
        // Backend espera List<String> permissionDescriptions direto no body
        console.log('Enviando remove permissionDescriptions:', [permissionDescription]);
        await axios.post(`http://localhost:8080/roles/${selectedRole.name}/permissions/remove`, [permissionDescription]);
        setSuccess('Permission removida com sucesso!');
        fetchRolePermissions(selectedRole.name);
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
    await fetchRolePermissions(role.name);
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
    <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
      <h3>Gerenciar Roles</h3>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
      <button onClick={() => setShowCreateModal(true)} style={{ padding: '10px', background: '#28a745', color: 'white', border: 'none', marginBottom: '10px' }}>
        Criar Nova Role
      </button>

      <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #ddd' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Nome</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Permissions</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Ações</th>
          </tr>
        </thead>
        <tbody>
          {roles.map((role) => (
            <tr key={role.id || role.name}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                {editingRoleId === (role.id || role.name) ? (
                  <div style={{ display: 'flex', gap: '5px' }}>
                    <input
                      type="text"
                      value={editingName}
                      onChange={(e) => setEditingName(e.target.value)}
                      style={{ padding: '5px', border: '1px solid #ddd', borderRadius: '4px' }}
                    />
                    <button onClick={() => handleInlineUpdateRole(role.id || role.name, role.name)} style={{ padding: '5px', background: '#28a745', color: 'white', border: 'none', borderRadius: '4px' }}>Salvar</button>
                    <button onClick={cancelInlineEdit} style={{ padding: '5px', background: '#6c757d', color: 'white', border: 'none', borderRadius: '4px' }}>Cancelar</button>
                  </div>
                ) : (
                  <>
                    {role.name}
                    <button onClick={() => startInlineEdit(role)} style={{ padding: '5px', background: '#007bff', color: 'white', border: 'none', marginLeft: '5px' }}>Editar</button>
                  </>
                )}
              </td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{role.permissions ? role.permissions.length : 0}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                <button onClick={() => openAssignModal(role)} style={{ padding: '5px', background: '#ffc107', color: 'black', border: 'none', marginRight: '5px' }}>Atribuir Permission</button>
                <button onClick={() => openManagePermissionsModal(role)} style={{ padding: '5px', background: '#dc3545', color: 'white', border: 'none' }}>Gerenciar Permissions</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal Manage Permissions */}
      {showManagePermissionsModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '500px', maxHeight: '400px', overflow: 'auto' }}>
            <h4>Permissions da Role: {selectedRole.name}</h4>
            <ul style={{ listStyle: 'none', padding: 0 }}>
              {rolePermissions.map((perm) => (
                <li key={perm} style={{ display: 'flex', justifyContent: 'space-between', padding: '5px', borderBottom: '1px solid #ddd' }}>
                  <span>{perm}</span>
                  <button onClick={() => handleRemovePermission(perm)} style={{ padding: '5px', background: '#dc3545', color: 'white', border: 'none' }}>Remover</button>
                </li>
              ))}
            </ul>
            <button onClick={() => setShowManagePermissionsModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none', marginTop: '10px' }}>Fechar</button>
          </div>
        </div>
      )}

      {/* Modal Create */}
      {showCreateModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Criar Role</h4>
            <form onSubmit={handleCreateRole}>
              <input
                type="text"
                name="name"
                placeholder="Nome da Role"
                value={createForm.name}
                onChange={(e) => handleInputChange(setCreateForm, e)}
                required
                style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
              />
              <div style={{ display: 'flex', gap: '10px' }}>
                <button type="submit" style={{ padding: '10px', background: '#28a745', color: 'white', border: 'none' }}>Criar</button>
                <button type="button" onClick={() => setShowCreateModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Assign */}
      {showAssignModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Atribuir Permission a Role: {selectedRole.name}</h4>
            <form onSubmit={handleAssignPermission}>
              <select
                name="permissionId"
                onChange={handlePermissionSelect}
                style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
              >
                <option value="">Selecione uma Permission</option>
                {permissions.map((perm) => (
                  <option key={perm.description} value={perm.description}>{perm.description}</option>
                ))}
              </select>
              <ul style={{ listStyle: 'none', padding: 0, marginBottom: '10px' }}>
                {assignForm.permissionDescriptions.map((desc) => (
                  <li key={desc} style={{ display: 'flex', justifyContent: 'space-between', padding: '5px' }}>
                    <span>{desc}</span>
                    <button type="button" onClick={() => setAssignForm({ ...assignForm, permissionDescriptions: assignForm.permissionDescriptions.filter(d => d !== desc) })} style={{ padding: '2px 5px', background: '#dc3545', color: 'white', border: 'none' }}>Remover</button>
                  </li>
                ))}
              </ul>
              <div style={{ display: 'flex', gap: '10px' }}>
                <button type="submit" style={{ padding: '10px', background: '#28a745', color: 'white', border: 'none' }}>Atribuir</button>
                <button type="button" onClick={() => setShowAssignModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default RoleManager;