import React from "react";
import { useAuth } from "../../contexts/AuthContext";
import AdminDashboard from "./AdminDashboard";
import FreeUserDashboard from "./FreeUserDashboard";
import ProUserDashboard from "./ProUserDashboard";

const Dashboard = () => {
  const { user } = useAuth();

  const getUserType = () => {
    if (user?.isAdmin) return "admin";
    if (user?.roles?.some(role => role === "PRO" || role === "USER" || role === "ROLE_PRO")) return "pro";
    return "free";
  };

  const userType = getUserType();

  const renderDashboard = () => {
    switch (userType) {
      case "admin":
        return <AdminDashboard />;
      case "pro":
        return <ProUserDashboard />;
      default:
        return <FreeUserDashboard />;
    }
  };

  return (
    <div className="dashboard-container">
      {renderDashboard()}
    </div>
  );
};

export default Dashboard;