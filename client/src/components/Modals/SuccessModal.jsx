import React from "react";
import { FaCheckCircle } from "react-icons/fa";
import "./Modal.css";

const SuccessModal = ({ message, onClose }) => {
  return (
    <div className="modal-backdrop">
      <div className="modal-content success-modal">
        <div className="success-icon">
          <FaCheckCircle />
        </div>
        <h3>Sucesso!</h3>
        <p>{message}</p>
        <button className="btn-ok btn-small" onClick={onClose}>
          OK
        </button>
      </div>
    </div>
  );
};

export default SuccessModal;