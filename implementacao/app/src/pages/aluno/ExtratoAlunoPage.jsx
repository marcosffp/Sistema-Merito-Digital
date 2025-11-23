import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { obterExtratoAluno } from '../../services/alunoService';
import { FaChartBar, FaArrowLeft, FaCoins, FaGift, FaArrowDown, FaArrowUp } from 'react-icons/fa';
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
        console.log('Dados recebidos do backend:', data);
        console.log('Transações:', data.transacoes);
        console.log('Resgates:', data.resgates);
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

  // Combinar e ordenar todas as transações
  const obterTransacoesOrdenadas = () => {
    const transacoes = [];

    // Adicionar resgates (saídas)
    if (extratoData?.resgates) {
      extratoData.resgates.forEach(resgate => {
        transacoes.push({
          id: resgate.id,
          tipo: 'resgate',
          dataTransacao: resgate.data,
          cupom: resgate.cupom,
          codigo: resgate.codigo,
          valor: resgate.valor,
          nomeVantagem: resgate.nomeVantagem
        });
      });
    }

    // Adicionar recebimentos (entradas)
    if (extratoData?.transacoes) {
      extratoData.transacoes.forEach(transacao => {
        transacoes.push({
          id: transacao.id,
          tipo: 'recebimento',
          dataTransacao: transacao.data,
          valor: transacao.valor,
          motivo: transacao.motivo,
          nomeProfessor: transacao.nomeProfessor
        });
      });
    }

    // Ordenar por data (mais recente primeiro)
    return transacoes.sort((a, b) => new Date(b.dataTransacao) - new Date(a.dataTransacao));
  };

  if (loading) return <div className={styles.dashboardPage}>Carregando...</div>;

  const transacoesOrdenadas = obterTransacoesOrdenadas();

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

          {transacoesOrdenadas.length > 0 ? (
            <div className={dashboardStyles.welcomeCard}>
              <h2><FaGift /> Histórico de Transações</h2>
              <div className={styles.historicoContainer}>
                {transacoesOrdenadas.map((transacao, index) => (
                  <div key={`${transacao.tipo}-${transacao.id}-${index}`} className={styles.resgateCard}>
                    {transacao.tipo === 'resgate' ? (
                      <>
                        <div className={styles.resgateInfo}>
                          <div className={styles.resgateNome}>
                            <FaArrowDown style={{ color: '#e74c3c' }} />
                            <strong>{transacao.nomeVantagem}</strong>
                          </div>
                          <p className={styles.resgateDetalhes}>
                            <strong>Cupom:</strong> {transacao.cupom || 'N/A'}
                          </p>
                          <p className={styles.resgateDetalhes}>
                            <strong>Código:</strong> {transacao.codigo || 'N/A'}
                          </p>
                          <p className={styles.resgateDetalhes}>
                            <strong>Data:</strong> {transacao.dataTransacao ? new Date(transacao.dataTransacao).toLocaleDateString('pt-BR', {
                              day: '2-digit',
                              month: '2-digit',
                              year: 'numeric',
                              hour: '2-digit',
                              minute: '2-digit'
                            }) : 'N/A'}
                          </p>
                        </div>
                        <div className={styles.resgateValor}>
                          <div className={styles.valorNegativo}>
                            - {transacao.valor?.toFixed(2) || '0.00'}
                          </div>
                          <div className={styles.valorLabel}>moedas</div>
                        </div>
                      </>
                    ) : (
                      <>
                        <div className={styles.resgateInfo}>
                          <div className={styles.resgateNome}>
                            <FaArrowUp style={{ color: '#27ae60' }} />
                            <strong>Recebimento de Moedas</strong>
                          </div>
                          <p className={styles.resgateDetalhes}>
                            <strong>Motivo:</strong> {transacao.motivo || 'Não especificado'}
                          </p>
                          <p className={styles.resgateDetalhes}>
                            <strong>Professor:</strong> {transacao.nomeProfessor || 'N/A'}
                          </p>
                          <p className={styles.resgateDetalhes}>
                            <strong>Data:</strong> {transacao.dataTransacao ? new Date(transacao.dataTransacao).toLocaleDateString('pt-BR', {
                              day: '2-digit',
                              month: '2-digit',
                              year: 'numeric',
                              hour: '2-digit',
                              minute: '2-digit'
                            }) : 'N/A'}
                          </p>
                        </div>
                        <div className={styles.resgateValor}>
                          <div className={styles.valorPositivo}>
                            + {transacao.valor?.toFixed(2) || '0.00'}
                          </div>
                          <div className={styles.valorLabel}>moedas</div>
                        </div>
                      </>
                    )}
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className={dashboardStyles.welcomeCard}>
              <h2>Nenhuma transação encontrada</h2>
              <p className={styles.emptyState}>
                Você ainda não realizou nenhuma transação.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ExtratoAlunoPage;
