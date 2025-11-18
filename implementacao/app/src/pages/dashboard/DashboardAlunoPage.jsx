import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FaBook, FaCoins, FaGift, FaChartBar, FaSignOutAlt } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const DashboardAlunoPage = () => {
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
          <h1>Dashboard do Aluno</h1>
          <button onClick={handleLogout} className={styles.logoutButton}>
            <FaSignOutAlt /> Sair
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
            <div className={styles.card}>
              <h3><FaBook /> Meus Cursos</h3>
              <p>Visualize seus cursos e progresso</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/aluno/moedas')}>
              <h3><FaCoins /> Minhas Moedas</h3>
              <p>Gerencie seu saldo de moedas estudantis</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/aluno/vantagens')}>
              <h3><FaGift /> Vantagens</h3>
              <p>Troque moedas por vantagens</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/aluno/extrato')}>
              <h3><FaChartBar /> Extrato</h3>
              <p>Histórico de transações</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardAlunoPage;
