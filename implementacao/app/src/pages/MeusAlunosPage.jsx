import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { listarAlunos } from '../services/professorService';
import { FaUsers, FaArrowLeft } from 'react-icons/fa';
import styles from './Dashboard.module.css';

const MeusAlunosPage = () => {
  const navigate = useNavigate();
  const [alunos, setAlunos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAlunos = async () => {
      try {
        const data = await listarAlunos();
        setAlunos(data);
      } catch (error) {
        console.error('Erro ao carregar alunos:', error);
        alert('Erro ao carregar alunos');
      } finally {
        setLoading(false);
      }
    };

    fetchAlunos();
  }, []);

  if (loading) return <div className={styles.dashboardPage}>Carregando...</div>;

  return (
    <div className={styles.dashboardPage}>
      <div className={styles.container}>
        <header className={styles.header}>
          <h1><FaUsers /> Meus Alunos</h1>
          <button onClick={() => navigate('/dashboard/professor')} className={styles.logoutButton}>
            <FaArrowLeft /> Voltar
          </button>
        </header>

        <div className={styles.content}>
          <div className={styles.infoCards}>
            {alunos.map((aluno, index) => (
              <div key={index} className={styles.card}>
                <h3>{aluno.nome}</h3>
                <p><strong>Curso:</strong> {aluno.curso}</p>
                <p><strong>Instituição:</strong> {aluno.instituicao}</p>
                <p><strong>Saldo:</strong> {aluno.saldoMoedas} moedas</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MeusAlunosPage;
