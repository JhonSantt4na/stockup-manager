import React, { useEffect, useState, useCallback } from "react";
import PermissionsService from "../../Services/PermissionsService";
import { FaPlus, FaPen, FaTrash } from "react-icons/fa";
import PermissionModal from "../../components/Modals/Permissions/AddPermissionModal";
import ConfirmModal from "../../components/Modals/ConfirmModal";
import SuccessModal from "../../components/Modals/SuccessModal";
import "./Permissions.css";

const Permissions = () => {
  const [permissions, setPermissions] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [modalOpen, setModalOpen] = useState(false);
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [successModalOpen, setSuccessModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [selectedPermission, setSelectedPermission] = useState(null);
  const [pendingAction, setPendingAction] = useState(null);

  const fetchPermissions = useCallback(async (pageNumber = 0) => {
    setLoading(true);
    try {
      const data = await PermissionsService.getAll(pageNumber, 10, search);
      setPermissions(data.content || []);
      setTotalPages(data.totalPages || 0);
      setPage(data.number || 0);
      setError("");
    } catch (err) {
      console.error(err);
      setError("Erro ao carregar permissões.");
    } finally {
      setLoading(false);
    }
  }, [search]);

  useEffect(() => {
    fetchPermissions();
  }, [fetchPermissions]);

  const handleSearch = (e) => setSearch(e.target.value);

  const handleAddPermission = () => {
    setSelectedPermission(null);
    setModalOpen(true);
  };

  const handleEditPermission = (permission) => {
    setSelectedPermission(permission);
    setModalOpen(true);
  };

  const handleDeleteClick = (permission) => {
    setSelectedPermission(permission);
    setPendingAction("deletar");
    setConfirmModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!selectedPermission) return;
    try {
      await PermissionsService.deletePermission(selectedPermission.name);
      fetchPermissions(page);
      setConfirmModalOpen(false);
      showSuccess("Permissão removida com sucesso!");
    } catch {
      alert("Erro ao deletar permissão.");
    }
  };

  const showSuccess = (message) => {
    setSuccessMessage(message);
    setSuccessModalOpen(true);
  };

  return (
    <div className="permissions-container">
      <header className="permissions-header">
        <div className="header-controls">
          <h2>Administração de Permissões</h2>
          <div className="controls-group">
            <input
              type="text"
              placeholder="Buscar permissão..."
              className="search-box"
              value={search}
              onChange={handleSearch}
            />
            <button className="btn-add" onClick={handleAddPermission}>
              <FaPlus /> Nova Permissão
            </button>
          </div>
        </div>
      </header>

      {loading ? (
        <p className="loading">Carregando permissões...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <div className="table-container">
          <table className="permissions-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Descrição</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {permissions.length ? (
                permissions.map((permission) => (
                  <tr key={permission.id}>
                    <td>{permission.name}</td>
                    <td>{permission.description || "-"}</td>
                    <td className="actions-inline">
                      <button
                        className="btn-edit"
                        onClick={() => handleEditPermission(permission)}
                      >
                        <FaPen /> Editar
                      </button>
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteClick(permission)}
                      >
                        <FaTrash /> Remover
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3" className="no-data">
                    Nenhuma permissão encontrada.
                  </td>
                </tr>
              )}
            </tbody>
          </table>

          {totalPages > 1 && (
            <div className="pagination">
              <button onClick={() => fetchPermissions(page - 1)} disabled={page === 0}>
                ◀ Anterior
              </button>
              <span>Página {page + 1} de {totalPages}</span>
              <button
                onClick={() => fetchPermissions(page + 1)}
                disabled={page + 1 >= totalPages}
              >
                Próxima ▶
              </button>
            </div>
          )}
        </div>
      )}

      {modalOpen && (
        <PermissionModal
          permission={selectedPermission}
          onClose={() => {
            setModalOpen(false);
            fetchPermissions(page);
          }}
          onSuccess={showSuccess}
        />
      )}

      {confirmModalOpen && (
        <ConfirmModal
          item={selectedPermission}
          itemType="permission"
          actionType={pendingAction}
          onClose={() => setConfirmModalOpen(false)}
          onConfirm={handleConfirmDelete}
        />
      )}

      {successModalOpen && (
        <SuccessModal
          message={successMessage}
          onClose={() => setSuccessModalOpen(false)}
        />
      )}
    </div>
  );
};

export default Permissions;
