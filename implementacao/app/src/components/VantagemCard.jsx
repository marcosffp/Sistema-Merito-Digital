import { useNavigate } from 'react-router-dom';
import { FaGift, FaCoins, FaEdit, FaTrash } from 'react-icons/fa';
import styles from './VantagemCard.module.css';

const VantagemCard = ({ vantagem, onDelete }) => {
  const navigate = useNavigate();

  const handleEdit = () => {
    navigate(`/empresa/vantagens/editar/${vantagem.id}`);
  };

  return (
    <div className={styles.card}>
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
        <p className={styles.description}>{vantagem.descricao}</p>
        
        <div className={styles.info}>
          <div className={styles.status}>
            <span className={styles.statusItem}>
              <strong>Estoque:</strong> {vantagem.estoque || 0}
            </span>
            <span className={`${styles.statusBadge} ${vantagem.disponivel ? styles.disponivel : styles.indisponivel}`}>
              {vantagem.disponivel ? 'Disponível' : 'Indisponível'}
            </span>
          </div>
        </div>

        <div className={styles.footer}>
          <span className={styles.price}><FaCoins /> {vantagem.custo} moedas</span>
          
          <div className={styles.actions}>
            <button onClick={handleEdit} className={styles.editButton}>
              <FaEdit /> Editar
            </button>
            <button onClick={() => onDelete(vantagem.id)} className={styles.deleteButton}>
              <FaTrash /> Excluir
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default VantagemCard;
