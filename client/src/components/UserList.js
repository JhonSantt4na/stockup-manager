// src/components/UserList.js (Sempre lista só enabled=true, sem toggle pra inativos)
import React, { useEffect, useState, useCallback, useRef } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [roleFilter, setRoleFilter] = useState('');
  const [pageSize] = useState(10);
  const navigate = useNavigate();
  const filterTimeoutRef = useRef(null);

  const fetchUsers = useCallback(async () => {
    try {
      console.log('FetchUsers chamado com filtro role:', roleFilter, 'página:', currentPage);
      const params = {
        page: currentPage,
        size: pageSize,
        enabled: true, // Sempre filtra só enabled=true (ativos)
        ...(roleFilter && { role: roleFilter }),
      };

      const response = await axios.get('http://localhost:8080/users', { params });
      const { content, totalPages: pages } = response.data;
      
      if (content.length > 0) {
        console.log('Exemplo de user do response:', content[0]);
      }
      
      setUsers(content);
      setTotalPages(pages);
      setError('');
    } catch (err) {
      console.error('Erro no fetchUsers:', err);
      setError('Erro ao listar usuários: ' + (err.response?.data?.message || err.message));
      if (err.response?.status === 403) {
        alert('Acesso negado. Apenas admins podem ver isso.');
        navigate('/dashboard');
      }
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, roleFilter, pageSize, navigate]);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleDelete = async (username) => {
    console.log('Delete clicado pra username:', username);
    if (window.confirm('Deletar usuário ' + username + '? Isso é irreversível!')) {
      console.log('Confirm ok, enviando DELETE pra /users/delete/' + username);
      try {
        const response = await axios.delete(`http://localhost:8080/users/delete/${username}`);
        console.log('Delete response:', response);
        alert('Usuário deletado com sucesso!');
        fetchUsers(); // Rebusca só ativos
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

  const handleFilterChange = (e) => {
    const newFilter = e.target.value.trim();
    setRoleFilter(newFilter);
    setCurrentPage(0);

    clearTimeout(filterTimeoutRef.current);
    if (newFilter.length === 0 || newFilter.length >= 2) {
      filterTimeoutRef.current = setTimeout(() => {
        console.log('Filtro debounced, chamando fetch com:', newFilter);
        fetchUsers();
      }, 500);
    }
  };

  const clearFilter = () => {
    setRoleFilter('');
    setCurrentPage(0);
    clearTimeout(filterTimeoutRef.current);
    fetchUsers();
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

  // Função pra checar status (se backend retornar false, mostra em cinza, mas como filtra, deve ser sempre true)
  const isUserActive = (user) => {
    return user.enabled !== false;
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Lista de Usuários (Admin)</h2>
      
      <div style={{ marginBottom: '20px' }}>
        <label>Filtrar por Role: </label>
        <input
          type="text"
          value={roleFilter}
          onChange={handleFilterChange}
          placeholder="Ex: ADMIN"
          style={{ padding: '5px', marginLeft: '10px' }}
        />
        <button onClick={clearFilter} style={{ marginLeft: '10px', padding: '5px' }}>
          Limpar Filtro
        </button>
        {/* Removido toggle - sempre só ativos */}
      </div>

      <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #ddd' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Username</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Email</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Roles</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Status</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Ações</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => {
              const active = isUserActive(user); // Sempre true com filtro backend
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
                  <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                    <button 
                      onClick={() => handleDelete(user.username)} 
                      disabled={!active}
                      style={{ 
                        padding: '5px 10px', 
                        background: '#dc3545', 
                        color: 'white', 
                        border: 'none', 
                        marginRight: '5px',
                        opacity: active ? 1 : 0.5,
                        cursor: active ? 'pointer' : 'not-allowed'
                      }}
                    >
                      Deletar
                    </button>
                  </td>
                </tr>
              );
            })
          ) : (
            <tr>
              <td colSpan="5" style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
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
    </div>
  );
};

export default UserList;