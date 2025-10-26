// src/components/Footer/Footer.jsx
import React from "react";
import "./Footer.css";

const Footer = () => {
  return (
    <footer className="app-footer">
      <div className="footer-content">
        <div className="footer-logo">
            <img src="/Logo.png" alt="StockUp Manager" className="logo-img"></img>
        </div>
        <div className="footer-info">
          <p>© {new Date().getFullYear()} StockUp Manager. Todos os direitos reservados.</p>
        </div>
        <div className="footer-links">
          <a href="/sobre">Sobre</a>
          <a href="/contato">Contato</a>
          <a href="/privacidade">Privacidade</a>
        </div>
        <div className="footer-copyright">
          <p>Desenvolvido com ❤️ por S4nttana</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;