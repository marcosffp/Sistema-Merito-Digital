import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import vantagemService from '../services/vantagemservice';
import styles from './VantagensAlunoPage.module.css';

const VantagensAlunoPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [vantagens, setVantagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filtroPreco, setFiltroPreco] = useState('');

  useEffect(() => {
    carregarVantagens();
  }, []);

  const carregarVantagens = async () => {
    try {
      setLoading(true);
      const data = await vantagemService.listarTodas();
      setVantagens(data);
    } catch (err) {
      console.error('Erro ao carregar vantagens:', err);
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrarPorPreco = async () => {
    if (!filtroPreco) {
      carregarVantagens();
      return;
    }

    try {
      setLoading(true);
      const data = await vantagemService.listarPorCustoMaximo(parseFloat(filtroPreco));
      setVantagens(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  const handleResgatar = (vantagemId) => {
    // TODO: Implementar lógica de resgate
    alert(`Resgate da vantagem ${vantagemId} - Funcionalidade em desenvolvimento`);
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
              ← Voltar
            </button>
            <h1>Vantagens Disponíveis</h1>
            <p>Troque suas moedas por vantagens incríveis!</p>
          </div>
        </header>

        <div className={styles.filterSection}>
          <div className={styles.saldoInfo}>
            <span className={styles.saldoLabel}>Seu saldo:</span>
            <span className={styles.saldoValue}>💰 0 moedas</span>
          </div>

          <div className={styles.filterGroup}>
            <input
              type="number"
              placeholder="Filtrar por preço máximo"
              value={filtroPreco}
              onChange={(e) => setFiltroPreco(e.target.value)}
              className={styles.filterInput}
            />
            <button onClick={handleFiltrarPorPreco} className={styles.filterButton}>
              Filtrar
            </button>
            <button onClick={() => { setFiltroPreco(''); carregarVantagens(); }} className={styles.clearButton}>
              Limpar
            </button>
          </div>
        </div>

        {error && (
          <div className={styles.errorMessage}>
            {error}
          </div>
        )}

        {vantagens.length === 0 ? (
          <div className={styles.emptyState}>
            <span className={styles.emptyIcon}>🎁</span>
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
                      <span>🎁</span>
                    </div>
                  )}
                </div>

                <div className={styles.content}>
                  <h3 className={styles.title}>{vantagem.nome}</h3>
                  {vantagem.empresaNome && (
                    <p className={styles.empresa}>🏢 {vantagem.empresaNome}</p>
                  )}
                  <p className={styles.description}>{vantagem.descricao}</p>
                  
                  <div className={styles.footer}>
                    <span className={styles.price}>💰 {vantagem.custo} moedas</span>
                    <button 
                      onClick={() => handleResgatar(vantagem.id)} 
                      className={styles.resgateButton}
                    >
                      Resgatar
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
