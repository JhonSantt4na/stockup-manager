// src/components/PermissionManager.js (Completo: Envia enabled sempre, fallback newDescription = old se não mudado)
import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';

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

  const fetchPermissions = useCallback(async () => {
    try {
      const endpoint = showOnlyActive ? '/listActive' : '/list';
      const params = {
        page: 0,
        size: 100,
        sort: 'description,asc'
      };
      console.log('Chamando endpoint:', `/permissions${endpoint}`);
      const response = await axios.get(`http://localhost:8080/permissions${endpoint}`, { params });
      console.log('Permissions recebidas:', response.data.content);
      setPermissions(response.data.content || []);
      setError('');
    } catch (err) {
      console.error('Erro no fetchPermissions:', err);
      setError('Erro ao listar permissions: ' + (err.response?.data?.message || err.message));
    }
  }, [showOnlyActive]);

  useEffect(() => {
    fetchPermissions();
  }, [fetchPermissions]);

  const handleCreatePermission = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/permissions/create', createForm);
      setSuccess('Permission criada com sucesso!');
      setShowCreateModal(false);
      setCreateForm({ description: '' });
      fetchPermissions();
    } catch (err) {
      setError('Erro ao criar permission: ' + err.response?.data?.message || err.message);
    }
  };

  const handleUpdatePermission = async (e) => {
    e.preventDefault();
    console.log('handleUpdatePermission chamado!');
    console.log('Form values:', updateForm, 'Old current:', currentOldDescription);
    // Fallback: se newDescription vazio, usa old (salva só enabled)
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
      console.log('Enviando updateData:', updateData);
      const response = await axios.put('http://localhost:8080/permissions/update', updateData);
      console.log('Update response:', response);
      setSuccess('Permission atualizada com sucesso!');
      setShowUpdateModal(false);
      setUpdateForm({ oldDescription: '', newDescription: '', enabled: false });
      setCurrentOldDescription('');
      setTimeout(() => fetchPermissions(), 500); // Delay pra re-fetch
    } catch (err) {
      console.error('Erro no update:', err);
      console.error('Status:', err.response?.status, 'Message:', err.response?.data?.message);
      setError('Erro ao atualizar permission: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleDeletePermission = async (description) => {
    console.log('Delete clicado pra description:', description);
    if (window.confirm('Deletar permission "' + description + '"? Isso é irreversível!')) {
      console.log('Confirm ok, enviando DELETE pra /permissions/delete/' + description);
      try {
        const encodedDescription = encodeURIComponent(description);
        const response = await axios.delete(`http://localhost:8080/permissions/delete/${encodedDescription}`);
        console.log('Delete response:', response);
        alert('Permission deletada com sucesso!');
        fetchPermissions();
      } catch (err) {
        console.error('Erro no delete:', err);
        console.error('Status:', err.response?.status, 'Message:', err.response?.data?.message);
        alert('Erro ao deletar: ' + (err.response?.data?.message || err.message || 'Tente novamente'));
        setError('Erro ao deletar ' + description + ': ' + (err.response?.data?.message || err.message));
      }
    } else {
      console.log('Delete cancelado pelo user');
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
    console.log('Abrindo modal pra perm:', perm, 'Enabled:', perm.enabled);
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
    <div style={{ padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
      <h3>Gerenciar Permissions</h3>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}
      <button onClick={() => setShowCreateModal(true)} style={{ padding: '10px', background: '#28a745', color: 'white', border: 'none', marginBottom: '10px' }}>
        Criar Nova Permission
      </button>

      <div style={{ marginBottom: '10px' }}>
        <label>
          <input
            type="checkbox"
            checked={showOnlyActive}
            onChange={toggleActiveFilter}
          />
          Mostrar só ativas
        </label>
      </div>

      <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #ddd' }}>
        <thead>
          <tr><th style={{ border: '1px solid #ddd', padding: '8px' }}>Descrição</th><th style={{ border: '1px solid #ddd', padding: '8px' }}>Status</th><th style={{ border: '1px solid #ddd', padding: '8px' }}>Ações</th></tr>
        </thead>
        <tbody>
          {permissions.map((perm) => (
            <tr key={perm.description}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{perm.description}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{perm.enabled ? 'Ativo' : 'Inativo'}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                <button onClick={() => openUpdateModal(perm)} style={{ padding: '5px', background: '#007bff', color: 'white', border: 'none', marginRight: '5px' }}>Editar</button>
                <button onClick={() => handleDeletePermission(perm.description)} style={{ padding: '5px', background: '#dc3545', color: 'white', border: 'none', cursor: 'pointer' }}>Deletar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal Create */}
      {showCreateModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Criar Permission</h4>
            <form onSubmit={handleCreatePermission}>
              <input
                type="text"
                name="description"
                placeholder="Descrição"
                value={createForm.description}
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

      {/* Modal Update - Checkbox Enabled */}
      {showUpdateModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Atualizar Permission</h4>
            <form onSubmit={handleUpdatePermission}>
              <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Descrição Atual: {currentOldDescription}</label>
              <input
                type="text"
                name="newDescription"
                placeholder="Nova Descrição (deixe igual se não quiser mudar)"
                value={updateForm.newDescription}
                onChange={(e) => handleInputChange(setUpdateForm, e)}
                style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
              />
              <label style={{ display: 'block', marginBottom: '10px' }}>
                <input
                  type="checkbox"
                  name="enabled"
                  checked={updateForm.enabled}
                  onChange={(e) => handleCheckboxChange(setUpdateForm, e)}
                />
                Ativar Permission (enabled)
              </label>
              <div style={{ display: 'flex', gap: '10px' }}>
                <button type="submit" style={{ padding: '10px', background: '#007bff', color: 'white', border: 'none' }}>Atualizar</button>
                <button type="button" onClick={() => setShowUpdateModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PermissionManager;