import { Injectable } from '@angular/core';
import { BaseService } from './base-service';
import { HttpClient } from '@angular/common/http';
import { UserAppDTO } from '../model/userapp-dto';
import { UserAppGridDTO } from '../model/userapp-grid-dto';

@Injectable()
export class AppUserService extends BaseService<UserAppGridDTO, UserAppDTO> {
  private static readonly APP_USER = 'appuser';

  constructor(httpClient: HttpClient) {
    super(httpClient, AppUserService.APP_USER);
  }
}
