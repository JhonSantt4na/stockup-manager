import React from "react";
import { FaExclamationTriangle, FaTrash, FaUserCheck } from "react-icons/fa";
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

  const effectiveActionType = actionType ?? 'delete';

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
      icon: <FaExclamationTriangle />,
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
      icon: <FaTrash />,
      btnClass: "toggle",
      warning: false
    }
  };

  const currentAction = actionConfigs[effectiveActionType] || { 
    title: customTitle || "Confirmar Ação", 
    verb: effectiveActionType || "confirmar",
    icon: <FaExclamationTriangle />,
    btnClass: effectiveActionType || "default",
    warning: true
  };

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

  const getWarningText = () => {
    if (customMessage) return customMessage;
    
    switch (effectiveActionType) {
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

  const getMainMessage = () => {
    if (customMessage) return customMessage;
    
    const itemName = getItemName();
    
    switch (effectiveActionType) {
      case 'toggle':
        const newStatus = item?.enabled === false ? "ativar" : "desativar";
        return `Tem certeza que deseja ${newStatus} ${itemName}?`;
      default:
        return `Tem certeza que deseja ${currentAction.verb || "executar"} ${itemName}?`;
    }
  };

  const getConfirmButtonText = () => {
    if (effectiveActionType === 'toggle') {
      return item?.enabled === false ? "Ativar" : "Desativar";
    }
    
    let verb = currentAction.verb || "confirmar";
    if (typeof verb !== 'string' || verb.trim() === '') {
      return "Confirmar"; 
    }
    
    return verb.charAt(0).toUpperCase() + verb.slice(1);
  };

  const getActionIcon = () => {
    if (effectiveActionType === 'toggle') {
      return item?.enabled === false ? <FaUserCheck /> : <FaExclamationTriangle />;
    }
    return currentAction.icon;
  };

  const warningText = getWarningText();
  const mainMessage = getMainMessage();

  if (!isOpen) {
    return null;
  }

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