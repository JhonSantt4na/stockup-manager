import React from 'react';
import { FaTimes } from 'react-icons/fa';
import './CustomModal.css';

const CustomModal = ({
  isOpen,
  onClose,
  title,
  children,
  size = 'medium', // 'small', 'medium', 'large'
  showFooter = true,
  onCancel,
  onConfirm,
  cancelText = 'Cancelar',
  confirmText = 'Confirmar',
  footerContent
}) => {
  if (!isOpen) return null;

  return (
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
        <div className="modal-body">
          {children}
        </div>
        {showFooter && (
          <div className="modal-footer">
            {footerContent ? (
              footerContent
            ) : (
              <>
                <button className="btn-manage" onClick={onCancel || onClose}>
                  {cancelText}
                </button>
                {onConfirm && (
                  <button className="btn-add" onClick={onConfirm}>
                    {confirmText}
                  </button>
                )}
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default CustomModal;