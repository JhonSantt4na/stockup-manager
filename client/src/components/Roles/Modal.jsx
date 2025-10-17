import React from "react";

const Modal = ({ isOpen, onClose, title, children, footer, className = "" }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className={`modal ${className}`}>
        <div className="modal-header">
          <h3>{title}</h3>
          <button className="modal-close" onClick={onClose}>
            ×
          </button>
        </div>
        <div className="modal-body">{children}</div>
        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>
  );
};

export default Modal;