import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./components/principal/principal.component').then(app => app.PrincipalComponent),
        loadChildren: () => import('./components/principal/principal.routes').then(principalRoutes => principalRoutes.routes)
    },
];
