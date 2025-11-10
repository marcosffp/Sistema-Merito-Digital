import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterAlunoPage from './pages/RegisterAlunoPage';
import RegisterEmpresaPage from './pages/RegisterEmpresaPage';
import DashboardAlunoPage from './pages/DashboardAlunoPage';
import DashboardProfessorPage from './pages/DashboardProfessorPage';
import DashboardEmpresaPage from './pages/DashboardEmpresaPage';
import VantagensAlunoPage from './pages/VantagensAlunoPage';
import VantagensEmpresaPage from './pages/VantagensEmpresaPage';
import CadastrarVantagemPage from './pages/CadastrarVantagemPage';
import EditarVantagemPage from './pages/EditarVantagemPage';
import UnauthorizedPage from './pages/UnauthorizedPage';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/cadastro/aluno" element={<RegisterAlunoPage />} />
          <Route path="/cadastro/empresa" element={<RegisterEmpresaPage />} />
          <Route path="/unauthorized" element={<UnauthorizedPage />} />
          
          {/* Rotas protegidas - Aluno */}
          <Route 
            path="/dashboard/aluno" 
            element={
              <ProtectedRoute allowedRoles={['Aluno']}>
                <DashboardAlunoPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/aluno/vantagens" 
            element={
              <ProtectedRoute allowedRoles={['Aluno']}>
                <VantagensAlunoPage />
              </ProtectedRoute>
            } 
          />
          
          {/* Rotas protegidas - Professor */}
          <Route 
            path="/dashboard/professor" 
            element={
              <ProtectedRoute allowedRoles={['Professor']}>
                <DashboardProfessorPage />
              </ProtectedRoute>
            } 
          />
          
          {/* Rotas protegidas - Empresa */}
          <Route 
            path="/dashboard/empresa" 
            element={
              <ProtectedRoute allowedRoles={['Empresa']}>
                <DashboardEmpresaPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/empresa/vantagens" 
            element={
              <ProtectedRoute allowedRoles={['Empresa']}>
                <VantagensEmpresaPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/empresa/vantagens/nova" 
            element={
              <ProtectedRoute allowedRoles={['Empresa']}>
                <CadastrarVantagemPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/empresa/vantagens/editar/:id" 
            element={
              <ProtectedRoute allowedRoles={['Empresa']}>
                <EditarVantagemPage />
              </ProtectedRoute>
            } 
          />
          
          <Route path="/" element={<Navigate to="/login" replace />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
