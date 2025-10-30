import React from "react";
import { FaExclamationTriangle } from "react-icons/fa";
import CustomModal from "../../Custom/CustomModal";
import "./Modal.css";

const DeleteConfirmModal = ({ isOpen, onClose, itemName, onConfirm }) => {
  const message = `Tem certeza que deseja excluir "${itemName}"? Essa ação não pode ser desfeita.`;

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title="Confirmar Exclusão"
      showFooter={false}
    >
      <div className="modal-icon">
        <FaExclamationTriangle />
      </div>
      <p className="modal-message">{message}</p>

      <div className="modal-actions-inline">
        <button className="btn-manage" onClick={onClose}>
          Cancelar
        </button>
        <button className="btn-delete" onClick={onConfirm}>
          Excluir
        </button>
      </div>
    </CustomModal>
  );
};

export default DeleteConfirmModal;