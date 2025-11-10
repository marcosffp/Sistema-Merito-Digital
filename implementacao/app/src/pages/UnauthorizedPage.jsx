import { useNavigate } from 'react-router-dom';
import { FaBan } from 'react-icons/fa';
import styles from './UnauthorizedPage.module.css';

const UnauthorizedPage = () => {
  const navigate = useNavigate();

  return (
    <div className={styles.unauthorizedPage}>
      <div className={styles.content}>
        <FaBan className={styles.icon} />
        <h2>Acesso Negado</h2>
        <p>Você não tem permissão para acessar esta página.</p>
        <button onClick={() => navigate('/login')} className={styles.button}>
          Voltar ao Login
        </button>
      </div>
    </div>
  );
};

export default UnauthorizedPage;
