import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
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
            <div className={styles.card}>
              <h3>📚 Meus Cursos</h3>
              <p>Visualize seus cursos e progresso</p>
            </div>

            <div className={styles.card}>
              <h3>💰 Minhas Moedas</h3>
              <p>Gerencie seu saldo de moedas estudantis</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/aluno/vantagens')}>
              <h3>🎁 Vantagens</h3>
              <p>Troque moedas por vantagens</p>
            </div>

            <div className={styles.card}>
              <h3>📊 Extrato</h3>
              <p>Histórico de transações</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardAlunoPage;
