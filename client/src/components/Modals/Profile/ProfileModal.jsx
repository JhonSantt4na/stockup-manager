import React from "react";
import { FaTimes, FaUserCircle, FaEnvelope, FaLock } from "react-icons/fa";
import "./ProfileModal.css";

const ProfileModal = ({ user, onClose }) => {
  return (
    <div className="profile-modal-backdrop">
      <div className="profile-modal-content">
        <div className="profile-modal-header">
          <h3 className="profile-header-center">Perfil do Usuário</h3>
          <button className="profile-btn-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="profile-modal-body">
          <div className="profile-user-info">
            <div className="profile-avatar-container">
              <img 
                src={user.profilePhotoUrl || "/default-avatar.png"} 
                alt="Foto de perfil" 
                className="profile-avatar-img" 
              />
            </div>
            <h4>{user.fullName || user.username}</h4>
            <p className="profile-role">{user.roles?.join(", ") || "Sem funções"}</p>
          </div>

          <div className="profile-details">
            <div className="profile-detail-item">
              <FaUserCircle className="profile-detail-icon" />
              <span>Username: {user.username}</span>
            </div>
            <div className="profile-detail-item">
              <FaEnvelope className="profile-detail-icon" />
              <span>Email: {user.email}</span>
            </div>
            <div className="profile-detail-item">
              <FaLock className="profile-detail-icon" />
              <span>Senha: ******** (Altere nas configurações)</span>
            </div>
          </div>

          <div className="profile-center-actions">
            <button className="profile-btn-close-action" onClick={onClose}>
              Fechar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileModal;