import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import { useAuth } from "./contexts/AuthContext";
import './index.css';

function Dashboard() {
  const { user, logout } = useAuth();
  return (
    <div>
      <h1>Bem-vindo, {user?.name}!</h1>
      <p>Seu painel de gerenciamento de estoque e PDV aqui.</p>
      <button onClick={logout} className="btn-secondary">Sair</button>
    </div>
  );
}

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