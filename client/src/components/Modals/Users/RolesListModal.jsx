import React from "react";
import CustomModal from "../../Custom/CustomModal";
import "../Modal.css";

const RolesListModal = ({ user, isOpen, onClose }) => {
  if (!user) return null;

  const getRoleColor = (role) => {
    const colors = {
      ADMIN: "#ef4444",
      USER: "#3b82f6", 
      MANAGER: "#f59e0b",
      SUPERVISOR: "#10b981",
      MODERATOR: "#8b5cf6",
      PRO: "#6366f1",
    };
    return colors[role] || "#666";
  };

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title={`Funções de ${user.username}`}
      size="medium"
      showFooter={false}
    >
      <div className="modal-content">
        <div className="roles-list-container">
          <h4>Funções Atribuídas:</h4>
          {user.roles && user.roles.length > 0 ? (
            <div className="roles-grid">
              {user.roles.map((role, index) => (
                <div 
                  key={index} 
                  className="role-item"
                  style={{
                    backgroundColor: `${getRoleColor(role)}20`,
                    color: getRoleColor(role),
                    border: `1px solid ${getRoleColor(role)}`,
                  }}
                >
                  {role}
                </div>
              ))}
            </div>
          ) : (
            <p className="no-roles-message">Nenhuma função atribuída</p>
          )}
        </div>
        
        <div className="modal-actions-center">
          <button className="btn-cancel" onClick={onClose}>
            Fechar
          </button>
        </div>
      </div>
    </CustomModal>
  );
};

export default RolesListModal;