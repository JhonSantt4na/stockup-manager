// src/components/UserList/UserList.jsx (Completo: Filtros verticais em grupos horizontais, sem modals, fetch só no btn, ESLint ajustado)
import React, { useEffect, useState, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { listUsers, findUser, deleteUser, assignRolesToUser, removeRolesFromUser, fetchAllRoles, getCurrentUser } from '../../services/userService';
import './UserList.css';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [usernameSearch, setUsernameSearch] = useState('');
  const [selectedRole, setSelectedRole] = useState('');
  const [showOnlyActive, setShowOnlyActive] = useState(true);
  const [allRoles, setAllRoles] = useState([]);
  const [pageSize] = useState(10);
  const navigate = useNavigate();
  const filterTimeoutRef = useRef(null);

  const formatDate = (dateString) => {
    return dateString ? new Date(dateString).toLocaleString('pt-BR') : 'N/A';
  };

  const fetchCurrentUser = useCallback(async () => {
    try {
      const response = await getCurrentUser();
      // currentUser não usado, mas mantido pra futuro
    } catch (err) {
      console.error('Erro ao buscar usuário atual:', err);
    }
  }, [getCurrentUser]); // eslint-disable-next-line react-hooks/exhaustive-deps

  const fetchAllRolesCallback = useCallback(async () => {
    try {
      const rolesArray = await fetchAllRoles();
      setAllRoles(Array.isArray(rolesArray) ? rolesArray.map(r => r.name) : []);
    } catch (err) {
      console.error('Erro no fetchAllRoles:', err);
    }
  }, [fetchAllRoles]); // eslint-disable-next-line react-hooks/exhaustive-deps

  const fetchUsersCallback = useCallback(async () => {
    try {
      const params = {
        page: currentPage,
        size: pageSize,
        ...(showOnlyActive && { enabled: true }),
        ...(usernameSearch && { username: usernameSearch }),
        ...(selectedRole && { role: selectedRole }),
      };

      const response = await listUsers(params);
      const { content, totalPages: pages } = response;
      
      setUsers(content);
      setTotalPages(pages);
      setError('');
    } catch (err) {
      const errMsg = err.response?.data?.message || err.message || 'Erro desconhecido no backend (verifique logs)';
      setError('Erro ao listar usuários: ' + errMsg);
      if (err.response?.status === 403) {
        alert('Acesso negado. Apenas admins podem ver isso.');
        navigate('/dashboard');
      }
    }
  }, [currentPage, usernameSearch, selectedRole, showOnlyActive, pageSize, navigate, listUsers]); // eslint-disable-next-line react-hooks/exhaustive-deps

  useEffect(() => {
    fetchCurrentUser();
    fetchAllRolesCallback();
    fetchUsersCallback();
  }, [fetchCurrentUser, fetchAllRolesCallback, fetchUsersCallback]); // eslint-disable-next-line react-hooks/exhaustive-deps

  useEffect(() => {
  if (usernameSearch.trim() === '') return;
  if (filterTimeoutRef.current) {
    clearTimeout(filterTimeoutRef.current);
  }
  filterTimeoutRef.current = setTimeout(() => {
    setCurrentPage(0);
    fetchUsersCallback();
    }, 500);
  }, [usernameSearch]);// eslint-disable-next-line react-hooks/exhaustive-deps

  const handleSearchUsername = async () => {
    setCurrentPage(0);
    if (usernameSearch.trim()) {
      try {
        const response = await findUser(usernameSearch);
        setUsers([response]);
        setTotalPages(1);
        setError('');
      } catch (err) {
        setError('Usuário não encontrado: ' + usernameSearch);
      }
    } else {
      fetchUsersCallback();
    }
  };

  const handleApplyFilters = () => {
    setCurrentPage(0);
    fetchUsersCallback();
  };

  const handleClearSearch = () => {
    setUsernameSearch('');
    setSelectedRole('');
    setShowOnlyActive(true);
    setCurrentPage(0);
    clearTimeout(filterTimeoutRef.current);
    fetchUsersCallback();
  };

  const handleToggleActive = () => {
    setShowOnlyActive(!showOnlyActive);
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  if (error) {
    return (
      <div className="error-container">
        <h2>Erro</h2>
        <p>{error}</p>
        <button onClick={fetchUsersCallback} className="retry-btn">Tentar Novamente</button>
      </div>
    );
  }

  const isUserActive = (user) => {
    return user.enabled !== false;
  };

  return (
    <div className="user-list-container">
      {success && <p className="success-message">{success}</p>}
      <h2 className="user-list-title">Lista de Usuários (Admin)</h2>
      
      <div className="filters-section">
        {/* 1. Buscar por Username */}
        <div className="filter-group">
          <label className="filter-label">Buscar por Username:</label>
          <div className="input-group">
            <input
              type="text"
              value={usernameSearch}
              onChange={(e) => setUsernameSearch(e.target.value)}
              placeholder="Ex: admin"
              className="search-input"
            />
          </div>
        </div>

        {/* 2. Filtrar por Role - Dropdown + btn Buscar */}
        <div className="filter-group">
          <label className="filter-label">Filtrar por Role:</label>
          <div className="input-group">
            <select
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
              className="role-select"
            >
              <option value="">Selecione uma Role</option>
              {allRoles.map((role) => (
                <option key={role} value={role}>{role}</option>
              ))}
            </select>

            <button onClick={handleApplyFilters} className="search-btn">
              Buscar
            </button>
          </div>
        </div>       
      </div>

      <div className="filter-group">
        <div className="input-group">
          <label className="toggle-label">
            <input
              type="checkbox"
              checked={showOnlyActive}
              onChange={handleToggleActive}
            />
            Mostrar só ativos
          </label>
          <button onClick={handleClearSearch} className="clear-btn">
            Limpar Filtros
          </button>
        </div>
      </div>

      <table className="users-table">
        <thead>
          <tr>
            <th className="table-header">Username</th>
            <th className="table-header">Email</th>
            <th className="table-header">Roles</th>
            <th className="table-header">Status</th>
            <th className="table-header">Criado em</th>
            <th className="table-header">Atualizado em</th>
            <th className="table-header">Deletado em</th>
            <th className="table-header">Ações</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => {
              const active = isUserActive(user);
              return (
                <tr key={user.username} className={`user-row ${active ? 'active' : 'inactive'}`}>
                  <td className="table-cell">{user.username}</td>
                  <td className="table-cell">{user.email || 'N/A'}</td>
                  <td className="table-cell">
                    {user.roles ? user.roles.join(', ') : 'N/A'}
                  </td>
                  <td className="table-cell">
                    {active ? 'Ativo' : 'Inativo'}
                  </td>
                  <td className="table-cell">{formatDate(user.deletedAt)}</td>
                  <td className="table-cell">{formatDate(user.updatedAt)}</td>
                  <td className="table-cell">{formatDate(user.deletedAt)}</td>
                  <td className="table-cell">
                    <button className="details-btn">Mais Informações</button>
                    <button className="add-role-btn">Adicionar Role</button>
                    <button className="remove-role-btn">Remover Role</button>
                    <button className="delete-btn">Deletar</button>
                  </td>
                </tr>
              );
            })
          ) : (
            <tr>
              <td colSpan="8" className="no-data">Nenhum usuário encontrado.</td>
            </tr>
          )}
        </tbody>
      </table>

      {totalPages > 1 && (
        <div className="pagination">
          <button 
            onClick={() => handlePageChange(currentPage - 1)} 
            disabled={currentPage === 0}
            className="page-btn"
          >
            Anterior
          </button>
          <span className="page-info">Página {currentPage + 1} de {totalPages}</span>
          <button 
            onClick={() => handlePageChange(currentPage + 1)} 
            disabled={currentPage === totalPages - 1}
            className="page-btn"
          >
            Próxima
          </button>
        </div>
      )}
    </div>
  );
};

export default UserList;