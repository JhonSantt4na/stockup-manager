import React, { useState, useRef, useEffect } from "react";
import { createPortal } from "react-dom";
import { FaTimes, FaUserCircle, FaEnvelope, FaLock, FaEdit, FaCamera } from "react-icons/fa";
import ChangePasswordModal from "./ChangePasswordModal";
import ProfileService from "../../../Services/ProfileService";
import "./ProfileModal.css";

const ProfileModal = ({
  isOpen,
  onClose,
  user, // user básico do contexto
  size = "medium"
}) => {
  const [profileData, setProfileData] = useState(null);
  const [editingField, setEditingField] = useState(null);
  const [formData, setFormData] = useState({
    username: "",
    fullName: "",
    email: ""
  });
  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isFetching, setIsFetching] = useState(false);
  const fileInputRef = useRef(null);

  // Buscar dados completos do perfil quando o modal abrir
  useEffect(() => {
    const fetchProfileData = async () => {
      if (isOpen && !profileData) {
        setIsFetching(true);
        try {
          const data = await ProfileService.getProfile();
          setProfileData(data);
          setFormData({
            username: data.username || "",
            fullName: data.fullName || "",
            email: data.email || ""
          });
        } catch (error) {
          console.error("Erro ao buscar dados do perfil:", error);
          // Fallback para dados básicos do contexto
          setProfileData(user);
          setFormData({
            username: user?.username || "",
            fullName: user?.fullName || "",
            email: user?.email || ""
          });
        } finally {
          setIsFetching(false);
        }
      }
    };

    fetchProfileData();
  }, [isOpen, profileData, user]);

  if (!isOpen) return null;

  const handleEditClick = (field) => {
    setEditingField(field);
  };

  const handleSave = async (field) => {
    setIsLoading(true);
    try {
      const updateData = {
        [field]: formData[field]
      };
      
      await ProfileService.updateProfile(updateData);
      console.log(`${field} atualizado com sucesso:`, formData[field]);
      
      // Atualizar os dados locais
      setProfileData(prev => ({
        ...prev,
        [field]: formData[field]
      }));
      
      setEditingField(null);
    } catch (error) {
      console.error(`Erro ao atualizar ${field}:`, error);
      alert(`Erro ao atualizar ${field}. Tente novamente.`);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    setEditingField(null);
    // Reverte as alterações não salvas para os dados atuais
    setFormData({
      username: profileData?.username || "",
      fullName: profileData?.fullName || "",
      email: profileData?.email || ""
    });
  };

  const handleInputChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleImageChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      setIsLoading(true);
      try {
        const result = await ProfileService.uploadProfilePhoto(file);
        console.log("Imagem atualizada com sucesso:", result);
        
        // Atualizar a URL da foto no estado
        setProfileData(prev => ({
          ...prev,
          profilePhotoUrl: result.profilePhotoUrl
        }));
        
        alert("Imagem de perfil atualizada com sucesso!");
      } catch (error) {
        console.error("Erro ao fazer upload da imagem:", error);
        alert("Erro ao atualizar imagem. Tente novamente.");
      } finally {
        setIsLoading(false);
      }
    }
  };

  const triggerFileInput = () => {
    fileInputRef.current?.click();
  };

  // Dados para exibição (usa profileData quando disponível, fallback para user do contexto)
  const displayData = profileData || user;

  return (
    <>
      {createPortal(
        <div className="modal-overlay" onClick={onClose}>
          <div
            className={`modal-container ${size}`}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="modal-header">
              <h3 className="modal-title">Perfil do Usuário</h3>
              <button className="modal-close" onClick={onClose}>
                <FaTimes />
              </button>
            </div>

            <div className="modal-body profile-modal-body-dark">
              {isFetching ? (
                <div className="profile-loading">
                  <p>Carregando dados do perfil...</p>
                </div>
              ) : (
                <>
                  <div className="profile-user-info">
                    <div className="profile-avatar-container">
                      <img 
                        src={displayData?.profilePhotoUrl || "/images/default-avatar.png"} 
                        alt="Foto de perfil" 
                        className="profile-avatar-img" 
                        onError={(e) => {
                          e.target.src = "/images/default-avatar.png";
                        }}
                      />
                      <button 
                        className="profile-change-image-btn"
                        onClick={triggerFileInput}
                        title="Alterar imagem"
                        disabled={isLoading}
                      >
                        <FaCamera />
                      </button>
                      <input
                        type="file"
                        ref={fileInputRef}
                        onChange={handleImageChange}
                        accept="image/*"
                        style={{ display: 'none' }}
                      />
                    </div>
                    <h4 className="profile-user-name">{displayData?.fullName || displayData?.username || "Usuário"}</h4>
                    <p className="profile-role">{displayData?.roles?.join(", ") || "Sem funções"}</p>
                  </div>

                  <div className="profile-details-inline">
                    {/* Nome de Usuário */}
                    <div className="profile-detail-item-inline">
                      <div className="profile-field-group">
                        <FaUserCircle className="profile-detail-icon" />
                        <div className="profile-field-content">
                          <span className="profile-field-label">Username:</span>
                          {editingField === 'username' ? (
                            <div className="profile-edit-container">
                              <input
                                type="text"
                                value={formData.username}
                                onChange={(e) => handleInputChange('username', e.target.value)}
                                className="profile-edit-input-dark"
                                autoFocus
                                disabled={isLoading}
                              />
                              <div className="profile-edit-actions">
                                <button 
                                  className="profile-save-btn"
                                  onClick={() => handleSave('username')}
                                  disabled={isLoading}
                                >
                                  {isLoading ? "..." : "Salvar"}
                                </button>
                                <button 
                                  className="profile-cancel-btn"
                                  onClick={handleCancel}
                                  disabled={isLoading}
                                >
                                  Cancelar
                                </button>
                              </div>
                            </div>
                          ) : (
                            <span className="profile-field-value">{formData.username}</span>
                          )}
                        </div>
                      </div>
                      {editingField !== 'username' && (
                        <button 
                          className="profile-edit-btn"
                          onClick={() => handleEditClick('username')}
                          disabled={isLoading}
                        >
                          <FaEdit />
                        </button>
                      )}
                    </div>

                    {/* Nome Completo */}
                    <div className="profile-detail-item-inline">
                      <div className="profile-field-group">
                        <FaUserCircle className="profile-detail-icon" />
                        <div className="profile-field-content">
                          <span className="profile-field-label">Nome completo:</span>
                          {editingField === 'fullName' ? (
                            <div className="profile-edit-container">
                              <input
                                type="text"
                                value={formData.fullName}
                                onChange={(e) => handleInputChange('fullName', e.target.value)}
                                className="profile-edit-input-dark"
                                autoFocus
                                disabled={isLoading}
                              />
                              <div className="profile-edit-actions">
                                <button 
                                  className="profile-save-btn"
                                  onClick={() => handleSave('fullName')}
                                  disabled={isLoading}
                                >
                                  {isLoading ? "..." : "Salvar"}
                                </button>
                                <button 
                                  className="profile-cancel-btn"
                                  onClick={handleCancel}
                                  disabled={isLoading}
                                >
                                  Cancelar
                                </button>
                              </div>
                            </div>
                          ) : (
                            <span className="profile-field-value">{formData.fullName || "Não definido"}</span>
                          )}
                        </div>
                      </div>
                      {editingField !== 'fullName' && (
                        <button 
                          className="profile-edit-btn"
                          onClick={() => handleEditClick('fullName')}
                          disabled={isLoading}
                        >
                          <FaEdit />
                        </button>
                      )}
                    </div>

                    {/* Email */}
                    <div className="profile-detail-item-inline">
                      <div className="profile-field-group">
                        <FaEnvelope className="profile-detail-icon" />
                        <div className="profile-field-content">
                          <span className="profile-field-label">Email:</span>
                          {editingField === 'email' ? (
                            <div className="profile-edit-container">
                              <input
                                type="email"
                                value={formData.email}
                                onChange={(e) => handleInputChange('email', e.target.value)}
                                className="profile-edit-input-dark"
                                autoFocus
                                disabled={isLoading}
                              />
                              <div className="profile-edit-actions">
                                <button 
                                  className="profile-save-btn"
                                  onClick={() => handleSave('email')}
                                  disabled={isLoading}
                                >
                                  {isLoading ? "..." : "Salvar"}
                                </button>
                                <button 
                                  className="profile-cancel-btn"
                                  onClick={handleCancel}
                                  disabled={isLoading}
                                >
                                  Cancelar
                                </button>
                              </div>
                            </div>
                          ) : (
                            <span className="profile-field-value">{formData.email || "Não disponível"}</span>
                          )}
                        </div>
                      </div>
                      {editingField !== 'email' && (
                        <button 
                          className="profile-edit-btn"
                          onClick={() => handleEditClick('email')}
                          disabled={isLoading}
                        >
                          <FaEdit />
                        </button>
                      )}
                    </div>

                    {/* Senha */}
                    <div className="profile-detail-item-inline">
                      <div className="profile-field-group">
                        <FaLock className="profile-detail-icon" />
                        <div className="profile-field-content">
                          <span className="profile-field-label">Senha:</span>
                          <span className="profile-field-value">••••••••</span>
                        </div>
                      </div>
                      <button 
                        className="profile-edit-btn"
                        onClick={() => setIsPasswordModalOpen(true)}
                        disabled={isLoading}
                      >
                        <FaEdit />
                      </button>
                    </div>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>,
        document.getElementById('modal-root')
      )}

      {/* Modal de Troca de Senha */}
      <ChangePasswordModal
        isOpen={isPasswordModalOpen}
        onClose={() => setIsPasswordModalOpen(false)}
        user={displayData}
      />
    </>
  );
};

export default ProfileModal;