import { Routes } from "@angular/router";

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./cadastro.component').then(app => app.CadastroComponent),        
    },
    {
        path: 'usuario',
        loadComponent: () => import('./usuario/usuario.component').then(app => app.UsuariosComponent),        
    },
    {
        path: 'usuario/:id',
        loadComponent: () => import('./usuario/usuario-detalhe/usuario-detalhe.component').then(app => app.UsuarioDetalheComponent),
    }
]