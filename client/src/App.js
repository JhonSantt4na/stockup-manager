import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import { useAuth } from "./contexts/AuthContext";
import './index.css';

import Dashboard from "./pages/Dashboard/Dashboard";
import Roles from "./pages/Roles/Roles";
import Permissions from "./pages/Permissions/Permissions";
import Config from "./pages/Config/Config";

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  const location = useLocation();
  if (loading) return <div>Carregando...</div>;
  return user ? children : <Navigate to="/" state={{ from: location }} replace />;
}

function GuestRoute({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div>Carregando...</div>;
  return user ? <Navigate to="/dashboard" replace /> : children;
}

function AppContent() {
  return (
    <div className="App">
      <Header />
      <main>
        <Routes>
          <Route 
            path="/" 
            element={
              <GuestRoute>
                <Login />
              </GuestRoute>
            } 
          />
          <Route 
            path="/register" 
            element={
              <GuestRoute>
                <Register />
              </GuestRoute>
            } 
          />
          <Route 
            path="/dashboard" 
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/roles" 
            element={
              <ProtectedRoute>
                <Roles />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/permissions" 
            element={
              <ProtectedRoute>
                <Permissions />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/config" 
            element={
              <ProtectedRoute>
                <Config />
              </ProtectedRoute>
            } 
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <Footer />
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