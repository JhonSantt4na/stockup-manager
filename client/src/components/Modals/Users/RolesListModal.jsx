import React from "react";
import { FaTimes } from "react-icons/fa";
import "../Modal.css";

const RolesListModal = ({ user, onClose }) => {
  const getRoleColor = (role) => {
    const colors = {
      'ADMIN': '#ef4444',
      'USER': '#3b82f6', 
      'MANAGER': '#f59e0b',
      'SUPERVISOR': '#10b981',
      'MODERATOR': '#8b5cf6'
    };
    return colors[role] || '#666';
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content roles-list-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">Funções de {user.username}</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body">
          <div className="roles-list-container">
            <h4>Todas as Funções Atribuídas</h4>
            <div className="roles-grid">
              {user.roles && user.roles.length > 0 ? (
                user.roles.map((role) => (
                  <span 
                    key={role} 
                    className="role-tag"
                    style={{ backgroundColor: `${getRoleColor(role)}20`, color: getRoleColor(role) }}
                  >
                    {role}
                  </span>
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

export default RolesListModal;