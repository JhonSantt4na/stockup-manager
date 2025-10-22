// EditPermissionModal.jsx
import React, { useState, useEffect } from 'react';
import './Modal.css'; // Shared modal CSS

const EditPermissionModal = ({ permission, onClose, onSubmit }) => {
  const [oldName, setOldName] = useState('');
  const [newName, setNewName] = useState('');
  const [newDescription, setNewDescription] = useState('');
  const [enabled, setEnabled] = useState(true);

  useEffect(() => {
    if (permission) {
      setOldName(permission.name);
      setNewName(permission.name);
      setNewDescription(permission.description);
      setEnabled(permission.enabled);
    }
  }, [permission]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ oldName, newName, newDescription, enabled });
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Edit Permission</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Old Name</label>
            <input type="text" value={oldName} readOnly />
          </div>
          <div className="form-group">
            <label>New Name</label>
            <input
              type="text"
              value={newName}
              onChange={(e) => setNewName(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>New Description</label>
            <textarea
              value={newDescription}
              onChange={(e) => setNewDescription(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Enabled</label>
            <input
              type="checkbox"
              checked={enabled}
              onChange={(e) => setEnabled(e.target.checked)}
            />
          </div>
          <div className="modal-actions">
            <button type="button" onClick={onClose} className="btn-cancel">Cancel</button>
            <button type="submit" className="btn-submit">Update</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditPermissionModal;