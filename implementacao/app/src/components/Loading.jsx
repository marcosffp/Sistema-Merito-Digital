import styles from './Loading.module.css';
import { FaCoins } from 'react-icons/fa';

const Loading = ({ message = 'Carregando...' }) => {
  return (
    <div className={styles.loadingOverlay}>
      <div className={styles.loadingContent}>
        <div className={styles.coinAnimation}>
          <FaCoins className={styles.coin} />
          <FaCoins className={styles.coin} />
          <FaCoins className={styles.coin} />
        </div>
        <p className={styles.loadingText}>{message}</p>
      </div>
    </div>
  );
};

export default Loading;
