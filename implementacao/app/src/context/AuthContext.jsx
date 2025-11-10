import { createContext, useContext, useState, useEffect } from 'react';
import authService from '../services/authservice';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Verifica se há um usuário autenticado ao carregar a aplicação
  useEffect(() => {
    const checkAuth = () => {
      try {
        if (authService.isAuthenticated()) {
          const userData = authService.getUserData();
          console.log('Dados do usuário carregados:', userData); // Debug
          setUser(userData);
          setIsAuthenticated(true);
        } else {
          setUser(null);
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error('Erro ao verificar autenticação:', error);
        setUser(null);
        setIsAuthenticated(false);
      } finally {
        setLoading(false);
      }
    };

    checkAuth();
  }, []);

  /**
   * Realiza login do usuário
   * @param {string} email 
   * @param {string} senha 
   * @returns {Promise<Object>} Dados do usuário
   */
  const login = async (email, senha) => {
    try {
      const userData = await authService.login(email, senha);
      console.log('Login realizado, dados do usuário:', userData); // Debug
      setUser(userData);
      setIsAuthenticated(true);
      return userData;
    } catch (error) {
      setUser(null);
      setIsAuthenticated(false);
      throw error;
    }
  };

  /**
   * Realiza logout do usuário
   */
  const logout = () => {
    authService.logout();
    setUser(null);
    setIsAuthenticated(false);
  };

  /**
   * Registra um novo usuário (Aluno ou Empresa)
   * @param {Object} data Dados do usuário
   * @param {string} type Tipo: 'aluno' ou 'empresa'
   * @returns {Promise<Object>} Dados do usuário
   */
  const register = async (data, type) => {
    try {
      // A função de registro será implementada no authService
      const userData = await authService.register(data, type);
      setUser(userData);
      setIsAuthenticated(true);
      return userData;
    } catch (error) {
      setUser(null);
      setIsAuthenticated(false);
      throw error;
    }
  };

  /**
   * Atualiza os dados do usuário no contexto
   */
  const refreshUser = () => {
    try {
      if (authService.isAuthenticated()) {
        const userData = authService.getUserData();
        setUser(userData);
      }
    } catch (error) {
      console.error('Erro ao atualizar usuário:', error);
      logout();
    }
  };

  const value = {
    user,
    loading,
    isAuthenticated,
    login,
    logout,
    register,
    refreshUser,
    // Funções auxiliares
    getUserRole: () => user?.role || null,
    getUserId: () => user?.id || null,
    getUserEmail: () => user?.email || null,
    isAluno: () => user?.role === 'Aluno',
    isProfessor: () => user?.role === 'Professor',
    isEmpresa: () => user?.role === 'Empresa',
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

/**
 * Hook personalizado para acessar o contexto de autenticação
 * @returns {Object} Contexto de autenticação
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  
  return context;
};

export default AuthContext;
