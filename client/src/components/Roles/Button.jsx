import React from "react";

const Button = ({ children, className, onClick, type = "button", disabled = false }) => {
  return (
    <button
      type={type}
      className={className}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </button>
  );
};

export default Button;