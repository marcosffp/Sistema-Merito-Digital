import api from './api';

export const obterDadosProfessor = async (professorId) => {
  const response = await api.get(`/professores/${professorId}`);
  return response.data;
};

export const listarAlunos = async () => {
  const response = await api.get('/alunos/resumo');
  return response.data;
};

export const distribuirMoedas = async (professorId, alunoId, valor, motivo) => {
  try {
    console.log('Enviando distribuição:', { professorId, alunoId, valor, motivo });
    const response = await api.post('/distribuicoes', {
      professorId: Number(professorId),
      alunoId: Number(alunoId),
      valor: Number(valor),
      motivo: motivo.trim()
    });
    return response.data;
  } catch (error) {
    console.error('Erro detalhado:', error.response?.data);
    throw error;
  }
};
