import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./home/home.component').then(app => app.HomeComponent)
    },
    {
        path: 'cadastro',
        loadChildren: () => import('../cadastro/cadastro.routes').then(cadastroRoutes => cadastroRoutes.routes)
    }
]