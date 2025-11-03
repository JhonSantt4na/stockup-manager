import React, { useState } from "react";
import { createPortal } from "react-dom";
import { FaTimes, FaLock } from "react-icons/fa";
import ProfileService from "../../../Services/ProfileService";
import "./ProfileModal.css";

const ChangePasswordModal = ({
  isOpen,
  onClose,
  user
}) => {
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: ""
  });
  const [isLoading, setIsLoading] = useState(false);

  if (!isOpen) return null;

  const handleInputChange = (field, value) => {
    setPasswordData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSavePassword = async () => {
    // Validar senhas
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
        newPassword: passwordData.newPassword
      });
      alert("Senha alterada com sucesso!");
      setPasswordData({
        currentPassword: "",
        newPassword: "",
        confirmPassword: ""
      });
      onClose();
    } catch (error) {
      console.error("Erro ao alterar senha:", error);
      alert(error.response?.data?.message || "Erro ao alterar senha. Verifique a senha atual e tente novamente.");
    } finally {
      setIsLoading(false);
    }
  };

  return createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-container small"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <h3 className="modal-title">Alterar Senha</h3>
          <button className="modal-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>

        <div className="modal-body profile-modal-body-dark">
          <div className="password-form">
            <div className="form-group">
              <label>Senha Atual</label>
              <input
                type="password"
                value={passwordData.currentPassword}
                onChange={(e) => handleInputChange('currentPassword', e.target.value)}
                placeholder="Digite sua senha atual"
                className="form-input"
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label>Nova Senha</label>
              <input
                type="password"
                value={passwordData.newPassword}
                onChange={(e) => handleInputChange('newPassword', e.target.value)}
                placeholder="Digite a nova senha"
                className="form-input"
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label>Confirmar Nova Senha</label>
              <input
                type="password"
                value={passwordData.confirmPassword}
                onChange={(e) => handleInputChange('confirmPassword', e.target.value)}
                placeholder="Confirme a nova senha"
                className="form-input"
                disabled={isLoading}
              />
            </div>
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn-cancel" onClick={onClose} disabled={isLoading}>
            Cancelar
          </button>
          <button className="btn-confirm" onClick={handleSavePassword} disabled={isLoading}>
            {isLoading ? "Salvando..." : "Salvar Senha"}
          </button>
        </div>
      </div>
    </div>,
    document.getElementById('modal-root')
  );
};

export default ChangePasswordModal;