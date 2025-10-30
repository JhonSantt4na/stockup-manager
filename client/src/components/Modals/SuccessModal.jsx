import React from "react";
import { FaCheckCircle } from "react-icons/fa";
import CustomModal from "../Custom/CustomModal";
import "./Modal.css";

const SuccessModal = ({ isOpen, onClose, message, onConfirm }) => {
  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title="Sucesso!"
      showFooter={false}
    >
      <div className="modal-icon success">
        <FaCheckCircle />
      </div>
      <p className="modal-message">{message}</p>

      <div className="modal-actions-inline">
        <button className="btn-manage" onClick={onConfirm || onClose}>
          OK
        </button>
      </div>
    </CustomModal>
  );
};

export default SuccessModal;