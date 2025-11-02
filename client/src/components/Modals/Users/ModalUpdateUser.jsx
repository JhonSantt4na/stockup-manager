import React, { useEffect, useState } from "react";
import UserService from "../../../Services/UserService";
import CustomModal from "../../Custom/CustomModal";
import "../Modal.css";

const ModalUpdateUser = ({ user, onClose, onSuccess }) => {
  const [showPassword, setShowPassword] = useState(false);
  const [form, setForm] = useState({
    username: "",
    fullName: "",
    email: "",
    password: "",
    roles: [],
  });

  const [availableRoles, setAvailableRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState("");
  const [loading, setLoading] = useState(false);
  const [initialized, setInitialized] = useState(false);
  const [rolesLoading, setRolesLoading] = useState(false);

  // Inicializar form quando o user prop mudar
  useEffect(() => {
    if (user && !initialized) {
      console.log("🔄 Initializing form with user:", user);
      const userRoles = user.roles?.map((r) => r.replace(/^ROLE_/, "")) || [];
      console.log("📋 User roles after processing:", userRoles);
      
      setForm({
        username: user.username || "",
        fullName: user.fullName || user.name || "",
        email: user.email || "",
        password: "",
        roles: userRoles,
      });
      setInitialized(true);
    }
  }, [user, initialized]);

  // Buscar todas as roles disponíveis do sistema
  useEffect(() => {
    const fetchAllRoles = async () => {
      if (!initialized || !user) return;
      
      setRolesLoading(true);
      try {
        console.log("🔍 Fetching ALL roles from system...");
        
        const allSystemRoles = await UserService.getAllRoles();
        console.log("📦 All system roles from backend:", allSystemRoles);
        
        if (!allSystemRoles || allSystemRoles.length === 0) {
          console.warn("⚠️ No roles returned from backend");
          setAvailableRoles([]);
          return;
        }

        const userCurrentRoles = form.roles || [];
        console.log("👤 User current roles for filtering:", userCurrentRoles);
        
        const available = allSystemRoles.filter(systemRole => 
          !userCurrentRoles.includes(systemRole)
        );
        
        console.log("✅ Available roles after filtering:", available);
        setAvailableRoles(available);
        
      } catch (err) {
        console.error("❌ Erro ao buscar todas as roles do sistema:", err);
        setAvailableRoles([]);
      } finally {
        setRolesLoading(false);
      }
    };

    fetchAllRoles();
  }, [form.roles, initialized, user]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleAddRole = () => {
    if (!selectedRole || form.roles.includes(selectedRole)) return;
    const newRoles = [...form.roles, selectedRole];
    console.log("➕ Adding role:", selectedRole, "New roles:", newRoles);
    setForm({ ...form, roles: newRoles });
    setSelectedRole("");
  };

  const handleRemoveRole = (role) => {
    const newRoles = form.roles.filter((r) => r !== role);
    console.log("➖ Removing role:", role, "New roles:", newRoles);
    setForm({ ...form, roles: newRoles });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      console.log("🚀 Iniciando atualização do usuário...");
      
      // Tente primeiro sem o prefixo ROLE_
      let rolesToSend = form.roles;
      console.log("📤 Roles to send to backend (sem prefixo):", rolesToSend);
      
      const updateData = {
        fullName: form.fullName,
        email: form.email,
        roles: rolesToSend,
      };

      // Só inclui password se foi preenchido
      if (form.password && form.password.trim() !== "") {
        updateData.password = form.password;
        console.log("🔑 Password will be updated");
      }

      console.log("📦 Final update data being sent:", updateData);
      console.log("🔄 Chamando UserService.updateUserAsAdmin...");
      
      const updatedUser = await UserService.updateUserAsAdmin(form.username, updateData);
      console.log("✅ User updated successfully, response:", updatedUser);
      
      // Se as roles não foram atualizadas, tente com o prefixo ROLE_
      if (!updatedUser.roles || updatedUser.roles.length === 0) {
        console.log("🔄 Tentando novamente com prefixo ROLE_...");
        rolesToSend = form.roles.map((r) => `ROLE_${r}`);
        updateData.roles = rolesToSend;
        
        console.log("📤 Roles to send to backend (com prefixo):", rolesToSend);
        const updatedUserWithPrefix = await UserService.updateUserAsAdmin(form.username, updateData);
        console.log("✅ User updated with prefix, response:", updatedUserWithPrefix);
      }
      
      // Chama onSuccess se for uma função
      if (typeof onSuccess === 'function') {
        onSuccess("Usuário atualizado com sucesso!");
      }
      
      console.log("🎉 Atualização concluída com sucesso!");
      onClose();
    } catch (err) {
      console.error("❌ Erro ao atualizar usuário:", err);
      console.error("🔍 Error details:", err.response?.data);
      console.error("🔍 Error status:", err.response?.status);
      console.error("🔍 Error message:", err.message);
      
      let errorMessage = "Erro ao atualizar usuário: ";
      if (err.response?.data?.message) {
        errorMessage += err.response.data.message;
      } else if (err.message) {
        errorMessage += err.message;
      } else {
        errorMessage += "Erro desconhecido";
      }
      
      alert(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  if (!user) {
    return null;
  }

  return (
    <CustomModal
      isOpen={true}
      onClose={onClose}
      title={`Editar Usuário: ${form.username}`}
      size="medium"
      onConfirm={handleSubmit}
      confirmText={loading ? "Salvando..." : "Atualizar"}
      cancelText="Cancelar"
      onCancel={onClose}
    >
      <form className="modal-form" onSubmit={handleSubmit}>
        <div className="form-grid centered-form">
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={form.username}
              disabled
              className="search-boxU"
            />
          </div>

          <div className="form-group">
            <label>Nome Completo</label>
            <input
              type="text"
              name="fullName"
              value={form.fullName}
              onChange={handleChange}
              required
              className="search-boxU"
              placeholder="Nome completo do usuário"
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
              className="search-boxU"
            />
          </div>

          <div className="form-group">
            <label>Senha</label>
            <div className="password-input-container">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Nova senha (opcional)"
                className="search-boxU"
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label="Alternar visibilidade da senha"
              >
                {showPassword ? "Ocultar" : "Ver"}
              </button>
            </div>
          </div>
        </div>

        <div className="permissions-management-section">
          <h4>Gerenciar Funções</h4>
          
          {rolesLoading ? (
            <p>Carregando funções disponíveis...</p>
          ) : (
            <>
              <div className="permissions-controls">
                <select
                  value={selectedRole}
                  onChange={(e) => setSelectedRole(e.target.value)}
                  disabled={loading || availableRoles.length === 0}
                  className="permission-select search-box"
                >
                  <option value="">
                    {availableRoles.length === 0 ? 
                      "Nenhuma função disponível" : 
                      "Selecione uma função"}
                  </option>
                  {availableRoles.map((role) => (
                    <option key={role} value={role}>
                      {role}
                    </option>
                  ))}
                </select>

                <button
                  type="button"
                  className="btn-manage"
                  onClick={handleAddRole}
                  disabled={!selectedRole || loading}
                >
                  Adicionar
                </button>
              </div>

              <div className="role-permissions-list">
                <h5>Funções Atribuídas ({form.roles.length}):</h5>
                {form.roles.length > 0 ? (
                  form.roles.map((role) => (
                    <div key={role} className="permission-item">
                      <span className="permission-name">{role}</span>
                      <button
                        type="button"
                        className="btn-delete"
                        onClick={() => handleRemoveRole(role)}
                        disabled={loading}
                      >
                        Remover
                      </button>
                    </div>
                  ))
                ) : (
                  <p className="no-permissions">Nenhuma função atribuída</p>
                )}
              </div>
            </>
          )}
        </div>
      </form>
    </CustomModal>
  );
};

export default ModalUpdateUser;