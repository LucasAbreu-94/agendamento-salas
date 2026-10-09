import { Component, computed, input, output, signal } from '@angular/core';
import { SalaPlanta } from '../../core/data/mapa-mock';
import { dataCurta, paraHora, paraMinutos } from '../../core/util/formatos';

type CampoAberto = 'data' | 'horario' | 'duracao' | null;

const FIM_DO_DIA = 24 * 60;

@Component({
  selector: 'app-painel-reserva',
  templateUrl: './painel-reserva.html',
  styleUrl: './painel-reserva.css',
})
export class PainelReserva {
  readonly sala = input<SalaPlanta | null>(null);
  readonly data = input<string>('');
  readonly horaInicio = input<string>('');
  readonly duracao = input<number>(60);
  readonly pessoas = input<number>(1);
  readonly duracoes = input<number[]>([30, 60, 90, 120]);

  readonly dataAlterada = output<string>();
  readonly horaAlterada = output<string>();
  readonly duracaoAlterada = output<number>();
  readonly pessoasAlterada = output<number>();

  readonly campoAberto = signal<CampoAberto>(null);

  readonly rotuloData = computed(() => dataCurta(this.data()));

  readonly horaFim = computed(() => {
    const inicio = paraMinutos(this.horaInicio());
    if (inicio === null) {
      return '--:--';
    }

    return paraHora(Math.min(inicio + this.duracao(), FIM_DO_DIA));
  });

  alternar(campo: Exclude<CampoAberto, null>): void {
    this.campoAberto.set(this.campoAberto() === campo ? null : campo);
  }

  mudarPessoas(delta: number): void {
    this.pessoasAlterada.emit(Math.max(1, this.pessoas() + delta));
  }

  valorDoCampo(evento: Event): string {
    return (evento.target as HTMLInputElement).value;
  }

  valorNumerico(evento: Event): number {
    return Number(this.valorDoCampo(evento));
  }

  rotuloDuracao(minutos: number): string {
    if (minutos < 60) {
      return `${minutos} min`;
    }

    const horas = Math.floor(minutos / 60);
    const resto = minutos % 60;
    const rotuloHoras = horas === 1 ? '1 hora' : `${horas} horas`;

    return resto === 0 ? rotuloHoras : `${rotuloHoras} e ${resto} min`;
  }
}
