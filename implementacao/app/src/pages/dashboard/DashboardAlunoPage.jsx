import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { obterResumoAluno } from '../../services/alunoService';
import { FaBook, FaCoins, FaGift, FaChartBar, FaSignOutAlt, FaUser, FaEnvelope } from 'react-icons/fa';
import styles from './Dashboard.module.css';
import { FaBoxArchive } from "react-icons/fa6";

const DashboardAlunoPage = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [saldo, setSaldo] = useState(0);
  const [nome, setNome] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSaldo = async () => {
      try {
        const data = await obterResumoAluno(user.id);
        setSaldo(data.saldoMoedas);
        setNome(data.nome || 'Aluno');
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
          <h1>Dashboard do Aluno</h1>
          <button onClick={handleLogout} className={styles.logoutButton}>
            <FaSignOutAlt /> Sair
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2>Bem-vindo(a) de volta!</h2>
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
                  <FaCoins className={styles.saldoIcon} /> Saldo
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
            <div className={styles.card}>
              <h3><FaBoxArchive /> Resgates</h3>
              <p>Veja seus resgates realizados</p>
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
