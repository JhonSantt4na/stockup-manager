// src/components/UserProfile.js (Atualizado com Form pra Editar Nome e Email)
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

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
        const response = await axios.get('http://localhost:8080/users/me');
        setProfile(response.data);
        // Preenche o form de edição com dados atuais
        setEditProfileForm({
          fullName: response.data.fullName || '',
          email: response.data.email || ''
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
      await axios.put('http://localhost:8080/users/update', editProfileForm); // Assumindo body { fullName, email }
      setSuccess('Perfil atualizado com sucesso!');
      setEditMode(false); // Sai do modo edição
      // Recarrega o perfil
      const response = await axios.get('http://localhost:8080/users/me');
      setProfile(response.data);
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
      await axios.put('http://localhost:8080/users/change-password', {
        currentPassword: changePasswordForm.currentPassword,
        newPassword: changePasswordForm.newPassword
      });
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
    return <div style={{ padding: '20px' }}>Carregando perfil...</div>;
  }

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <h2>Meu Perfil</h2>
      
      {/* Informações (Visualização ou Edição) */}
      <div style={{ border: '1px solid #ccc', padding: '15px', borderRadius: '8px', marginBottom: '20px' }}>
        <h3>Informações Pessoais</h3>
        {!editMode ? (
          <>
            <p><strong>Username:</strong> {profile.username}</p>
            <p><strong>Nome Completo:</strong> {profile.fullName || 'N/A'}</p>
            <p><strong>Email:</strong> {profile.email}</p>
            <p><strong>Roles:</strong> {profile.roles ? profile.roles.join(', ') : 'N/A'}</p>
            <button 
              onClick={() => setEditMode(true)} 
              style={{ padding: '8px 12px', background: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
            >
              Editar Nome e Email
            </button>
          </>
        ) : (
          <form onSubmit={handleEditProfile}>
            <input
              type="text"
              name="fullName"
              placeholder="Nome Completo"
              value={editProfileForm.fullName}
              onChange={(e) => handleInputChange('profile', e)}
              required
              style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
            />
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={editProfileForm.email}
              onChange={(e) => handleInputChange('profile', e)}
              required
              style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
            />
            <div style={{ display: 'flex', gap: '10px' }}>
              <button 
                type="submit" 
                disabled={loading}
                style={{ 
                  padding: '10px', 
                  background: loading ? '#6c757d' : '#007bff', 
                  color: 'white', 
                  border: 'none', 
                  cursor: loading ? 'not-allowed' : 'pointer' 
                }}
              >
                {loading ? 'Atualizando...' : 'Salvar Alterações'}
              </button>
              <button 
                type="button" 
                onClick={() => setEditMode(false)} 
                style={{ padding: '10px', background: '#6c757d', color: 'white', border: 'none', cursor: 'pointer' }}
              >
                Cancelar
              </button>
            </div>
          </form>
        )}
      </div>

      {/* Trocar Senha (Mantido igual) */}
      <h3>Trocar Senha</h3>
      {success && <p style={{ color: 'green' }}>{success}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleChangePassword} style={{ marginBottom: '20px' }}>
        <input
          type="password"
          name="currentPassword"
          placeholder="Senha Atual"
          value={changePasswordForm.currentPassword}
          onChange={(e) => handleInputChange('password', e)}
          required
          disabled={loading}
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <input
          type="password"
          name="newPassword"
          placeholder="Nova Senha (mín. 8 chars)"
          value={changePasswordForm.newPassword}
          onChange={(e) => handleInputChange('password', e)}
          required
          disabled={loading}
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <input
          type="password"
          name="confirmNewPassword"
          placeholder="Confirmar Nova Senha"
          value={changePasswordForm.confirmNewPassword}
          onChange={(e) => handleInputChange('password', e)}
          required
          disabled={loading}
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <button 
          type="submit" 
          disabled={loading}
          style={{ 
            padding: '10px', 
            background: loading ? '#6c757d' : '#007bff', 
            color: 'white', 
            border: 'none', 
            cursor: loading ? 'not-allowed' : 'pointer' 
          }}
        >
          {loading ? 'Alterando...' : 'Alterar Senha'}
        </button>
      </form>

      <button 
        onClick={logout}
        style={{ padding: '10px', background: '#dc3545', color: 'white', border: 'none', cursor: 'pointer' }}
      >
        Logout
      </button>
    </div>
  );
};

export default UserProfile;