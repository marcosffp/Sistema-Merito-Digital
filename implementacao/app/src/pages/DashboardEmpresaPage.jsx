import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './Dashboard.module.css';

const DashboardEmpresaPage = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1>Dashboard da Empresa</h1>
          <button onClick={handleLogout} className={styles.logoutButton}>
            Sair
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2>Bem-vindo(a)!</h2>
            <div className={styles.userInfo}>
              <p><strong>Email:</strong> {user?.email}</p>
              <p><strong>Tipo de Usuário:</strong> {user?.role}</p>
              <p><strong>ID:</strong> {user?.id}</p>
            </div>
          </div>

          <div className={styles.infoCards}>
            <div className={styles.card} onClick={() => navigate('/empresa/vantagens')}>
              <h3>🎁 Minhas Vantagens</h3>
              <p>Gerencie suas vantagens cadastradas</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/empresa/vantagens/nova')}>
              <h3>➕ Nova Vantagem</h3>
              <p>Cadastre novas vantagens</p>
            </div>

            <div className={styles.card}>
              <h3>📦 Resgates</h3>
              <p>Veja os resgates realizados</p>
            </div>

            <div className={styles.card}>
              <h3>📊 Estatísticas</h3>
              <p>Acompanhe as estatísticas</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardEmpresaPage;
