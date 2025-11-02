import React, { useEffect, useState } from "react";
import RolesService from "../../../Services/RolesService";
import CustomModal from "../../Custom/CustomModal";
import "../Modal.css";

const RoleModal = ({ role, onClose, onSuccess }) => {
  const [form, setForm] = useState({
    name: "",
    permissions: [],
    active: true,
  });

  const [availablePermissions, setAvailablePermissions] = useState([]);
  const [selectedPermission, setSelectedPermission] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchAvailablePermissions = async () => {
      try {
        const perms = await RolesService.getAllActivePermissions();
        setAvailablePermissions(Array.isArray(perms) ? perms : []);
      } catch {
        setAvailablePermissions([]);
      }
    };

    const fetchRolePermissions = async () => {
      if (role) {
        try {
          const rolePerms = await RolesService.getRolePermissions(role.name);
          setForm({
            name: role.name || "",
            permissions: Array.isArray(rolePerms) ? rolePerms : [],
            active: role.enabled ?? true,
          });
        } catch {
          setForm({
            name: role.name || "",
            permissions: [],
            active: role.enabled ?? true,
          });
        }
      }
    };

    fetchAvailablePermissions();
    fetchRolePermissions();
  }, [role]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleToggleActive = () => {
    setForm({ ...form, active: !form.active });
  };

  const handleAddPermission = () => {
    if (!selectedPermission || form.permissions.includes(selectedPermission)) return;
    setForm({ ...form, permissions: [...form.permissions, selectedPermission] });
    setSelectedPermission("");
  };

  const handleRemovePermission = (perm) => {
    setForm({ ...form, permissions: form.permissions.filter((p) => p !== perm) });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    let currentName = role ? role.name : form.name;

    try {
      if (role && form.name !== role.name) {
        await RolesService.updateRole({
          oldName: role.name,
          newName: form.name,
          enabled: form.active,
        });
        currentName = form.name;
      } else if (!role) {
        const createdRole = await RolesService.createRole({
          name: form.name,
          enabled: form.active,
        });
        currentName = createdRole.name;
      } else if (role && role.name === form.name) {
        await RolesService.updateRole({
          oldName: currentName,
          newName: currentName,
          enabled: form.active,
        });
      }
      const originalPerms = await RolesService.getRolePermissions(currentName);
      const newPerms = form.permissions.filter((p) => !originalPerms.includes(p));
      if (newPerms.length > 0) await RolesService.assignPermissions(currentName, newPerms);

      const removedPerms = originalPerms.filter((p) => !form.permissions.includes(p));
      if (removedPerms.length > 0) await RolesService.removePermissions(currentName, removedPerms);

      onSuccess(role ? "Função atualizada com sucesso!" : "Função criada com sucesso!");
    } catch (err) {
      console.error("Erro detalhado:", err.response ? err.response.data : err.message);
      alert("Erro ao salvar função: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
      onClose();
    }
  };

  return (
    <CustomModal
      isOpen={true}
      onClose={onClose}
      title={role ? "Editar Função" : "Nova Função"}
      size="medium"
      onConfirm={handleSubmit}
      confirmText={loading ? "Salvando..." : role ? "Atualizar" : "Adicionar"}
      cancelText="Cancelar"
      onCancel={onClose}
    >
      <form onSubmit={handleSubmit} className="modal-form">
        <div className="form-grid centered-form">
          <div className="form-group">
            <label>Nome :</label>
            <input
              type="text"
              name="name"
              placeholder="Nome da função"
              value={form.name}
              onChange={handleChange}
              className="search-box"
              required
              disabled={loading}
            />
          </div>
        </div>

        {role && (
          <div className="checkbox-group">
            <input
              type="checkbox"
              checked={form.active}
              onChange={handleToggleActive}
              disabled={loading}
            />
            <label>Função Ativa</label>
          </div>
        )}

        <div className="permissions-management-section">
          <h4>Gerenciar Permissões</h4>
          <div className="permissions-controls">
            <select
              value={selectedPermission}
              onChange={(e) => setSelectedPermission(e.target.value)}
              disabled={loading}
              className="permission-select search-box"
            >
              <option value="">Selecione uma permissão</option>
              {availablePermissions
                .filter((perm) => !form.permissions.includes(perm))
                .map((perm) => (
                  <option key={perm} value={perm}>
                    {perm}
                  </option>
                ))}
            </select>
            <button
              type="button"
              className="btn-manage"
              onClick={handleAddPermission}
              disabled={!selectedPermission || loading}
            >
              Adicionar
            </button>
          </div>

          <div className="role-permissions-list">
            <h5>Permissões Atribuídas:</h5>
            {form.permissions.length > 0 ? (
              form.permissions.map((perm) => (
                <div key={perm} className="permission-item">
                  <span className="permission-name">{perm}</span>
                  <button
                    type="button"
                    className="btn-delete"
                    onClick={() => handleRemovePermission(perm)}
                    disabled={loading}
                  >
                    Remover
                  </button>
                </div>
              ))
            ) : (
              <p className="no-permissions">Nenhuma permissão atribuída</p>
            )}
          </div>
        </div>
      </form>
    </CustomModal>
  );
};

export default RoleModal;