import { FormGroup } from '@angular/forms';
import { ICallback } from './field-config.interface';

export interface Field {
  config: ICallback;
  group: FormGroup;
}
