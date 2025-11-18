import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { obterExtratoProfessor } from '../services/professorService';
import { FaChartBar, FaArrowLeft, FaCoins, FaGem, FaArrowUp } from 'react-icons/fa';
import styles from './Dashboard.module.css';

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
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1><FaChartBar /> Histórico de Distribuições</h1>
          <button onClick={() => navigate('/dashboard/professor')} className={styles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2><FaCoins /> Saldo Disponível</h2>
            <div style={{ fontSize: '2.5rem', fontWeight: 'bold', color: '#667eea', marginTop: '0.5rem' }}>
              {extratoData?.saldoMoedas?.toFixed(2)} moedas
            </div>
          </div>

          {extratoData?.distribuicoes && extratoData.distribuicoes.length > 0 ? (
            <div className={styles.welcomeCard}>
              <h2><FaGem /> Histórico de Distribuições</h2>
              <div style={{ marginTop: '1rem' }}>
                {extratoData.distribuicoes
                  .sort((a, b) => new Date(b.data) - new Date(a.data))
                  .map((dist) => (
                    <div key={dist.id} style={{ 
                      padding: '1.25rem', 
                      background: '#fff',
                      border: '1px solid #e0e0e0',
                      marginBottom: '0.75rem',
                      borderRadius: '8px'
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
                        <div style={{ flex: 1 }}>
                          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
                            <FaArrowUp style={{ color: '#27ae60' }} />
                            <strong style={{ fontSize: '1.1rem' }}>{dist.nomeAluno}</strong>
                          </div>
                          <p style={{ color: '#666', margin: '0.25rem 0', fontSize: '0.9rem' }}>
                            <strong>Motivo:</strong> {dist.motivo}
                          </p>
                          <p style={{ color: '#999', margin: '0.25rem 0', fontSize: '0.85rem' }}>
                            {new Date(dist.data).toLocaleString('pt-BR')}
                          </p>
                        </div>
                        <div style={{ textAlign: 'right' }}>
                          <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#27ae60' }}>
                            + {dist.valor.toFixed(2)}
                          </div>
                          <div style={{ fontSize: '0.85rem', color: '#999' }}>moedas</div>
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          ) : (
            <div className={styles.welcomeCard}>
              <h2>Nenhuma distribuição encontrada</h2>
              <p style={{ color: '#666', marginTop: '1rem' }}>
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
