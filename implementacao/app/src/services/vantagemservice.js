import api from './api';

/**
 * Lista todas as vantagens
 * @returns {Promise<Array>}
 */
export const listarVantagens = async () => {
  const response = await api.get('/vantagens');
  return response.data;
};

/**
 * Busca vantagem por ID
 * @param {number} id
 * @returns {Promise<Object>}
 */
export const buscarVantagemPorId = async (id) => {
  const response = await api.get(`/vantagens/${id}`);
  return response.data;
};

/**
 * Cadastra nova vantagem
 * @param {Object} data - { nome, descricao, custo, empresaId, imagem }
 * @returns {Promise<Object>}
 */
export const cadastrarVantagem = async (data) => {
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
};

/**
 * Atualiza vantagem existente
 * @param {number} id
 * @param {Object} data
 * @returns {Promise<Object>}
 */
export const atualizarVantagem = async (id, data) => {
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
};

/**
 * Deleta vantagem
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deletarVantagem = async (id) => {
  await api.delete(`/vantagens/${id}`);
};

/**
 * Resgata vantagem para aluno
 * @param {number} alunoId
 * @param {number} vantagemId
 * @returns {Promise<Object>}
 */
export const resgatarVantagem = async (alunoId, vantagemId) => {
  const response = await api.post('/resgates', {
    alunoId,
    vantagemId
  });
  return response.data;
};

/**
 * Lista vantagens por custo máximo
 * @param {number} custoMaximo
 * @returns {Promise<Array>}
 */
export const listarPorCustoMaximo = async (custoMaximo) => {
  const response = await api.get('/vantagens/custo-maximo', {
    params: { custoMaximo },
  });
  return response.data;
};
