import React from "react";
import "./PageStruct.css";

const PageStruct = ({ header, body, footer }) => {
  return (
    <div className="page-struct">
      {header && <div className="page-header">{header}</div>}
      {body && <div className="page-body">{body}</div>}
      {footer && <div className="page-footer">{footer}</div>}
    </div>
  );
};

export default PageStruct;
