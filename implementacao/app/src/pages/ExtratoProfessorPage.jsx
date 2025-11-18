import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { obterExtratoProfessor } from '../services/professorService';
import { FaChartBar, FaArrowLeft, FaCoins, FaGem, FaArrowUp } from 'react-icons/fa';
import dashboardStyles from './Dashboard.module.css';
import styles from './ExtratoProfessorPage.module.css';

const ExtratoProfessorPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [extratoData, setExtratoData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await obterExtratoProfessor(user.id);
        setExtratoData(data);
      } catch (error) {
        console.error('Erro ao carregar extrato:', error);
        alert('Erro ao carregar extrato');
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
          <h1><FaChartBar /> Histórico de Distribuições</h1>
          <button onClick={() => navigate('/dashboard/professor')} className={dashboardStyles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={dashboardStyles.content}>
          <div className={dashboardStyles.welcomeCard}>
            <h2><FaCoins /> Saldo Disponível</h2>
            <div className={styles.saldoValor}>
              {extratoData?.saldoMoedas?.toFixed(2)} moedas
            </div>
          </div>

          {extratoData?.distribuicoes && extratoData.distribuicoes.length > 0 ? (
            <div className={dashboardStyles.welcomeCard}>
              <h2><FaGem /> Histórico de Distribuições</h2>
              <div className={styles.historicoContainer}>
                {extratoData.distribuicoes
                  .sort((a, b) => new Date(b.data) - new Date(a.data))
                  .map((dist) => (
                    <div key={dist.id} className={styles.transacaoCard}>
                      <div className={styles.transacaoHeader}>
                        <div className={styles.transacaoInfo}>
                          <div className={styles.alunoNome}>
                            <FaArrowUp style={{ color: '#27ae60' }} />
                            <strong>{dist.nomeAluno}</strong>
                          </div>
                          <p className={styles.transacaoMotivo}>
                            <strong>Motivo:</strong> {dist.motivo}
                          </p>
                          <p className={styles.transacaoData}>
                            {new Date(dist.data).toLocaleString('pt-BR')}
                          </p>
                        </div>
                        <div className={styles.transacaoValor}>
                          <div className={styles.valorPositivo}>
                            + {dist.valor.toFixed(2)}
                          </div>
                          <div className={styles.valorLabel}>moedas</div>
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          ) : (
            <div className={dashboardStyles.welcomeCard}>
              <h2>Nenhuma distribuição encontrada</h2>
              <p className={styles.emptyState}>
                Você ainda não realizou nenhuma distribuição de moedas.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ExtratoProfessorPage;
