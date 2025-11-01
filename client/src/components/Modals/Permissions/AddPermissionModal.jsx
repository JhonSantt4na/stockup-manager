import React, { useState } from "react";
import CustomModal from "../../Custom/CustomModal";
import "./Modal.css";

const AddPermissionModal = ({ isOpen, onClose, onSubmit }) => {
  const [form, setForm] = useState({ description: "" });

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(form);
  };

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title="Adicionar Permissão"
      onConfirm={handleSubmit}
      confirmText="Adicionar"
    >
      <form className="modal-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Descrição da Permissão</label>
          <input
            type="text"
            name="description"
            value={form.description}
            onChange={handleChange}
            required
            placeholder="Ex: USER_READ"
          />
        </div>
      </form>
    </CustomModal>
  );
};

export default AddPermissionModal;