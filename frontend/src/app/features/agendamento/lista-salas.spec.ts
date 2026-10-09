import { TestBed } from '@angular/core/testing';
import { resolverEstados } from '../../core/data/mapa-mock';
import { ListaSalas } from './lista-salas';

describe('ListaSalas', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaSalas],
    }).compileComponents();
  });

  function montar(disponiveis: string[], selecionada: string | null = null) {
    const fixture = TestBed.createComponent(ListaSalas);
    fixture.componentRef.setInput('salas', resolverEstados(disponiveis));
    fixture.componentRef.setInput('selecionada', selecionada);
    fixture.detectChanges();
    return fixture;
  }

  function habilitadas(fixture: { nativeElement: HTMLElement }) {
    const lista = fixture.nativeElement as HTMLElement;
    return Array.from(lista.querySelectorAll<HTMLButtonElement>('button')).filter(
      (item) => !item.disabled,
    );
  }

  it('should create the component', () => {
    const fixture = TestBed.createComponent(ListaSalas);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render one row per room', () => {
    const fixture = montar([]);
    const lista = (fixture.nativeElement as HTMLElement).querySelectorAll('button');

    expect(lista.length).toBe(8);
  });

  it('should allow selecting only the available rooms', () => {
    const fixture = montar(['Sala Comp 02']);
    const itens = habilitadas(fixture);

    expect(itens.length).toBe(1);
    expect(itens[0].textContent).toContain('Sala Comp 02');
  });

  it('should show the state of each room', () => {
    const fixture = montar(['Sala Comp 02']);
    const texto = (fixture.nativeElement as HTMLElement).textContent ?? '';

    expect(texto).toContain('Livre');
    expect(texto).toContain('Ocupada');
    expect(texto).toContain('Indisponível');
  });

  it('should emit the selected room', () => {
    const fixture = montar(['Sala Comp 02']);
    let escolhida = '';

    fixture.componentInstance.selecionar.subscribe((nome) => (escolhida = nome));

    habilitadas(fixture)[0].click();

    expect(escolhida).toBe('Sala Comp 02');
  });

  it('should mark the selected room', () => {
    const fixture = montar(['Sala Comp 02'], 'Sala Comp 02');
    const marcada = (fixture.nativeElement as HTMLElement).querySelector(
      '.lista__item--selecionada',
    );

    expect(marcada?.textContent).toContain('Sala Comp 02');
    expect(marcada?.getAttribute('aria-pressed')).toBe('true');
  });
});
