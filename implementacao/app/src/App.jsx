import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/login/LoginPage';
import RegisterAlunoPage from './pages/registro/RegisterAlunoPage';
import RegisterEmpresaPage from './pages/registro/RegisterEmpresaPage';
import DashboardAlunoPage from './pages/dashboard/DashboardAlunoPage';
import DashboardProfessorPage from './pages/dashboard/DashboardProfessorPage';
import DashboardEmpresaPage from './pages/dashboard/DashboardEmpresaPage';
import VantagensAlunoPage from './pages/aluno/VantagensAlunoPage';
import VantagensEmpresaPage from './pages/empresa/VantagensEmpresaPage';
import CadastrarVantagemPage from './pages/empresa/CadastrarVantagemPage';
import EditarVantagemPage from './pages/empresa/EditarVantagemPage';
import MinhasMoedasPage from './pages/aluno/MinhasMoedasPage';
import MeusAlunosPage from './pages/professor/MeusAlunosPage';
import DistribuirMoedasPage from './pages/professor/DistribuirMoedasPage';
import SaldoProfessorPage from './pages/professor/SaldoProfessorPage';
import ExtratoAlunoPage from './pages/aluno/ExtratoAlunoPage';
import ExtratoProfessorPage from './pages/professor/ExtratoProfessorPage';
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

          <Route 
            path="/aluno/moedas" 
            element={
              <ProtectedRoute allowedRoles={['Aluno']}>
                <MinhasMoedasPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/aluno/extrato" 
            element={
              <ProtectedRoute allowedRoles={['Aluno']}>
                <ExtratoAlunoPage />
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

          <Route 
            path="/professor/alunos" 
            element={
              <ProtectedRoute allowedRoles={['Professor']}>
                <MeusAlunosPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/professor/distribuir" 
            element={
              <ProtectedRoute allowedRoles={['Professor']}>
                <DistribuirMoedasPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/professor/saldo" 
            element={
              <ProtectedRoute allowedRoles={['Professor']}>
                <SaldoProfessorPage />
              </ProtectedRoute>
            } 
          />

          <Route 
            path="/professor/extrato" 
            element={
              <ProtectedRoute allowedRoles={['Professor']}>
                <ExtratoProfessorPage />
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
