import React, { useState, useEffect } from "react";
import axios from "axios";
import CustomModal from "../../Custom/CustomModal";
import "../Modal.css"; // ou ModalUser.css se preferir

const ModalRegisterUser = ({ user, onClose, onSuccess }) => {
  const [form, setForm] = useState({
    username: "",
    fullName: "",
    email: "",
    password: "",
    confirmPassword: "",
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
        confirmPassword: "",
      });
    }
  }, [user]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

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
          email: form.email,
        };
        if (form.password) updateData.password = form.password;

        await axios.put(`/users/update/${user.username}`, updateData, {
          headers: { Authorization: `Bearer ${token}` },
        });
        onSuccess("Usuário atualizado com sucesso!");
      } else {
        await axios.post(
          `/users/register`,
          {
            username: form.username,
            fullName: form.fullName,
            email: form.email,
            password: form.password,
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        onSuccess("Usuário criado com sucesso!");
      }
      onClose();
    } catch (err) {
      console.error(err);
      alert("Erro ao salvar usuário.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <CustomModal
      isOpen={true}
      onClose={onClose}
      title={user ? "Editar Usuário" : "Novo Usuário"}
      size="medium"
      onConfirm={handleSubmit}
      confirmText={loading ? "Salvando..." : user ? "Atualizar" : "Cadastrar"}
      cancelText="Cancelar"
      onCancel={onClose}
    >
      <form className="modal-form">
        {!user && (
          <div className="form-group">
            <label>Usuário</label>
            <input
              type="text"
              name="username"
              placeholder="Usuário"
              value={form.username}
              onChange={handleChange}
              className="search-boxU"
              required
              disabled={loading}
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
            className="search-boxU"
            onChange={handleChange}
            required
            disabled={loading}
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
            className="search-boxU"
            required
            disabled={loading}
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
            className="search-boxU"
            required={!user}
            disabled={loading}
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
            className="search-boxU"
            required={!user}
            disabled={loading}
          />
        </div>
      </form>
    </CustomModal>
  );
};

export default ModalRegisterUser;
