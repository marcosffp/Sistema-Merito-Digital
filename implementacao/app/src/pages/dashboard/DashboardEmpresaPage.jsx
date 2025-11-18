import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FaGift, FaPlus, FaBox, FaChartBar, FaSignOutAlt, FaEnvelope, FaBuilding } from 'react-icons/fa';
import styles from './Dashboard.module.css';
import { FaBoxArchive } from "react-icons/fa6";


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
            <FaSignOutAlt /> Sair
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2>Bem-vindo(a), Empresa!</h2>
            <div className={styles.userInfo}>
              <div className={styles.infoItem}>
                <span className={styles.infoLabel}>
                  <FaEnvelope /> Email
                </span>
                <span className={styles.infoValue}>{user?.email}</span>
              </div>
              <div className={styles.infoItem}>
                <span className={styles.infoLabel}>
                  <FaBuilding /> Tipo de Conta
                </span>
                <span className={styles.infoValue}>{user?.role}</span>
              </div>
            </div>
          </div>

          <div className={styles.infoCards}>
            <div className={styles.card} onClick={() => navigate('/empresa/vantagens')}>
              <h3><FaGift /> Minhas Vantagens</h3>
              <p>Gerencie suas vantagens cadastradas</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/empresa/vantagens/nova')}>
              <h3><FaPlus /> Nova Vantagem</h3>
              <p>Cadastre novas vantagens</p>
            </div>

            <div className={styles.card}>
              <h3><FaBoxArchive /> Resgates</h3>
              <p>Veja os resgates realizados</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardEmpresaPage;
