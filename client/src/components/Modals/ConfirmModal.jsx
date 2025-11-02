import React from "react";
import { FaExclamationTriangle, FaToggleOn, FaToggleOff, FaTrash, FaUserSlash, FaUserCheck } from "react-icons/fa";
import CustomModal from "../../components/Custom/CustomModal";
import "./Modal.css";

const ConfirmModal = ({ 
  isOpen, 
  onClose, 
  item, 
  itemType = "item", 
  actionType = 'delete', 
  onConfirm,
  customMessage,
  customTitle 
}) => {
  // Mapeia ações para textos, ícones e cores
  const actionConfigs = {
    delete: { 
      title: "Confirmar Exclusão", 
      verb: "excluir",
      icon: <FaTrash />,
      btnClass: "delete",
      warning: true
    },
    activate: { 
      title: "Confirmar Ativação", 
      verb: "ativar",
      icon: <FaUserCheck />,
      btnClass: "activate",
      warning: false
    },
    deactivate: { 
      title: "Confirmar Desativação", 
      verb: "desativar",
      icon: <FaUserSlash />,
      btnClass: "deactivate",
      warning: false
    },
    remove: { 
      title: "Confirmar Remoção", 
      verb: "remover",
      icon: <FaTrash />,
      btnClass: "remove",
      warning: true
    },
    toggle: {
      title: "Confirmar Alteração de Status",
      verb: "alterar o status de",
      icon: <FaToggleOn />,
      btnClass: "toggle",
      warning: false
    }
  };

  // Obtém a configuração da ação ou usa valores padrão
  const currentAction = actionConfigs[actionType] || { 
    title: customTitle || "Confirmar Ação", 
    verb: actionType,
    icon: <FaExclamationTriangle />,
    btnClass: actionType,
    warning: true
  };

  // Obtém o nome do item baseado no tipo
  const getItemName = () => {
    if (!item) return "este item";
    
    switch (itemType.toLowerCase()) {
      case "user":
      case "usuário":
      case "usuario":
        return item.username || item.fullName || item.name || "o usuário";
      
      case "role":
      case "função":
      case "funcao":
        return item.name || item.description || "a função";
      
      case "permission":
      case "permissão":
      case "permissao":
        return item.name || item.description || "a permissão";
      
      case "product":
      case "produto":
        return item.name || item.description || "o produto";
      
      case "category":
      case "categoria":
        return item.name || item.description || "a categoria";
      
      default:
        return item.name || item.title || item.description || item.username || item.email || "este item";
    }
  };

  // Obtém o texto adicional baseado no tipo de ação
  const getWarningText = () => {
    if (customMessage) return customMessage;
    
    switch (actionType) {
      case 'delete':
      case 'remove':
        return "Esta ação não pode ser desfeita.";
      case 'deactivate':
        return "O item ficará indisponível até ser ativado novamente.";
      case 'activate':
        return "O item ficará disponível para uso.";
      case 'toggle':
        const newStatus = item?.enabled === false ? "ativado" : "desativado";
        return `O item será ${newStatus} e ${newStatus === "ativado" ? "ficará disponível para uso" : "ficará indisponível"}.`;
      default:
        return null;
    }
  };

  // Mensagem principal
  const getMainMessage = () => {
    if (customMessage) return customMessage;
    
    const itemName = getItemName();
    
    switch (actionType) {
      case 'toggle':
        const newStatus = item?.enabled === false ? "ativar" : "desativar";
        return `Tem certeza que deseja ${newStatus} ${itemName}?`;
      default:
        return `Tem certeza que deseja ${currentAction.verb} ${itemName}?`;
    }
  };

  // Texto do botão de confirmação
  const getConfirmButtonText = () => {
    switch (actionType) {
      case 'toggle':
        return item?.enabled === false ? "Ativar" : "Desativar";
      default:
        return currentAction.verb.charAt(0).toUpperCase() + currentAction.verb.slice(1);
    }
  };

  // Ícone baseado no tipo de ação
  const getActionIcon = () => {
    if (actionType === 'toggle') {
      return item?.enabled === false ? <FaToggleOn /> : <FaToggleOff />;
    }
    return currentAction.icon;
  };

  const warningText = getWarningText();
  const mainMessage = getMainMessage();

  return (
    <CustomModal
      isOpen={isOpen}
      onClose={onClose}
      title={customTitle || currentAction.title}
      showFooter={false}
    >
      <div className={`modal-icon ${currentAction.btnClass}`}>
        {getActionIcon()}
      </div>
      
      <p className="modal-message">{mainMessage}</p>
      
      {warningText && (
        <p className={`modal-warning-text ${currentAction.warning ? 'warning' : 'info'}`}>
          {warningText}
        </p>
      )}

      <div className="modal-actions-inline">
        <button className="btn-cancel" onClick={onClose}>
          Cancelar
        </button>
        <button 
          className={`btn-confirm ${currentAction.btnClass}`} 
          onClick={onConfirm}
        >
          {getConfirmButtonText()}
        </button>
      </div>
    </CustomModal>
  );
};

export default ConfirmModal;