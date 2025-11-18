import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaUsers, FaGem, FaCoins, FaChartBar, FaSignOutAlt } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const DashboardProfessorPage = () => {
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
          <h1>Dashboard do Professor</h1>
          <button onClick={handleLogout} className={styles.logoutButton}>
            <FaSignOutAlt /> Sair
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2>Bem-vindo(a), Professor(a)!</h2>
            <div className={styles.userInfo}>
              <p><strong>Email:</strong> {user?.email}</p>
              <p><strong>Tipo de Usuário:</strong> {user?.role}</p>
              <p><strong>ID:</strong> {user?.id}</p>
            </div>
          </div>

          <div className={styles.infoCards}>
            <div className={styles.card} onClick={() => navigate('/professor/alunos')}>
              <h3><FaUsers /> Meus Alunos</h3>
              <p>Gerencie seus alunos</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/professor/distribuir')}>
              <h3><FaGem /> Distribuir Moedas</h3>
              <p>Envie moedas para os alunos</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/professor/saldo')}>
              <h3><FaCoins /> Saldo Disponível</h3>
              <p>Consulte seu saldo de moedas</p>
            </div>

            <div className={styles.card}>
              <h3><FaChartBar /> Histórico</h3>
              <p>Veja o histórico de distribuição</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardProfessorPage;
