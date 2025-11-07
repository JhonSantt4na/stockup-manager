import React, { useEffect } from "react";
import CustomModal from "../Custom/CustomModal";
import { FaExclamationTriangle } from "react-icons/fa";
import "./Modal.css";

const ErroModal = ({
  isOpen,
  onClose,
  title = "Erro",
  message,
  size = "medium",
  autoCloseAfter = 5000,
}) => {
  useEffect(() => {
    if (isOpen && autoCloseAfter > 0) {
      const timer = setTimeout(() => {
        onClose();
      }, autoCloseAfter);
      return () => clearTimeout(timer);
    }
  }, [isOpen, onClose, autoCloseAfter]);

  const errorContent = (
    <div className="error-content">
      <FaExclamationTriangle className="error-icon" />
      <p className="error-message">{message}</p>
    </div>
  );

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title={title}
      size={size}
      showFooter={true}
      footerContent={
        <div className="modal-footer-loading">
          <div
            className="modal-footer-loading-bar"
            style={{ animationDuration: `${autoCloseAfter}ms` }}
          ></div>
        </div>
      }
    >
      {errorContent}
    </CustomModal>
  );
};

export default ErroModal;
