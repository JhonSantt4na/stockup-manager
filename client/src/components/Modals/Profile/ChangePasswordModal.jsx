import React, { useState, useRef, useEffect } from "react";
import { createPortal } from "react-dom";
import { FaTimes, FaLock, FaCheckCircle, FaExclamationCircle } from "react-icons/fa";
import ProfileService from "../../../Services/ProfileService";
import "./ProfileModal.css";

const ChangePasswordModal = ({ isOpen, onClose }) => {
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [status, setStatus] = useState(null); // "success" | "error" | null
  const [message, setMessage] = useState("");

  // tempos em ms
  const SUCCESS_DURATION = 2200;
  const ERROR_DURATION = 3000;

  // refs para limpar timeouts ao desmontar/comportamentos
  const timeoutRef = useRef(null);

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  if (!isOpen) return null;

  const handleChange = (field, value) =>
    setPasswordData((prev) => ({ ...prev, [field]: value }));

  const resetStatusAndKeepForm = () => {
    // volta para o formulário mantendo o conteúdo dos campos (usuário pode corrigir)
    setStatus(null);
    setMessage("");
    setIsLoading(false);
  };

  const handleSavePassword = async () => {
    // validações locais
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setStatus("error");
      setMessage("As senhas não coincidem!");
      // voltar para o formulário depois de ERROR_DURATION
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
      timeoutRef.current = setTimeout(() => {
        resetStatusAndKeepForm();
      }, ERROR_DURATION);
      return;
    }

    if (passwordData.newPassword.length < 6) {
      setStatus("error");
      setMessage("A senha deve ter pelo menos 6 caracteres!");
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
      timeoutRef.current = setTimeout(() => {
        resetStatusAndKeepForm();
      }, ERROR_DURATION);
      return;
    }

    setIsLoading(true);
    setStatus(null);
    setMessage("");

    try {
      await ProfileService.changePassword({
        currentPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
      });

      setStatus("success");
      setMessage("Senha alterada com sucesso!");

      // limpa campos de senha (opcional, melhora UX)
      setPasswordData({ currentPassword: "", newPassword: "", confirmPassword: "" });

      // fecha após SUCCESS_DURATION
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
      timeoutRef.current = setTimeout(() => {
        setIsLoading(false);
        setStatus(null);
        setMessage("");
        onClose();
      }, SUCCESS_DURATION);
    } catch (error) {
      console.error("Erro ao alterar senha:", error);
      const serverMsg =
        error?.response?.data?.message ||
        error?.message ||
        "Erro ao alterar senha. Verifique e tente novamente.";

      setStatus("error");
      setMessage(serverMsg);

      // habilita inputs e volta ao formulário após ERROR_DURATION
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
      timeoutRef.current = setTimeout(() => {
        resetStatusAndKeepForm();
      }, ERROR_DURATION);
    }
  };

  return createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-container small password-modal"
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
        aria-label="Alterar senha"
      >
        <div className="modal-header">
          <h3 className="modal-title">Alterar Senha</h3>
          <button className="modal-close" onClick={onClose} aria-label="Fechar">
            <FaTimes />
          </button>
        </div>

        <div className="modal-body password-body">
          {/* Mensagem de sucesso */}
          {status === "success" && (
            <div className="modal-message success" aria-live="polite">
              <FaCheckCircle className="modal-icon success" />
              <div>{message}</div>
            </div>
          )}

          {/* Mensagem de erro */}
          {status === "error" && (
            <div className="modal-message error" aria-live="assertive">
              <FaExclamationCircle className="modal-icon" />
              <div>{message}</div>
            </div>
          )}

          {/* Formulário (apenas quando não está mostrando status) */}
          {!status && (
            <>
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
                  disabled={isLoading}
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
                  disabled={isLoading}
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
                  disabled={isLoading}
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
            </>
          )}
        </div>

        {/* Footer com progress bar apenas em sucesso/erro */}
        {(status === "success" || status === "error") && (
          <div className="modal-footer">
            <div className="modal-footer-loading" aria-hidden="true">
              <div
                className="modal-footer-loading-bar"
                style={{
                  animationDuration: `${status === "success" ? SUCCESS_DURATION : ERROR_DURATION}ms`,
                }}
              />
            </div>
          </div>
        )}
      </div>
    </div>,
    document.getElementById("modal-root")
  );
};

export default ChangePasswordModal;