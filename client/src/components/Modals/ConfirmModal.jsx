import React from "react";
import { FaTimes, FaExclamationTriangle } from "react-icons/fa";
import "./Modal.css";

const ConfirmModal = ({ item, itemType, actionType, onClose, onConfirm }) => {
  const getItemName = () => {
    if (!item) return "item desconhecido";
    if (itemType === "role") {
      return item.name || "função desconhecida";
    }
    return item.username || "usuário desconhecido";
  };

  const getTitle = () => {
    if (itemType === "role") {
      return `Confirmar ${actionType} da função?`;
    }
    return `Confirmar ${actionType} do usuário?`;
  };

  const getMessage = () => {
    if (itemType === "role") {
      return `Você tem certeza que deseja ${actionType} a função "${getItemName()}"? Esta ação pode ser irreversível.`;
    }
    return `Você tem certeza que deseja ${actionType} o usuário "${getItemName()}"? Esta ação pode ser irreversível.`;
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content confirm-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">{getTitle()}</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body">
          <div className="confirm-content">
            <div className="confirm-icon">
              <FaExclamationTriangle />
            </div>
            <h4>{getMessage()}</h4>
            <div className="center-actions">
              <button className="btn-confirm btn-small btn-red" onClick={onConfirm}>
                Confirmar
              </button>
              <button className="btn-cancel btn-small" onClick={onClose}>
                Cancelar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;