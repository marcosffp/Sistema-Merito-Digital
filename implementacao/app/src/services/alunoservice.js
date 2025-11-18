import api from './api';

export const obterDadosAluno = async (alunoId) => {
  const response = await api.get(`/alunos/${alunoId}/completo`);
  return response.data;
};

export const obterResumoAluno = async (alunoId) => {
  const response = await api.get(`/alunos/${alunoId}/resumo`);
  return response.data;
};
