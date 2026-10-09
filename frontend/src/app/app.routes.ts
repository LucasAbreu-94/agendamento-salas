import { Routes } from '@angular/router';
import { Agendamento } from './features/agendamento/agendamento';

export const routes: Routes = [
  { path: '', redirectTo: 'agendamento', pathMatch: 'full' },
  { path: 'agendamento', component: Agendamento },
  { path: '**', redirectTo: 'agendamento' },
];
