import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterAlunoPage from './pages/RegisterAlunoPage';
import RegisterEmpresaPage from './pages/RegisterEmpresaPage';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/cadastro/aluno" element={<RegisterAlunoPage />} />
          <Route path="/cadastro/empresa" element={<RegisterEmpresaPage />} />
          <Route path="/" element={<Navigate to="/login" replace />} />
          {/* Adicione outras rotas aqui conforme necessário */}
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
