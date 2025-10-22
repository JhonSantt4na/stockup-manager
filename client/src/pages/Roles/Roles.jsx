import React, { useEffect, useState, useCallback } from "react";
import RolesService from "../../Services/RolesService";
import { FaPlus, FaUserShield, FaTrash } from "react-icons/fa";
import RoleModal from "../../components/Modals/Roles/RoleModal";
import ConfirmModal from "../../components/Modals/ConfirmModal";
import SuccessModal from "../../components/Modals/SuccessModal";
import PermissionsListModal from "../../components/Modals/Roles/PermissionsListModal";
import "./Roles.css";

const Roles = () => {
  const [roles, setRoles] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [roleModalOpen, setRoleModalOpen] = useState(false);
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [successModalOpen, setSuccessModalOpen] = useState(false);
  const [permissionsListModalOpen, setPermissionsListModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [selectedRole, setSelectedRole] = useState(null);
  const [pendingAction, setPendingAction] = useState(null);

  const fetchRoles = useCallback(
    async (pageNumber = 0) => {
      setLoading(true);
      try {
        const data = await RolesService.getRolesWithUsers(pageNumber, 10, search);
        setRoles(data.content || []);
        setTotalPages(data.totalPages || 0);
        setPage(data.number || 0);
        setError("");
      } catch (err) {
        console.error("Erro ao carregar roles:", err);
        setError("Erro ao carregar roles.");
      } finally {
        setLoading(false);
      }
    },
    [search]
  );

  useEffect(() => {
    fetchRoles();
  }, [fetchRoles]);

  const handleSearch = (e) => setSearch(e.target.value);

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
    setPendingAction("deletar");
    setConfirmModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!selectedRole) return;
    
    try {
      await RolesService.deleteRole(selectedRole.name);
      fetchRoles(page);
      setConfirmModalOpen(false);
      showSuccess(`Role deletada com sucesso!`);
    } catch (err) {
      alert(`Erro ao deletar role.`);
    }
  };

  const handleShowPermissions = (role) => {
    setSelectedRole(role);
    setPermissionsListModalOpen(true);
  };

  const showSuccess = (message) => {
    setSuccessMessage(message);
    setSuccessModalOpen(true);
  };

  const handleNextPage = () => {
    if (page + 1 < totalPages) fetchRoles(page + 1);
  };

  const handlePrevPage = () => {
    if (page > 0) fetchRoles(page - 1);
  };

  const getPermissionColor = (permission) => {
    // Mapa de cores para permissions, ajuste conforme necessário
    const colors = {
      'READ': '#3b82f6',
      'WRITE': '#f59e0b',
      'DELETE': '#ef4444',
      'UPDATE': '#10b981',
      // Adicione mais se houver permissions específicas
    };
    return colors[permission] || '#8b5cf6';
  };

  const renderPermissions = (permissions, role) => {
    if (!permissions || permissions.length === 0) return "-";
    
    const sortedPermissions = [...permissions].sort();

    return (
      <div className="permissions-wrapper">
        {sortedPermissions.slice(0, 3).map((perm) => (
          <span 
            key={perm} 
            className="permission-tag"
            style={{ backgroundColor: `${getPermissionColor(perm)}20`, color: getPermissionColor(perm) }}
          >
            {perm}
          </span>
        ))}
        {sortedPermissions.length > 3 && (
          <span className="ver-mais-link" onClick={() => handleShowPermissions(role)}>
            +{sortedPermissions.length - 3} mais
          </span>
        )}
      </div>
    );
  };

  return (
    <div className="roles-container">
      <div className="roles-header">
        <div className="header-controls">
          <h2>Administração de Funções</h2>
          <div className="controls-group">
            <input
              type="text"
              placeholder="Buscar função..."
              className="search-input search-box"
              value={search}
              onChange={handleSearch}
            />
            <button className="btn-add" onClick={handleAddRole}>
              <FaPlus /> Nova Função
            </button>
          </div>
        </div>
      </div>

      {loading ? (
        <p className="loading">Carregando funções...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <div className="table-container">
          <table className="roles-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Permissões</th>
                <th>Usuários</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {roles.length > 0 ? (
                roles.map((role) => (
                  <tr key={role.id}>
                    <td>{role.name}</td>
                    <td className="permissions-column">
                      {renderPermissions(role.strings, role)}
                    </td>
                    <td>{role.users ? role.users.length : 0}</td>
                    <td className="actions-inline">
                      <button className="btn-manage" onClick={() => handleManageRole(role)}>
                        <FaUserShield /> Gerenciar
                      </button>
                      <button className="btn-delete" onClick={() => handleDeleteClick(role)}>
                        <FaTrash /> Remover
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" style={{ textAlign: "center", color: "#666" }}>
                    Nenhuma função encontrada.
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

      {/* Modal para criar/editar role */}
      {roleModalOpen && (
        <RoleModal 
          role={selectedRole} 
          onClose={() => { 
            setRoleModalOpen(false); 
            fetchRoles(page); 
          }}
          onSuccess={showSuccess}
        />
      )}

      {/* Modal para visualizar lista de permissions */}
      {permissionsListModalOpen && (
        <PermissionsListModal 
          role={selectedRole} 
          onClose={() => setPermissionsListModalOpen(false)} 
        />
      )}

      {/* Modal de confirmação para deletar */}
      {confirmModalOpen && (
        <ConfirmModal 
          item={selectedRole}
          itemType="role"
          actionType={pendingAction}
          onClose={() => setConfirmModalOpen(false)}
          onConfirm={handleConfirmDelete}
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

export default Roles;