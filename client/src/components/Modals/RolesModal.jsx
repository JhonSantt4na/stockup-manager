import React, { useState, useEffect } from "react";
import axios from "axios";
import { FaTimes } from "react-icons/fa";
import "./Modal.css";

const RolesModal = ({ user, onClose, onSuccess }) => {
  const [userData, setUserData] = useState({ 
    username: "", 
    fullName: "", 
    email: "", 
    password: "",
    confirmPassword: ""
  });
  const [availableRoles, setAvailableRoles] = useState([]);
  const [userRoles, setUserRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState("");
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (user) {
      setUserData({
        username: user.username || "",
        fullName: user.fullName || "",
        email: user.email || "",
        password: "",
        confirmPassword: ""
      });
      
      const fetchUserRoles = async () => {
        try {
          const res = await axios.get(`/users/${user.username}/roles`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          setUserRoles(res.data || []);
        } catch (err) {
          console.error("Erro ao carregar roles:", err);
        }
      };

      const fetchAvailableRoles = async () => {
        try {
          setAvailableRoles(["ADMIN", "USER", "MANAGER", "SUPERVISOR", "MODERATOR"]);
        } catch (err) {
          console.error("Erro ao carregar roles disponíveis:", err);
        }
      };

      fetchUserRoles();
      fetchAvailableRoles();
    }
  }, [user, token]);

  const handleAddRole = async () => {
    if (!selectedRole) return;
    
    setLoading(true);
    try {
      await axios.post(`/users/${user.username}/roles/assign`, 
        { role: selectedRole }, 
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setUserRoles([...userRoles, selectedRole]);
      setSelectedRole("");
      onSuccess(`Role ${selectedRole} adicionada com sucesso!`);
    } catch (err) {
      console.error("Erro ao adicionar role:", err);
      alert("Erro ao adicionar role.");
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveRole = async (roleToRemove) => {
    setLoading(true);
    try {
      await axios.post(`/users/${user.username}/roles/remove`, 
        { role: roleToRemove }, 
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setUserRoles(userRoles.filter(role => role !== roleToRemove));
      onSuccess(`Role ${roleToRemove} removida com sucesso!`);
    } catch (err) {
      console.error("Erro ao remover role:", err);
      alert("Erro ao remover role.");
    } finally {
      setLoading(false);
    }
  };

  const handleUserUpdate = async (e) => {
    e.preventDefault();
    
    if (userData.password && userData.password !== userData.confirmPassword) {
      alert("As senhas não coincidem!");
      return;
    }

    setLoading(true);
    try {
      const updateData = {
        fullName: userData.fullName,
        email: userData.email
      };

      if (userData.password) {
        updateData.password = userData.password;
      }

      await axios.put(`/users/update/${user.username}`, updateData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      onClose();
      onSuccess("Usuário atualizado com sucesso!");
    } catch (err) {
      console.error("Erro ao atualizar usuário:", err);
      alert("Erro ao atualizar usuário.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setUserData({ ...userData, [e.target.name]: e.target.value });
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content roles-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">Gerenciar Usuário - {user.username}</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body">
          {/* Seção de atualização do usuário */}
          <div className="user-update-section">
            <h4>Atualizar Dados</h4>
            <form onSubmit={handleUserUpdate} className="modal-form">
              <div className="form-grid">
                <div className="form-group">
                  <label>Nome Completo</label>
                  <input
                    type="text"
                    name="fullName"
                    placeholder="Nome completo"
                    value={userData.fullName}
                    onChange={handleChange}
                    className="search-box"
                  />
                </div>
                <div className="form-group">
                  <label>Email</label>
                  <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={userData.email}
                    onChange={handleChange}
                    className="search-box"
                  />
                </div>
                <div className="form-group">
                  <label>Nova Senha</label>
                  <input
                    type="password"
                    name="password"
                    placeholder="Nova senha (opcional)"
                    value={userData.password}
                    onChange={handleChange}
                    className="search-box"
                  />
                </div>
                <div className="form-group">
                  <label>Confirmar Senha</label>
                  <input
                    type="password"
                    name="confirmPassword"
                    placeholder="Confirmar senha"
                    value={userData.confirmPassword}
                    onChange={handleChange}
                    className="search-box"
                  />
                </div>
              </div>
              <div className="center-actions">
                <button type="submit" className="btn-save btn-small" disabled={loading}>
                  {loading ? "Salvando..." : "Atualizar"}
                </button>
              </div>
            </form>
          </div>

          {/* Seção de gerenciamento de roles */}
          <div className="roles-management-section">
            <h4>Gerenciar Funções</h4>
            
            <div className="roles-controls">
              <div className="form-group">
                <label>Selecionar Função</label>
                <select 
                  value={selectedRole} 
                  onChange={(e) => setSelectedRole(e.target.value)}
                  className="role-select search-box"
                  disabled={loading}
                >
                  <option value="">Selecione uma função</option>
                  {availableRoles
                    .filter(role => !userRoles.includes(role))
                    .map(role => (
                      <option key={role} value={role}>{role}</option>
                    ))
                  }
                </select>
              </div>
              <button 
                className="btn-add-role btn-small" 
                onClick={handleAddRole}
                disabled={!selectedRole || loading}
              >
                {loading ? "..." : "Adicionar"}
              </button>
            </div>

            <div className="user-roles-list">
              <h5>Funções Atribuídas:</h5>
              {userRoles.length > 0 ? (
                userRoles.map(role => (
                  <div key={role} className="role-item">
                    <span className="role-name">{role}</span>
                    <button 
                      className="btn-remove-role btn-small btn-danger"
                      onClick={() => handleRemoveRole(role)}
                      disabled={loading}
                    >
                      Remover
                    </button>
                  </div>
                ))
              ) : (
                <p className="no-roles">Nenhuma função atribuída</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RolesModal;