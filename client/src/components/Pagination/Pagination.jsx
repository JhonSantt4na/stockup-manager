import React from 'react';
import './Pagination.css';

const Pagination = ({ currentPage, totalPages, onPrev, onNext }) => {
  return (
    <div className="pagination">
      <button onClick={onPrev} disabled={currentPage === 0}>◀ Anterior</button>
      <span>Página {currentPage + 1} de {totalPages}</span>
      <button onClick={onNext} disabled={currentPage + 1 >= totalPages}>Próxima ▶</button>
    </div>
  );
};

export default Pagination;