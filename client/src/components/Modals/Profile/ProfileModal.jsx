import React, { useState, useRef, useEffect } from "react";
import { createPortal } from "react-dom";
import {
  FaTimes,
  FaUserCircle,
  FaEnvelope,
  FaLock,
  FaEdit,
  FaCamera,
} from "react-icons/fa";
import ChangePasswordModal from "./ChangePasswordModal";
import ProfileService from "../../../Services/ProfileService";
import "./ProfileModal.css";

const ProfileModal = ({ isOpen, onClose, user = null, size = "medium" }) => {
  const [profileData, setProfileData] = useState(null);
  const [editingField, setEditingField] = useState(null);
  const [formData, setFormData] = useState({
    username: "",
    fullName: "",
    email: "",
  });
  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isFetching, setIsFetching] = useState(false);
  const fileInputRef = useRef(null);

  // Sempre busca fresh data ao abrir o modal (garante que não usemos dados stale)
  useEffect(() => {
    const fetchProfileData = async () => {
      if (!isOpen) return;
      setIsFetching(true);
      try {
        const data = await ProfileService.getProfile();
        // se o backend retornar o DTO corretamente, popula tudo
        setProfileData(data || null);
        setFormData({
          username: data?.username || "",
          fullName: data?.fullName || "",
          email: data?.email || "",
        });
      } catch (error) {
        console.error("Erro ao buscar dados do perfil:", error);
        // fallback para o user vindo do contexto (se tiver)
        if (user) {
          setProfileData(user);
          setFormData({
            username: user?.username || "",
            fullName: user?.fullName || "",
            email: user?.email || "",
          });
        } else {
          // limpa para evitar undefined
          setProfileData(null);
          setFormData({ username: "", fullName: "", email: "" });
        }
      } finally {
        setIsFetching(false);
      }
    };

    fetchProfileData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen]); // deliberadamente só isOpen

  if (!isOpen) return null;

  const handleEditClick = (field) => setEditingField(field);

  const handleSave = async (field) => {
    setIsLoading(true);
    try {
      const updateData = { [field]: formData[field] };
      await ProfileService.updateProfile(updateData);

      setProfileData((prev) => ({ ...(prev || {}), [field]: formData[field] }));
      setEditingField(null);
    } catch (error) {
      console.error(`Erro ao atualizar ${field}:`, error);
      alert(error?.message || `Erro ao atualizar ${field}. Tente novamente.`);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    setEditingField(null);
    setFormData({
      username: profileData?.username || "",
      fullName: profileData?.fullName || "",
      email: profileData?.email || "",
    });
  };

  const handleInputChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleImageChange = async (event) => {
    const file = event.target.files?.[0];
    if (!file) return;
    setIsLoading(true);
    try {
      const result = await ProfileService.uploadProfilePhoto(file);
      setProfileData((prev) => ({ ...(prev || {}), profilePhotoUrl: result.profilePhotoUrl }));
      alert("Imagem atualizada com sucesso!");
    } catch (error) {
      console.error("Erro ao atualizar imagem:", error);
      alert(error?.message || "Erro ao atualizar imagem. Tente novamente.");
    } finally {
      setIsLoading(false);
    }
  };

  const triggerFileInput = () => fileInputRef.current?.click();

  // displayData é a fonte de verdade (profileData priorizado)
  const displayData = profileData || user || {};

  return createPortal(
    <>
      <div className="modal-overlay" onClick={onClose}>
        <div className={`modal-container ${size}`} onClick={(e) => e.stopPropagation()}>
          <div className="modal-header">
            <h3 className="modal-title">Perfil do Usuário</h3>
            <button className="modal-close" onClick={onClose} aria-label="Fechar">
              <FaTimes />
            </button>
          </div>

          <div className="modal-body profile-modal-body-dark">
            {isFetching ? (
              <div className="profile-loading"><p>Carregando dados do perfil...</p></div>
            ) : (
              <>
                <div className="profile-user-info">
                  <div className="profile-avatar-container">
                    <img
                      src={displayData?.profilePhotoUrl || "/images/default-avatar.png"}
                      alt="Foto de perfil"
                      className="profile-avatar-img"
                      onError={(e) => (e.target.src = "/images/default-avatar.png")}
                    />
                    <button
                      className="profile-change-image-btn-modern"
                      onClick={triggerFileInput}
                      title="Alterar imagem"
                      disabled={isLoading}
                      aria-label="Alterar imagem"
                    >
                      <FaCamera />
                    </button>
                    <input
                      type="file"
                      ref={fileInputRef}
                      onChange={handleImageChange}
                      accept="image/*"
                      style={{ display: "none" }}
                    />
                  </div>

                  <h4 className="profile-user-name">
                    {displayData?.fullName || displayData?.username || "Usuário"}
                  </h4>
                  <p className="profile-role">{displayData?.roles?.join(", ") || "Sem funções"}</p>
                </div>

                <div className="profile-details-inline">
                  {["username", "fullName", "email"].map((field) => (
                    <div className="profile-detail-item-inline border-left-green" key={field}>
                      <div className="profile-field-group">
                        {field === "email" ? (
                          <FaEnvelope className="profile-detail-icon" />
                        ) : (
                          <FaUserCircle className="profile-detail-icon" />
                        )}

                        <div className="profile-field-content">
                          <span className="profile-field-label">
                            {field === "username" ? "Username:" : field === "fullName" ? "Nome completo:" : "Email:"}
                          </span>

                          {editingField === field ? (
                            <div className="profile-edit-inline">
                              <input
                                type={field === "email" ? "email" : "text"}
                                value={formData[field]}
                                onChange={(e) => handleInputChange(field, e.target.value)}
                                className="profile-edit-input-dark"
                                autoFocus
                                disabled={isLoading}
                              />
                              <button className="profile-save-btn" onClick={() => handleSave(field)} disabled={isLoading}>
                                {isLoading ? "..." : "Salvar"}
                              </button>
                              <button className="profile-cancel-btn" onClick={handleCancel} disabled={isLoading}>
                                Cancelar
                              </button>
                            </div>
                          ) : (
                            <span className="profile-field-value">
                              {formData[field]?.trim() !== "" ? formData[field] : "Não definido"}
                            </span>
                          )}
                        </div>
                      </div>

                      {editingField !== field && (
                        <button className="profile-edit-btn" onClick={() => handleEditClick(field)} disabled={isLoading}>
                          <FaEdit />
                        </button>
                      )}
                    </div>
                  ))}

                  {/* Senha */}
                  <div className="profile-detail-item-inline border-left-green">
                    <div className="profile-field-group">
                      <FaLock className="profile-detail-icon" />
                      <div className="profile-field-content">
                        <span className="profile-field-label">Senha:</span>
                        <span className="profile-field-value">••••••••</span>
                      </div>
                    </div>
                    <button className="profile-edit-btn" onClick={() => setIsPasswordModalOpen(true)}>
                      <FaEdit />
                    </button>
                  </div>
                </div>
              </>
            )}
          </div>
        </div>
      </div>

      <ChangePasswordModal isOpen={isPasswordModalOpen} onClose={() => setIsPasswordModalOpen(false)} />
    </>,
    document.getElementById("modal-root")
  );
};

export default ProfileModal;
