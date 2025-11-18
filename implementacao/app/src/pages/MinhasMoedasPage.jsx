import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { obterDadosAluno } from '../services/alunoService';
import { FaCoins, FaArrowLeft, FaHistory } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const MinhasMoedasPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [alunoData, setAlunoData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await obterDadosAluno(user.id);
        setAlunoData(data);
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
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1><FaCoins /> Minhas Moedas</h1>
          <button onClick={() => navigate('/dashboard/aluno')} className={styles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2>Saldo Atual</h2>
            <div style={{ fontSize: '3rem', fontWeight: 'bold', color: '#667eea', marginTop: '1rem' }}>
              {alunoData?.saldoMoedas?.toFixed(2)} moedas
            </div>
          </div>

          {alunoData?.resgates && alunoData.resgates.length > 0 && (
            <div className={styles.welcomeCard}>
              <h2><FaHistory /> Histórico de Resgates</h2>
              <div style={{ marginTop: '1rem' }}>
                {alunoData.resgates.map((resgate) => (
                  <div key={resgate.id} style={{ 
                    padding: '1rem', 
                    background: '#f5f5f5', 
                    marginBottom: '0.5rem',
                    borderRadius: '8px'
                  }}>
                    <p><strong>Vantagem:</strong> {resgate.nomeVantagem}</p>
                    <p><strong>Valor:</strong> {resgate.valor} moedas</p>
                    <p><strong>Cupom:</strong> {resgate.cupom}</p>
                    <p><strong>Código:</strong> {resgate.codigo}</p>
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

export default MinhasMoedasPage;
