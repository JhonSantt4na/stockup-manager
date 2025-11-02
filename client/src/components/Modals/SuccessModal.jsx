import React from "react";
import { FaCheckCircle } from "react-icons/fa";
import CustomModal from "../../components/Custom/CustomModal";
import "./Modal.css";

const SuccessModal = ({ isOpen, onClose, message }) => {
  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title="Sucesso"
      showFooter={false}
    >
      <div className="modal-icon success">
        <FaCheckCircle />
      </div>
      <p className="modal-message success">{message}</p>
    </CustomModal>
  );
};

export default SuccessModal;