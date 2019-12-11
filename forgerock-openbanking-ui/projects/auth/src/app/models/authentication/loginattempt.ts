import { ICallbackHandlers } from '../field-config.interface';

export class LoginAttempt {
  config: ICallbackHandlers;
  constructor(config: ICallbackHandlers) {
    this.config = config;
  }
}
