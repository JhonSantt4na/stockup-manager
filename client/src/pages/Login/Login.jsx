import React from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../contexts/AuthContext";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import "./Login.css";

const schema = yup.object({
  username: yup.string().trim().required("Username é obrigatório."),
  password: yup.string().required("Senha é obrigatória."),
});

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [showPassword, setShowPassword] = React.useState(false);
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
    const trimmedUsername = data.username.trim();
    const trimmedPassword = data.password;

    console.log("Tentando login com:", { username: trimmedUsername, password: trimmedPassword ? "***" : "" });

    try {
      const response = await axios.post("/auth/login", {
        username: trimmedUsername,
        password: trimmedPassword,
      });

      console.log("Resposta do backend:", response.data);

      const { accessToken, refreshToken, username } = response.data;

      const userData = {
        name: username,
        isAdmin: false,
      };

      login(accessToken, userData);

      if (refreshToken) {
        localStorage.setItem("refreshToken", refreshToken);
      }

      setSuccessMsg("Login realizado com sucesso! Redirecionando...");
      setTimeout(() => {
      navigate("/dashboard");
      }, 1500);
    } catch (err) {
      console.error("Erro no login:", err.response?.data || err.message);

      let errorMessage = "Tente novamente.";
      if (err.response?.status === 401) {
        errorMessage = "Credenciais inválidas. Verifique username e senha.";
      } else if (err.response?.status === 404) {
        errorMessage = "Endpoint não encontrado. Verifique o backend.";
      } else if (err.response?.data?.message) {
        errorMessage = err.response.data.message;
      } else if (err.message.includes("Network Error")) {
        errorMessage = "Erro de conexão. Backend em localhost:8080?";
      }
      reset();
      alert(errorMessage);
    }
  };

  return (
    <div className="page-wrapper">
      <div className="main-content">
        <div className="login-container">
          <div className="login-header">
            <div className="form-logo">
              <img src="/Logo.png" alt="StockUp Manager" className="logo-img" />
            </div>
            <p>Acesse seu painel com gerenciamento de estoque <br /> e PDV de forma segura</p>
          </div>
          <div className="login-body">
            <form id="loginForm" onSubmit={handleSubmit(onSubmit)} className="login-form">
              <div className="form-group">
                <label htmlFor="loginUsername">Username</label>
                <input
                  type="text"
                  id="loginUsername"
                  placeholder="Seu username"
                  {...register("username")}
                  aria-describedby="loginUsernameError"
                />
                {errors.username && (
                  <div className="error-msg" id="loginUsernameError" role="alert">
                    {errors.username.message}
                  </div>
                )}
              </div>
              <div className="form-group password-wrapper">
                <label htmlFor="loginPassword">Senha</label>
                <input
                  type={showPassword ? "text" : "password"}
                  id="loginPassword"
                  placeholder="Senha"
                  {...register("password")}
                  aria-describedby="loginPasswordError"
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword(!showPassword)}
                  aria-label="Alternar visibilidade da senha"
                >
                  {showPassword ? "Ocultar" : "Ver"}
                </button>
                {errors.password && (
                  <div className="error-msg" id="loginPasswordError" role="alert">
                    {errors.password.message}
                  </div>
                )}
              </div>
              {errors.root && (
                <div className="error-msg" role="alert">{errors.root.message}</div>
              )}
              <button type="submit" className="btn-primary" disabled={isSubmitting}>
                {isSubmitting ? "Entrando..." : "Entrar"}
              </button>
              {successMsg && (
                <div className="success-msg" id="loginSuccess" role="status">
                  {successMsg}
                </div>
              )}
            </form>

            <div className="switch-text">
              <p>
                Não tem conta?{" "}
                <span
                  className="switch-link"
                  onClick={() => navigate("/register")}
                  style={{ cursor: "pointer" }}
                >
                  Cadastre-se
                </span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;