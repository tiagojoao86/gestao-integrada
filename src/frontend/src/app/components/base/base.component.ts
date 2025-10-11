import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Location } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Toolbar } from 'primeng/toolbar';
import { BadgeModule } from 'primeng/badge';

@Component({
  selector: 'app-base',
  imports: [CommonModule, ButtonModule, Toolbar, BadgeModule],
  templateUrl: './base.component.html',
  styleUrl: './base.component.css',
})
export class BaseComponent {
  constructor(private location: Location) {}

  @Input('title') title: string = $localize `Título`;
  @Input('actions') actions: RegisterActionToolbar[] = [];
  @Input('hideFooter') hideFooter: boolean = false;
  @Input('hideToolbar') hideToolbar: boolean = false;
  @Input('goBackFn') goBackFn: Function | null = null;
  
  goBack() {
    if (this.goBackFn) {
      this.goBackFn();
      return;
    }

    this.location.back();
  }
}

export interface RegisterActionToolbar {
  action: Function;
  icon: string;
  title: string;
  value?: string;
}
