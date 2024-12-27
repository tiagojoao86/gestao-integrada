import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { GrupoMenu, MenuPrincipalItemComponent } from '../base/menu/menu-principal-item/menu-principal-item.component';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Toolbar } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'principal-component',
    imports: [RouterModule, CommonModule, MenuPrincipalItemComponent, Toolbar, ButtonModule],
    templateUrl: './principal.component.html',
    styleUrl: './principal.component.css',
    animations: [
        trigger('openClose', [
            state('open', style({
                transform: 'translateX(0)'
            })),
            state('closed', style({
                transform: 'translateX(-100%)'
            })),
            transition('open => closed', [animate('0.2s')]),
            transition('closed => open', [animate('0.2s')]),
        ]),
    ]
})
export class PrincipalComponent {

  showDrawer: boolean = false;

  tituloApp: string = 'Gestão Integrada';

  menu: GrupoMenu[] = [
    {
      nome: 'Cadastros',        
      icone: 'manufacturing',
      url: '/cadastro'
    },
    {
      nome: 'Financeiro',
      icone: 'attach_money',
      url: '/financeiro'
    },
    {
      nome: 'Atendimento',
      icone: 'child_care',
      url: '/atendimento'
    }
  ];

  toogleDrawer() {    
    this.showDrawer = !this.showDrawer;
  }

  onGoToEvent() {
    this.toogleDrawer();
  }
  
}