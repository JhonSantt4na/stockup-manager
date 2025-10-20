import React from "react";
import { FaExclamationTriangle, FaTimes } from "react-icons/fa";
import "./Modal.css";

const ConfirmModal = ({ 
  user, 
  onClose, 
  onConfirm, 
  actionType = "ativar" 
}) => {
  const isActivate = actionType === "ativar";
  
  return (
    <div className="modal-backdrop">
      <div className="modal-content confirm-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">Confirmação</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body">
          <div className="confirm-content">
            <div className="confirm-icon">
              <FaExclamationTriangle />
            </div>
            <h4>Deseja realmente {actionType} o usuário?</h4>
            <div className="user-info">
              <p><strong>Usuário:</strong> {user.username}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Status atual:</strong> {user.enabled ? "Ativo" : "Inativo"}</p>
            </div>
            <p className="warning-text">
              Esta ação {isActivate ? "habilitará" : "desabilitará"} o acesso do usuário ao sistema.
            </p>
          </div>
          
          <div className="center-actions">
            <button 
              className={`btn-confirm btn-small ${isActivate ? 'btn-green' : 'btn-red'}`}
              onClick={onConfirm}
            >
              Sim, {actionType}
            </button>
            <button className="btn-cancel btn-small red-cancel" onClick={onClose}>
              Cancelar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;