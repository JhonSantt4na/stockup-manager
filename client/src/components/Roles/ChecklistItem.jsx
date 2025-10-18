import React from "react";

const ChecklistItem = ({ perm, checked, onChange, className = "" }) => {
  return (
    <div 
      className={`checklist-item ${className}`} 
      onClick={() => onChange(perm)}
    >
      <input 
        type="checkbox" 
        checked={checked} 
        onChange={() => {}} // to prevent warning
      />
      <span className="perm-tag-small">{perm}</span>
    </div>
  );
};

export default ChecklistItem;