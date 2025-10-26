import React, { useEffect, useState, useCallback } from "react";
import UserService from "../../Services/UserService";
import Pagination from "../../components/Pagination/Pagination";
import { FaPlus, FaUserShield, FaSearch } from "react-icons/fa";
import UserModal from "../../components/Modals/Users/ModalRegisterUser";
import ModalUpdateUser from "../../components/Modals/Users/ModalUpdateUser";
import ModalManageRoles from "../../components/Modals/Users/ModalManageRoles";
import RolesListModal from "../../components/Modals/Users/RolesListModal";
import ConfirmModal from "../../components/Modals/ConfirmModal";
import SuccessModal from "../../components/Modals/SuccessModal";
import PageStruct from "../../pages/Layout/PageStruct/PageStruct";
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
  const [updateUserModalOpen, setUpdateUserModalOpen] = useState(false);
  const [manageRolesModalOpen, setManageRolesModalOpen] = useState(false);
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

  const handleAddUser = () => {
    setSelectedUser(null);
    setUserModalOpen(true);
  };

  const handleUpdateUser = (user) => {
    setSelectedUser(user);
    setUpdateUserModalOpen(true);
  };

  const handleManageRoles = (user) => {
    setSelectedUser(user);
    setManageRolesModalOpen(true);
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
      ADMIN: "#ef4444",
      USER: "#3b82f6",
      MANAGER: "#f59e0b",
      SUPERVISOR: "#10b981",
      MODERATOR: "#8b5cf6",
      PRO: "#6366f1",
    };
    return colors[role] || "#666";
  };

  const renderRoles = (roles, user) => {
    if (!roles || roles.length === 0) return "-";

    const sortedRoles = [...roles].sort((a, b) => {
      if (a === "ADMIN") return -1;
      if (b === "ADMIN") return 1;
      return a.localeCompare(b);
    });

    return (
      <div className="roles-wrapper">
        {sortedRoles.slice(0, 2).map((role) => (
          <span
            key={role}
            className="role-tag"
            style={{
              backgroundColor: `${getRoleColor(role)}20`,
              color: getRoleColor(role),
            }}
          >
            {role}
          </span>
        ))}
        {sortedRoles.length > 2 && (
          <span className="ver-mais-link" onClick={() => handleShowRoles(user)}>
            +{sortedRoles.length - 2} mais
          </span>
        )}
      </div>
    );
  };

  const header = (
    <div className="users-header">
      <h2>Administração de Usuários</h2>
      <div className="controls-group">
        <select
          className="filter-select search-box"
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        >
          <option value="all">Exibir todos</option>
          <option value="active">Usuários ativos</option>
          <option value="inactive">Usuários inativos</option>
        </select>
        <div className="search-box">
          <input
          type="text"
          placeholder="Buscar por username ou email..."
          className="search-input"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <FaSearch className="search-icon" />
       
        <button className="btn-add" onClick={handleAddUser}>
          <FaPlus /> Novo Usuário
        </button>
      </div>
    </div>
  );

  const body = (
    <>
      {loading ? (
        <p className="loading">Carregando usuários...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <div className="table-container">
          <div className="table-wrapper">
            <table className="table">
              <thead>
                <tr>
                  <th>Usuário</th>
                  <th>Nome Completo</th>
                  <th>Email</th>
                  <th>Funções</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {users.length > 0 ? (
                  users.map((user) => (
                    <tr key={user.id}>
                      <td>{user.username}</td>
                      <td>{user.fullName || "-"}</td>
                      <td>{user.email}</td>
                      <td className="roles-column">
                        {user.roles ? renderRoles(user.roles, user) : "-"}
                      </td>
                      <td className="status-toggler">
                        <span
                          className={user.enabled ? "dot-green" : "dot-red"}
                        ></span>
                        {user.enabled ? "Ativo" : "Inativo"}
                      </td>
                      <td className="actions-inline">
                        <button
                          className={`btn-toggle ${
                            user.enabled ? "btn-red" : "btn-green"
                          }`}
                          onClick={() => handleToggleClick(user)}
                        >
                          {user.enabled ? "Desativar" : "Ativar"}
                        </button>
                        <button
                          className="btn-manage"
                          onClick={() => handleUpdateUser(user)}
                        >
                          <FaUserShield /> Gerenciar
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" style={{ textAlign: "center", color: "#666" }}>
                      Nenhum usuário encontrado.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </>
  );

  const footer = (
  <div className="users-footer">
    {!loading && !error ? (
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPrev={handlePrevPage}
        onNext={handleNextPage}
      />
    ) : (
      <div className="footer-empty"></div>
    )}
  </div>
  );


  return (
    <PageStruct header={header} body={body} footer={footer}>
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

      {updateUserModalOpen && (
        <ModalUpdateUser
          user={selectedUser}
          onClose={() => {
            setUpdateUserModalOpen(false);
            fetchUsers(page);
          }}
          onManageRoles={() => {
            setUpdateUserModalOpen(false);
            setManageRolesModalOpen(true);
          }}
          onSuccess={showSuccess}
        />
      )}

      {manageRolesModalOpen && (
        <ModalManageRoles
          user={selectedUser}
          onClose={() => {
            setManageRolesModalOpen(false);
            fetchUsers(page);
          }}
          onSuccess={showSuccess}
        />
      )}

      {rolesListModalOpen && (
        <RolesListModal
          user={selectedUser}
          onClose={() => setRolesListModalOpen(false)}
        />
      )}

      {confirmModalOpen && (
        <ConfirmModal
          user={selectedUser}
          actionType={pendingAction}
          onClose={() => setConfirmModalOpen(false)}
          onConfirm={handleConfirmToggle}
        />
      )}

      {successModalOpen && (
        <SuccessModal
          message={successMessage}
          onClose={() => setSuccessModalOpen(false)}
        />
      )}
    </PageStruct>
  );
};

export default Users;