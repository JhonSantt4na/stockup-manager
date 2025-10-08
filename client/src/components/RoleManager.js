// src/components/RoleManager.js (Ajustado pros DTOs: create com RoleDTO, update com RoleUpdateDTO, assign com List<String> permissions)
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const RoleManager = () => {
  const [roles, setRoles] = useState([]);
  const [permissions, setPermissions] = useState([]);
  const [rolePermissions, setRolePermissions] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showAssignModal, setShowAssignModal] = useState(false);
  const [showManagePermissionsModal, setShowManagePermissionsModal] = useState(false);
  const [selectedRole, setSelectedRole] = useState(null);
  const [createForm, setCreateForm] = useState({ name: '', enabled: true, permissions: [] }); // RoleDTO: name, enabled, permissions (List<String>)
  const [updateForm, setUpdateForm] = useState({ oldName: '', newName: '' }); // RoleUpdateDTO: oldName, newName
  const [assignForm, setAssignForm] = useState({ permissionId: '' }); // Pra select, depois converte pra List

  useEffect(() => {
    fetchRoles();
    fetchPermissions();
  }, []);

  const fetchRoles = async () => {
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
  };

  const fetchPermissions = async () => {
    try {
      const response = await axios.get('http://localhost:8080/permissions/list?page=0&size=100');
      setPermissions(response.data.content || []);
    } catch (err) {
      setError('Erro ao listar permissions: ' + err.message);
    }
  };

  const fetchRolePermissions = async (roleName) => {
    try {
      const response = await axios.get(`http://localhost:8080/roles/${roleName}/permissions`);
      setRolePermissions(response.data);
    } catch (err) {
      setError('Erro ao listar permissions da role: ' + err.message);
    }
  };

  const handleCreateRole = async (e) => {
    e.preventDefault();
    try {
      const createData = {
        name: createForm.name,
        enabled: createForm.enabled,
        permissions: createForm.permissions // Array vazio por padrão
      };
      await axios.post('http://localhost:8080/roles/create', createData);
      setSuccess('Role criada com sucesso!');
      setShowCreateModal(false);
      setCreateForm({ name: '', enabled: true, permissions: [] });
      fetchRoles();
    } catch (err) {
      setError('Erro ao criar role: ' + err.response?.data?.message || err.message);
    }
  };

  const handleUpdateRole = async (e) => {
    e.preventDefault();
    try {
      const updateData = {
        oldName: updateForm.oldName,
        newName: updateForm.newName
      };
      await axios.put('http://localhost:8080/roles/update', updateData);
      setSuccess('Role atualizada com sucesso!');
      setShowUpdateModal(false);
      setUpdateForm({ oldName: '', newName: '' });
      fetchRoles();
    } catch (err) {
      setError('Erro ao atualizar role: ' + err.response?.data?.message || err.message);
    }
  };

  const handleAssignPermission = async (e) => {
    e.preventDefault();
    try {
      // DTO espera List<String> permissions, então envia array com o id
      const assignData = {
        permissions: [assignForm.permissionId] // Array com o ID da permission
      };
      await axios.post(`http://localhost:8080/roles/${selectedRole.name}/permissions/assign`, assignData);
      setSuccess('Permission atribuída com sucesso!');
      setShowAssignModal(false);
      setAssignForm({ permissionId: '' });
      fetchRoles();
    } catch (err) {
      setError('Erro ao atribuir permission: ' + err.response?.data?.message || err.message);
    }
  };

  const handleRemovePermission = async (permissionId) => {
    if (window.confirm('Remover permission?')) {
      try {
        const removeData = {
          permissions: [permissionId] // Array com o ID pra remover
        };
        await axios.post(`http://localhost:8080/roles/${selectedRole.name}/permissions/remove`, removeData);
        setSuccess('Permission removida com sucesso!');
        fetchRolePermissions(selectedRole.name);
      } catch (err) {
        setError('Erro ao remover permission: ' + err.response?.data?.message || err.message);
      }
    }
  };

  const openUpdateModal = (role) => {
    setSelectedRole(role);
    setUpdateForm({ oldName: role.name, newName: role.name });
    setShowUpdateModal(true);
  };

  const openAssignModal = (role) => {
    setSelectedRole(role);
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
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Descrição</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Permissions</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Ações</th>
          </tr>
        </thead>
        <tbody>
          {roles.map((role) => (
            <tr key={role.name}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{role.name}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{role.description || 'N/A'}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{role.permissions ? role.permissions.length : 0}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                <button onClick={() => openUpdateModal(role)} style={{ padding: '5px', background: '#007bff', color: 'white', border: 'none', marginRight: '5px' }}>Editar</button>
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
                <li key={perm.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '5px', borderBottom: '1px solid #ddd' }}>
                  <span>{perm.description}</span>
                  <button onClick={() => handleRemovePermission(perm.id)} style={{ padding: '5px', background: '#dc3545', color: 'white', border: 'none' }}>Remover</button>
                </li>
              ))}
            </ul>
            <button onClick={() => setShowManagePermissionsModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none', marginTop: '10px' }}>Fechar</button>
          </div>
        </div>
      )}

      {/* Modal Create - Sem description (DTO não tem) */}
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

      {/* Modal Update - Sem description */}
      {showUpdateModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Atualizar Role: {selectedRole.name}</h4>
            <form onSubmit={handleUpdateRole}>
              <input
                type="text"
                name="oldName"
                placeholder="Nome Atual (não mude)"
                value={updateForm.oldName}
                onChange={(e) => handleInputChange(setUpdateForm, e)}
                required
                style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
              />
              <input
                type="text"
                name="newName"
                placeholder="Novo Nome"
                value={updateForm.newName}
                onChange={(e) => handleInputChange(setUpdateForm, e)}
                required
                style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
              />
              <div style={{ display: 'flex', gap: '10px' }}>
                <button type="submit" style={{ padding: '10px', background: '#007bff', color: 'white', border: 'none' }}>Atualizar</button>
                <button type="button" onClick={() => setShowUpdateModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Assign - Envia como List<String> */}
      {showAssignModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Atribuir Permission a Role: {selectedRole.name}</h4>
            <form onSubmit={handleAssignPermission}>
              <select
                name="permissionId"
                value={assignForm.permissionId}
                onChange={(e) => handleInputChange(setAssignForm, e)}
                required
                style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
              >
                <option value="">Selecione uma Permission</option>
                {permissions.map((perm) => (
                  <option key={perm.id} value={perm.id}>{perm.description}</option>
                ))}
              </select>
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