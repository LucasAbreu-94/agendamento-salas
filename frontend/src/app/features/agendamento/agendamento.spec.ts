import { TestBed } from '@angular/core/testing';
import { Agendamento } from './agendamento';

describe('Agendamento', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Agendamento],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(Agendamento);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render the page title', async () => {
    const fixture = TestBed.createComponent(Agendamento);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain(
      'Agendamento de Salas',
    );
  });
});
