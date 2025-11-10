import { useNavigate } from 'react-router-dom';
import styles from './VantagemCard.module.css';

const VantagemCard = ({ vantagem, onDelete, showActions = true }) => {
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
            <span>🎁</span>
          </div>
        )}
      </div>

      <div className={styles.content}>
        <h3 className={styles.title}>{vantagem.nome}</h3>
        <p className={styles.description}>{vantagem.descricao}</p>
        
        <div className={styles.footer}>
          <span className={styles.price}>💰 {vantagem.custo} moedas</span>
          
          {showActions && (
            <div className={styles.actions}>
              <button onClick={handleEdit} className={styles.editButton}>
                ✏️ Editar
              </button>
              <button onClick={() => onDelete(vantagem.id)} className={styles.deleteButton}>
                🗑️ Excluir
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default VantagemCard;
