import React from "react";
import { FaQuestionCircle } from "react-icons/fa";
import CustomModal from "../Custom/CustomModal";
import "./Modal.css";

const ConfirmModal = ({
  isOpen,
  onClose,
  title = "Confirmação",
  message,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  onConfirm,
}) => {
  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title={title}
      showFooter={false}
    >
      <div className="modal-icon">
        <FaQuestionCircle />
      </div>
      <p className="modal-message">{message}</p>

      <div className="modal-actions-inline">
        <button className="btn-manage" onClick={onClose}>
          {cancelText}
        </button>
        <button className="btn-delete" onClick={onConfirm}>
          {confirmText}
        </button>
      </div>
    </CustomModal>
  );
};

export default ConfirmModal;