import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { obterDadosProfessor } from '../services/professorService';
import { FaCoins, FaArrowLeft, FaHistory } from 'react-icons/fa';
import dashboardStyles from './Dashboard.module.css';
import styles from './SaldoProfessorPage.module.css';

const SaldoProfessorPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [professorData, setProfessorData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await obterDadosProfessor(user.id);
        setProfessorData(data);
      } catch (error) {
        console.error('Erro ao carregar dados:', error);
        alert('Erro ao carregar saldo');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user.id]);

  if (loading) return <div className={styles.dashboardPage}>Carregando...</div>;

  return (
    <div className={dashboardStyles.dashboardPage}>
      <div className={dashboardStyles.container}>
        <header className={dashboardStyles.header}>
          <h1><FaCoins /> Saldo Disponível</h1>
          <button onClick={() => navigate('/dashboard/professor')} className={dashboardStyles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={dashboardStyles.content}>
          <div className={dashboardStyles.welcomeCard}>
            <h2>Saldo Atual</h2>
            <div className={styles.saldoValor}>
              {professorData?.saldoMoedas?.toFixed(2)} moedas
            </div>
          </div>

          {professorData?.distribuicoes && professorData.distribuicoes.length > 0 && (
            <div className={dashboardStyles.welcomeCard}>
              <h2><FaHistory /> Histórico de Distribuições</h2>
              <div className={styles.historicoContainer}>
                {professorData.distribuicoes.map((dist) => (
                  <div key={dist.id} className={styles.distribuicaoItem}>
                    <p><strong>Aluno:</strong> {dist.nomeAluno}</p>
                    <p><strong>Valor:</strong> {dist.valor} moedas</p>
                    <p><strong>Motivo:</strong> {dist.motivo}</p>
                    <p><strong>Data:</strong> {new Date(dist.data).toLocaleString('pt-BR')}</p>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SaldoProfessorPage;
