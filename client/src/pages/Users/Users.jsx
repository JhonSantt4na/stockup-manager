import React, { useEffect, useState, useCallback } from "react";
import UserService from "../../Services/UserService";
import { FaPlus, FaUserShield } from "react-icons/fa";
import UserModal from "../../components/Modals/UserModal";
import RolesModal from "../../components/Modals/RolesModal";
import RolesListModal from "../../components/Modals/RolesListModal";
import ConfirmModal from "../../components/Modals/ConfirmModal";
import SuccessModal from "../../components/Modals/SuccessModal";
import "./Users.css";

const Users = () => {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");
  const [filter, setFilter] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [userModalOpen, setUserModalOpen] = useState(false);
  const [rolesModalOpen, setRolesModalOpen] = useState(false);
  const [rolesListModalOpen, setRolesListModalOpen] = useState(false);
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [successModalOpen, setSuccessModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [selectedUser, setSelectedUser] = useState(null);
  const [pendingAction, setPendingAction] = useState(null);

  const fetchUsers = useCallback(
    async (pageNumber = 0) => {
      setLoading(true);
      try {
        const data = await UserService.getUsers(pageNumber, 10, search, filter);
        setUsers(data.content || []);
        setTotalPages(data.totalPages || 0);
        setPage(data.number || 0);
        setError("");
      } catch (err) {
        console.error("Erro ao carregar usuários:", err);
        setError("Erro ao carregar usuários.");
      } finally {
        setLoading(false);
      }
    },
    [search, filter]
  );

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleSearch = (e) => setSearch(e.target.value);
  const handleFilterChange = (value) => setFilter(value);

  const handleAddUser = () => {
    setSelectedUser(null);
    setUserModalOpen(true);
  };

  const handleManageRoles = (user) => {
    setSelectedUser(user);
    setRolesModalOpen(true);
  };

  const handleShowRoles = (user) => {
    setSelectedUser(user);
    setRolesListModalOpen(true);
  };

  const showSuccess = (message) => {
    setSuccessMessage(message);
    setSuccessModalOpen(true);
  };

  const handleToggleClick = (user) => {
    setSelectedUser(user);
    setPendingAction(user.enabled ? "desativar" : "ativar");
    setConfirmModalOpen(true);
  };

  const handleConfirmToggle = async () => {
    if (!selectedUser) return;
    
    try {
      await UserService.toggleUser(selectedUser.username);
      fetchUsers(page);
      setConfirmModalOpen(false);
      showSuccess(`Usuário ${pendingAction}do com sucesso!`);
    } catch (err) {
      alert(`Erro ao ${pendingAction} usuário.`);
    }
  };

  const handleNextPage = () => {
    if (page + 1 < totalPages) fetchUsers(page + 1);
  };

  const handlePrevPage = () => {
    if (page > 0) fetchUsers(page - 1);
  };

  const getRoleColor = (role) => {
    const colors = {
      'ADMIN': '#ef4444',
      'USER': '#3b82f6', 
      'MANAGER': '#f59e0b',
      'SUPERVISOR': '#10b981',
      'MODERATOR': '#8b5cf6'
    };
    return colors[role] || '#666';
  };

  const renderRoles = (roles, user) => {
    if (!roles || roles.length === 0) return "-";
    
    return (
      <div className="roles-wrapper">
        {roles.slice(0, 2).map((role) => (
          <span 
            key={role} 
            className="role-text"
            style={{ color: getRoleColor(role) }}
          >
            {role}
          </span>
        ))}
        {roles.length > 2 && (
          <button className="ver-mais" onClick={() => handleShowRoles(user)}>
            +{roles.length - 2} mais
          </button>
        )}
      </div>
    );
  };

  return (
    <div className="users-container">
      <div className="users-header">
        <div className="header-controls">
          <h2>Administração de Usuários</h2>
          <div className="controls-group">
            <select className="filter-select search-box" value={filter} onChange={(e) => handleFilterChange(e.target.value)}>
              <option value="all">Exibir todos</option>
              <option value="active">Exibir usuários ativos</option>
              <option value="inactive">Exibir usuários inativos</option>
            </select>
            <input
              type="text"
              placeholder="Buscar usuário..."
              className="search-input search-box"
              value={search}
              onChange={handleSearch}
            />
            <button className="btn-add" onClick={handleAddUser}>
              <FaPlus /> Novo Usuário
            </button>
          </div>
        </div>
      </div>

      {loading ? (
        <p className="loading">Carregando usuários...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <div className="table-container">
          <table className="users-table">
            <thead>
              <tr>
                <th>Usuário</th>
                <th>Email</th>
                <th>Funções</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {users.length > 0 ? (
                users.map((user, index) => (
                  <tr key={user.id}>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    <td className="roles-column">{user.roles ? renderRoles(user.roles, user) : "-"}</td>
                    <td>
                      <span className={user.enabled ? "dot-green" : "dot-red"}></span>
                      {user.enabled ? " Ativo" : " Inativo"}
                    </td>
                    <td className="actions-inline">
                      <button
                        className={`btn-toggle ${user.enabled ? "btn-red" : "btn-green"}`}
                        onClick={() => handleToggleClick(user)}
                      >
                        {user.enabled ? "Desativar" : "Ativar"}
                      </button>
                      <button className="btn-roles" onClick={() => handleManageRoles(user)}>
                        <FaUserShield /> Gerenciar
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" style={{ textAlign: "center", color: "#666" }}>
                    Nenhum usuário encontrado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {!loading && totalPages > 1 && (
        <div className="pagination">
          <button onClick={handlePrevPage} disabled={page === 0}>◀ Anterior</button>
          <span>Página {page + 1} de {totalPages}</span>
          <button onClick={handleNextPage} disabled={page + 1 >= totalPages}>Próxima ▶</button>
        </div>
      )}

      {/* Modal para criar/editar usuário */}
      {userModalOpen && (
        <UserModal 
          user={selectedUser} 
          onClose={() => { 
            setUserModalOpen(false); 
            fetchUsers(page); 
          }}
          onSuccess={showSuccess}
        />
      )}

      {/* Modal para gerenciar roles */}
      {rolesModalOpen && (
        <RolesModal 
          user={selectedUser} 
          onClose={() => { 
            setRolesModalOpen(false); 
            fetchUsers(page); 
          }}
          onSuccess={showSuccess}
        />
      )}

      {/* Modal para visualizar lista de roles */}
      {rolesListModalOpen && (
        <RolesListModal 
          user={selectedUser} 
          onClose={() => setRolesListModalOpen(false)} 
        />
      )}

      {/* Modal de confirmação para ativar/desativar */}
      {confirmModalOpen && (
        <ConfirmModal 
          user={selectedUser}
          actionType={pendingAction}
          onClose={() => setConfirmModalOpen(false)}
          onConfirm={handleConfirmToggle}
        />
      )}

      {/* Modal de sucesso */}
      {successModalOpen && (
        <SuccessModal 
          message={successMessage}
          onClose={() => setSuccessModalOpen(false)}
        />
      )}
    </div>
  );
};

export default Users;