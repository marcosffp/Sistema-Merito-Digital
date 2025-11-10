import api from './api';

const vantagemService = {
  /**
   * Lista todas as vantagens
   * @returns {Promise<Array>}
   */
  listarTodas: async () => {
    try {
      const response = await api.get('/vantagens');
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao listar vantagens';
    }
  },

  /**
   * Busca vantagem por ID
   * @param {number} id
   * @returns {Promise<Object>}
   */
  buscarPorId: async (id) => {
    try {
      const response = await api.get(`/vantagens/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao buscar vantagem';
    }
  },

  /**
   * Cadastra nova vantagem
   * @param {Object} data - { nome, descricao, custo, empresaId, imagem }
   * @returns {Promise<Object>}
   */
  cadastrar: async (data) => {
    try {
      const formData = new FormData();
      formData.append('nome', data.nome);
      formData.append('descricao', data.descricao);
      formData.append('custo', data.custo);
      formData.append('empresaId', data.empresaId);
      
      if (data.imagem) {
        formData.append('imagem', data.imagem);
      }

      const response = await api.post('/vantagens', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao cadastrar vantagem';
    }
  },

  /**
   * Atualiza vantagem existente
   * @param {number} id
   * @param {Object} data
   * @returns {Promise<Object>}
   */
  atualizar: async (id, data) => {
    try {
      const formData = new FormData();
      formData.append('nome', data.nome);
      formData.append('descricao', data.descricao);
      formData.append('custo', data.custo);
      formData.append('empresaId', data.empresaId);
      
      if (data.imagem) {
        formData.append('imagem', data.imagem);
      }

      const response = await api.put(`/vantagens/${id}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao atualizar vantagem';
    }
  },

  /**
   * Deleta vantagem
   * @param {number} id
   * @returns {Promise<void>}
   */
  deletar: async (id) => {
    try {
      await api.delete(`/vantagens/${id}`);
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao deletar vantagem';
    }
  },

  /**
   * Lista vantagens por custo máximo
   * @param {number} custoMaximo
   * @returns {Promise<Array>}
   */
  listarPorCustoMaximo: async (custoMaximo) => {
    try {
      const response = await api.get('/vantagens/custo-maximo', {
        params: { custoMaximo },
      });
      return response.data;
    } catch (error) {
      throw error.response?.data?.message || 'Erro ao listar vantagens';
    }
  },
};

export default vantagemService;
