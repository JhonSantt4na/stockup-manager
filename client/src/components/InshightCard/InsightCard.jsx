import React from 'react';
import './InsightCard.css';

const InsightCard = ({ 
  title, 
  value, 
  trend, 
  trendValue, 
  trendType = 'up', // 'up' ou 'down'
  className = '',
  onClick 
}) => {
  const getTrendIcon = () => {
    return trendType === 'up' ? '▲' : '▼';
  };

  const getTrendClass = () => {
    return trendType === 'up' ? 'trend-up' : 'trend-down';
  };

  return (
    <div 
      className={`insight-card ${className}`}
      onClick={onClick}
      style={{ cursor: onClick ? 'pointer' : 'default' }}
    >
      <h3 className="insight-title">{title}</h3>
      <div className="insight-value">{value}</div>
      {trend && (
        <p className={`insight-trend ${getTrendClass()}`}>
          <span className="trend-icon">{getTrendIcon()}</span>
          {trendValue}
        </p>
      )}
    </div>
  );
};

export default InsightCard;