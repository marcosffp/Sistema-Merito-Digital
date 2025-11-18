import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { listarVantagens, deletarVantagem } from '../../services/vantagemService';
import VantagemCard from '../../components/VantagemCard';
import { FaArrowLeft, FaPlus, FaBox } from 'react-icons/fa';
import styles from './VantagensEmpresaPage.module.css';

const VantagensEmpresaPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [vantagens, setVantagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    carregarVantagens();
  }, []);

  const carregarVantagens = async () => {
    try {
      setLoading(true);
      const data = await listarVantagens();
      console.log('Todas as vantagens:', data);
      console.log('ID da empresa logada:', user.id);
      
      // Filtrar apenas vantagens da empresa logada
      const vantagensEmpresa = data.filter(v => {
        console.log(`Vantagem ${v.id}: empresaId = ${v.empresaId}`);
        return v.empresaId === user.id;
      });
      
      console.log('Vantagens filtradas:', vantagensEmpresa);
      setVantagens(vantagensEmpresa);
    } catch (err) {
      console.error('Erro ao carregar vantagens:', err);
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Deseja realmente excluir esta vantagem?')) {
      return;
    }

    try {
      await deletarVantagem(id);
      setVantagens(vantagens.filter(v => v.id !== id));
      alert('Vantagem excluída com sucesso!');
    } catch (err) {
      alert('Erro ao excluir vantagem: ' + err);
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
            <button onClick={() => navigate('/dashboard/empresa')} className={styles.backButton}>
              <FaArrowLeft /> Voltar
            </button>
            <h1>Minhas Vantagens</h1>
            <p>Gerencie as vantagens oferecidas pela sua empresa</p>
          </div>
          <button onClick={() => navigate('/empresa/vantagens/nova')} className={styles.addButton}>
            <FaPlus /> Nova Vantagem
          </button>
        </header>

        {error && (
          <div className={styles.errorMessage}>
            {error}
          </div>
        )}

        {vantagens.length === 0 ? (
          <div className={styles.emptyState}>
            <FaBox className={styles.emptyIcon} />
            <h2>Nenhuma vantagem cadastrada</h2>
            <p>Comece cadastrando sua primeira vantagem!</p>
            <button onClick={() => navigate('/empresa/vantagens/nova')} className={styles.addButton}>
              Cadastrar Vantagem
            </button>
          </div>
        ) : (
          <div className={styles.grid}>
            {vantagens.map(vantagem => (
              <VantagemCard
                key={vantagem.id}
                vantagem={vantagem}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default VantagensEmpresaPage;
