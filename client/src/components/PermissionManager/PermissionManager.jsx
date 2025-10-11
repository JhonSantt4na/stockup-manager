// src/components/PermissionManager/PermissionManager.jsx
import React, { useEffect, useState, useCallback } from 'react';
import { fetchPermissions, createPermission, updatePermission, deletePermission } from '../../services/permissionService';  // Extraídas pra service
import './PermissionManager.css';  // CSS separado

const PermissionManager = () => {
  const [permissions, setPermissions] = useState([]);
  const [createForm, setCreateForm] = useState({ description: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [updateForm, setUpdateForm] = useState({ oldDescription: '', newDescription: '', enabled: false });
  const [currentOldDescription, setCurrentOldDescription] = useState('');
  const [showOnlyActive, setShowOnlyActive] = useState(true);

  const fetchPermissionsCallback = useCallback(async () => {
    try {
      const endpoint = showOnlyActive ? '/listActive' : '/list';
      const params = {
        page: 0,
        size: 100,
        sort: 'description,asc'
      };
      const response = await fetchPermissions(endpoint, params);  // Usa service
      setPermissions(response.content || []);
      setError('');
    } catch (err) {
      console.error('Erro no fetchPermissions:', err);
      setError('Erro ao listar permissions: ' + (err.response?.data?.message || err.message));
    }
  }, [showOnlyActive]);

  useEffect(() => {
    fetchPermissionsCallback();
  }, [fetchPermissionsCallback]);

  const handleCreatePermission = async (e) => {
    e.preventDefault();
    try {
      await createPermission(createForm);  // Usa service
      setSuccess('Permission criada com sucesso!');
      setShowCreateModal(false);
      setCreateForm({ description: '' });
      fetchPermissionsCallback();
    } catch (err) {
      setError('Erro ao criar permission: ' + err.response?.data?.message || err.message);
    }
  };

  const handleUpdatePermission = async (e) => {
    e.preventDefault();
    const newDesc = updateForm.newDescription ? updateForm.newDescription.trim() : currentOldDescription;
    if (!newDesc) {
      setError('Descrição é obrigatória!');
      return;
    }
    try {
      const updateData = {
        oldDescription: currentOldDescription,
        newDescription: newDesc,
        enabled: updateForm.enabled // Sempre enviado
      };
      await updatePermission(updateData);  // Usa service
      setSuccess('Permission atualizada com sucesso!');
      setShowUpdateModal(false);
      setUpdateForm({ oldDescription: '', newDescription: '', enabled: false });
      setCurrentOldDescription('');
      setTimeout(() => fetchPermissionsCallback(), 500); // Delay pra re-fetch
    } catch (err) {
      console.error('Erro no update:', err);
      setError('Erro ao atualizar permission: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleDeletePermission = async (description) => {
    if (window.confirm('Deletar permission "' + description + '"? Isso é irreversível!')) {
      try {
        await deletePermission(description);  // Usa service
        alert('Permission deletada com sucesso!');
        fetchPermissionsCallback();
      } catch (err) {
        console.error('Erro no delete:', err);
        alert('Erro ao deletar: ' + (err.response?.data?.message || err.message || 'Tente novamente'));
        setError('Erro ao deletar ' + description + ': ' + (err.response?.data?.message || err.message));
      }
    }
  };

  const openUpdateModal = (perm) => {
    const oldDesc = perm.description;
    setCurrentOldDescription(oldDesc);
    setUpdateForm({ 
      oldDescription: oldDesc, 
      newDescription: oldDesc, 
      enabled: perm.enabled
    });
    setShowUpdateModal(true);
  };

  const handleInputChange = (stateSetter, e) => {
    stateSetter({ ...stateSetter, [e.target.name]: e.target.value });
  };

  const handleCheckboxChange = (stateSetter, e) => {
    stateSetter({ ...stateSetter, [e.target.name]: e.target.checked });
  };

  const toggleActiveFilter = () => {
    setShowOnlyActive(!showOnlyActive);
  };

  return (
    <div className="permission-manager-container">
      <h3 className="permission-manager-title">Gerenciar Permissions</h3>
      {error && <p className="error-message">{error}</p>}
      {success && <p className="success-message">{success}</p>}
      <button onClick={() => setShowCreateModal(true)} className="create-btn">
        Criar Nova Permission
      </button>

      <div className="filter-section">
        <label className="filter-label">
          <input
            type="checkbox"
            checked={showOnlyActive}
            onChange={toggleActiveFilter}
          />
          Mostrar só ativas
        </label>
      </div>

      <table className="permissions-table">
        <thead>
          <tr>
            <th className="table-header">Descrição</th>
            <th className="table-header">Status</th>
            <th className="table-header">Ações</th>
          </tr>
        </thead>
        <tbody>
          {permissions.map((perm) => (
            <tr key={perm.description} className="table-row">
              <td className="table-cell">{perm.description}</td>
              <td className="table-cell">{perm.enabled ? 'Ativo' : 'Inativo'}</td>
              <td className="table-cell">
                <button onClick={() => openUpdateModal(perm)} className="edit-btn">Editar</button>
                <button onClick={() => handleDeletePermission(perm.description)} className="delete-btn">Deletar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal Create */}
      {showCreateModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h4 className="modal-title">Criar Permission</h4>
            <form onSubmit={handleCreatePermission}>
              <input
                type="text"
                name="description"
                placeholder="Descrição"
                value={createForm.description}
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

      {/* Modal Update - Checkbox Enabled */}
      {showUpdateModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h4 className="modal-title">Atualizar Permission</h4>
            <form onSubmit={handleUpdatePermission}>
              <label className="modal-label">Descrição Atual: {currentOldDescription}</label>
              <input
                type="text"
                name="newDescription"
                placeholder="Nova Descrição (deixe igual se não quiser mudar)"
                value={updateForm.newDescription}
                onChange={(e) => handleInputChange(setUpdateForm, e)}
                className="modal-input"
              />
              <label className="modal-checkbox-label">
                <input
                  type="checkbox"
                  name="enabled"
                  checked={updateForm.enabled}
                  onChange={(e) => handleCheckboxChange(setUpdateForm, e)}
                />
                Ativar Permission (enabled)
              </label>
              <div className="modal-buttons">
                <button type="submit" className="submit-btn">Atualizar</button>
                <button type="button" onClick={() => setShowUpdateModal(false)} className="cancel-btn">Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PermissionManager;