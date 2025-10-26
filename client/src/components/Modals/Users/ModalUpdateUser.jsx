import React, { useState } from "react";
import ManageRoleModal from "./ModalManageRoles";
import "./ModalUser.css";

const ModalUpdateUser = ({ user, onClose, onUpdate }) => {
  const [formData, setFormData] = useState({
    name: user?.name || "",
    email: user?.email || "",
  });
  const [showRolesModal, setShowRolesModal] = useState(false);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSave = () => {
    onUpdate(formData);
  };

  return (
    <>
      <div className="modal-overlay" onClick={onClose}>
        <div className="modal-box" onClick={(e) => e.stopPropagation()}>
          <div className="modal-header">
            <h3>Atualizar Dados</h3>
            <button className="close-btn" onClick={onClose}>
              ✕
            </button>
          </div>

          <div className="modal-body">
            <div className="form-grid">
              <div className="form-group">
                <label>Nome</label>
                <input
                  type="text"
                  name="name"
                  placeholder="Digite o nome completo"
                  value={formData.name}
                  onChange={handleChange}
                />
              </div>

              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  name="email"
                  placeholder="Digite o email"
                  value={formData.email}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="modal-actions-center">
              <button className="btn-save" onClick={handleSave}>
                Atualizar
              </button>
              <button
                className="btn-secondary"
                onClick={() => setShowRolesModal(true)}
              >
                Funções
              </button>
            </div>
          </div>
        </div>
      </div>

      {showRolesModal && (
        <ManageRoleModal user={user} onClose={() => setShowRolesModal(false)} />
      )}
    </>
  );
};

export default ModalUpdateUser;