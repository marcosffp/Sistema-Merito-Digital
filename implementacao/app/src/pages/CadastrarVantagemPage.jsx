import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import vantagemService from '../services/vantagemservice';
import { FaArrowLeft } from 'react-icons/fa';
import styles from './CadastrarVantagemPage.module.css';

const CadastrarVantagemPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [imagePreview, setImagePreview] = useState(null);
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    custo: '',
    imagem: null,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
    setError('');
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData(prev => ({ ...prev, imagem: file }));
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const data = {
        nome: formData.nome,
        descricao: formData.descricao,
        custo: parseFloat(formData.custo),
        empresaId: user.id,
        imagem: formData.imagem,
      };

      await vantagemService.cadastrar(data);
      alert('Vantagem cadastrada com sucesso!');
      navigate('/empresa/vantagens');
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <div className={styles.content}>
          <header className={styles.header}>
            <button onClick={() => navigate('/empresa/vantagens')} className={styles.backButton}>
              <FaArrowLeft /> Voltar
            </button>
            <h1>Cadastrar Nova Vantagem</h1>
            <p>Preencha os dados da vantagem</p>
          </header>

          <form className={styles.form} onSubmit={handleSubmit}>
            {error && (
              <div className={styles.errorMessage}>{error}</div>
            )}

            <div className={styles.formGroup}>
              <label htmlFor="nome">Nome da Vantagem *</label>
              <input
                type="text"
                id="nome"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                placeholder="Ex: Desconto de 20% em produtos"
                required
                disabled={loading}
              />
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="descricao">Descrição</label>
              <textarea
                id="descricao"
                name="descricao"
                value={formData.descricao}
                onChange={handleChange}
                placeholder="Descreva a vantagem..."
                rows="4"
                maxLength="500"
                disabled={loading}
              />
              <span className={styles.charCount}>{formData.descricao.length}/500</span>
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="custo">Custo em Moedas *</label>
              <input
                type="number"
                id="custo"
                name="custo"
                value={formData.custo}
                onChange={handleChange}
                placeholder="Ex: 100"
                min="1"
                step="1"
                required
                disabled={loading}
              />
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="imagem">Imagem da Vantagem</label>
              <input
                type="file"
                id="imagem"
                accept="image/*"
                onChange={handleImageChange}
                disabled={loading}
                className={styles.fileInput}
              />
              {imagePreview && (
                <div className={styles.imagePreview}>
                  <img src={imagePreview} alt="Preview" />
                </div>
              )}
            </div>

            <button type="submit" className={styles.submitButton} disabled={loading}>
              {loading ? 'Cadastrando...' : 'Cadastrar Vantagem'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CadastrarVantagemPage;
