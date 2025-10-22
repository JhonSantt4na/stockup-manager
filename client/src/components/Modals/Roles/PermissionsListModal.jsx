import React from "react";
import { FaTimes } from "react-icons/fa";
import "../Modal.css";

const PermissionsListModal = ({ role, onClose }) => {
  const getPermissionColor = (perm) => {
    const colors = {
      'READ': '#3b82f6',
      'WRITE': '#f59e0b',
      'DELETE': '#ef4444',
      'UPDATE': '#10b981',
    };
    return colors[perm] || '#8b5cf6';
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content permissions-list-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">Permissões de {role.name}</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body">
          <div className="permissions-list-container">
            <h4>Todas as Permissões Atribuídas</h4>
            <div className="permissions-grid">
              {role.strings && role.strings.length > 0 ? (
                role.strings.map((perm) => (
                  <span 
                    key={perm} 
                    className="permission-tag"
                    style={{ backgroundColor: `${getPermissionColor(perm)}20`, color: getPermissionColor(perm) }}
                  >
                    {perm}
                  </span>
                ))
              ) : (
                <p className="no-permissions">Nenhuma permissão atribuída</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PermissionsListModal;