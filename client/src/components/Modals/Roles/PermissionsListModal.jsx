import React from "react";
import CustomModal from "../../Custom/CustomModal";
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
    <CustomModal
      isOpen={true}
      onClose={onClose}
      title={`Permissões de ${role.name}`}
      size="medium"
      showFooter={false}
    >
      <div className="permissions-list-container">
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
    </CustomModal>
  );
};

export default PermissionsListModal;