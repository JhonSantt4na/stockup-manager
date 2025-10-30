import React, { useEffect, useState } from "react";
import CustomModal from "../../Custom/CustomModal";
import "./Modal.css";

const EditPermissionModal = ({ isOpen, onClose, permission, onSubmit }) => {
  const [form, setForm] = useState({
    oldDescription: "",
    newDescription: "",
    enabled: true,
  });

  useEffect(() => {
    if (permission) {
      setForm({
        oldDescription: permission.description || '',
        newDescription: permission.description || '',
        enabled: permission.enabled ?? true,
      });
    }
  }, [permission]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleCheckbox = (e) =>
    setForm({ ...form, enabled: e.target.checked });

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(form);
  };

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title="Editar Permissão"
      onConfirm={handleSubmit}
      confirmText="Salvar Alterações"
    >
      <form className="modal-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Descrição Atual</label>
          <input type="text" value={form.oldDescription} readOnly />
        </div>

        <div className="form-group">
          <label>Nova Descrição</label>
          <input
            type="text"
            name="newDescription"
            value={form.newDescription}
            onChange={handleChange}
            required
          />
        </div>

        <div className="checkbox-group">
          <input
            type="checkbox"
            checked={form.enabled}
            onChange={handleCheckbox}
          />
          <label>Permissão Ativa</label>
        </div>
      </form>
    </CustomModal>
  );
};

export default EditPermissionModal;