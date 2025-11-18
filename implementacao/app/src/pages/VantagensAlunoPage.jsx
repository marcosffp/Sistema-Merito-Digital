import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { listarVantagens, resgatarVantagem } from '../services/vantagemService';
import { obterResumoAluno } from '../services/alunoService';
import { FaGift, FaArrowLeft, FaCoins } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const VantagensAlunoPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [vantagens, setVantagens] = useState([]);
  const [saldoAluno, setSaldoAluno] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [vantagensData, alunoData] = await Promise.all([
          listarVantagens(),
          obterResumoAluno(user.id)
        ]);
        setVantagens(vantagensData);
        setSaldoAluno(alunoData.saldoMoedas);
      } catch (error) {
        console.error('Erro ao carregar dados:', error);
        alert('Erro ao carregar vantagens');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user.id]);

  const handleResgatar = async (vantagemId, custo) => {
    if (custo > saldoAluno) {
      alert(`Saldo insuficiente. Você tem ${saldoAluno.toFixed(2)} moedas e precisa de ${custo.toFixed(2)} moedas.`);
      return;
    }

    const confirmar = window.confirm(`Confirmar resgate? Serão debitadas ${custo} moedas da sua conta.`);
    if (!confirmar) return;

    try {
      const resultado = await resgatarVantagem(user.id, vantagemId);
      alert(`Resgate realizado com sucesso!\n\nCódigo: ${resultado.codigo}\nCupom: ${resultado.cupom}`);
      
      // Atualizar saldo
      const alunoData = await obterResumoAluno(user.id);
      setSaldoAluno(alunoData.saldoMoedas);
    } catch (error) {
      console.error('Erro ao resgatar vantagem:', error);
      alert('Erro ao resgatar vantagem');
    }
  };

  if (loading) return <div className={styles.dashboardPage}>Carregando...</div>;

  return (
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1><FaGift /> Vantagens Disponíveis</h1>
          <button onClick={() => navigate('/dashboard/aluno')} className={styles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2><FaCoins /> Seu Saldo</h2>
            <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#667eea', marginTop: '0.5rem' }}>
              {saldoAluno.toFixed(2)} moedas
            </div>
          </div>

          <div className={styles.infoCards}>
            {vantagens.map((vantagem) => (
              <div key={vantagem.id} className={styles.card}>
                {vantagem.imagem && (
                  <img 
                    src={vantagem.imagem} 
                    alt={vantagem.nome}
                    style={{ width: '100%', height: '150px', objectFit: 'cover', borderRadius: '8px', marginBottom: '1rem' }}
                  />
                )}
                <h3>{vantagem.nome}</h3>
                <p>{vantagem.descricao}</p>
                <p style={{ fontSize: '1.2rem', fontWeight: 'bold', color: '#667eea', margin: '1rem 0' }}>
                  {vantagem.custo} moedas
                </p>
                <p style={{ fontSize: '0.9rem', color: '#666' }}>
                  Empresa: {vantagem.nomeEmpresa}
                </p>
                <button
                  onClick={() => handleResgatar(vantagem.id, vantagem.custo)}
                  disabled={saldoAluno < vantagem.custo}
                  style={{
                    width: '100%',
                    padding: '0.75rem',
                    marginTop: '1rem',
                    background: saldoAluno >= vantagem.custo 
                      ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                      : '#ccc',
                    color: 'white',
                    border: 'none',
                    borderRadius: '8px',
                    fontSize: '1rem',
                    fontWeight: 'bold',
                    cursor: saldoAluno >= vantagem.custo ? 'pointer' : 'not-allowed'
                  }}
                >
                  {saldoAluno >= vantagem.custo ? 'Resgatar' : 'Saldo Insuficiente'}
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default VantagensAlunoPage;
