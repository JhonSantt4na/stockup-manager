import { useState } from 'react';

export const useAuthForm = (initialToggles = 1) => {
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [successMsg, setSuccessMsg] = useState("");
  const [passwordToggles, setPasswordToggles] = useState(Array(initialToggles).fill(false));

  const validateRequired = (value, fieldName) => {
    if (!value?.trim()) {
      return `${fieldName} é obrigatório.`;
    }
    return null;
  };

  const validatePassword = (password) => {
    if (password && password.length < 8) {
      return "Senha deve ter pelo menos 8 caracteres.";
    }
    return null;
  };

  const validateEmail = (email) => {
    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      return "Formato de email inválido.";
    }
    return null;
  };

  const validateConfirmPassword = (password, confirmPassword) => {
    if (password && confirmPassword && password !== confirmPassword) {
      return "Senhas não coincidem.";
    }
    return null;
  };

  const validateForm = (fields) => {
    const newErrors = {};
    Object.entries(fields).forEach(([key, value]) => {
      const error = validateRequired(value, key);
      if (error) newErrors[key] = error;
    });
    
    if (fields.registerPassword && validatePassword(fields.registerPassword)) {
      newErrors.registerPassword = validatePassword(fields.registerPassword);
    }
    if (fields.registerEmail && validateEmail(fields.registerEmail)) {
      newErrors.registerEmail = validateEmail(fields.registerEmail);
    }
    if (fields.confirmPassword && validateConfirmPassword(fields.registerPassword, fields.confirmPassword)) {
      newErrors.confirmPassword = validateConfirmPassword(fields.registerPassword, fields.confirmPassword);
    }
    return newErrors;
  };

  const resetForm = () => {
    setErrors({});
    setSuccessMsg("");
    setLoading(false);
  };

  const setError = (message, type = 'general') => {
    setErrors({ [type]: message });
  };

  const togglePassword = (index = 0) => {
    setPasswordToggles(prev => prev.map((val, i) => i === index ? !val : val));
  };

  const getShowPassword = (index = 0) => passwordToggles[index];

  return {
    loading,
    setLoading,
    errors,
    setErrors,
    successMsg,
    setSuccessMsg,
    getShowPassword,
    togglePassword,
    validateForm,
    resetForm,
    setError,
    validateRequired,
    validatePassword,
    validateEmail,
    validateConfirmPassword
  };
};