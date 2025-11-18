import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { obterExtratoAluno } from '../services/alunoService';
import { FaChartBar, FaArrowLeft, FaCoins, FaGift, FaArrowUp, FaArrowDown } from 'react-icons/fa';
import styles from './Dashboard.module.css';

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
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1><FaChartBar /> Extrato de Transações</h1>
          <button onClick={() => navigate('/dashboard/aluno')} className={styles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2><FaCoins /> Saldo Atual</h2>
            <div style={{ fontSize: '2.5rem', fontWeight: 'bold', color: '#667eea', marginTop: '0.5rem' }}>
              {extratoData?.saldoMoedas?.toFixed(2)} moedas
            </div>
          </div>

          {extratoData?.resgates && extratoData.resgates.length > 0 ? (
            <div className={styles.welcomeCard}>
              <h2><FaGift /> Histórico de Resgates</h2>
              <div style={{ marginTop: '1rem' }}>
                {extratoData.resgates
                  .sort((a, b) => new Date(b.data) - new Date(a.data))
                  .map((resgate) => (
                    <div key={resgate.id} style={{ 
                      padding: '1.25rem', 
                      background: '#fff',
                      border: '1px solid #e0e0e0',
                      marginBottom: '0.75rem',
                      borderRadius: '8px',
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center'
                    }}>
                      <div style={{ flex: 1 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
                          <FaArrowDown style={{ color: '#e74c3c' }} />
                          <strong style={{ fontSize: '1.1rem' }}>{resgate.nomeVantagem}</strong>
                        </div>
                        <p style={{ color: '#666', margin: '0.25rem 0' }}>
                          <strong>Cupom:</strong> {resgate.cupom}
                        </p>
                        <p style={{ color: '#666', margin: '0.25rem 0' }}>
                          <strong>Código:</strong> {resgate.codigo}
                        </p>
                      </div>
                      <div style={{ textAlign: 'right' }}>
                        <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#e74c3c' }}>
                          - {resgate.valor.toFixed(2)}
                        </div>
                        <div style={{ fontSize: '0.85rem', color: '#999' }}>moedas</div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          ) : (
            <div className={styles.welcomeCard}>
              <h2>Nenhuma transação encontrada</h2>
              <p style={{ color: '#666', marginTop: '1rem' }}>
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
