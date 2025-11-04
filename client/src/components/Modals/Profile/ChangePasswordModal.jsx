import React, { useState } from "react";
import { createPortal } from "react-dom";
import { FaTimes, FaLock } from "react-icons/fa";
import ProfileService from "../../../Services/ProfileService";
import "./ProfileModal.css";

const ChangePasswordModal = ({ isOpen, onClose }) => {
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [isLoading, setIsLoading] = useState(false);

  if (!isOpen) return null;

  const handleChange = (field, value) =>
    setPasswordData((prev) => ({ ...prev, [field]: value }));

  const handleSavePassword = async () => {
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      alert("As senhas não coincidem!");
      return;
    }

    if (passwordData.newPassword.length < 6) {
      alert("A senha deve ter pelo menos 6 caracteres!");
      return;
    }

    setIsLoading(true);
    try {
      await ProfileService.changePassword({
        currentPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
      });
      alert("Senha alterada com sucesso!");
      onClose();
    } catch (error) {
      alert(
        error.response?.data?.message ||
          "Erro ao alterar senha. Verifique e tente novamente."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-container small password-modal"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <h3 className="modal-title">Alterar Senha</h3>
          <button className="modal-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>

        <div className="modal-body password-body">
          <div className="form-group">
            <label>
              <FaLock /> Senha Atual
            </label>
            <input
              type="password"
              value={passwordData.currentPassword}
              onChange={(e) =>
                handleChange("currentPassword", e.target.value)
              }
              className="form-input"
              placeholder="Digite sua senha atual"
            />
          </div>

          <div className="form-group">
            <label>
              <FaLock /> Nova Senha
            </label>
            <input
              type="password"
              value={passwordData.newPassword}
              onChange={(e) => handleChange("newPassword", e.target.value)}
              className="form-input"
              placeholder="Digite a nova senha"
            />
          </div>

          <div className="form-group">
            <label>
              <FaLock /> Confirmar Nova Senha
            </label>
            <input
              type="password"
              value={passwordData.confirmPassword}
              onChange={(e) =>
                handleChange("confirmPassword", e.target.value)
              }
              className="form-input"
              placeholder="Confirme a nova senha"
            />
          </div>

          <div className="single-action-btn">
            <button
              className="btn-confirm"
              onClick={handleSavePassword}
              disabled={isLoading}
            >
              {isLoading ? "Salvando..." : "Confirmar"}
            </button>
          </div>
        </div>
      </div>
    </div>,
    document.getElementById("modal-root")
  );
};

export default ChangePasswordModal;
