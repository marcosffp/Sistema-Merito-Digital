import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { listarVantagens, resgatarVantagem } from '../services/vantagemService';
import { obterResumoAluno } from '../services/alunoService';
import { FaGift, FaArrowLeft, FaCoins, FaBuilding } from 'react-icons/fa';
import styles from './VantagensAlunoPage.module.css';

const VantagensAlunoPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [vantagens, setVantagens] = useState([]);
  const [saldoAluno, setSaldoAluno] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

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
        setError('Erro ao carregar vantagens');
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

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <p>Carregando vantagens...</p>
      </div>
    );
  }

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <header className={styles.header}>
          <div>
            <button onClick={() => navigate('/dashboard/aluno')} className={styles.backButton}>
              <FaArrowLeft /> Voltar
            </button>
            <h1>Vantagens Disponíveis</h1>
            <p>Troque suas moedas por vantagens incríveis!</p>
          </div>
        </header>

        <div className={styles.saldoSection}>
          <div className={styles.saldoCard}>
            <span className={styles.saldoLabel}>Seu saldo:</span>
            <span className={styles.saldoValue}>
              <FaCoins /> {saldoAluno.toFixed(2)} moedas
            </span>
          </div>
        </div>

        {error && (
          <div className={styles.errorMessage}>
            {error}
          </div>
        )}

        {vantagens.length === 0 ? (
          <div className={styles.emptyState}>
            <FaGift className={styles.emptyIcon} />
            <h2>Nenhuma vantagem disponível</h2>
            <p>Aguarde novas vantagens serem cadastradas pelas empresas parceiras!</p>
          </div>
        ) : (
          <div className={styles.grid}>
            {vantagens.map(vantagem => (
              <div key={vantagem.id} className={styles.card}>
                <div className={styles.imageContainer}>
                  {vantagem.imagem ? (
                    <img src={vantagem.imagem} alt={vantagem.nome} className={styles.image} />
                  ) : (
                    <div className={styles.noImage}>
                      <FaGift />
                    </div>
                  )}
                </div>

                <div className={styles.content}>
                  <h3 className={styles.title}>{vantagem.nome}</h3>
                  {vantagem.nomeEmpresa && (
                    <p className={styles.empresa}>
                      <FaBuilding /> {vantagem.nomeEmpresa}
                    </p>
                  )}
                  <p className={styles.description}>{vantagem.descricao}</p>
                  
                  <div className={styles.footer}>
                    <span className={styles.price}>
                      <FaCoins /> {vantagem.custo} moedas
                    </span>
                    <button 
                      onClick={() => handleResgatar(vantagem.id, vantagem.custo)}
                      disabled={saldoAluno < vantagem.custo}
                      className={`${styles.resgateButton} ${saldoAluno < vantagem.custo ? styles.disabled : ''}`}
                    >
                      {saldoAluno >= vantagem.custo ? 'Resgatar' : 'Saldo Insuficiente'}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default VantagensAlunoPage;
