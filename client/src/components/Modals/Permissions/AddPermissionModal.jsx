// AddPermissionModal.jsx
import React, { useState } from 'react';
import './Modal.css'; // Shared modal CSS

const AddPermissionModal = ({ onClose, onSubmit }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ name, description });
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Add New Permission</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>
          <div className="modal-actions">
            <button type="button" onClick={onClose} className="btn-cancel">Cancel</button>
            <button type="submit" className="btn-submit">Add</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddPermissionModal;