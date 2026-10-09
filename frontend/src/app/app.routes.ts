import { Routes } from '@angular/router';
import { Agendamento } from './features/agendamento/agendamento';

export const routes: Routes = [
  { path: '', component: Agendamento },
  { path: 'agendamento', component: Agendamento },
  { path: '**', redirectTo: '' },
];
