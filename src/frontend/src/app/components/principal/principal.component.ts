import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  GrupoMenu,
  MenuPrincipalItemComponent,
} from '../base/menu/menu-principal-item/menu-principal-item.component';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import { Toolbar } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { MessagesComponent } from '../base/messages/messages.component';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'principal-component',
  imports: [
    RouterModule,
    CommonModule,
    MenuPrincipalItemComponent,
    Toolbar,
    ButtonModule,
    MessagesComponent,
  ],
  templateUrl: './principal.component.html',
  styleUrl: './principal.component.css',
  animations: [
    trigger('openClose', [
      state(
        'open',
        style({
          transform: 'translateY(0)',
        })
      ),
      state(
        'closed',
        style({
          transform: 'translateY(-200%)',
        })
      ),
      transition('open => closed', [animate('0.2s')]),
      transition('closed => open', [animate('0.2s')]),
    ]),
  ],
})
export class PrincipalComponent {
  showDrawer: boolean = false;

  tituloApp: string = 'Gestão Integrada';

  openDrawerMenu = {
    nome: 'Menu',
    icone: 'menu',
  };

  menu: GrupoMenu[] = [
    {
      nome: 'Cadastros',
      icone: 'widgets',
      url: '/cadastro',
    },
    {
      nome: 'Financeiro',
      icone: 'attach_money',
      url: '/financeiro',
    },
    {
      nome: 'Atendimento',
      icone: 'child_care',
      url: '/atendimento',
    },
  ];

  constructor(private router: Router, private authService: AuthService) {}

  toogleDrawer() {
    this.showDrawer = !this.showDrawer;
  }

  onGoToEvent() {
    this.toogleDrawer();
  }

  goToHome() {
    this.router.navigate(['/']);
  }

  logout() {
    this.authService.logout();
  }

  getUsername() {
    return this.authService.getUsername();
  }

  getName() {
    return this.authService.getName();
  }
}
