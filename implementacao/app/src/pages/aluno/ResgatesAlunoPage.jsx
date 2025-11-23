import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { obterExtratoAluno } from '../../services/alunoService';
import { FaArrowLeft, FaGift, FaTicketAlt, FaBarcode, FaCalendar } from 'react-icons/fa';
import Loading from '../../components/Loading';
import dashboardStyles from '../dashboard/Dashboard.module.css';
import styles from './ResgatesAlunoPage.module.css';

const ResgatesAlunoPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [resgates, setResgates] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await obterExtratoAluno(user.id);
        const resgatesOrdenados = (data.resgates || []).sort(
          (a, b) => new Date(b.data) - new Date(a.data)
        );
        setResgates(resgatesOrdenados);
      } catch (error) {
        console.error('Erro ao carregar resgates:', error);
        alert('Erro ao carregar resgates');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user.id]);

  if (loading) {
    return <Loading message="Carregando resgates..." />;
  }

  return (
    <div className={dashboardStyles.dashboardPage}>
      <div className={dashboardStyles.container}>
        <header className={dashboardStyles.header}>
          <h1><FaGift /> Minhas Vantagens Resgatadas</h1>
          <button onClick={() => navigate('/dashboard/aluno')} className={dashboardStyles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={dashboardStyles.content}>
          {resgates.length === 0 ? (
            <div className={dashboardStyles.welcomeCard}>
              <div className={styles.emptyState}>
                <FaGift className={styles.emptyIcon} />
                <h2>Nenhuma vantagem resgatada</h2>
                <p>Você ainda não resgatou nenhuma vantagem.</p>
                <button 
                  onClick={() => navigate('/aluno/vantagens')} 
                  className={styles.goToVantagens}
                >
                  Ver Vantagens Disponíveis
                </button>
              </div>
            </div>
          ) : (
            <div className={styles.resgatesGrid}>
              {resgates.map((resgate) => (
                <div key={resgate.id} className={styles.resgateCard}>
                  <div className={styles.resgateHeader}>
                    <FaGift className={styles.resgateIcon} />
                    <h3>{resgate.nomeVantagem}</h3>
                  </div>

                  <div className={styles.resgateBody}>
                    <div className={styles.infoRow}>
                      <span className={styles.label}>
                        <FaTicketAlt /> Cupom:
                      </span>
                      <span className={styles.value}>{resgate.cupom}</span>
                    </div>

                    <div className={styles.infoRow}>
                      <span className={styles.label}>
                        <FaBarcode /> Código:
                      </span>
                      <span className={styles.value}>{resgate.codigo}</span>
                    </div>

                    <div className={styles.infoRow}>
                      <span className={styles.label}>
                        <FaCalendar /> Data:
                      </span>
                      <span className={styles.value}>
                        {new Date(resgate.data).toLocaleDateString('pt-BR', {
                          day: '2-digit',
                          month: '2-digit',
                          year: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </span>
                    </div>

                    <div className={styles.valorRow}>
                      <span className={styles.valorLabel}>Valor:</span>
                      <span className={styles.valorValue}>
                        {resgate.valor.toFixed(2)} moedas
                      </span>
                    </div>
                  </div>

                  <div className={styles.resgateFooter}>
                    <p className={styles.footerText}>
                      Apresente o código na empresa para retirar sua vantagem
                    </p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ResgatesAlunoPage;
