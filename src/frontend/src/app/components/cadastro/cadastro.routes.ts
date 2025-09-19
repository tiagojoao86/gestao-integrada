import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./cadastro.component').then((app) => app.CadastroComponent),
  },
  {
    path: 'usuario',
    loadComponent: () =>
      import('./usuario/usuario.component').then(
        (app) => app.UsuariosComponent
      ),
  },
];
