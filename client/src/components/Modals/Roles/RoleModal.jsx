import React, { useState, useEffect } from "react";
import RolesService from "../../../Services/RolesService";
import { FaTimes } from "react-icons/fa";
import "../Modal.css";

const RoleModal = ({ role, onClose, onSuccess }) => {
  const [form, setForm] = useState({ 
    name: "",
    permissions: []
  });
  const [availablePermissions, setAvailablePermissions] = useState([]);
  const [selectedPermission, setSelectedPermission] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchAvailablePermissions = async () => {
      try {
        const perms = await RolesService.getAllPermissions();
        setAvailablePermissions(Array.isArray(perms) ? perms : []);
      } catch (err) {
        console.error("Erro ao carregar permissões disponíveis:", err);
        setAvailablePermissions([]);
      }
    };

    fetchAvailablePermissions();

    const fetchRolePermissions = async () => {
      if (role) {
        try {
          const rolePerms = await RolesService.getRolePermissions(role.name);
          setForm({ 
            name: role.name || "",
            permissions: Array.isArray(rolePerms) ? rolePerms : []
          });
        } catch (err) {
          console.error("Erro ao carregar permissões da função:", err);
          setForm({ 
            name: role.name || "",
            permissions: []
          });
        }
      }
    };

    fetchRolePermissions();
  }, [role]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleAddPermission = () => {
    if (!selectedPermission || form.permissions.includes(selectedPermission)) return;
    setForm({ ...form, permissions: [...form.permissions, selectedPermission] });
    setSelectedPermission("");
  };

  const handleRemovePermission = (perm) => {
    setForm({ ...form, permissions: form.permissions.filter(p => p !== perm) });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    let currentName = role ? role.name : form.name;
    try {
      if (role && form.name !== role.name) {
        await RolesService.updateRole({ oldName: role.name, newName: form.name });
        currentName = form.name;
      } else if (!role) {
        const createdRole = await RolesService.createRole({ name: form.name });
        currentName = createdRole.name;
      }
      // Assign new permissions
      const originalPerms = await RolesService.getRolePermissions(currentName);
      const newPerms = form.permissions.filter(p => !originalPerms.includes(p));
      if (newPerms.length > 0) {
        await RolesService.assignPermissions(currentName, newPerms);
      }
      // Remove old permissions
      const removedPerms = originalPerms.filter(p => !form.permissions.includes(p));
      if (removedPerms.length > 0) {
        await RolesService.removePermissions(currentName, removedPerms);
      }
      onSuccess(role ? "Função atualizada com sucesso!" : "Função criada com sucesso!");
    } catch (err) {
      console.error('Erro detalhado:', err.response ? err.response.data : err.message);
      alert("Erro ao salvar função: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
      onClose(); // Chamar onClose no finally para garantir refetch
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content role-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">{role ? "Editar Função" : "Nova Função"}</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body">
          <form onSubmit={handleSubmit} className="modal-form">
            <div className="form-grid centered-form">
              <div className="form-group">
                <label>Nome</label>
                <input
                  type="text"
                  name="name"
                  placeholder="Nome da função"
                  value={form.name}
                  onChange={handleChange}
                  className="search-box"
                  required
                />
              </div>
            </div>

            <div className="permissions-management-section">
              <h4>Gerenciar Permissões</h4>
              
              <div className="permissions-controls">
                <div className="form-group">
                  <label>Selecionar Permissão</label>
                  <select 
                    value={selectedPermission} 
                    onChange={(e) => setSelectedPermission(e.target.value)}
                    className="permission-select search-box"
                    disabled={loading}
                  >
                    <option value="">Selecione uma permissão</option>
                    {availablePermissions
                      .filter(perm => !form.permissions.includes(perm))
                      .map(perm => (
                        <option key={perm} value={perm}>{perm}</option>
                      ))
                    }
                  </select>
                </div>
                <button 
                  type="button"
                  className="btn-add-permission btn-small" 
                  onClick={handleAddPermission}
                  disabled={!selectedPermission || loading}
                >
                  {loading ? "..." : "Adicionar"}
                </button>
              </div>

              <div className="role-permissions-list">
                <h5>Permissões Atribuídas:</h5>
                {form.permissions.length > 0 ? (
                  form.permissions.map(perm => (
                    <div key={perm} className="permission-item">
                      <span className="permission-name">{perm}</span>
                      <button 
                        type="button"
                        className="btn-remove-permission btn-small btn-danger"
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

            <div className="center-actions">
              <button type="submit" className="btn-save btn-small" disabled={loading}>
                {loading ? "Salvando..." : (role ? "Atualizar" : "Criar")}
              </button>
              <button type="button" className="btn-cancel btn-small red-cancel" onClick={onClose}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RoleModal;