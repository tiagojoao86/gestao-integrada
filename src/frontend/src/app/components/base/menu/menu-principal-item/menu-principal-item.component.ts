import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
    selector: 'menu-principal-item',
    imports: [CommonModule, MatButtonModule, MatIconModule],
    templateUrl: './menu-principal-item.component.html',
    styleUrl: './menu-principal-item.component.css'
})
export class MenuPrincipalItemComponent {

  constructor(
    private router: Router
  ) { }

  @Input('grupo') grupo: GrupoMenu | undefined;
  @Output() onGoToEvent = new EventEmitter<string>;

  goTo(url: string) {
    this.onGoToEvent.emit(url);
    this.router.navigate([url]);
  }
}

export interface GrupoMenu {
  nome: string;  
  icone: string;
  url: string;
}