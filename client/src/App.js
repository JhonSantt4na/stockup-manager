import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";
import Footer from "./pages/Layout/Footer/Footer";
import Login from "./pages/Auth/Login";
import Register from "./pages/Auth/Register";
import Header from "./pages/Layout/Header/Header";
import Navbar from "./pages/Layout/Navbar/Navbar";
import Dashboard from "./pages/Dashboard/Dashboard";
import Settings from "./pages/settings/settings";
import Users from "./pages/Users/Users";
import Roles from "./pages/Roles/Roles";
import Permissions from "./pages/Permissions/Permissions";
import { useAuth } from "./contexts/AuthContext";
import './index.css';

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  const location = useLocation();
  
  if (loading) return <div>Carregando...</div>;
  return user ? children : <Navigate to="/" state={{ from: location }} replace />;
}

function AdminRoute({ children }) {
  const { user, loading } = useAuth();
  const location = useLocation();
  
  if (loading) return <div>Carregando...</div>;
  if (!user || !user.roles?.includes("ADMIN")) {
    return <Navigate to="/dashboard" state={{ from: location }} replace />;
  }
  return children;
}

function GuestRoute({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div>Carregando...</div>;
  return user ? <Navigate to="/dashboard" replace /> : children;
}

function DashboardLayout({ children }) {
  return (
    <div className="dashboard-layout">
      <Header />
      <div className="dashboard-body">
        <Navbar />
        <main className="main-content">
          {children}
        </main>
      </div>
      <Footer />
    </div>
  );
}

function PublicLayout({ children }) {
  return (
    <div className="public-layout">
      <Header />
      <main className="public-main">
        {children}
      </main>
      <Footer />
    </div>
  );
}

function PlaceholderPage({ title }) {
  return (
    <div>
      <h1>{title}</h1>
      <p>Página em desenvolvimento</p>
    </div>
  );
}

function AppContent() {
  const { user } = useAuth();
  
  return (
    <div className="App">
      <Routes>
        {/* Rotas Públicas */}
        <Route 
          path="/" 
          element={
            <GuestRoute>
              <PublicLayout>
                <Login />
              </PublicLayout>
            </GuestRoute>
          } 
        />
        <Route 
          path="/register" 
          element={
            <GuestRoute>
              <PublicLayout>
                <Register />
              </PublicLayout>
            </GuestRoute>
          } 
        />
        
        {/* Rotas Protegidas do Dashboard */}
        <Route 
          path="/dashboard" 
          element={
            <ProtectedRoute>
              <DashboardLayout>
                <Dashboard />
              </DashboardLayout>
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/produtos" 
          element={
            <ProtectedRoute>
              <DashboardLayout>
                <PlaceholderPage title="Produtos" />
              </DashboardLayout>
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/users" 
          element={
            <AdminRoute>
              <DashboardLayout>
                <Users />
              </DashboardLayout>
            </AdminRoute>
          } 
        />
        <Route 
          path="/roles" 
          element={
            <AdminRoute>
              <DashboardLayout>
                <Roles />
              </DashboardLayout>
            </AdminRoute>
          } 
        />
        <Route 
          path="/permissions" 
          element={
            <AdminRoute>
              <DashboardLayout>
                <Permissions />
              </DashboardLayout>
            </AdminRoute>
          } 
        />
        <Route 
          path="/settings" 
          element={
            <AdminRoute>
              <DashboardLayout>
                <Settings />
              </DashboardLayout>
            </AdminRoute>
          } 
        />
        
        <Route path="*" element={<Navigate to={user ? "/dashboard" : "/"} replace />} />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;