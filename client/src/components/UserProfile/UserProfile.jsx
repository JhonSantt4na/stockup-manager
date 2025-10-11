// src/components/UserProfile/UserProfile.jsx
import React, { useEffect, useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';  // Novo caminho
import { useNavigate } from 'react-router-dom';
import { getCurrentUser, updateProfile, changePassword } from '../../services/userService';  // Extraídas pra service
import './UserProfile.css';  // CSS separado

const UserProfile = () => {
  const { user, logout } = useAuth();
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [editMode, setEditMode] = useState(false); // Novo: modo edição pra nome/email
  const [changePasswordForm, setChangePasswordForm] = useState({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: ''
  });
  const [editProfileForm, setEditProfileForm] = useState({
    fullName: '',
    email: ''
  }); // Novo: form pra nome e email
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getCurrentUser();  // Usa service
        setProfile(response);
        // Preenche o form de edição com dados atuais
        setEditProfileForm({
          fullName: response.fullName || '',
          email: response.email || ''
        });
        setError('');
      } catch (err) {
        console.error('Erro ao buscar perfil:', err);
        setError('Erro ao carregar perfil. Tente novamente.');
        if (err.response?.status === 401) {
          await logout();
          navigate('/login');
        }
      }
    };
    fetchProfile();
  }, [user, logout, navigate]);

  const handleEditProfile = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');
    try {
      await updateProfile(editProfileForm);  // Usa service
      setSuccess('Perfil atualizado com sucesso!');
      setEditMode(false); // Sai do modo edição
      // Recarrega o perfil
      const response = await getCurrentUser();
      setProfile(response);
    } catch (err) {
      console.error('Erro ao atualizar perfil:', err);
      setError(err.response?.data?.message || 'Erro ao atualizar perfil. Verifique os dados.');
    } finally {
      setLoading(false);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (changePasswordForm.newPassword !== changePasswordForm.confirmNewPassword) {
      setError('As novas senhas não coincidem.');
      return;
    }
    if (changePasswordForm.newPassword.length < 8) {
      setError('A nova senha deve ter pelo menos 8 caracteres.');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess('');
    try {
      await changePassword({
        currentPassword: changePasswordForm.currentPassword,
        newPassword: changePasswordForm.newPassword
      });  // Usa service
      setSuccess('Senha alterada com sucesso!');
      setChangePasswordForm({ currentPassword: '', newPassword: '', confirmNewPassword: '' });
    } catch (err) {
      console.error('Erro ao trocar senha:', err);
      setError(err.response?.data?.message || 'Erro ao trocar senha. Verifique a senha atual.');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (formType, e) => {
    const { name, value } = e.target;
    if (formType === 'password') {
      setChangePasswordForm({ ...changePasswordForm, [name]: value });
    } else {
      setEditProfileForm({ ...editProfileForm, [name]: value });
    }
  };

  if (!profile) {
    return <div className="loading">Carregando perfil...</div>;
  }

  return (
    <div className="profile-container">
      <h2 className="profile-title">Meu Perfil</h2>
      
      {/* Informações (Visualização ou Edição) */}
      <div className="info-section">
        <h3 className="section-title">Informações Pessoais</h3>
        {!editMode ? (
          <>
            <p className="info-item"><strong>Username:</strong> {profile.username}</p>
            <p className="info-item"><strong>Nome Completo:</strong> {profile.fullName || 'N/A'}</p>
            <p className="info-item"><strong>Email:</strong> {profile.email}</p>
            <p className="info-item"><strong>Roles:</strong> {profile.roles ? profile.roles.join(', ') : 'N/A'}</p>
            <button 
              onClick={() => setEditMode(true)} 
              className="edit-info-btn"
            >
              Editar Nome e Email
            </button>
          </>
        ) : (
          <form onSubmit={handleEditProfile} className="edit-form">
            <input
              type="text"
              name="fullName"
              placeholder="Nome Completo"
              value={editProfileForm.fullName}
              onChange={(e) => handleInputChange('profile', e)}
              required
              className="edit-input"
            />
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={editProfileForm.email}
              onChange={(e) => handleInputChange('profile', e)}
              required
              className="edit-input"
            />
            <div className="edit-buttons">
              <button 
                type="submit" 
                disabled={loading}
                className={`save-btn ${loading ? 'disabled' : ''}`}
              >
                {loading ? 'Atualizando...' : 'Salvar Alterações'}
              </button>
              <button 
                type="button" 
                onClick={() => setEditMode(false)} 
                className="cancel-btn"
              >
                Cancelar
              </button>
            </div>
          </form>
        )}
      </div>

      {/* Trocar Senha (Mantido igual) */}
      <h3 className="section-title">Trocar Senha</h3>
      {success && <p className="success-message">{success}</p>}
      {error && <p className="error-message">{error}</p>}
      <form onSubmit={handleChangePassword} className="password-form">
        <input
          type="password"
          name="currentPassword"
          placeholder="Senha Atual"
          value={changePasswordForm.currentPassword}
          onChange={(e) => handleInputChange('password', e)}
          required
          disabled={loading}
          className="password-input"
        />
        <input
          type="password"
          name="newPassword"
          placeholder="Nova Senha (mín. 8 chars)"
          value={changePasswordForm.newPassword}
          onChange={(e) => handleInputChange('password', e)}
          required
          disabled={loading}
          className="password-input"
        />
        <input
          type="password"
          name="confirmNewPassword"
          placeholder="Confirmar Nova Senha"
          value={changePasswordForm.confirmNewPassword}
          onChange={(e) => handleInputChange('password', e)}
          required
          disabled={loading}
          className="password-input"
        />
        <button 
          type="submit" 
          disabled={loading}
          className={`change-password-btn ${loading ? 'disabled' : ''}`}
        >
          {loading ? 'Alterando...' : 'Alterar Senha'}
        </button>
      </form>

      <button 
        onClick={logout}
        className="logout-btn"
      >
        Logout
      </button>
    </div>
  );
};

export default UserProfile;