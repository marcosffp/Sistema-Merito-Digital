import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useEffect } from 'react';
import AuthForm from '../../components/AuthForm';
import styles from './LoginPage.module.css';

const LoginPage = () => {
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();

  useEffect(() => {
    if (isAuthenticated) {
      // Redireciona para o dashboard apropriado baseado no tipo de usuário
      if (user?.role === 'Aluno') {
        navigate('/dashboard/aluno');
      } else if (user?.role === 'Professor') {
        navigate('/dashboard/professor');
      } else if (user?.role === 'Empresa') {
        navigate('/dashboard/empresa');
      } else {
        navigate('/dashboard');
      }
    }
  }, [isAuthenticated, user, navigate]);

  const handleLoginSuccess = () => {
    // O redirecionamento será feito pelo useEffect acima
  };

  return (
    <div className={styles.loginPage}>
      <div className={styles.loginContainer}>
        <div className={styles.loginContent}>
          <div className={styles.loginBrand}>
            <h1>Sistema Mérito</h1>
            <p>Sistema de Moedas Estudantis</p>
          </div>

          <AuthForm onSuccess={handleLoginSuccess} />

          <div className={styles.loginFooter}>
            <p>Não tem uma conta?</p>
            <div className={styles.registerLinks}>
              <button 
                onClick={() => navigate('/cadastro/aluno')}
                className={styles.linkButton}
              >
                Cadastrar como Aluno
              </button>
              <span className={styles.separator}>|</span>
              <button 
                onClick={() => navigate('/cadastro/empresa')}
                className={styles.linkButton}
              >
                Cadastrar como Empresa
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className={styles.loginBackground}>
        <div className={styles.backgroundShape1}></div>
        <div className={styles.backgroundShape2}></div>
        <div className={styles.backgroundShape3}></div>
      </div>
    </div>
  );
};

export default LoginPage;
