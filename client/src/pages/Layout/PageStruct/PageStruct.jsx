import React from "react";
import "./PageStruct.css";

const PageStruct = ({ header, body, footer, children }) => {
  return (
    <div className="page-struct">
      {header && <div className="page-header">{header}</div>}
      {body && <div className="page-body">{body}</div>}
      {footer && <div className="page-footer">{footer}</div>}
      {children}
    </div>
  );
};

export default PageStruct;
