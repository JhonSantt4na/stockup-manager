import React from "react";
import { FaExclamationTriangle } from "react-icons/fa";
import CustomModal from "../../components/Custom/CustomModal";
import "./Modal.css";

const ConfirmModal = ({ isOpen, onClose, item, itemType, actionType = 'deletar', onConfirm }) => {
  const message = `Tem certeza que deseja ${actionType} "${item?.name || item?.description || 'o item'}"?`;

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title={`${actionType.charAt(0).toUpperCase() + actionType.slice(1)} ?`}
      showFooter={false}
    >
      <div className="modal-icon">
        <FaExclamationTriangle />
      </div>
      <p className="modal-message">{message}
        <br/> Essa ação não pode ser desfeita.
      </p>

      <div className="modal-actions-inline">
        <button className="btn-cancel" onClick={onClose}>
          Cancelar
        </button>
        <button className="btn-confirm" onClick={onConfirm}>
          {actionType.charAt(0).toUpperCase() + actionType.slice(1)}
        </button>
      </div>
    </CustomModal>
  );
};

export default ConfirmModal;