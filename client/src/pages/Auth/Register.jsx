import React from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../contexts/AuthContext";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import "./css/Register.css";

const schema = yup.object({
  fullName: yup.string().trim().required("Nome completo é obrigatório."),
  username: yup.string().trim().required("Username é obrigatório."),
  registerEmail: yup
    .string()
    .email("Formato de email inválido.")
    .required("Email é obrigatório."),
  registerPassword: yup
    .string()
    .min(8, "Senha deve ter pelo menos 8 caracteres.")
    .required("Senha é obrigatória."),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref("registerPassword")], "Senhas não coincidem.")
    .required("Confirmação de senha é obrigatória."),
});

function Register() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [showRegisterPassword, setShowRegisterPassword] = React.useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = React.useState(false);
  const [successMsg, setSuccessMsg] = React.useState("");

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm({
    resolver: yupResolver(schema),
  });

  const onSubmit = async (data) => {
    try {
      const response = await axios.post("/users/register", {
        fullName: data.fullName.trim(),
        username: data.username.trim(),
        email: data.registerEmail.trim(),
        password: data.registerPassword,
      });

      console.log("Resposta do register:", response.data);

      const { message, accessToken, refreshToken } = response.data;

      if (accessToken) {
        const userData = { name: data.fullName, username: data.username, isAdmin: false };
        login(accessToken, userData);
        if (refreshToken) localStorage.setItem("refreshToken", refreshToken);
        setSuccessMsg("Cadastro realizado! Redirecionando...");
        setTimeout(() => window.location.href = "/dashboard", 1500);
      } else {
        setSuccessMsg(message || "Cadastro realizado com sucesso! Faça login agora.");
        setTimeout(() => {
          navigate("/");
          reset();
        }, 2000);
      }
    } catch (err) {
      console.error("Erro no register:", err.response?.data || err.message);
      let errorMessage = "Tente novamente.";
      if (err.response?.status === 409 || err.response?.status === 400) {
        errorMessage = err.response.data.message || "Usuário ou email já existe.";
      } else if (err.response?.data?.message) {
        errorMessage = err.response.data.message;
      } else if (err.message.includes("Network Error")) {
        errorMessage = "Erro de conexão. Backend rodando?";
      }
      alert(errorMessage);
      reset();
    }
  };

  return (
    <div className="main-content">
      <div className="login-container">
        <div className="login-header">
          <div className="form-logo">
            <span className="logo-highlight">StockUp</span>
            <span className="logoP-highlight">Manager</span>
          </div>
          <p className="header-description">
            Crie sua conta para acessar o <br />
            <span className="feature-highlight">Gerenciamento de Estoque e PDV</span>
          </p>
        </div>
        <div className="login-body">
          <form id="registerForm" onSubmit={handleSubmit(onSubmit)} className="login-form">
            <div className="form-row">
              <div className="form-groupp">
                <label htmlFor="fullName">Nome Completo</label>
                <input
                  type="text"
                  id="fullName"
                  placeholder="Nome completo"
                  {...register("fullName")}
                  aria-describedby="fullNameError"
                />
                {errors.fullName && (
                  <div className="error-msg" id="fullNameError" role="alert">
                    {errors.fullName.message}
                  </div>
                )}
              </div>
              <div className="form-groupp">
                <label htmlFor="username">Username</label>
                <input
                  type="text"
                  id="username"
                  placeholder="Username"
                  {...register("username")}
                  aria-describedby="usernameError"
                />
                {errors.username && (
                  <div className="error-msg" id="usernameError" role="alert">
                    {errors.username.message}
                  </div>
                )}
              </div>
            </div>
            <div className="form-groupp">
              <label htmlFor="registerEmail">Email</label>
              <input
                type="email"
                id="registerEmail"
                placeholder="seu@email.com"
                {...register("registerEmail")}
                aria-describedby="registerEmailError"
              />
              {errors.registerEmail && (
                <div className="error-msg" id="registerEmailError" role="alert">
                  {errors.registerEmail.message}
                </div>
              )}
            </div>
            <div className="form-groupp password-wrapper">
              <label htmlFor="registerPassword">Senha</label>
              <div className="password-input-container">
                <input
                  type={showRegisterPassword ? "text" : "password"}
                  id="registerPassword"
                  placeholder="Senha (mín. 8 caracteres)"
                  {...register("registerPassword")}
                  aria-describedby="registerPasswordError"
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowRegisterPassword(!showRegisterPassword)}
                  aria-label="Alternar visibilidade da senha"
                >
                  {showRegisterPassword ? "Ocultar" : "Ver"}
                </button>
              </div>
              {errors.registerPassword && (
                <div className="error-msg" id="registerPasswordError" role="alert">
                  {errors.registerPassword.message}
                </div>
              )}
            </div>
            <div className="form-groupp password-wrapper">
              <label htmlFor="confirmPassword">Confirmar Senha</label>
              <div className="password-input-container">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  id="confirmPassword"
                  placeholder="Confirme a senha"
                  {...register("confirmPassword")}
                  aria-describedby="confirmPasswordError"
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  aria-label="Alternar visibilidade da confirmação de senha"
                >
                  {showConfirmPassword ? "Ocultar" : "Ver"}
                </button>
              </div>
              {errors.confirmPassword && (
                <div className="error-msg" id="confirmPasswordError" role="alert">
                  {errors.confirmPassword.message}
                </div>
              )}
            </div>
            {errors.root && (
              <div className="error-msg" role="alert">{errors.root.message}</div>
            )}
            <button type="submit" className="btn-primary" disabled={isSubmitting}>
              {isSubmitting ? "Cadastrando..." : "Cadastrar"}
            </button>
            {successMsg && (
              <div className="success-msg" id="registerSuccess" role="status">
                {successMsg}
              </div>
            )}
          </form>

          <div className="switch-text">
            <p>
              Já tem conta?{" "}
              <span
                className="switch-link"
                onClick={() => navigate("/")}
                style={{ cursor: "pointer" }}
              >
                Entre aqui
              </span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;