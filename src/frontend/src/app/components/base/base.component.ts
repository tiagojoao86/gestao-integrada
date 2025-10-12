import { CommonModule } from '@angular/common';
import { Component, HostListener, Input } from '@angular/core';
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

  @HostListener('window:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    const key = event.key.toLowerCase();
    const shortcut = this.buildShortcutString(event);

    const action = this.actions.find(a => a.shortcut?.toLowerCase() === shortcut);

    if (action) {
      event.preventDefault();
      action.action();
    }
  }

  private buildShortcutString(event: KeyboardEvent): string {
    const parts: string[] = [];
    if (event.ctrlKey) parts.push('control');
    if (event.altKey) parts.push('alt');
    if (event.shiftKey) parts.push('shift');
    
    parts.push(event.key.toLowerCase());
    return parts.join('.');
  }
}

export interface RegisterActionToolbar {
  action: Function;
  icon: string;
  title: string;
  shortcut?: string;
  value?: string;
}
