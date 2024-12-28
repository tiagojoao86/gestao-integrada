import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'recurso',
    imports: [CommonModule],
    templateUrl: './recurso.component.html',
    styleUrl: './recurso.component.css'
})
export class RecursoComponent {
  
  @Input('recurso') recurso: Recurso | undefined;

  constructor(private router: Router) { }

  goTo(url: string) {
    this.router.navigate([url]);
  }
}

export interface Recurso {
  nome: string;
  icone: string;
  url: string;
}
