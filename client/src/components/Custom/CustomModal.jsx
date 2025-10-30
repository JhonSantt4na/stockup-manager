import React from "react";
import { createPortal } from "react-dom";
import { FaTimes } from "react-icons/fa";
import "./CustomModal.css";

const CustomModal = ({
  isOpen,
  onClose,
  title,
  children,
  size = "medium", // 'small', 'medium', 'large'
  showFooter = true,
  onCancel,
  onConfirm,
  cancelText = "Cancelar",
  confirmText = "Confirmar",
  footerContent,
}) => {
  if (!isOpen) return null;

  console.log(`Modal "${title}" está aberto:`, isOpen); // Depuração

  return createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div
        className={`modal-container ${size}`}
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <h3 className="modal-title">{title}</h3>
          <button className="modal-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>

        <div className="modal-body">{children}</div>

        {showFooter && (
          <div className="modal-footer">
            {footerContent ? (
              footerContent
            ) : (
              <>
                <button className="btn-cancel" onClick={onCancel || onClose}>
                  {cancelText}
                </button>
                {onConfirm && (
                  <button className="btn-confirm" onClick={onConfirm}>
                    {confirmText}
                  </button>
                )}
              </>
            )}
          </div>
        )}
      </div>
    </div>,
    document.getElementById('modal-root') // Use o container dedicado
  );
};

export default CustomModal;