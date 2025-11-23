import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { listarVantagens, resgatarVantagem } from '../../services/vantagemService';
import { obterResumoAluno } from '../../services/alunoService';
import { FaGift, FaArrowLeft, FaCoins, FaBuilding, FaExclamationTriangle } from 'react-icons/fa';
import styles from './VantagensAlunoPage.module.css';
import dashboardStyles from '../dashboard/Dashboard.module.css';
import Loading from '../../components/Loading';

const VantagensAlunoPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [vantagens, setVantagens] = useState([]);
  const [saldoAluno, setSaldoAluno] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [resgatando, setResgatando] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [vantagensData, alunoData] = await Promise.all([
          listarVantagens(),
          obterResumoAluno(user.id)
        ]);
        // Filtrar apenas vantagens disponíveis
        const vantagensDisponiveis = vantagensData.filter(v => v.disponivel && v.estoque > 0);
        setVantagens(vantagensDisponiveis);
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

  const handleResgatar = async (vantagemId, custo, estoque, nomeVantagem) => {
    if (estoque <= 0) {
      alert('Esta vantagem está sem estoque no momento.');
      return;
    }

    if (custo > saldoAluno) {
      alert(`Saldo insuficiente. Você tem ${saldoAluno.toFixed(2)} moedas e precisa de ${custo.toFixed(2)} moedas.`);
      return;
    }

    const confirmar = window.confirm(
      `Confirmar resgate de "${nomeVantagem}"?\n\n` +
      `Valor: ${custo.toFixed(2)} moedas\n\n` +
      `Você receberá um email com o cupom de troca.`
    );
    
    if (!confirmar) return;

    try {
      setResgatando(true);
      const resultado = await resgatarVantagem(user.id, vantagemId);
      
      alert(
        `✅ Resgate realizado com sucesso!\n\n` +
        `Código: ${resultado.codigo}\n` +
        `Cupom: ${resultado.cupom}\n\n` +
        `📧 Um email foi enviado com todas as informações!`
      );

      // Atualizar dados
      const [vantagensData, alunoData] = await Promise.all([
        listarVantagens(),
        obterResumoAluno(user.id)
      ]);
      const vantagensDisponiveis = vantagensData.filter(v => v.disponivel && v.estoque > 0);
      setVantagens(vantagensDisponiveis);
      setSaldoAluno(alunoData.saldoMoedas);
    } catch (error) {
      console.error('Erro ao resgatar vantagem:', error);
      alert(error?.response?.data?.message || 'Erro ao resgatar vantagem');
    } finally {
      setResgatando(false);
    }
  };

  const podeResgatar = (vantagem) => {
    return vantagem.disponivel && 
           vantagem.estoque > 0 && 
           saldoAluno >= vantagem.custo;
  };

  const getButtonText = (vantagem) => {
    if (!vantagem.disponivel) return 'Indisponível';
    if (vantagem.estoque <= 0) return 'Sem Estoque';
    if (saldoAluno < vantagem.custo) return 'Saldo Insuficiente';
    return 'Resgatar';
  };

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <p>Carregando vantagens...</p>
      </div>
    );
  }

  return (
    <>
      {resgatando && <Loading message="Processando resgate..." />}
      
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

          <div className={`${dashboardStyles.welcomeCard} ${styles.visualizarSaldo}`}>
            <h2><FaCoins /> Saldo Atual</h2>
            <div className={styles.saldoValor}>
              {saldoAluno.toFixed(2)} moedas
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
                    {vantagem.estoque <= 5 && vantagem.estoque > 0 && (
                      <div className={styles.lowStockBadge}>
                        <FaExclamationTriangle /> Últimas unidades!
                      </div>
                    )}
                  </div>

                  <div className={styles.content}>
                    <h3 className={styles.title}>{vantagem.nome}</h3>
                    {vantagem.empresaNome && (
                      <p className={styles.empresa}>
                        <FaBuilding /> {vantagem.empresaNome}
                      </p>
                    )}
                    <p className={styles.description}>{vantagem.descricao}</p>
                    
                    <p className={styles.estoque}>
                      <strong>Estoque:</strong> {vantagem.estoque} {vantagem.estoque === 1 ? 'unidade' : 'unidades'}
                    </p>

                    <div className={styles.footer}>
                      <span className={styles.price}>
                        <FaCoins /> {vantagem.custo} moedas
                      </span>
                      <button
                        onClick={() => handleResgatar(vantagem.id, vantagem.custo, vantagem.estoque, vantagem.nome)}
                        disabled={!podeResgatar(vantagem)}
                        className={`${styles.resgateButton} ${!podeResgatar(vantagem) ? styles.disabled : ''}`}
                      >
                        {getButtonText(vantagem)}
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default VantagensAlunoPage;
