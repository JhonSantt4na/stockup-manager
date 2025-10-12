import React from "react";
import "./Footer.css";

const Footer = () => {
  return (
    <footer>
      <div className="container">
        <div class="form-logo">
            <img src="/Logo.png" alt="StockUp Manager" class="logo-img"></img>
        </div>
        <nav className="footer-links">
          <a href="#">Privacidade</a>
          <a href="#">Termos</a>
          <a href="#">Suporte</a>
          <a href="#">Contato</a>
        </nav>
        <div className="footer-info">
          <p>© 2025 StockUp Manager. Todos os direitos reservados.</p>
        </div>
        <div className="footer-copyright">
          <p>Desenvolvido com ❤️ por S4nttana</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;