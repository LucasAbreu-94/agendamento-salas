import { normalizarNome } from '../util/formatos';

export type EstadoSala = 'disponivel' | 'ocupada' | 'indisponivel';

export interface SalaPlanta {
  id: number;
  nome: string;
  capacidade: number;
  localizacao: string;
  area: string;
  situacaoBase: 'livre' | 'indisponivel';
  estado: EstadoSala;
}

interface SalaBase {
  id: number;
  nome: string;
  capacidade: number;
  area: string;
  situacaoBase: 'livre' | 'indisponivel';
}

const LOCALIZACAO = 'Tamboré';

const SALAS_BASE: SalaBase[] = [
  {
    id: 1,
    nome: 'Sala Focus',
    capacidade: 2,
    area: 'focus',
    situacaoBase: 'livre',
  },
  {
    id: 2,
    nome: 'Sala Comp 01',
    capacidade: 8,
    area: 'comp01',
    situacaoBase: 'livre',
  },
  {
    id: 3,
    nome: 'Sala Comp 02',
    capacidade: 8,
    area: 'comp02',
    situacaoBase: 'livre',
  },
  {
    id: 4,
    nome: 'Sala Comp 03',
    capacidade: 8,
    area: 'comp03',
    situacaoBase: 'indisponivel',
  },
  {
    id: 5,
    nome: 'Sala Comp 04',
    capacidade: 8,
    area: 'comp04',
    situacaoBase: 'livre',
  },
  {
    id: 6,
    nome: 'Sala 01',
    capacidade: 4,
    area: 'sala01',
    situacaoBase: 'livre',
  },
  {
    id: 7,
    nome: 'Sala 02',
    capacidade: 4,
    area: 'sala02',
    situacaoBase: 'livre',
  },
  {
    id: 8,
    nome: 'Sala 03',
    capacidade: 4,
    area: 'sala03',
    situacaoBase: 'indisponivel',
  },
];

export function resolverEstados(disponiveis: string[]): SalaPlanta[] {
  const livres = new Set(disponiveis.map(normalizarNome));

  return SALAS_BASE.map((sala) => ({
    ...sala,
    localizacao: LOCALIZACAO,
    estado: situacaoFinal(sala, livres),
  }));
}

function situacaoFinal(sala: SalaBase, livres: Set<string>): EstadoSala {
  if (sala.situacaoBase === 'indisponivel') {
    return 'indisponivel';
  }

  return livres.has(normalizarNome(sala.nome)) ? 'disponivel' : 'ocupada';
}
