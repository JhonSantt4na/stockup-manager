// src/components/UserList.js (Atualizado: Botão "Mais Informações" no lugar do editar, modal de detalhes com todas as infos do User incluindo BaseEntity e roles)
import React, { useEffect, useState, useCallback, useRef } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [usernameSearch, setUsernameSearch] = useState('');
  const [selectedRole, setSelectedRole] = useState(''); // Single select role
  const [showOnlyActive, setShowOnlyActive] = useState(true);
  const [allRoles, setAllRoles] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [pageSize] = useState(10);
  const navigate = useNavigate();
  const filterTimeoutRef = useRef(null);

  // Modals
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showAddRoleModal, setShowAddRoleModal] = useState(false);
  const [showRemoveRoleModal, setShowRemoveRoleModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [addRoleForm, setAddRoleForm] = useState({ roles: [] });
  const [removeRoleForm, setRemoveRoleForm] = useState({ roles: [] });

  const formatDate = (dateString) => {
    return dateString ? new Date(dateString).toLocaleString('pt-BR') : 'N/A';
  };

  const fetchCurrentUser = useCallback(async () => {
    try {
      const response = await axios.get('http://localhost:8080/users/me');
      setCurrentUser(response.data);
    } catch (err) {
      console.error('Erro ao buscar usuário atual:', err);
    }
  }, []);

  const fetchAllRoles = useCallback(async () => {
    try {
      const response = await axios.get('http://localhost:8080/roles/list');
      const rolesArray = response.data.content || response.data || [];
      setAllRoles(Array.isArray(rolesArray) ? rolesArray.map(r => r.name) : []);
    } catch (err) {
      console.error('Erro no fetchAllRoles:', err);
    }
  }, []);

  const fetchUsers = useCallback(async () => {
    try {
      console.log('FetchUsers chamado com username:', usernameSearch, 'role:', selectedRole, 'active:', showOnlyActive, 'página:', currentPage);
      const params = {
        page: currentPage,
        size: pageSize,
        ...(showOnlyActive && { enabled: true }),
        ...(usernameSearch && { username: usernameSearch }),
        ...(selectedRole && { role: selectedRole }),
      };

      console.log('Params enviados:', params);

      const response = await axios.get('http://localhost:8080/users/listActive', { params });
      const { content, totalPages: pages } = response.data;
      
      if (content.length > 0) {
        console.log('Exemplo de user do response:', content[0]);
      }
      
      setUsers(content);
      setTotalPages(pages);
      setError('');
    } catch (err) {
      console.error('Erro no fetchUsers:', err);
      const errMsg = err.response?.data?.message || err.message || 'Erro desconhecido no backend (verifique logs)';
      setError('Erro ao listar usuários: ' + errMsg);
      if (err.response?.status === 403) {
        alert('Acesso negado. Apenas admins podem ver isso.');
        navigate('/dashboard');
      }
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, usernameSearch, selectedRole, showOnlyActive, pageSize, navigate]);

  useEffect(() => {
    fetchCurrentUser();
    fetchAllRoles();
    fetchUsers();
  }, [fetchAllRoles, fetchUsers]);

  useEffect(() => {
    if (filterTimeoutRef.current) {
      clearTimeout(filterTimeoutRef.current);
    }
    filterTimeoutRef.current = setTimeout(() => {
      setCurrentPage(0);
      fetchUsers();
    }, 500);
  }, [usernameSearch]);

  const handleSearchUsername = async () => {
    console.log('Botão Buscar clicado, usernameSearch:', usernameSearch);
    if (usernameSearch.trim()) {
      try {
        const response = await axios.get(`http://localhost:8080/users/find/${usernameSearch}`);
        setUsers([response.data]);
        setTotalPages(1);
        setCurrentPage(0);
        setError('');
      } catch (err) {
        console.error('Erro na busca username:', err);
        setError('Usuário não encontrado: ' + usernameSearch);
      }
    } else {
      setCurrentPage(0);
      fetchUsers();
    }
  };

  const handleClearSearch = () => {
    console.log('Limpar filtros clicado');
    setUsernameSearch('');
    setSelectedRole('');
    setShowOnlyActive(true);
    setCurrentPage(0);
    clearTimeout(filterTimeoutRef.current);
    fetchUsers();
  };

  const handleToggleActive = () => {
    console.log('Toggle active changed to:', !showOnlyActive);
    setShowOnlyActive(!showOnlyActive);
    setCurrentPage(0);
    fetchUsers();
  };

  const handleDetailsUser = (user) => {
    setSelectedUser(user);
    setShowDetailsModal(true);
  };

  const handleAddRole = (user) => {
    setSelectedUser(user);
    setAddRoleForm({ roles: [] });
    setShowAddRoleModal(true);
  };

  const handleSaveAddRole = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`http://localhost:8080/users/${selectedUser.username}/roles/assign`, addRoleForm.roles);
      setSuccess('Roles adicionadas com sucesso!');
      setShowAddRoleModal(false);
      fetchUsers();
    } catch (err) {
      setError('Erro ao adicionar roles: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleRemoveRole = (user) => {
    setSelectedUser(user);
    setRemoveRoleForm({ roles: [] });
    setShowRemoveRoleModal(true);
  };

  const handleSaveRemoveRole = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`http://localhost:8080/users/${selectedUser.username}/roles/remove`, removeRoleForm.roles);
      setSuccess('Roles removidas com sucesso!');
      setShowRemoveRoleModal(false);
      fetchUsers();
    } catch (err) {
      setError('Erro ao remover roles: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleRoleSelect = (formSetter, roleName) => {
    formSetter(prev => ({
      ...prev,
      roles: prev.roles.includes(roleName) ? prev.roles.filter(r => r !== roleName) : [...prev.roles, roleName]
    }));
  };

  const handleDelete = async (username) => {
    console.log('Delete clicado pra username:', username);
    if (window.confirm('Deletar usuário ' + username + '? Isso é irreversível!')) {
      console.log('Confirm ok, enviando DELETE pra /users/delete/' + username);
      try {
        const response = await axios.delete(`http://localhost:8080/users/delete/${username}`);
        console.log('Delete response:', response);
        alert('Usuário deletado com sucesso!');
        fetchUsers();
      } catch (err) {
        console.error('Erro no delete:', err);
        console.error('Status:', err.response?.status, 'Message:', err.response?.data?.message);
        alert('Erro ao deletar: ' + (err.response?.data?.message || err.message || 'Tente novamente'));
        setError('Erro ao deletar ' + username + ': ' + (err.response?.data?.message || err.message));
      }
    } else {
      console.log('Delete cancelado pelo user');
    }
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  if (error) {
    return (
      <div style={{ padding: '20px', color: 'red' }}>
        <h2>Erro</h2>
        <p>{error}</p>
        <button onClick={fetchUsers}>Tentar Novamente</button>
      </div>
    );
  }

  const isUserActive = (user) => {
    return user.enabled !== false;
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Lista de Usuários (Admin)</h2>
      
      <div style={{ marginBottom: '20px' }}>
        {/* Busca por Username */}
        <label>Buscar por Username: </label>
        <input
          type="text"
          value={usernameSearch}
          onChange={(e) => setUsernameSearch(e.target.value)}
          placeholder="Ex: admin"
          style={{ padding: '5px', marginLeft: '10px' }}
        />
        <button onClick={handleSearchUsername} style={{ marginLeft: '10px', padding: '5px' }}>
          Buscar
        </button>

        {/* Toggle Active/Inactive */}
        <label style={{ marginLeft: '20px' }}>
          <input
            type="checkbox"
            checked={showOnlyActive}
            onChange={handleToggleActive}
          />
          Mostrar só ativos
        </label>

        {/* Select Role */}
        <label style={{ marginLeft: '20px' }}>Filtrar por Role:</label>
        <select
          value={selectedRole}
          onChange={(e) => {
            setSelectedRole(e.target.value);
            setCurrentPage(0);
            fetchUsers();
          }}
          style={{ marginLeft: '10px', padding: '5px' }}
        >
          <option value="">Todos</option>
          {allRoles.map((role) => (
            <option key={role} value={role}>{role}</option>
          ))}
        </select>

        <button onClick={handleClearSearch} style={{ marginLeft: '10px', padding: '5px' }}>
          Limpar Filtros
        </button>
      </div>

      <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #ddd' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Username</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Email</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Roles</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Status</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Criado em</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Atualizado em</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Deletado em</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Ações</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => {
              const active = isUserActive(user);
              return (
                <tr key={user.username} style={{ opacity: active ? 1 : 0.5, backgroundColor: active ? 'white' : '#f8f9fa' }}>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.username}</td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.email || 'N/A'}</td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                    {user.roles ? user.roles.join(', ') : 'N/A'}
                  </td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                    {active ? 'Ativo' : 'Inativo'}
                  </td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>{formatDate(user.createdAt)}</td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>{formatDate(user.updatedAt)}</td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>{formatDate(user.deletedAt)}</td>
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                    <button onClick={() => handleDetailsUser(user)} style={{ padding: '5px', background: '#007bff', color: 'white', border: 'none', marginRight: '5px' }}>Mais Informações</button>
                    <button onClick={() => handleAddRole(user)} style={{ padding: '5px', background: '#28a745', color: 'white', border: 'none', marginRight: '5px' }}>Adicionar Role</button>
                    <button onClick={() => handleRemoveRole(user)} style={{ padding: '5px', background: '#ffc107', color: 'black', border: 'none', marginRight: '5px' }}>Remover Role</button>
                    <button onClick={() => handleDelete(user.username)} style={{ padding: '5px', background: '#dc3545', color: 'white', border: 'none' }}>Deletar</button>
                  </td>
                </tr>
              );
            })
          ) : (
            <tr>
              <td colSpan="8" style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
                Nenhum usuário encontrado.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {totalPages > 1 && (
        <div style={{ marginTop: '20px', textAlign: 'center' }}>
          <button 
            onClick={() => handlePageChange(currentPage - 1)} 
            disabled={currentPage === 0}
            style={{ padding: '10px', margin: '0 5px', background: '#6c757d', color: 'white', border: 'none' }}
          >
            Anterior
          </button>
          <span>Página {currentPage + 1} de {totalPages}</span>
          <button 
            onClick={() => handlePageChange(currentPage + 1)} 
            disabled={currentPage === totalPages - 1}
            style={{ padding: '10px', margin: '0 5px', background: '#6c757d', color: 'white', border: 'none' }}
          >
            Próxima
          </button>
        </div>
      )}

      {/* Modal Detalhes User */}
      {showDetailsModal && selectedUser && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '500px', maxHeight: '80vh', overflowY: 'auto' }}>
            <h4>Informações do Usuário: {selectedUser.username}</h4>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
              <div>
                <strong>ID:</strong> {selectedUser.id || 'N/A'}
              </div>
              <div>
                <strong>Username:</strong> {selectedUser.username || 'N/A'}
              </div>
              <div>
                <strong>Nome Completo:</strong> {selectedUser.fullName || 'N/A'}
              </div>
              <div>
                <strong>Email:</strong> {selectedUser.email || 'N/A'}
              </div>
              <div>
                <strong>Ativo:</strong> {selectedUser.enabled ? 'Sim' : 'Não'}
              </div>
              <div>
                <strong>Criado em:</strong> {formatDate(selectedUser.createdAt)}
              </div>
              <div>
                <strong>Atualizado em:</strong> {formatDate(selectedUser.updatedAt)}
              </div>
              <div>
                <strong>Deletado em:</strong> {formatDate(selectedUser.deletedAt)}
              </div>
            </div>
            <div style={{ marginTop: '10px' }}>
              <strong>Roles:</strong> {selectedUser.roles ? selectedUser.roles.join(', ') : 'N/A'}
            </div>
            <div style={{ display: 'flex', gap: '10px', marginTop: '20px' }}>
              <button onClick={() => setShowDetailsModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Fechar</button>
            </div>
          </div>
        </div>
      )}

      {/* Modal Adicionar Role */}
      {showAddRoleModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Adicionar Roles para {selectedUser.username}</h4>
            <form onSubmit={handleSaveAddRole}>
              {allRoles.filter(r => !selectedUser.roles.includes(r)).map((roleName) => (
                <label key={roleName} style={{ display: 'block', marginBottom: '5px' }}>
                  <input
                    type="checkbox"
                    checked={addRoleForm.roles.includes(roleName)}
                    onChange={() => handleRoleSelect(setAddRoleForm, roleName)}
                  />
                  {roleName}
                </label>
              ))}
              <div style={{ display: 'flex', gap: '10px' }}>
                <button type="submit" style={{ padding: '10px', background: '#28a745', color: 'white', border: 'none' }}>Adicionar</button>
                <button type="button" onClick={() => setShowAddRoleModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Remover Role */}
      {showRemoveRoleModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ background: 'white', padding: '20px', borderRadius: '8px', maxWidth: '400px' }}>
            <h4>Remover Roles de {selectedUser.username}</h4>
            <form onSubmit={handleSaveRemoveRole}>
              {selectedUser.roles.map((roleName) => (
                <label key={roleName} style={{ display: 'block', marginBottom: '5px' }}>
                  <input
                    type="checkbox"
                    checked={removeRoleForm.roles.includes(roleName)}
                    onChange={() => handleRoleSelect(setRemoveRoleForm, roleName)}
                  />
                  {roleName}
                </label>
              ))}
              <div style={{ display: 'flex', gap: '10px' }}>
                <button type="submit" style={{ padding: '10px', background: '#dc3545', color: 'white', border: 'none' }}>Remover</button>
                <button type="button" onClick={() => setShowRemoveRoleModal(false)} style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none' }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserList;