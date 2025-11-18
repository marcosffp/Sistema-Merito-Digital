import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { obterExtratoAluno } from '../../services/alunoService';
import { FaChartBar, FaArrowLeft, FaCoins, FaGift, FaArrowDown } from 'react-icons/fa';
import dashboardStyles from '../dashboard/Dashboard.module.css';
import styles from './ExtratoAlunoPage.module.css';

const ExtratoAlunoPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [extratoData, setExtratoData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await obterExtratoAluno(user.id);
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
          <h1><FaChartBar /> Extrato de Transações</h1>
          <button onClick={() => navigate('/dashboard/aluno')} className={dashboardStyles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={dashboardStyles.content}>
          <div className={dashboardStyles.welcomeCard}>
            <h2><FaCoins /> Saldo Atual</h2>
            <div className={styles.saldoValor}>
              {extratoData?.saldoMoedas?.toFixed(2)} moedas
            </div>
          </div>

          {extratoData?.resgates && extratoData.resgates.length > 0 ? (
            <div className={dashboardStyles.welcomeCard}>
              <h2><FaGift /> Histórico de Resgates</h2>
              <div className={styles.historicoContainer}>
                {extratoData.resgates
                  .sort((a, b) => new Date(b.data) - new Date(a.data))
                  .map((resgate) => (
                    <div key={resgate.id} className={styles.resgateCard}>
                      <div className={styles.resgateInfo}>
                        <div className={styles.resgateNome}>
                          <FaArrowDown style={{ color: '#e74c3c' }} />
                          <strong>{resgate.nomeVantagem}</strong>
                        </div>
                        <p className={styles.resgateDetalhes}>
                          <strong>Cupom:</strong> {resgate.cupom}
                        </p>
                        <p className={styles.resgateDetalhes}>
                          <strong>Código:</strong> {resgate.codigo}
                        </p>
                      </div>
                      <div className={styles.resgateValor}>
                        <div className={styles.valorNegativo}>
                          - {resgate.valor.toFixed(2)}
                        </div>
                        <div className={styles.valorLabel}>moedas</div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          ) : (
            <div className={dashboardStyles.welcomeCard}>
              <h2>Nenhuma transação encontrada</h2>
              <p className={styles.emptyState}>
                Você ainda não realizou nenhum resgate de vantagens.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ExtratoAlunoPage;
