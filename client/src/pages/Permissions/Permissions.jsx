import React, { useEffect, useState, useCallback } from "react";
import PermissionsService from "../../Services/PermissionsService";
import Pagination from "../../components/Pagination/Pagination";
import { FaPlus, FaPen, FaTrash, FaSearch} from "react-icons/fa";
import PageStruct from "../Layout/PageStruct/PageStruct";
import AddPermissionModal from "../../components/Modals/Permissions/AddPermissionModal";
import EditPermissionModal from "../../components/Modals/Permissions/EditPermissionModal";
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

  const [modalAddOpen, setModalAddOpen] = useState(false);
  const [modalEditOpen, setModalEditOpen] = useState(false);
  const [modalConfirmOpen, setModalConfirmOpen] = useState(false);
  const [modalSuccessOpen, setModalSuccessOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [selectedPermission, setSelectedPermission] = useState(null);
  const [pendingAction, setPendingAction] = useState(null);

  const fetchPermissions = useCallback(async (pageNumber = 0) => {
    setLoading(true);
    try {
      const data = await PermissionsService.getAllPermissions(pageNumber, 10);
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
  }, []);

  useEffect(() => {
    fetchPermissions();
  }, [fetchPermissions]);

  const handleSearch = (e) => setSearch(e.target.value);
  const handleAddPermission = () => setModalAddOpen(true);

  const handleEditPermission = (permission) => {
    setSelectedPermission(permission);
    setModalEditOpen(true);
  };

  const handleDeleteClick = (permission) => {
    setSelectedPermission(permission);
    setPendingAction("delete");
    setModalConfirmOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!selectedPermission) return;
    try {
      await PermissionsService.deletePermission(selectedPermission.description);
      setModalConfirmOpen(false);
      showSuccess("Permissão removida com sucesso!");
      fetchPermissions(page);
    } catch (error) {
      console.error(error);
      alert("Erro ao deletar permissão.");
    }
  };

  const handleCreatePermission = async (form) => {
    try {
      await PermissionsService.createPermission({
        description: form.description,
        enabled: true,
      });
      showSuccess("Permissão criada com sucesso!");
      setModalAddOpen(false);
      fetchPermissions(page);
    } catch (error) {
      console.error(error);
      alert("Erro ao criar permissão.");
    }
  };

  const handleUpdatePermission = async (form) => {
    try {
      await PermissionsService.updatePermission({
        oldDescription: form.oldDescription,
        newDescription: form.newDescription,
        enabled: form.enabled,
      });
      showSuccess("Permissão atualizada com sucesso!");
      setModalEditOpen(false);
      fetchPermissions(page);
    } catch (error) {
      console.error(error);
      alert("Erro ao atualizar permissão.");
    }
  };

  const showSuccess = (message) => {
    setSuccessMessage(message);
    setModalSuccessOpen(true);
  };

  const handleNextPage = () => {
    if (page + 1 < totalPages) fetchPermissions(page + 1);
  };

  const handlePrevPage = () => {
    if (page > 0) fetchPermissions(page - 1);
  };

  const filteredPermissions = permissions.filter((p) =>
    (p.description || "").toLowerCase().includes(search.toLowerCase())
  );

  const header = (
    <div className="permissions-header">
      <h2>Administração de Permissões</h2>
      <div className="controls-group">
        <div className="search-box">
          <FaSearch className="search-icon" />
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
                <th>Descrição</th>
                <th>Roles</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {filteredPermissions.length ? (
                filteredPermissions.map((permission) => (
                  <tr key={permission.id || permission.description}>
                    <td>{permission.description || "N/A"}</td>
                    <td>{permission.roles?.length || 0}</td>
                
                    <td className="status-toggler">
                      <span
                        className={
                          permission.enabled ? "dot-green" : "dot-red"
                        }
                      ></span>
                      {permission.enabled ? "Ativa" : "Inativa"}
                    </td>

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
                  <td colSpan="4" className="no-data">
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
      <AddPermissionModal
        isOpen={modalAddOpen}
        onClose={() => setModalAddOpen(false)}
        onSubmit={handleCreatePermission}
      />

      <EditPermissionModal
        isOpen={modalEditOpen}
        onClose={() => setModalEditOpen(false)}
        permission={selectedPermission}
        onSubmit={handleUpdatePermission}
      />

      <ConfirmModal
        isOpen={modalConfirmOpen}
        onClose={() => setModalConfirmOpen(false)}
        item={selectedPermission}
        itemType="permission"
        actionType={pendingAction}
        onConfirm={handleConfirmDelete}
      />

      <SuccessModal
        isOpen={modalSuccessOpen}
        message={successMessage}
        onClose={() => setModalSuccessOpen(false)}
      />
    </PageStruct>
  );
};

export default Permissions;