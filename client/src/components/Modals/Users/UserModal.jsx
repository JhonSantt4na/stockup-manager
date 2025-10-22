import React, { useState, useEffect } from "react";
import axios from "axios";
import { FaTimes } from "react-icons/fa";
import "../Modal.css";

const UserModal = ({ user, onClose, onSuccess }) => {
  const [form, setForm] = useState({ 
    username: "", 
    fullName: "", 
    email: "", 
    password: "",
    confirmPassword: ""
  });
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (user) {
      setForm({ 
        username: user.username, 
        fullName: user.fullName, 
        email: user.email, 
        password: "",
        confirmPassword: ""
      });
    }
  }, [user]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (form.password !== form.confirmPassword) {
      alert("As senhas não coincidem!");
      return;
    }

    setLoading(true);
    try {
      if (user) {
        const updateData = {
          fullName: form.fullName,
          email: form.email
        };

        if (form.password) {
          updateData.password = form.password;
        }

        await axios.put(`/users/update/${user.username}`, updateData, {
          headers: { Authorization: `Bearer ${token}` },
        });
        onSuccess("Usuário atualizado com sucesso!");
      } else {
        await axios.post(`/users/register`, {
          username: form.username,
          fullName: form.fullName,
          email: form.email,
          password: form.password
        }, {
          headers: { Authorization: `Bearer ${token}` },
        });
        onSuccess("Usuário criado com sucesso!");
      }
      onClose();
    } catch (err) {
      alert("Erro ao salvar usuário.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content user-modal compact-modal">
        <div className="modal-header dark-header">
          <h3 className="header-center">{user ? "Editar Usuário" : "Novo Usuário"}</h3>
          <button className="btn-close red-close" onClick={onClose}>
            <FaTimes />
          </button>
        </div>
        
        <div className="modal-body white-body compact-body">
          <form onSubmit={handleSubmit} className="modal-form">
            <div className="form-grid centered-form compact-grid">
              {!user && (
                <div className="form-group">
                  <label>Usuário</label>
                  <input
                    type="text"
                    name="username"
                    placeholder="Usuário"
                    value={form.username}
                    onChange={handleChange}
                    className="search-box"
                    required
                  />
                </div>
              )}
              <div className="form-group">
                <label>Nome Completo</label>
                <input
                  type="text"
                  name="fullName"
                  placeholder="Nome completo"
                  value={form.fullName}
                  onChange={handleChange}
                  className="search-box"
                  required
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={form.email}
                  onChange={handleChange}
                  className="search-box"
                  required
                />
              </div>
              <div className="form-group">
                <label>{user ? "Nova Senha" : "Senha"}</label>
                <input
                  type="password"
                  name="password"
                  placeholder={user ? "Nova senha (opcional)" : "Senha"}
                  value={form.password}
                  onChange={handleChange}
                  className="search-box"
                  required={!user}
                />
              </div>
              <div className="form-group">
                <label>Confirmar Senha</label>
                <input
                  type="password"
                  name="confirmPassword"
                  placeholder="Confirmar senha"
                  value={form.confirmPassword}
                  onChange={handleChange}
                  className="search-box"
                  required={!user}
                />
              </div>
            </div>
            <div className="center-actions compact-actions">
              <button type="submit" className="btn-save btn-small" disabled={loading}>
                {loading ? "Salvando..." : (user ? "Atualizar" : "Cadastrar")}
              </button>
              <button type="button" className="btn-cancel btn-small red-cancel" onClick={onClose}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default UserModal;