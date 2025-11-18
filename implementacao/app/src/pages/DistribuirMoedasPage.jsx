import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { obterDadosProfessor, listarAlunos, distribuirMoedas } from '../services/professorService';
import { FaGem, FaArrowLeft } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const DistribuirMoedasPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [alunos, setAlunos] = useState([]);
  const [saldoProfessor, setSaldoProfessor] = useState(0);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    alunoId: '',
    valor: '',
    motivo: ''
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [professorData, alunosData] = await Promise.all([
          obterDadosProfessor(user.id),
          listarAlunos()
        ]);
        setSaldoProfessor(professorData.saldoMoedas);
        setAlunos(alunosData);
      } catch (error) {
        console.error('Erro ao carregar dados:', error);
        alert('Erro ao carregar dados');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const valor = parseFloat(formData.valor);
    const alunoId = parseInt(formData.alunoId);
    
    // Validações
    if (!alunoId || isNaN(alunoId)) {
      alert('Por favor, selecione um aluno válido');
      return;
    }
    
    if (isNaN(valor) || valor <= 0) {
      alert('O valor deve ser um número maior que zero');
      return;
    }
    
    if (valor > saldoProfessor) {
      alert(`Saldo insuficiente. Você tem apenas ${saldoProfessor.toFixed(2)} moedas disponíveis`);
      return;
    }

    if (!formData.motivo || formData.motivo.trim() === '') {
      alert('Por favor, informe o motivo da distribuição');
      return;
    }

    try {
      console.log('Dados do formulário:', {
        professorId: user.id,
        alunoId,
        valor,
        motivo: formData.motivo
      });
      
      await distribuirMoedas(user.id, alunoId, valor, formData.motivo);
      alert('Moedas distribuídas com sucesso!');
      navigate('/dashboard/professor');
    } catch (error) {
      console.error('Erro ao distribuir moedas:', error);
      const errorMessage = error.response?.data?.message || error.response?.data || 'Erro ao distribuir moedas';
      alert(`Erro: ${errorMessage}`);
    }
  };

  if (loading) return <div className={styles.dashboardPage}>Carregando...</div>;

  return (
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1><FaGem /> Distribuir Moedas</h1>
          <button onClick={() => navigate('/dashboard/professor')} className={styles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.welcomeCard}>
            <h2>Saldo Disponível: {saldoProfessor.toFixed(2)} moedas</h2>
          </div>

          <div className={styles.welcomeCard}>
            <form onSubmit={handleSubmit}>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
                  Selecione o Aluno:
                </label>
                <select
                  value={formData.alunoId}
                  onChange={(e) => setFormData({ ...formData, alunoId: e.target.value })}
                  required
                  style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #ddd' }}
                >
                  <option value="">Selecione um aluno</option>
                  {alunos.map((aluno) => (
                    <option key={aluno.id} value={aluno.id}>
                      {aluno.nome} - {aluno.curso}
                    </option>
                  ))}
                </select>
              </div>

              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
                  Valor (moedas):
                </label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.valor}
                  onChange={(e) => setFormData({ ...formData, valor: e.target.value })}
                  required
                  style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #ddd' }}
                />
              </div>

              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 'bold' }}>
                  Motivo:
                </label>
                <textarea
                  value={formData.motivo}
                  onChange={(e) => setFormData({ ...formData, motivo: e.target.value })}
                  required
                  rows="4"
                  style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #ddd' }}
                />
              </div>

              <button
                type="submit"
                style={{
                  width: '100%',
                  padding: '1rem',
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  fontSize: '1rem',
                  fontWeight: 'bold',
                  cursor: 'pointer'
                }}
              >
                Distribuir Moedas
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DistribuirMoedasPage;
