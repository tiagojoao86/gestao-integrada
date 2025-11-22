import { Component, OnInit } from '@angular/core';
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
import { AuthService } from '../base/auth/auth-service';

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
export class PrincipalComponent implements OnInit{
  showDrawer: boolean = false;

  tituloApp: string = $localize `Gestão Integrada`;

  openDrawerMenu = {
    nome: $localize `Menu`,
    icone: 'menu',
  };

  menu: GrupoMenu[] = [];

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.buildMenu();
  }
  
  buildMenu() {
    if (this.authService.hasAuthorityToGrupo('CADASTROS')) {
      this.menu.push({
        nome: $localize`Cadastros`,
        icone: 'widgets',
        url: '/cadastro',
      });
    }

    if (this.authService.hasAuthorityToGrupo('FINANCEIRO')) {
      this.menu.push({
        nome: $localize`Financeiro`,
        icone: 'attach_money',
        url: '/financeiro',
      });
    }

    if (this.authService.hasAuthorityToGrupo('ATENDIMENTO')) {
      this.menu.push({
        nome: $localize`Atendimento`,
        icone: 'child_care',
        url: '/atendimento',
      });
    }
  }

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
    return this.authService.getNome();
  }
}
