import React, { useEffect, useState, useCallback } from "react";
import RolesService from "../../Services/RolesService";
import Pagination from "../../components/Pagination/Pagination";
import { FaPlus, FaUserShield, FaTrash, FaSearch } from "react-icons/fa";
import RoleModal from "../../components/Modals/Roles/RoleModal";
import ConfirmModal from "../../components/Modals/ConfirmModal";
import SuccessModal from "../../components/Modals/SuccessModal";
import ErroModal from "../../components/Modals/ErroModal";
import PermissionsListModal from "../../components/Modals/Roles/PermissionsListModal";
import PageStruct from "../Layout/PageStruct/PageStruct";
import "./Roles.css";

const Roles = () => {
  const [roles, setRoles] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Modais
  const [roleModalOpen, setRoleModalOpen] = useState(false);
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [permissionsListModalOpen, setPermissionsListModalOpen] = useState(false);
  const [successModalOpen, setSuccessModalOpen] = useState(false);
  const [errorModalOpen, setErrorModalOpen] = useState(false);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [selectedRole, setSelectedRole] = useState(null);
  const [pendingAction, setPendingAction] = useState(null);

  // ===========================================
  // 🔹 Buscar roles com paginação e busca
  // ===========================================
  const fetchRoles = useCallback(async (pageNumber = 0, searchTerm = search) => {
    setLoading(true);
    try {
      const data = await RolesService.getRolesWithUsers(pageNumber, 10, searchTerm);
      setRoles(data.content || []);
      setTotalPages(data.totalPages || 0);
      setPage(data.number || 0);
      setError("");
    } catch (err) {
      console.error("Erro ao carregar funções:", err);
      setError("Erro ao carregar funções.");
      showError("Falha ao carregar a lista de funções.");
    } finally {
      setLoading(false);
    }
  }, [search]);

  useEffect(() => {
    fetchRoles(0, search);
  }, [fetchRoles, search]);

  // ===========================================
  // 🔹 Funções auxiliares de feedback
  // ===========================================
  const showSuccess = (message) => {
    setSuccessMessage(message);
    setSuccessModalOpen(true);
  };

  const showError = (message) => {
    setErrorMessage(message);
    setErrorModalOpen(true);
  };

  // ===========================================
  // 🔹 Handlers principais
  // ===========================================
  const handleSearch = (e) => {
    const value = e.target.value;
    setSearch(value);
    setPage(0);
    fetchRoles(0, value);
  };

  const handleAddRole = () => {
    setSelectedRole(null);
    setRoleModalOpen(true);
  };

  const handleManageRole = (role) => {
    setSelectedRole(role);
    setRoleModalOpen(true);
  };

  const handleDeleteClick = (role) => {
    setSelectedRole(role);
    setPendingAction("delete");
    setConfirmModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!selectedRole) return;
    try {
      await RolesService.deleteRole(selectedRole.name);
      setConfirmModalOpen(false);
      showSuccess("Função removida com sucesso!");
      setPage(0);
      fetchRoles(0, search);
    } catch (err) {
      console.error(err);
      showError("Erro ao remover a função.");
    }
  };

  const handleShowPermissions = (role) => {
    setSelectedRole(role);
    setPermissionsListModalOpen(true);
  };

  // ===========================================
  // 🔹 Paginação
  // ===========================================
  const handleNextPage = () => {
    if (page + 1 < totalPages) fetchRoles(page + 1, search);
  };

  const handlePrevPage = () => {
    if (page > 0) fetchRoles(page - 1, search);
  };

  // ===========================================
  // 🔹 Renderização das permissões
  // ===========================================
  const getPermissionColor = (permission) => {
    const colors = {
      READ: "#3b82f6",
      WRITE: "#f59e0b",
      DELETE: "#ef4444",
      UPDATE: "#10b981",
    };
    return colors[permission] || "#8b5cf6";
  };

  const renderPermissions = (permissions, role) => {
    if (!permissions?.length) return "-";
    const sorted = [...permissions].sort();
    const VISIBLE_COUNT = 2;
    const visible = sorted.slice(0, VISIBLE_COUNT);
    const remaining = sorted.length - visible.length;

    return (
      <div className="permissions-wrapper">
        {visible.map((perm) => {
          const color = getPermissionColor(perm);
          return (
            <span
              key={perm}
              className="permission-tag"
              style={{
                backgroundColor: `${color}20`,
                color: color,
                border: `1px solid ${color}`,
              }}
              title={perm}
            >
              {perm}
            </span>
          );
        })}
        {remaining > 0 && (
          <span
            className="ver-mais-link"
            onClick={() => handleShowPermissions(role)}
            style={{ cursor: "pointer" }}
          >
            +{remaining} mais
          </span>
        )}
      </div>
    );
  };

  // ===========================================
  // 🔹 Estrutura visual
  // ===========================================
  const header = (
    <div className="roles-header">
      <h2>Administração de Funções</h2>
      <div className="controls-group">
        <div className="search-box">
          <FaSearch className="search-icon" />
          <input
            type="text"
            placeholder="Buscar função..."
            className="search-input"
            value={search}
            onChange={handleSearch}
          />
        </div>
        <button className="btn-add" onClick={handleAddRole}>
          <FaPlus /> Nova Função
        </button>
      </div>
    </div>
  );

  const body = (
    <div className="table-container">
      {loading ? (
        <p className="loading">Carregando funções...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <div className="table-wrapper">
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Permissões</th>
                <th>Usuários</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {roles.length ? (
                roles.map((role) => (
                  <tr key={role.id}>
                    <td>{role.name}</td>
                    <td className="td-permissions">
                      {renderPermissions(role.strings, role)}
                    </td>
                    <td>{role.users?.length || 0}</td>
                    <td className="status-toggler">
                      <span
                        className={role.enabled ? "dot-green" : "dot-red"}
                      ></span>
                      {role.enabled ? "Ativa" : "Inativa"}
                    </td>
                    <td className="actions-inline">
                      <button
                        className="btn-manage"
                        onClick={() => handleManageRole(role)}
                      >
                        <FaUserShield /> Gerenciar
                      </button>
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteClick(role)}
                      >
                        <FaTrash /> Remover
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="no-data">
                    Nenhuma função encontrada.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );

  const footer = (
    <div className="roles-footer">
      {!loading && !error ? (
        <Pagination
          currentPage={page}
          totalPages={totalPages}
          onPrev={handlePrevPage}
          onNext={handleNextPage}
        />
      ) : (
        <div className="footer-empty" />
      )}
    </div>
  );

  // ===========================================
  // 🔹 Render final
  // ===========================================
  return (
    <PageStruct header={header} body={body} footer={footer}>
      {roleModalOpen && (
        <RoleModal
          role={selectedRole}
          onClose={() => {
            setRoleModalOpen(false);
            fetchRoles(page, search);
          }}
          onSuccess={showSuccess}
          onError={showError}
        />
      )}

      {permissionsListModalOpen && (
        <PermissionsListModal
          role={selectedRole}
          isOpen={permissionsListModalOpen}
          onClose={() => setPermissionsListModalOpen(false)}
        />
      )}

      {confirmModalOpen && (
        <ConfirmModal
          isOpen={confirmModalOpen}
          onClose={() => setConfirmModalOpen(false)}
          item={selectedRole}
          itemType="role"
          actionType={pendingAction}
          onConfirm={handleConfirmDelete}
        />
      )}

      {successModalOpen && (
        <SuccessModal
          isOpen={successModalOpen}
          message={successMessage}
          onClose={() => setSuccessModalOpen(false)}
        />
      )}

      {errorModalOpen && (
        <ErroModal
          isOpen={errorModalOpen}
          message={errorMessage}
          onClose={() => setErrorModalOpen(false)}
        />
      )}
    </PageStruct>
  );
};

export default Roles;