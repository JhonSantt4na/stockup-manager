import { Routes, Route, Navigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../contexts/AuthContext";
import { Dashboard } from "../pages/Dashboard";
import { Users } from "../pages/Users";
import { Stock } from "../pages/Stock";
import { Reports } from "../pages/Reports";
import { settings } from "../pages/Config";

export const AppRoutes = () => {
  const { user, loading } = useContext(AuthContext);

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      {user.roles?.includes("ADMIN") && (
        <>
          <Route path="/users" element={<Users />} />
          <Route path="/settings" element={<Config />} />
        </>
      )}
      <Route path="/stock" element={<Stock />} />
      <Route path="/reports" element={<Reports />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};