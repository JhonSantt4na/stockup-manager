import React, { useEffect, useState } from "react";
import UserService from "../../../Services/UserService";
import "./ModalUser.css";

const ManageRoleModal = ({ user, onClose }) => {
  const [roles, setRoles] = useState([]);
  const [newRole, setNewRole] = useState("");
  const [selectedRole, setSelectedRole] = useState("");

  useEffect(() => {
    const fetchRoles = async () => {
      try {
        const data = await UserService.getRoles(user.username);
        setRoles(data);
      } catch (error) {
        console.error("Erro ao buscar roles:", error);
      }
    };
    fetchRoles();
  }, [user.username]);

  const handleAddRole = async () => {
    if (!newRole) return;
    try {
      await UserService.assignRole(user.username, newRole);
      setRoles((prev) => [...prev, newRole]);
      setNewRole("");
    } catch (error) {
      console.error("Erro ao adicionar role:", error);
    }
  };

  const handleRemoveRole = async () => {
    if (!selectedRole) return;
    try {
      await UserService.removeRole(user.username, selectedRole);
      setRoles((prev) => prev.filter((r) => r !== selectedRole));
      setSelectedRole("");
    } catch (error) {
      console.error("Erro ao remover role:", error);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-box" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Gerenciar Funções</h3>
          <button className="close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="modal-body">
          <div className="roles-inline">
            <input
              type="text"
              placeholder="Nova função..."
              value={newRole}
              onChange={(e) => setNewRole(e.target.value)}
            />
            <button className="btn-add" onClick={handleAddRole}>
              Adicionar
            </button>
          </div>

          <ul className="roles-list">
            {roles.map((role, idx) => (
              <li
                key={idx}
                className={`role-item ${
                  selectedRole === role ? "selected" : ""
                }`}
                onClick={() =>
                  setSelectedRole((prev) => (prev === role ? "" : role))
                }
              >
                {role}
              </li>
            ))}
          </ul>

          <div className="modal-actions-center">
            <button
              className="btn-cancel"
              onClick={handleRemoveRole}
              disabled={!selectedRole}
            >
              Remover Selecionado
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManageRoleModal;
