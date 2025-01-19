import { Component, OnInit } from '@angular/core';
import { MessageService, MessageType } from './messages.service';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { messageServiceProvider } from './message.factory';

@Component({
  selector: 'messages',
  templateUrl: './messages.component.html',
  styleUrl: './messages.component.css',
  imports: [CommonModule, ButtonModule],
  providers: [{ provide: MessageService, useFactory: messageServiceProvider }],
})
export class MessagesComponent implements OnInit {
  message: string = '';
  messageType: MessageType = MessageType.INFO;

  show: boolean = false;

  timer = setTimeout(() => {});

  constructor(private service: MessageService) {}

  ngOnInit() {
    this.service.stateSubject.subscribe((message) => {
      if (message) {
        this.message = message.message;
        this.messageType = message.messageType;
        this.show = true;
      }

      this.timer = setTimeout(() => {
        this.show = false;
      }, 3000);
    });
  }

  getClass() {
    switch (this.messageType) {
      case MessageType.INFO:
        return 'info-message';
      case MessageType.SUCCESS:
        return 'success-message';
      case MessageType.WARNING:
        return 'warning-message';
      case MessageType.ERROR:
        return 'error-message';
      default:
        return 'info-message';
    }
  }

  closeMessage() {
    this.show = false;
    clearTimeout(this.timer);
  }
}
