import React, { useEffect, useState, useCallback } from "react";
import PermissionsService from "../../Services/PermissionsService";
import Pagination from "../../components/Pagination/Pagination";
import { FaPlus, FaPen, FaTrash, FaCheckCircle } from "react-icons/fa";  // Adicionado FaCheckCircle para ícone de sucesso
import PageStruct from "../Layout/PageStruct/PageStruct";
import AddPermissionModal from "../../components/Modals/Permissions/AddPermissionModal";
import EditPermissionModal from "../../components/Modals/Permissions/EditPermissionModal";
import DeleteConfirmModal from "../../components/Modals/ConfirmModal";
import CustomModal from "../../components/Custom/CustomModal";
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

  const handleAddPermission = () => {
    console.log('Abrindo modal de adição');
    setModalAddOpen(true);
  };

  const handleEditPermission = (permission) => {
    console.log('Abrindo modal de edição', permission);
    setSelectedPermission(permission);
    setModalEditOpen(true);
  };

  const handleDeleteClick = (permission) => {
    console.log('Abrindo modal de deleção', permission);
    setSelectedPermission(permission);
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
      await PermissionsService.createPermission({ description: form.description, enabled: true });
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
    (p.description || '').toLowerCase().includes(search.toLowerCase())
  );

  useEffect(() => {
    console.log('Modal Add Open:', modalAddOpen);
  }, [modalAddOpen]);

  useEffect(() => {
    console.log('Modal Edit Open:', modalEditOpen);
  }, [modalEditOpen]);

  useEffect(() => {
    console.log('Modal Confirm Open:', modalConfirmOpen);
  }, [modalConfirmOpen]);

  useEffect(() => {
    console.log('Modal Success Open:', modalSuccessOpen);
  }, [modalSuccessOpen]);

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
                <th>Descrição</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {filteredPermissions.length ? (
                filteredPermissions.map((permission) => (
                  <tr key={permission.id || permission.description}>
                    <td>{permission.description || 'N/A'}</td>
                    <td>
                      {permission.enabled ? (
                        <span className="enabled">Ativa</span>
                      ) : (
                        <span className="disabled">Inativa</span>
                      )}
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

      <DeleteConfirmModal
        isOpen={modalConfirmOpen}
        onClose={() => setModalConfirmOpen(false)}
        itemName={selectedPermission?.description || 'Desconhecido'}
        onConfirm={handleConfirmDelete}
      />

      <CustomModal
        isOpen={modalSuccessOpen}
        onClose={() => setModalSuccessOpen(false)}
        title="Sucesso"
        showFooter={false}
      >
        <div className="modal-icon success">
          <FaCheckCircle />
        </div>
        <p className="modal-message success">{successMessage}</p>
      </CustomModal>
    </PageStruct>
  );
};

export default Permissions;