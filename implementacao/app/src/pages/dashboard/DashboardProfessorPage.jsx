import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { obterDadosProfessor } from '../../services/professorService';
import { FaUsers, FaGem, FaChartBar, FaSignOutAlt, FaCoins, FaEnvelope, FaUser } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const DashboardProfessorPage = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [saldo, setSaldo] = useState(0);
  const [nome, setNome] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSaldo = async () => {
      try {
        const data = await obterDadosProfessor(user.id);
        setSaldo(data.saldoMoedas);
        setNome(data.nome || 'Professor');
      } catch (error) {
        console.error('Erro ao carregar saldo:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchSaldo();
  }, [user.id]);

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
            <h2>Olá, Professor(a)!</h2>
            <div className={styles.userInfo}>
              <div className={styles.infoItem}>
                <span className={styles.infoLabel}>
                  <FaUser /> Nome
                </span>
                <span className={styles.infoValue}>{nome}</span>
              </div>
              <div className={styles.infoItem}>
                <span className={styles.infoLabel}>
                  <FaEnvelope /> Email
                </span>
                <span className={styles.infoValue}>{user?.email}</span>
              </div>
              <div className={styles.infoItem}>
                <span className={styles.infoLabel}>
                  <FaCoins className={styles.saldoIcon} /> Saldo Disponível
                </span>
                <span className={styles.infoValue}>
                  {loading ? 'Carregando...' : (
                    <>
                      {saldo.toFixed(2)} moedas
                    </>
                  )}
                </span>
              </div>
            </div>
          </div>

          <div className={styles.infoCards}>
            <div className={styles.card} onClick={() => navigate('/professor/alunos')}>
              <h3><FaUsers /> Visualizar Alunos</h3>
              <p>Gerencie os alunos</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/professor/distribuir')}>
              <h3><FaGem /> Distribuir Moedas</h3>
              <p>Envie moedas para os alunos</p>
            </div>

            <div className={styles.card} onClick={() => navigate('/professor/extrato')}>
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
