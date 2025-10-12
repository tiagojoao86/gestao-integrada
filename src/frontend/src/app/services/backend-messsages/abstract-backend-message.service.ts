import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root',
})
export abstract class AbstractBackendMessageService {
  constructor() {}

  getMessage(code: string): string {
    return this.messages()[code] || code;
  }

  getMessages(codes: string[]): string[] {
    return codes.map((code) => this.getMessage(code));
  }

  abstract messages(): { [key: string]: string };

}