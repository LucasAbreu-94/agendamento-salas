import { Component, input, output } from '@angular/core';
import { EstadoSala, SalaPlanta } from '../../core/data/mapa-mock';

const ROTULOS: Record<EstadoSala, string> = {
  disponivel: 'Livre',
  ocupada: 'Ocupada',
  indisponivel: 'Indisponível',
};

@Component({
  selector: 'app-lista-salas',
  templateUrl: './lista-salas.html',
  styleUrl: './lista-salas.css',
})
export class ListaSalas {
  readonly salas = input<SalaPlanta[]>([]);
  readonly selecionada = input<string | null>(null);

  readonly selecionar = output<string>();

  rotulo(estado: EstadoSala): string {
    return ROTULOS[estado];
  }
}
