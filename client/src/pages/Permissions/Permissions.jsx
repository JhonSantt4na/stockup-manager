import React, { useEffect, useState, useCallback } from "react";
import PermissionsService from "../../Services/PermissionsService";
import Pagination from "../../components/Pagination/Pagination";
import { FaPlus, FaPen, FaTrash } from "react-icons/fa";
import PermissionModal from "../../components/Modals/Permissions/AddPermissionModal";
import ConfirmModal from "../../components/Modals/ConfirmModal";
import SuccessModal from "../../components/Modals/SuccessModal";
import PageStruct from "../Layout/PageStruct/PageStruct";
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

  const handleNextPage = () => {
    if (page + 1 < totalPages) fetchPermissions(page + 1);
  };

  const handlePrevPage = () => {
    if (page > 0) fetchPermissions(page - 1);
  };

  const header = (
    <div className="permissions-header">
      <h2>Administração de Permissões</h2>
      <div className="controls-group">
        <div className="search-box">
          <input
            type="text"
            placeholder="Buscar permissão..."
            className="search-input"
            value={search}
            onChange={handleSearch}
          />
        </div>
        <button className="btn-add" onClick={handleAddPermission}>
          <FaPlus /> Nova Permissão
        </button>
      </div>
    </div>
  );

  const body = (
    <div className="table-container">
      {loading ? (
        <p className="loading">Carregando permissões...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <div className="table-wrapper">
          <table className="table">
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
                        className="btn-manage"
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

  return (
    <PageStruct header={header} body={body} footer={footer}>
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
    </PageStruct>
  );
};

export default Permissions;