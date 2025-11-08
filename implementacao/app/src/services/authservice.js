import api from './api';

const authService = {
  /**
   * Realiza login e armazena o token JWT
   * @param {string} email 
   * @param {string} senha 
   * @returns {Promise<Object>} Dados do usuário decodificados
   */
  login: async (email, senha) => {
    try {
      const response = await api.post('/auth/login', { email, senha });
      const { token } = response.data;
      
      if (token) {
        localStorage.setItem('token', token);
        return authService.decodeToken(token);
      }
      
      throw new Error('Token não recebido');
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao realizar login';
    }
  },

  /**
   * Remove o token e desloga o usuário
   */
  logout: () => {
    localStorage.removeItem('token');
  },

  /**
   * Retorna o token armazenado
   * @returns {string|null}
   */
  getToken: () => {
    return localStorage.getItem('token');
  },

  /**
   * Verifica se o usuário está autenticado
   * @returns {boolean}
   */
  isAuthenticated: () => {
    const token = authService.getToken();
    if (!token) return false;

    try {
      const decoded = authService.decodeToken(token);
      const currentTime = Date.now() / 1000;
      
      // Verifica se o token está expirado
      if (decoded.exp < currentTime) {
        authService.logout();
        return false;
      }
      
      return true;
    } catch (error) {
      authService.logout();
      return false;
    }
  },

  /**
   * Decodifica o token JWT
   * @param {string} token 
   * @returns {Object} Payload do token
   */
  decodeToken: (token) => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      
      return JSON.parse(jsonPayload);
    } catch (error) {
      throw new Error('Token inválido');
    }
  },

  /**
   * Retorna o tipo/role do usuário (Aluno, Professor, Empresa)
   * @returns {string|null}
   */
  getUserRole: () => {
    const token = authService.getToken();
    if (!token) return null;

    try {
      const decoded = authService.decodeToken(token);
      return decoded.role || null;
    } catch (error) {
      return null;
    }
  },

  /**
   * Retorna o ID do usuário logado
   * @returns {number|null}
   */
  getUserId: () => {
    const token = authService.getToken();
    if (!token) return null;

    try {
      const decoded = authService.decodeToken(token);
      return decoded.id || null;
    } catch (error) {
      return null;
    }
  },

  /**
   * Retorna o email do usuário logado
   * @returns {string|null}
   */
  getUserEmail: () => {
    const token = authService.getToken();
    if (!token) return null;

    try {
      const decoded = authService.decodeToken(token);
      return decoded.sub || null;
    } catch (error) {
      return null;
    }
  },

  /**
   * Retorna todos os dados do usuário decodificados do token
   * @returns {Object|null}
   */
  getUserData: () => {
    const token = authService.getToken();
    if (!token) return null;

    try {
      const decoded = authService.decodeToken(token);
      return {
        id: decoded.id,
        email: decoded.sub,
        role: decoded.role,
      };
    } catch (error) {
      return null;
    }
  },

  /**
   * Registra um novo usuário (Aluno ou Empresa)
   * @param {Object} data Dados do cadastro
   * @param {string} type Tipo: 'aluno' ou 'empresa'
   * @returns {Promise<Object>} Dados do usuário decodificados
   */
  register: async (data, type) => {
    try {
      const endpoint = type === 'aluno' ? '/alunos/cadastro' : '/empresas/cadastro';
      const response = await api.post(endpoint, data);
      const { token } = response.data;
      
      if (token) {
        localStorage.setItem('token', token);
        return authService.decodeToken(token);
      }
      
      throw new Error('Token não recebido');
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao realizar cadastro';
    }
  },
};

export default authService;
