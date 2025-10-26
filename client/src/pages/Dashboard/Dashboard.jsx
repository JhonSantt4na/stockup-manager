// Dashboard.jsx
import React, { useState, useEffect } from 'react';
import InsightCard from '../../components/InshightCard/InsightCard'; // Ajuste o path conforme necessário
import Button from '../../components/Button/Button'; // Ajuste o path conforme necessário
import { FaShoppingCart, FaEdit,FaCog, FaPlus, FaUserPlus } from 'react-icons/fa';
import PageStruct from "../../pages/Layout/PageStruct/PageStruct";
import './Dashboard.css';

const Dashboard = () => {
  // Lista de 12 insights para ciclar
  const allInsights = [
    {
      title: 'Total Sales',
      value: 'R$ 200,00',
      trend: true,
      trendValue: '+12% last month',
      trendType: 'up',
    },
    {
      title: 'New Orders',
      value: '150.000.00',
      trend: true,
      trendValue: '+0% last month',
      trendType: 'up',
    },
    {
      title: 'Active Customers',
      value: '320',
      trend: true,
      trendValue: '+5% last month',
      trendType: 'up',
    },
    {
      title: 'Revenue Growth',
      value: '18%',
      trend: true,
      trendValue: '+3% this quarter',
      trendType: 'up',
    },
    {
      title: 'Average Order Value',
      value: 'R$ 300,00',
      trend: true,
      trendValue: '-2% last month',
      trendType: 'down',
    },
    {
      title: 'Customer Retention',
      value: '85%',
      trend: true,
      trendValue: '+4% last month',
      trendType: 'up',
    },
    {
      title: 'Top Product Sales',
      value: 'R$ 500,00',
      trend: true,
      trendValue: '+8% last month',
      trendType: 'up',
    },
    {
      title: 'New Users',
      value: '45',
      trend: true,
      trendValue: '+10% last month',
      trendType: 'up',
    },
    {
      title: 'Return Rate',
      value: '5%',
      trend: true,
      trendValue: '-1% last month',
      trendType: 'down',
    },
    {
      title: 'Inventory Turnover',
      value: '4.2',
      trend: true,
      trendValue: '+0.5 last month',
      trendType: 'up',
    },
    {
      title: 'Website Traffic',
      value: '12.200',
      trend: true,
      trendValue: '+15% last month',
      trendType: 'up',
    },
    {
      title: 'Conversion Rate',
      value: '3.5%',
      trend: true,
      trendValue: '+0.2% last month',
      trendType: 'up',
    },
  ];

  const [currentIndex, setCurrentIndex] = useState(0);
  const [currentInsights, setCurrentInsights] = useState(allInsights.slice(0, 3));
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const handleEditConfig = () => {
  
  };

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => {
        const nextIndex = (prevIndex + 3) % allInsights.length;
        setCurrentInsights(allInsights.slice(nextIndex, nextIndex + 3));
        return nextIndex;
      });
    }, 8000); // Cicla a cada 10 segundos

    return () => clearInterval(interval);
  }, []);

  const header = (
    <div className="dashboard-header">
        <h2 className="dashboard-title">Jhon Tec LTDA - CNPJ: 785.421.556-22</h2>
        <button className="logout-btn" onClick={handleEditConfig}>
            <FaCog /> Configurações da Loja
        </button>
    </div>
  );

  const body = (
    <div className="dashboard-body">
      <div className="insights-container">
        {currentInsights.map((insight, index) => (
          <InsightCard key={index} {...insight} />
        ))}
        <InsightCard 
          title="STOCK LOW" 
          value="15" 
          trend={true}
          trendValue="Stock Low"
          trendType="down"
          className="warning"
        />
      </div>
      
      <div className="dashboard-content">
        <div className="actions-section">
          <Button variant="blue" size="medium">
            <FaShoppingCart style={{ marginRight: '8px' }} /> INICIAR VENDA RÁPIDO
          </Button>
          <Button variant="gree" size="medium">
            <FaEdit style={{ marginRight: '8px' }} /> EDITAR USERS
          </Button>
          <Button variant="orange" size="medium">
            <FaPlus style={{ marginRight: '8px' }} /> ADICIONAR PRODUTO
          </Button>
          <Button variant="purple" size="medium">
            <FaUserPlus style={{ marginRight: '8px' }} /> CADASTRAR CLIENTE
          </Button>
        </div>

        <div className="graphs-container">
          <div className="chart-section">
            <h2 className="section-title">VENDAS DO DIA</h2>
            <div className="chart-placeholder">
              Gráfico de Vendas por dia
            </div>
          </div>
          <div className="chart-section">
            <h2 className="section-title">VENDAS DO MÊS</h2>
            <div className="chart-placeholder">
              Gráfico de Vendas por Mês
            </div>
          </div>
        </div>
      </div>
    </div>
  );

  const footer = (
    <div className="footer-empty">s</div>
  );

  return (
    <PageStruct header={header} body={body} footer={footer}/>
  );
};

export default Dashboard;