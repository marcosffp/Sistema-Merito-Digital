import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './RegisterEmpresaPage.module.css';

const RegisterEmpresaPage = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: '',
    cnpj: '',
    endereco: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (formData.senha !== formData.confirmarSenha) {
      setError('As senhas não coincidem');
      return;
    }

    if (formData.senha.length < 6) {
      setError('A senha deve ter no mínimo 6 caracteres');
      return;
    }

    setLoading(true);

    try {
      const { confirmarSenha, ...dataToSend } = formData;
      await register(dataToSend, 'empresa');
      navigate('/dashboard/empresa');
    } catch (err) {
      setError(err || 'Erro ao realizar cadastro. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.registerPage}>
      <div className={styles.registerContainer}>
        <div className={styles.registerContent}>
          <div className={styles.registerHeader}>
            <button 
              onClick={() => navigate('/login')}
              className={styles.backButton}
            >
              ← Voltar ao Login
            </button>
            <h1>Cadastro de Empresa</h1>
            <p>Preencha os dados da sua empresa para criar uma conta</p>
          </div>

          <form className={styles.registerForm} onSubmit={handleSubmit}>
            {error && (
              <div className={styles.errorMessage}>
                {error}
              </div>
            )}

            <div className={styles.formGroup}>
              <label htmlFor="nome">Nome da Empresa *</label>
              <input
                type="text"
                id="nome"
                name="nome"
                value={formData.nome}
                onChange={handleChange}
                placeholder="Razão social ou nome fantasia"
                required
                disabled={loading}
              />
            </div>

            <div className={styles.formGrid}>
              <div className={styles.formGroup}>
                <label htmlFor="email">E-mail Corporativo *</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="contato@empresa.com"
                  required
                  disabled={loading}
                />
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="cnpj">CNPJ *</label>
                <input
                  type="text"
                  id="cnpj"
                  name="cnpj"
                  value={formData.cnpj}
                  onChange={handleChange}
                  placeholder="00.000.000/0000-00"
                  required
                  disabled={loading}
                />
              </div>
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="endereco">Endereço *</label>
              <input
                type="text"
                id="endereco"
                name="endereco"
                value={formData.endereco}
                onChange={handleChange}
                placeholder="Rua, número, bairro, cidade, estado"
                required
                disabled={loading}
              />
            </div>

            <div className={styles.formGrid}>
              <div className={styles.formGroup}>
                <label htmlFor="senha">Senha *</label>
                <input
                  type="password"
                  id="senha"
                  name="senha"
                  value={formData.senha}
                  onChange={handleChange}
                  placeholder="Mínimo 6 caracteres"
                  required
                  disabled={loading}
                />
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="confirmarSenha">Confirmar Senha *</label>
                <input
                  type="password"
                  id="confirmarSenha"
                  name="confirmarSenha"
                  value={formData.confirmarSenha}
                  onChange={handleChange}
                  placeholder="Digite a senha novamente"
                  required
                  disabled={loading}
                />
              </div>
            </div>

            <div className={styles.infoBox}>
              <p>
                <strong>Atenção:</strong> Ao criar uma conta como empresa, você poderá cadastrar 
                produtos e vantagens que os alunos poderão trocar por moedas estudantis.
              </p>
            </div>

            <button 
              type="submit" 
              className={styles.submitButton}
              disabled={loading}
            >
              {loading ? 'Cadastrando...' : 'Criar Conta'}
            </button>
          </form>
        </div>
      </div>

      <div className={styles.registerBackground}>
        <div className={styles.backgroundShape1}></div>
        <div className={styles.backgroundShape2}></div>
      </div>
    </div>
  );
};

export default RegisterEmpresaPage;
