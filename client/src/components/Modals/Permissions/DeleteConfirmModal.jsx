// DeleteConfirmModal.jsx
import React from 'react';
import './Modal.css'; // Shared modal CSS

const DeleteConfirmModal = ({ itemName, onClose, onConfirm }) => {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Confirm Deletion</h2>
        <p>Are you sure you want to delete "{itemName}"?</p>
        <div className="modal-actions">
          <button onClick={onClose} className="btn-cancel">Cancel</button>
          <button onClick={onConfirm} className="btn-delete">Delete</button>
        </div>
      </div>
    </div>
  );
};

export default DeleteConfirmModal;