/**
 * Aplica máscara de CPF (000.000.000-00)
 */
export const maskCPF = (value) => {
  if (!value) return '';
  
  value = value.replace(/\D/g, '');
  value = value.substring(0, 11);
  
  if (value.length <= 3) {
    return value;
  } else if (value.length <= 6) {
    return value.replace(/(\d{3})(\d+)/, '$1.$2');
  } else if (value.length <= 9) {
    return value.replace(/(\d{3})(\d{3})(\d+)/, '$1.$2.$3');
  } else {
    return value.replace(/(\d{3})(\d{3})(\d{3})(\d+)/, '$1.$2.$3-$4');
  }
};

/**
 * Aplica máscara de CNPJ (00.000.000/0000-00)
 */
export const maskCNPJ = (value) => {
  if (!value) return '';
  
  value = value.replace(/\D/g, '');
  value = value.substring(0, 14);
  
  if (value.length <= 2) {
    return value;
  } else if (value.length <= 5) {
    return value.replace(/(\d{2})(\d+)/, '$1.$2');
  } else if (value.length <= 8) {
    return value.replace(/(\d{2})(\d{3})(\d+)/, '$1.$2.$3');
  } else if (value.length <= 12) {
    return value.replace(/(\d{2})(\d{3})(\d{3})(\d+)/, '$1.$2.$3/$4');
  } else {
    return value.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d+)/, '$1.$2.$3/$4-$5');
  }
};

/**
 * Aplica máscara de RG (00.000.000-0)
 */
export const maskRG = (value) => {
  if (!value) return '';
  
  value = value.replace(/\D/g, '');
  value = value.substring(0, 9);
  
  if (value.length <= 2) {
    return value;
  } else if (value.length <= 5) {
    return value.replace(/(\d{2})(\d+)/, '$1.$2');
  } else if (value.length <= 8) {
    return value.replace(/(\d{2})(\d{3})(\d+)/, '$1.$2.$3');
  } else {
    return value.replace(/(\d{2})(\d{3})(\d{3})(\d+)/, '$1.$2.$3-$4');
  }
};

/**
 * Remove a máscara de um valor
 */
export const removeMask = (value) => {
  if (!value) return '';
  return value.replace(/\D/g, '');
};
