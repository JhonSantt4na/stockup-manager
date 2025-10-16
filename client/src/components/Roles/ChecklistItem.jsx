// src/components/ChecklistItem.jsx
import React from "react";

const ChecklistItem = ({ perm, checked, onChange }) => (
  <label className="checklist-item">
    <input
      type="checkbox"
      checked={checked}
      onChange={() => onChange(perm)}
    />
    <span className="perm-tag-small">{perm}</span>
  </label>
);

export default ChecklistItem;